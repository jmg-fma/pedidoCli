package br.com.pedidocli.message.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PedidoProducer {

    @Autowired
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topic.topic-prod}")
    private String topic;

    public PedidoProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviaMensagem(String message) {
        try {
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao produzir mensagem para o kafka - Teste");
        }
    }
}
