package br.com.dealership.modules.shared.useCases;

import br.com.dealership.modules.shared.dtos.FindVehicleDTO;

import java.util.Optional;

public interface FindAvailableVehicleByIdUseCasePort {
    Optional<FindVehicleDTO> execute(String vehicleVin);
}
