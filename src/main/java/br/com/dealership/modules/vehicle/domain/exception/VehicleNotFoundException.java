package br.com.dealership.modules.vehicle.domain.exception;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String message) {
        super(message);
    }

    public VehicleNotFoundException(String vin, String field) {
        super(String.format("Vehicle not found with %s: %s", field, vin));
    }
}