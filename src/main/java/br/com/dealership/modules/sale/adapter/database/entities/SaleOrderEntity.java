package br.com.dealership.modules.sale.adapter.database.entities;

import br.com.dealership.modules.sale.adapter.database.converters.CPFConverter;
import br.com.dealership.modules.sale.domain.entities.SaleStatus;
import br.com.dealership.modules.sale.domain.entities.valueobjects.CPF;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "sale_orders")
public class SaleOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_cpf", nullable = false)
    @Convert(converter = CPFConverter.class)
    private CPF customerCpf;

    @Column(name = "vehicle_vin", nullable = false)
    private String vehicleVin;

    @Column(name = "sale_price", nullable = false)
    private Double salePrice;

    @Column(name = "vehicle_id", nullable = false)
    private UUID vehicleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SaleStatus status;

    public SaleOrderEntity() {
    }

    public SaleOrderEntity(Long id, String customerName, CPF customerCpf, String vehicleVin, Double salePrice, UUID vehicleId, SaleStatus status) {
        this.id = id;
        this.customerName = customerName;
        this.customerCpf = customerCpf;
        this.vehicleVin = vehicleVin;
        this.salePrice = salePrice;
        this.vehicleId = vehicleId;

        if(status == null) {
            this.status = SaleStatus.PENDING;
        } else {
            this.status = status;
        }
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
