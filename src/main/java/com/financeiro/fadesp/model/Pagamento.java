package com.financeiro.fadesp.model;

import com.financeiro.fadesp.model.enumeration.MetodoPagamento;
import com.financeiro.fadesp.model.enumeration.StatusPagamento;
import com.financeiro.fadesp.model.enumeration.TipoPagador;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cod_debito;

    @Column(length = 100)
    private String cartao;

    @Column(length = 100)
    private String numeroDocumento;

    @Column(length = 100, nullable = false)
    private Double valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_pagador")
    private TipoPagador tipoPagador;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pagamento")
    private MetodoPagamento metodoPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusPagamento statusPagamento;

}
