package br.com.dealership.modules.sale.adapter.database.repositories;

import br.com.dealership.modules.sale.adapter.database.entities.SaleOrderEntity;
import br.com.dealership.modules.sale.domain.entities.valueobjects.CPF;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SaleRepository extends JpaRepository<SaleOrderEntity, Long> {
    List<SaleOrderEntity> findAllByCustomerCpf(CPF customerCpf);
}
