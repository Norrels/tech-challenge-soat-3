package br.com.dealership.modules.sale.application.useCases;

import br.com.dealership.modules.sale.domain.entities.SaleOrder;
import br.com.dealership.modules.sale.domain.exception.InvalidSaleException;
import br.com.dealership.modules.sale.domain.ports.out.SaleRepositoryPort;

public class CreateSaleUseCase {
    private final SaleRepositoryPort saleRepositoryPort;

    public CreateSaleUseCase(SaleRepositoryPort saleRepositoryPort) {
        this.saleRepositoryPort = saleRepositoryPort;
    }

    public SaleOrder execute(SaleOrder saleOrder) {
        if (saleOrder == null) {
            throw new InvalidSaleException("Sale order cannot be null");
        }

        saleOrder.validate();

        return saleRepositoryPort.save(saleOrder);
    }
}
