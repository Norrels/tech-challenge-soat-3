package br.com.dealership.modules.vehicle.application.useCases;

import br.com.dealership.modules.shared.dtos.FindVehicleDTO;
import br.com.dealership.modules.shared.useCases.FindAvaliableVehicleByIdUseCasePort;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;
import br.com.dealership.modules.vehicle.domain.ports.out.VehicleRepositoryPort;

import java.util.Optional;

public class FindAvaliableVehicleByIdUseCase implements FindAvaliableVehicleByIdUseCasePort {
    private final VehicleRepositoryPort repository;

    public FindAvaliableVehicleByIdUseCase(VehicleRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Optional<FindVehicleDTO> execute(String vehicleVin) {
        return repository.getVehicleByVin(vehicleVin).map(vehicle ->
                new FindVehicleDTO(
                        vehicle.getId(),
                        vehicle.getVin(),
                        vehicle.getStatus()
                )
        );
    }
}
