package br.com.dealership.modules.vehicle.adapter.database.models;

import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "vehicles")
public class VehicleEntity {
    @Id
    private UUID id;

    private String make;
    private String model;
    private int year;

    @Column(nullable = false)
    private String vin;

    private String color;

    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    public VehicleEntity() {
    }

    public VehicleEntity(UUID id, String make, String model, int year, String vin, String color, VehicleStatus status) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.color = color;
        this.status = status;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
