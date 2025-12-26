package br.com.dealership.modules.vehicle.application.useCases;

import br.com.dealership.modules.shared.useCases.MarkVehicleAsSoldUseCasePort;
import br.com.dealership.modules.vehicle.domain.ports.out.VehicleRepositoryPort;

import java.util.UUID;

public class MarkVehicleAsSoldUseCase implements MarkVehicleAsSoldUseCasePort {
    private final VehicleRepositoryPort repository;

    public MarkVehicleAsSoldUseCase(VehicleRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void execute(UUID id) {
        repository.getVehicleById(id).ifPresent(vehicle -> {
            vehicle.markAsSold();
            repository.saveVehicle(vehicle);
        });
    }
}
