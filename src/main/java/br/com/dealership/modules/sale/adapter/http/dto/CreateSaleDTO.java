package br.com.dealership.modules.sale.adapter.http.dto;

import br.com.dealership.modules.sale.domain.entities.SaleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Data Transfer Object for creating a new sale order")
public class CreateSaleDTO {

    @NotBlank(message = "Customer name is required")
    @Schema(description = "Customer's full name", example = "João Silva", required = true)
    private String customerName;

    @NotBlank(message = "Customer CPF is required")
    @Schema(description = "Customer's CPF (Brazilian ID)", example = "12345678900", required = true)
    private String customerCpf;

    @NotBlank(message = "Vehicle VIN is required")
    @Schema(description = "Vehicle Identification Number", example = "1HGBH41JXMN109186", required = true)
    private String vehicleVin;

    @NotNull(message = "Sale price is required")
    @Min(value = 1, message = "Sale price must be greater than zero")
    @Schema(description = "Sale price", example = "45000.00", required = true)
    private Double salePrice;

    @NotNull(message = "Vehicle ID is required")
    @Schema(description = "Vehicle ID", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    private UUID vehicleId;

    @Schema(description = "Sale status", example = "PENDING")
    private SaleStatus status;

    public CreateSaleDTO() {
    }

    public CreateSaleDTO(String customerName, String customerCpf, String vehicleVin, Double salePrice, UUID vehicleId, SaleStatus status) {
        this.customerName = customerName;
        this.customerCpf = customerCpf;
        this.vehicleVin = vehicleVin;
        this.salePrice = salePrice;
        this.vehicleId = vehicleId;
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCpf() {
        return customerCpf;
    }

    public void setCustomerCpf(String customerCpf) {
        this.customerCpf = customerCpf;
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

    public UUID getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(UUID vehicleId) {
        this.vehicleId = vehicleId;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }
}