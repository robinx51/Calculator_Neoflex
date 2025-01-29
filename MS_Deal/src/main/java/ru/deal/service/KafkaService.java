package ru.deal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.deal.dto.EmailMessageDto;

@Service
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, EmailMessageDto> kafkaTemplate;
    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);

    public void sendMessage(String topic, EmailMessageDto message) {
        kafkaTemplate.send(topic, message);
        logger.info("Опубликовано сообщение {}", message);
    }
}
