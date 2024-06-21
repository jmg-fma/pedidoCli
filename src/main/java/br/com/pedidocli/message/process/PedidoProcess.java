//package br.com.pedidocli.message.process;
//
//import br.com.pedidocli.model.Pedido;
//import br.com.pedidocli.repository.PedidoRepository;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.apache.kafka.common.serialization.StringDeserializer;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//
//@Slf4j
//public class PedidoProcess {
//    private final PedidoRepository pedidoRepository;
//    private static final String CONSUMER_GROUP_ID = "consumer-committed";
//
//    public PedidoProcess(PedidoRepository pedidoRepository) {
//        this.pedidoRepository = pedidoRepository;
//    }
//
//    public static void main(String[] args) throws JsonProcessingException {
//        process();
//    }
//
//    private static void process() throws JsonProcessingException {
//        KafkaConsumer<String, String> kafkaConsumer = createConsumer();
//        kafkaConsumer.subscribe(Collections.singleton("topic-pedido-efetuado"));
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        while (true) {
//            ConsumerRecords<String, String> records = kafkaConsumer.poll(10);
//            for (ConsumerRecord<String, String> record : records) {
//                Pedido pedido = objectMapper.readValue(record.value(), Pedido.class);
//                pedido.setStatus("ENVIADO_TRANSPORTADORA");
//
//                logger.info("Consumindo mensagem comitada = {}", record.value());
//            }
//
//            kafkaConsumer.commitSync();
//        }
//
//    }
//
//    private static String transformMessage(String message) {
//        return message.concat("-processed");
//    }
//
//    private static KafkaConsumer<String, String> createConsumer() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_ID);
//        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "CONSUMER_COMMITTED");
//        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        return new KafkaConsumer<>(props);
//    }
//}
