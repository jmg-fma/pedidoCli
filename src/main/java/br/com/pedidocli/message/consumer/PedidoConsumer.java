package br.com.pedidocli.message.consumer;

import br.com.pedidocli.model.Pedido;
import br.com.pedidocli.repository.PedidoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@EnableKafka
public class PedidoConsumer {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final PedidoRepository pedidoRepository;

    public PedidoConsumer(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    private static final String CONSUMER_GROUP_ID = "consumer-committed";

    @KafkaListener(topics = "${topic.topic-prod}", groupId = "consumer-committed")
    public void receiveMessage(String message,
                               @Header(KafkaHeaders.RECEIVED_TOPIC) String topico,
                               Acknowledgment ack) throws JsonProcessingException {
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            logger.info("Iniciando consumo do tópico {}, mensagem {} ", topico, message);
            Pedido pedido = objectMapper.readValue(message, Pedido.class);
            pedido.setStatus("ENVIADO_TRANSPORTADORA");
            pedidoRepository.save(pedido);
            ack.acknowledge();
            logger.info("Commit realizado");

        } catch (Exception e) {
            /*
             * Se ocorrer um erro de conexão com a base (ex: timeout por firewall, credenciais inválidas ..) então tentaremos
             * novamente.
             * */
            if (e.getCause() instanceof JDBCConnectionException) {
                logger.error("Problemas ao comunicar com a base de dados, tentaremos novamente em 10segundos", e);
                ack.nack(Duration.ofDays(1000));
            } else {
                /*
                 * Qualquer outro erro não mapeado, estamos apenas dando commit na mensagem.
                 * pode ser um deadletter aqui
                 * */
                logger.error("Erro desconhecido ao tentar salvar", e);
                ack.acknowledge();
            }
        }


    }
}
