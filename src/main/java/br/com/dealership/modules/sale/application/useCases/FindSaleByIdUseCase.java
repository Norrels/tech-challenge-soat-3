package br.com.dealership.modules.sale.application.useCases;

import br.com.dealership.modules.sale.domain.entities.SaleOrder;
import br.com.dealership.modules.sale.domain.exception.InvalidSaleException;
import br.com.dealership.modules.sale.domain.exception.SaleNotFoundException;
import br.com.dealership.modules.sale.domain.ports.out.SaleRepositoryPort;

public class FindSaleByIdUseCase {
    private final SaleRepositoryPort saleRepositoryPort;

    public FindSaleByIdUseCase(SaleRepositoryPort saleRepositoryPort) {
        this.saleRepositoryPort = saleRepositoryPort;
    }

    public SaleOrder execute(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidSaleException("Sale ID cannot be null or empty");
        }

        SaleOrder saleOrder = saleRepositoryPort.getSaleById(id);

        if (saleOrder == null) {
            throw new SaleNotFoundException("Sale not found with ID: " + id);
        }

        return saleOrder;
    }
}
