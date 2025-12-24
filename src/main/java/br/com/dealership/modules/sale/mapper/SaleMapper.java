package br.com.dealership.modules.sale.mapper;

import br.com.dealership.modules.sale.adapter.database.entities.SaleOrderEntity;
import br.com.dealership.modules.sale.domain.entities.SaleOrder;

public class SaleMapper {

    public SaleOrder mapToDomain(SaleOrderEntity entity) {
        if (entity == null) {
            return null;
        }

        return new SaleOrder(
                entity.getId(),
                entity.getCustomerName(),
                entity.getCustomerCpf(),
                entity.getVehicleVin(),
                entity.getSalePrice(),
                entity.getVehicleId(),
                entity.getStatus()
        );
    }

    public SaleOrderEntity mapToEntity(SaleOrder saleOrder) {
        if (saleOrder == null) {
            return null;
        }

        return new SaleOrderEntity(
                saleOrder.getId(),
                saleOrder.getCustomerName(),
                saleOrder.getCustomerCpf(),
                saleOrder.getVehicleVin(),
                saleOrder.getSalePrice(),
                saleOrder.getVihicleId(),
                saleOrder.getStatus()
        );
    }
}
