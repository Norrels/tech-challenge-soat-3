package br.com.dealership.modules.shared.dtos;

import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;

import java.util.UUID;

public record FindVehicleDTO(UUID vehicleId, String vin, VehicleStatus status) {
}
