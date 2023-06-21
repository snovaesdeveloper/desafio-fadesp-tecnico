package com.financeiro.fadesp.repository;

import com.financeiro.fadesp.model.Pagamento;
import com.financeiro.fadesp.model.enumeration.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    @Query(
            value = "SELECT p " +
                    "  FROM Pagamento p " +
                    " WHERE p.numeroDocumento = :numeroDocumento ", nativeQuery = false
    )
    List<Pagamento> listarPorNumeroDoc(
            @Param("numeroDocumento") String numeroDocumento
    );

    @Query(
            value = "SELECT p " +
                    "  FROM Pagamento p " +
                    " WHERE p.statusPagamento = :status ", nativeQuery = false
    )
    List<Pagamento> listarPorStatus(
            @Param("status") StatusPagamento status
    );

}
