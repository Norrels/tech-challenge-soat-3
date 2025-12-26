package br.com.dealership.modules.sale.domain.entities;

import br.com.dealership.modules.sale.domain.entities.valueobjects.CPF;
import br.com.dealership.modules.sale.domain.exception.InvalidSaleException;

import java.util.UUID;

public class SaleOrder {
    private Long id;
    private String customerName;
    private CPF customerCpf;
    private String vehicleVin;
    private Double salePrice;
    private UUID vihicleId;
    private SaleStatus status;


    public SaleOrder(Long id, String customerName, CPF customerCpf, String vehicleVin, Double salePrice, UUID vihicleId, SaleStatus status) {
        this.id = id;
        this.customerName = customerName;
        this.customerCpf = customerCpf;
        this.vehicleVin = vehicleVin;
        this.salePrice = salePrice;
        this.vihicleId = vihicleId;
        this.status = status;
    }

    public CPF getCustomerCpf() {
        return customerCpf;
    }

    public void setCustomerCpf(CPF customerCpf) {
        this.customerCpf = customerCpf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public UUID getVihicleId() {
        return vihicleId;
    }

    public void setVihicleId(UUID vihicleId) {
        this.vihicleId = vihicleId;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setStatus(SaleStatus status) {
        this.status = status;
    }

    public void validate() {
        if (customerName == null || customerName.isBlank()) {
            throw new InvalidSaleException("Customer name is required");
        }

        if (customerCpf == null) {
            throw new InvalidSaleException("Customer CPF is required");
        }

        if (vehicleVin == null || vehicleVin.isBlank()) {
            throw new InvalidSaleException("Vehicle VIN is required");
        }

        if (vihicleId == null) {
            throw new InvalidSaleException("Vehicle ID is required");
        }

        if (salePrice == null || salePrice <= 0) {
            throw new InvalidSaleException("Sale price must be greater than zero");
        }

        if (status == null) {
            throw new InvalidSaleException("Sale status is required");
        }
    }

    public void markAsPaid() {
        if( this.status != SaleStatus.PENDING ) {
            throw new InvalidSaleException("Only pending sales can be marked as paid");
        }

        this.status = SaleStatus.COMPLETED;
    }
}
