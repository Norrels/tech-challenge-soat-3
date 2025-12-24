package br.com.dealership.modules.sale.application.services;

import br.com.dealership.modules.sale.application.useCases.CreateSaleUseCase;
import br.com.dealership.modules.sale.application.useCases.FindAllSaleByCustomerCPFUseCase;
import br.com.dealership.modules.sale.application.useCases.FindAllSalesUseCase;
import br.com.dealership.modules.sale.application.useCases.FindSaleByIdUseCase;
import br.com.dealership.modules.sale.domain.entities.SaleOrder;
import br.com.dealership.modules.sale.domain.ports.in.SaleServicePort;

import java.util.List;

public class SaleService implements SaleServicePort {
    private final CreateSaleUseCase createSaleUseCase;
    private final FindSaleByIdUseCase findSaleByIdUseCase;
    private final FindAllSalesUseCase findAllSalesUseCase;
    private final FindAllSaleByCustomerCPFUseCase findAllSaleByCustomerCPFUseCase;

    public SaleService(CreateSaleUseCase createSaleUseCase, FindSaleByIdUseCase findSaleByIdUseCase, FindAllSalesUseCase findAllSalesUseCase, FindAllSaleByCustomerCPFUseCase findAllSaleByCustomerCPFUseCase) {
        this.createSaleUseCase = createSaleUseCase;
        this.findSaleByIdUseCase = findSaleByIdUseCase;
        this.findAllSalesUseCase = findAllSalesUseCase;
        this.findAllSaleByCustomerCPFUseCase = findAllSaleByCustomerCPFUseCase;
    }

    @Override
    public SaleOrder getSaleById(String id) {
        return findSaleByIdUseCase.execute(id);
    }

    @Override
    public SaleOrder createSale(SaleOrder sale) {
        return createSaleUseCase.execute(sale);
    }

    @Override
    public List<SaleOrder> getAllSales() {
        return findAllSalesUseCase.execute();
    }

    @Override
    public List<SaleOrder> getAllSalesByCustomerCPF(String cpf) {
        return findAllSaleByCustomerCPFUseCase.execute(cpf);
    }
}
