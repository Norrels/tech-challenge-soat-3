package br.com.dealership.modules.sale.adapter.http.dto;

import br.com.dealership.modules.sale.domain.entities.SaleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Data Transfer Object for updating a sale order status")
public class UpdateSaleDTO {

    @NotNull(message = "Sale status is required")
    @Schema(description = "New sale status", example = "COMPLETED", required = true)
    private SaleStatus status;

    public UpdateSaleDTO() {
    }

    public UpdateSaleDTO(SaleStatus status) {
        this.status = status;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }
}