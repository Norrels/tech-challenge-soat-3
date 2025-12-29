package br.com.dealership.modules.sale.adapter.http.dto;

import br.com.dealership.modules.sale.domain.entities.SaleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Data Transfer Object for creating a new sale order")
public class CreateSaleDTO {

    @NotBlank(message = "Vehicle VIN is required")
    @Schema(description = "Vehicle Identification Number", example = "1HGBH41JXMN109186", required = true)
    private String vehicleVin;

    @NotNull(message = "Sale price is required")
    @Min(value = 1, message = "Sale price must be greater than zero")
    @Schema(description = "Sale price", example = "45000.00", required = true)
    private Double salePrice;

    public CreateSaleDTO() {
    }

    public CreateSaleDTO(String vehicleVin, Double salePrice) {
        this.vehicleVin = vehicleVin;
        this.salePrice = salePrice;
    }

    public String getVehicleVin() {
        return vehicleVin;
    }

    public void setVehicleVin(String vehicleVin) {
        this.vehicleVin = vehicleVin;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }
}