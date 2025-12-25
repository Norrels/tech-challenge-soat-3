package br.com.dealership.modules.shared.useCases;

import br.com.dealership.modules.shared.dtos.FindVehicleDTO;

import java.util.Optional;

public interface FindAvaliableVehicleByIdUseCasePort {
    Optional<FindVehicleDTO> execute(String vehicleVin);
}
