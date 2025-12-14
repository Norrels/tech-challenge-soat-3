package br.com.dealership.modules.sale.model;

import br.com.dealership.modules.sale.model.valueobject.CPF;

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
}
