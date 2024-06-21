package br.com.pedidocli.controller;

import br.com.pedidocli.model.Pedido;
import br.com.pedidocli.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/pedido")
public class PedidoController {
    @Autowired
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/pendente")
    public ResponseEntity<List<Pedido>> pendenteAll() {
        List<Pedido> pedido = pedidoService.pendenteAll();
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/pedidos")
    public ResponseEntity<List<Pedido>> listAll() {
        List<Pedido> pedido = pedidoService.listAll();
        return ResponseEntity.ok(pedido);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<Optional<Pedido>> listById(@PathVariable long id) {
        Optional<Pedido> pedido = pedidoService.listById(id);
        return ResponseEntity.ok(pedido);
    }

    @PostMapping("/create")
    public ResponseEntity<Pedido> create(@RequestBody Pedido pedido) {
        pedidoService.create(pedido);
        return ResponseEntity.ok(pedido);
    }
}
