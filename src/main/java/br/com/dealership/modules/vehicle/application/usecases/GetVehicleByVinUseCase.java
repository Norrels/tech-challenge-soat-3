package br.com.dealership.modules.vehicle.application.useCases;

import br.com.dealership.modules.vehicle.domain.entities.Vehicle;
import br.com.dealership.modules.vehicle.domain.ports.out.VehicleRepositoryPort;

import java.util.Optional;

public class GetVehicleByVinUseCase {
    private final VehicleRepositoryPort vehicleRepositoryPort;

    public GetVehicleByVinUseCase(VehicleRepositoryPort vehicleRepositoryPort) {
        this.vehicleRepositoryPort = vehicleRepositoryPort;
    }

    public Optional<Vehicle> execute(String vin) {
        if (vin == null || vin.isBlank()) {
            throw new IllegalArgumentException("VIN cannot be null or empty");
        }

        return vehicleRepositoryPort.getVehicleByVin(vin);
    }
}