package br.com.dealership.modules.sale.mapper;

import br.com.dealership.modules.sale.adapter.database.entities.SaleOrderEntity;
import br.com.dealership.modules.sale.adapter.http.dto.CreateSaleDTO;
import br.com.dealership.modules.sale.adapter.http.dto.UpdateSaleDTO;
import br.com.dealership.modules.sale.domain.entities.SaleOrder;
import br.com.dealership.modules.sale.domain.entities.SaleStatus;
import br.com.dealership.modules.sale.domain.entities.valueobjects.CPF;

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
                entity.getStatus(),
                entity.getSaleDate()
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
                saleOrder.getVehicleId(),
                saleOrder.getStatus(),
                saleOrder.getSaleDate()
        );
    }

    public SaleOrder mapFromCreateDTO(CreateSaleDTO dto, String customerName, String customerCpf) {
        if (dto == null) {
            return null;
        }

        CPF cpf = new CPF(customerCpf);

        return new SaleOrder(
                null,
                customerName,
                cpf,
                dto.getVehicleVin(),
                dto.getSalePrice(),
                null,
                SaleStatus.PENDING,
                null
        );
    }

    public SaleOrder mapFromUpdateDTO(UpdateSaleDTO dto, SaleOrder existingSaleOrder) {
        if (dto == null || existingSaleOrder == null) {
            return existingSaleOrder;
        }

        existingSaleOrder.setStatus(dto.getStatus());
        return existingSaleOrder;
    }
}
