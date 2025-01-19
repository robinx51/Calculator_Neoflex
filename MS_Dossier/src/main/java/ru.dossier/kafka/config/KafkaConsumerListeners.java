package ru.dossier.kafka.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

//@Component
public class KafkaConsumerListeners {
//    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerListeners.class);
//
//    @KafkaListener(id = "consumer-group-1", topics = "finish-registration",
//            groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
//    public void listenFinishRegistration(@Payload EmailMessageDto message) {
//        logger.info("Received Message from finish-registration: {}", message);
//    }
//
//    @KafkaListener(id = "consumer-group-1", topics = "create-documents",
//            groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
//    public void listenCreateDocuments(@Payload EmailMessageDto message) {
//        logger.info("Received Message from create-documents: {}", message);
//    }
//
//    @KafkaListener(id = "consumer-group-1", topics = "send-documents",
//            groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
//    public void listenSendDocuments(@Payload EmailMessageDto message) {
//        logger.info("Received Message from send-documents: {}", message);
//    }
//
//    @KafkaListener(id = "consumer-group-2", topics = "send-ses",
//            groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
//    public void listenSendSes(@Payload EmailMessageDto message) {
//        logger.info("Received Message from send-ses: {}", message);
//    }
//
//    @KafkaListener(id = "consumer-group-2", topics = "credit-issued",
//            groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
//    public void listenCreditIssued(@Payload EmailMessageDto message) {
//        logger.info("Received Message from credit-issued: {}", message);
//    }
//
//    @KafkaListener(id = "consumer-group-2", topics = "statement-denied",
//            groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
//    public void listenStatementDenied(@Payload EmailMessageDto message) {
//        logger.info("Received Message from statement-denied: {}", message);
//    }
}