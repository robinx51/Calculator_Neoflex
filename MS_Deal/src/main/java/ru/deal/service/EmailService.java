package ru.deal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.deal.dto.EmailMessageDto;

@Service
public class EmailService {
    private static final String TOPIC_FINISH_REGISTRATION = "finish-registration";
    private static final String TOPIC_CREATE_DOCUMENTS = "create-documents";
    private static final String TOPIC_SEND_DOCUMENTS = "send-documents";
    private static final String TOPIC_SEND_SES = "send-ses";
    private static final String TOPIC_CREDIT_ISSUED = "credit-issued";
    private static final String TOPIC_STATEMENT_DENIED = "statement-denied";

    @Autowired
    private KafkaTemplate<String, EmailMessageDto> kafkaTemplate;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public void sendMessage(String topic, EmailMessageDto message) {
        kafkaTemplate.send(topic, message);
        logger.info("Опубликовано сообщение {}", message);
    }

    public void sendFinishRegistrationEmail(EmailMessageDto message) {
        sendMessage(TOPIC_FINISH_REGISTRATION, message);
    }

    public void sendCreateDocumentsEmail(EmailMessageDto message) {
        sendMessage(TOPIC_CREATE_DOCUMENTS, message);
    }

    public void sendSendDocumentsEmail(EmailMessageDto message) {
        sendMessage(TOPIC_SEND_DOCUMENTS, message);
    }

    public void sendSendSesEmail(EmailMessageDto message) {
        sendMessage(TOPIC_SEND_SES, message);
    }

    public void sendCreditIssuedEmail(EmailMessageDto message) {
        sendMessage(TOPIC_CREDIT_ISSUED, message);
    }

    public void sendStatementDeniedEmail(EmailMessageDto message) {
        sendMessage(TOPIC_STATEMENT_DENIED, message);
    }
}
