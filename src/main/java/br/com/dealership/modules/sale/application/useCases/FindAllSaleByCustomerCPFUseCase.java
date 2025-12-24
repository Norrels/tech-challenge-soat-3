package br.com.dealership.modules.sale.application.useCases;

import br.com.dealership.modules.sale.domain.entities.SaleOrder;
import br.com.dealership.modules.sale.domain.entities.valueobjects.CPF;
import br.com.dealership.modules.sale.domain.exception.InvalidSaleException;
import br.com.dealership.modules.sale.domain.ports.out.SaleRepositoryPort;

import java.util.List;

public class FindAllSaleByCustomerCPFUseCase {
    private final SaleRepositoryPort saleRepositoryPort;

    public FindAllSaleByCustomerCPFUseCase(SaleRepositoryPort saleRepositoryPort) {
        this.saleRepositoryPort = saleRepositoryPort;
    }

    public List<SaleOrder> execute(String customerCpf) {
        if (customerCpf == null || customerCpf.isBlank()) {
            throw new InvalidSaleException("Customer CPF cannot be null or empty");
        }

        try {
            new CPF(customerCpf);
        } catch (IllegalArgumentException e) {
            throw new InvalidSaleException("Invalid CPF format: " + e.getMessage());
        }

        return saleRepositoryPort.getAllSales(customerCpf);
    }
}
