package ru.deal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.calculator.dto.ValidationError;
import ru.calculator.dto.ValidationErrorResponse;
import ru.deal.exception.FeignValidationException;

import java.io.IOException;
import java.util.List;

public class FeignErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(FeignErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {
        logger.warn("Ошибка при вызове микросервиса: {}", methodKey);
        logger.warn("Статус: {}", response.status());

        if (response.status() == 400) {
            try {
                String responseBody = new String(response.body().asInputStream().readAllBytes());
                logger.warn("Тело ошибки: {}", responseBody);

                ValidationErrorResponse errorResponse = objectMapper.readValue(
                        responseBody,
                        ValidationErrorResponse.class
                );

                return new FeignValidationException(errorResponse.getErrors());
            } catch (IOException e) {
                logger.error("Ошибка при обработке ответа от микросервиса", e);
                return new FeignValidationException(List.of(
                        new ValidationError("unknown", "Ошибка при обработке ответа от микросервиса")
                ));
            }
        }
        return new FeignValidationException(List.of(
                new ValidationError("unknown", "Ошибка при вызове микросервиса")
        ));
    }
}