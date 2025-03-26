package com.example.produit.config.kafka;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

     @KafkaListener(topics = "user-topic", groupId = "my-group")
     public void consumeMessage(String message) {
           System.out.println("Message reçu dans Produit : " + message);
         }
}
