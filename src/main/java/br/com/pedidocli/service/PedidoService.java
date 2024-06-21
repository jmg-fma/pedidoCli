package br.com.pedidocli.service;

import br.com.pedidocli.message.producer.PedidoProducer;
import br.com.pedidocli.model.Pedido;
import br.com.pedidocli.repository.PedidoRepository;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class PedidoService {

    private final PedidoProducer pedidoProducer;
    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoProducer pedidoProducer, PedidoRepository pedidoRepository) {
        this.pedidoProducer = pedidoProducer;
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> listAll() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> pendenteAll() {
        return pedidoRepository.findByStatus("AGUARDANDO_ENVIO");
    }

    public Optional<Pedido> listById(Long id) {
        try {
            return pedidoRepository.findById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao buscar dados " + e.getMessage());
        }
    }

    public void create(Pedido pedido) {

        Gson json = new Gson();
        String msg = json.toJson(pedido);
        try {
            pedidoRepository.save(pedido);
            msg = json.toJson(pedido);
            pedidoProducer.enviaMensagem(msg);
        } catch (Exception e) {
            msg = "Erro ao gravar dados! ";
//            pedidoProducer.enviaMensagem(msg + e.getMessage());
            throw new IllegalArgumentException(msg + e.getMessage());
        }
    }

    public void update(Pedido pedido) {
        Gson json = new Gson();
        String msg = "Atualização executada com sucesso para: ";
        try {
            pedidoRepository.save(pedido);
            pedidoProducer.enviaMensagem(msg + json.toJson(pedido));
        } catch (Exception e) {
            msg = "Erro ao gravar dados: ";
            pedidoProducer.enviaMensagem(msg + e.getMessage());
            throw new IllegalArgumentException("Erro ao gravar dados! " + e.getMessage() + " " + json.toJson(pedido));
        }

    }

    public void delete(Long id) {
        Gson json = new Gson();
        String msg = "Exclusão realizada sucesso para: ";
        try {
            if (!pedidoRepository.existsById(id)) {
                throw new IllegalArgumentException("Erro ao excluir dados de " + id);
            }
            pedidoRepository.deleteById(id);
            pedidoProducer.enviaMensagem(msg + id);
        } catch (Exception e) {
            pedidoProducer.enviaMensagem(e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }

    }
}
