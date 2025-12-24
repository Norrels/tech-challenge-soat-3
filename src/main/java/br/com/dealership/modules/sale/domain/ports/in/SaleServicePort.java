package br.com.dealership.modules.sale.domain.ports.in;

import br.com.dealership.modules.sale.domain.entities.SaleOrder;

import java.util.List;

public interface SaleServicePort {
    SaleOrder getSaleById(String id);
    SaleOrder createSale(SaleOrder sale);
    List<SaleOrder> getAllSales();
    List<SaleOrder> getAllSalesByCustomerCPF(String cpf);
}
