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

    public SaleOrder mapFromCreateDTO(CreateSaleDTO dto) {
        if (dto == null) {
            return null;
        }

        CPF cpf = new CPF(dto.getCustomerCpf());
        SaleStatus status = dto.getStatus() != null ? dto.getStatus() : SaleStatus.PENDING;

        return new SaleOrder(
                null,
                dto.getCustomerName(),
                cpf,
                dto.getVehicleVin(),
                dto.getSalePrice(),
                dto.getVehicleId(),
                status
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
