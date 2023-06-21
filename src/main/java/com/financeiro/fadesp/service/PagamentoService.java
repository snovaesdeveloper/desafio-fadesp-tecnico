package com.financeiro.fadesp.service;

import com.financeiro.fadesp.model.Pagamento;
import com.financeiro.fadesp.model.enumeration.MetodoPagamento;
import com.financeiro.fadesp.model.enumeration.StatusPagamento;
import com.financeiro.fadesp.model.enumeration.TipoPagador;
import com.financeiro.fadesp.repository.PagamentoRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Validated
@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public List<Pagamento> list(){
        return pagamentoRepository.findAll();
    }

    public Optional<Pagamento> findById(@PathVariable @NotNull @Positive Long id){
        return pagamentoRepository.findById(id);
    }

    public Pagamento create(@Valid Pagamento pagamento) {
        if (pagamento.getMetodoPagamento() == MetodoPagamento.BOLETO || pagamento.getMetodoPagamento() == MetodoPagamento.PIX) {
            pagamento.setCartao(null);
        }

        pagamento.setStatusPagamento(StatusPagamento.PENDENTE);
        return pagamentoRepository.save(pagamento);
    }

    public Optional<Object> update(@NotNull @Positive Long id, @Valid Pagamento pagamento){
        return pagamentoRepository.findById(id)
                .map(recordFound -> {
                    recordFound.setStatusPagamento(pagamento.getStatusPagamento());
                    return pagamentoRepository.save(recordFound);
                });
    }

    public boolean delete(@PathVariable @NotNull @Positive Long id){
        return pagamentoRepository.findById(id)
                .map(recordFound -> {
                    pagamentoRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }

}
