package com.example.produit.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ProduitKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ProduitKafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Message envoyé : " + message);
    }
}
