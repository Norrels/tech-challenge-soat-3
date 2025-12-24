package br.com.dealership.modules.sale.adapter.database.repositories;

import br.com.dealership.modules.sale.domain.entities.SaleOrder;
import br.com.dealership.modules.sale.domain.ports.out.SaleRepositoryPort;
import br.com.dealership.modules.sale.mapper.SaleMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class SaleRepositoryAdapter implements SaleRepositoryPort {
    private final SaleRepository repository;
    private final SaleMapper saleMapper;

    public SaleRepositoryAdapter(SaleRepository repository, SaleMapper mapper) {
        this.repository = repository;
        this.saleMapper = mapper;
    }

    @Override
    public SaleOrder save(SaleOrder saleOrder) {
        return saleMapper
                .mapToDomain(repository
                        .save(saleMapper.mapToEntity(saleOrder)));
    }

    @Override
    public List<SaleOrder> getAllSales() {
        return repository.findAll().stream()
                .map(saleMapper::mapToDomain)
                .toList();
    }

    @Override
    public List<SaleOrder> getAllSalesByCustomerCpf(String customerCpf) {
        return repository.findAllByCustomerCpf(customerCpf).stream()
                .map(saleMapper::mapToDomain)
                .toList();
    }

    @Override
    public SaleOrder getSaleById(String id) {
        return repository.findById(Long.getLong(id))
                .map(saleMapper::mapToDomain)
                .orElse(null);
    }
}
