package br.com.pedidocli.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pedido")
@Setter
@Getter
public class Pedido {

    public Pedido(String cliente, String email, String status) {
        this.cliente = cliente;
        this.email = email;
        this.status = status;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cliente;
    private String email;
    private String status;

    public Pedido() {

    }
}
