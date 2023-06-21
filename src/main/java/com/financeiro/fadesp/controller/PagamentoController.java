package com.financeiro.fadesp.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.financeiro.fadesp.model.Pagamento;
import com.financeiro.fadesp.model.enumeration.StatusPagamento;
import com.financeiro.fadesp.model.enumeration.TipoPagador;
import com.financeiro.fadesp.repository.PagamentoRepository;
import com.financeiro.fadesp.service.PagamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

@Validated
@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    private final PagamentoRepository pagamentoRepository;

    public PagamentoController(PagamentoService pagamentoService, PagamentoRepository pagamentoRepository) {
        this.pagamentoService = pagamentoService;
        this.pagamentoRepository = pagamentoRepository;
    }

    @GetMapping
    public @ResponseBody List<Pagamento> list(){
        return pagamentoService.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> findById(@PathVariable @NotNull @Positive Long id){
        return pagamentoService.findById(id)
                .map(recordFound -> ResponseEntity.ok().body(recordFound))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Pagamento create(@RequestBody @Valid Pagamento pagamento) {
        return pagamentoService.create(pagamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable @NotNull @Positive Long id, @RequestBody @Valid Pagamento pagamento ) {
        Pagamento pagamendoDaBase = pagamentoRepository.getById(id);

        if (pagamendoDaBase.getStatusPagamento() == StatusPagamento.REJEITADO) {
            if (pagamento.getStatusPagamento() != StatusPagamento.PENDENTE) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O status desse pedido só pode ser alterado para pendente");

            }
        }

        if (pagamendoDaBase.getStatusPagamento() == StatusPagamento.PROCESSADO) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O Pagamento desse registro já foi processado");
        }
        return pagamentoService.update(id, pagamento)
                .map(recordFound ->
                        ResponseEntity.ok().body(recordFound))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull @Positive Long id){
        Pagamento pagamendoDaBase = pagamentoRepository.getById(id);

        if (pagamendoDaBase.getStatusPagamento() == StatusPagamento.PENDENTE) {
            if (pagamentoService.delete(id)) {
                return ResponseEntity.noContent().<Void>build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/listar-por-documento")
    public ResponseEntity<List<Pagamento>> getAllPayments(@RequestBody Pagamento pagamento) {

        List<Pagamento> pagamentoList = pagamentoRepository.listarPorNumeroDoc(pagamento.getNumeroDocumento());
        List<Pagamento> listaRetornoCpf = new ArrayList<>();

        for (Pagamento pagamento1:
             pagamentoList) {
            if (Objects.equals(pagamento1.getNumeroDocumento(), pagamento.getNumeroDocumento())){
                listaRetornoCpf.add(pagamento1);
            }
        }
        return ResponseEntity.ok().body(listaRetornoCpf);
    }

    @GetMapping("/listar-por-status")
    public ResponseEntity<List<Pagamento>> getAllStatus(@RequestBody Pagamento pagamento) {

        List<Pagamento> statusList = pagamentoRepository.listarPorStatus(pagamento.getStatusPagamento());
        List<Pagamento> listaRetornoStatus = new ArrayList<>();

        for (Pagamento pagamento1:
                statusList) {
            if (Objects.equals(pagamento1.getStatusPagamento(), pagamento.getStatusPagamento())){
                listaRetornoStatus.add(pagamento1);
            }
        }
        return ResponseEntity.ok().body(listaRetornoStatus);
    }


}
