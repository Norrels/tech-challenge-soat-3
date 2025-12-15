package br.com.dealership.modules.vehicle.domain.exception;

public class InvalidVehicleException extends RuntimeException {
    public InvalidVehicleException(String message) {
        super(message);
    }
}