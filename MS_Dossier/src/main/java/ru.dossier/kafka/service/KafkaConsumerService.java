package ru.dossier.kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.deal.dto.EmailMessageDto;

@Service
public class KafkaConsumerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(id = "finish-registration-listener", topics = "finish-registration",
            groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void listenFinishRegistration(@Payload EmailMessageDto message) {
        logger.info("Received Message from finish-registration: {}", message);
    }

    @KafkaListener(id = "create-documents-listener", topics = "create-documents",
            groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void listenCreateDocuments(@Payload EmailMessageDto message) {
        logger.info("Received Message from create-documents: {}", message);
    }

    @KafkaListener(id = "send-documents-listener", topics = "send-documents",
            groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void listenSendDocuments(@Payload EmailMessageDto message) {
        logger.info("Received Message from send-documents: {}", message);
    }

    @KafkaListener(id = "send-ses-listener", topics = "send-ses",
            groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void listenSendSes(@Payload EmailMessageDto message) {
        logger.info("Received Message from send-ses: {}", message);
    }

    @KafkaListener(id = "credit-issued-listener", topics = "credit-issued",
            groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void listenCreditIssued(@Payload EmailMessageDto message) {
        logger.info("Received Message from credit-issued: {}", message);
    }

    @KafkaListener(id = "statement-denied-listener", topics = "statement-denied",
            groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void listenStatementDenied(@Payload EmailMessageDto message) {
        logger.info("Received Message from statement-denied: {}", message);
    }
}