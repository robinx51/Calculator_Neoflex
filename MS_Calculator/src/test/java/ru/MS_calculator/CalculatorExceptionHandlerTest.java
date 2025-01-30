package ru.MS_calculator;

import ru.calculator.CalculatorApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(classes = CalculatorApplication.class)
@AutoConfigureMockMvc
public class CalculatorExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testHandleValidationErrors() throws Exception {
        String requestBody =
                "{" +
                        "  \"amount\": 10000," +
                        "  \"term\": 5," +
                        "  \"firstName\": \"Y\"," +
                        "  \"lastName\": \"Sin1\"," +
                        "  \"middleName\": \"A\"," +
                        "  \"email\": \"Stringmail.ru\"," +
                        "  \"birthdate\": \"2014-01-01\"," +
                        "  \"passportSeries\": \"12344\"," +
                        "  \"passportNumber\": \"12346\"" +
                        "}";

        mockMvc.perform(post("/calculator/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}
