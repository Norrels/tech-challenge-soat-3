package br.com.dealership.modules.sale.application.useCases;

import br.com.dealership.modules.sale.domain.entities.SaleOrder;
import br.com.dealership.modules.sale.domain.ports.out.SaleRepositoryPort;

import java.util.List;

public class FindAllSalesUseCase {
    private final SaleRepositoryPort saleRepositoryPort;

    public FindAllSalesUseCase(SaleRepositoryPort saleRepositoryPort) {
        this.saleRepositoryPort = saleRepositoryPort;
    }

    public List<SaleOrder> execute() {
        return saleRepositoryPort.getAllSales();
    }
}