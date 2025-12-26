package br.com.dealership.modules.vehicle.domain.entities;

import java.math.BigDecimal;
import java.util.UUID;

public class Vehicle {
    private UUID id;
    private String make;
    private String model;
    private int year;
    private String vin;
    private String color;
    private VehicleStatus status;
    private BigDecimal price;

    public Vehicle(UUID id, String make, String model, int year, String vin, String color, VehicleStatus status, BigDecimal price) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.color = color;
        this.status = status;
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void validate() {
        if (this.make == null || this.getMake().isBlank()) {
            throw new IllegalArgumentException("Vehicle make cannot be null or empty");
        }

        if (this.price == null || this.price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Vehicle price cannot be null or negative");
        }

        if (this.getModel() == null || this.getModel().isBlank()) {
            throw new IllegalArgumentException("Vehicle model cannot be null or empty");
        }

        if (this.getYear() <= 0) {
            throw new IllegalArgumentException("Vehicle year must be positive");
        }

        if (this.getVin() == null || this.getVin().isBlank()) {
            throw new IllegalArgumentException("Vehicle VIN cannot be null or empty");
        }

        if (this.getStatus() == null) {
            throw new IllegalArgumentException("Vehicle status cannot be null");
        }
    }

    public void markAsSold() {
        if(this.status == VehicleStatus.SOLD) {
            throw new IllegalStateException("Vehicle is already marked as sold");
        }

        this.status = VehicleStatus.SOLD;
    }
}
