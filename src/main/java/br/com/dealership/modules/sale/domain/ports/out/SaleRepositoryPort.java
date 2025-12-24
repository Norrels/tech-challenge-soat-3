package br.com.dealership.modules.sale.domain.ports.out;

import br.com.dealership.modules.sale.domain.entities.SaleOrder;

import java.util.List;

public interface SaleRepositoryPort {
    SaleOrder save(SaleOrder saleOrder);
    List<SaleOrder> getAllSales();
    List<SaleOrder> getAllSalesByCustomerCpf(String cpfClient);
    SaleOrder getSaleById(String id);
}
