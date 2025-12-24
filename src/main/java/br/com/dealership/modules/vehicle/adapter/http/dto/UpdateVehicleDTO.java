package br.com.dealership.modules.vehicle.adapter.http.dto;

import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Data Transfer Object for updating a vehicle")
public class UpdateVehicleDTO {

    @NotBlank(message = "Vehicle make is required")
    @Schema(description = "Vehicle manufacturer", example = "Toyota", required = true)
    private String make;

    @NotBlank(message = "Vehicle model is required")
    @Schema(description = "Vehicle model", example = "Corolla", required = true)
    private String model;

    @NotNull(message = "Vehicle year is required")
    @Min(value = 1900, message = "Vehicle year must be greater than or equal to 1900")
    @Schema(description = "Manufacturing year", example = "2023", required = true)
    private Integer year;

    @Schema(description = "Vehicle color", example = "Blue")
    private String color;

    @NotNull(message = "Vehicle status is required")
    @Schema(description = "Current status of the vehicle", example = "AVAILABLE", required = true)
    private VehicleStatus status;

    @Min(value = 0, message = "Vehicle price must be non-negative")
    @NotNull(message = "Vehicle price is required")
    @Schema(description = "Price of the vehicle", example = "25000.00", required = true)
    private BigDecimal price;

    public UpdateVehicleDTO() {
    }

    public UpdateVehicleDTO(String make, String model, Integer year, String color, VehicleStatus status) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.status = status;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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
}