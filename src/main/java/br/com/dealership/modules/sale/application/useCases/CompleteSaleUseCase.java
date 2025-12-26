package br.com.dealership.modules.sale.application.useCases;

import br.com.dealership.modules.sale.domain.entities.SaleOrder;
import br.com.dealership.modules.sale.domain.entities.SaleStatus;
import br.com.dealership.modules.sale.domain.exception.InvalidSaleStatusException;
import br.com.dealership.modules.sale.domain.exception.SaleNotFoundException;
import br.com.dealership.modules.sale.domain.ports.out.SaleRepositoryPort;

public class CompleteSaleUseCase {
    private final SaleRepositoryPort repository;

    public CompleteSaleUseCase(SaleRepositoryPort repository) {
        this.repository = repository;
    }

    public SaleOrder execute(String id, Boolean paymentSuccess) {
        SaleOrder saleOrder = repository.getSaleById(id);

        if(saleOrder == null) {
            throw new SaleNotFoundException("Sale with id " + id + " does not exist");
        }

        if(saleOrder.getStatus() != SaleStatus.PENDING) {
            throw new InvalidSaleStatusException("Sale with id " + id + " is not in PENDING status. Current status: " + saleOrder.getStatus());
        }

        if (paymentSuccess) {
            saleOrder.setStatus(SaleStatus.COMPLETED);
        } else {
            saleOrder.setStatus(SaleStatus.CANCELED);
        }

        return repository.save(saleOrder);
    }
}
