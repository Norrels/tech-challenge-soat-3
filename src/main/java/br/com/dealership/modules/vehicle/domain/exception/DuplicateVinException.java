package br.com.dealership.modules.vehicle.domain.exception;

public class DuplicateVinException extends RuntimeException {
    public DuplicateVinException(String vin) {
        super(String.format("Vehicle with VIN '%s' already exists", vin));
    }
}