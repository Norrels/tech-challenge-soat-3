package br.com.dealership.modules.vehicle.mapper;

import br.com.dealership.modules.vehicle.adapter.database.models.VehicleEntity;
import br.com.dealership.modules.vehicle.adapter.http.dto.CreateVehicleDTO;
import br.com.dealership.modules.vehicle.adapter.http.dto.UpdateVehicleDTO;
import br.com.dealership.modules.vehicle.domain.entities.Vehicle;

import java.util.UUID;

public class VehicleMapper {

    public Vehicle mapToDomain(VehicleEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Vehicle(
                entity.getId(),
                entity.getMake(),
                entity.getModel(),
                entity.getYear(),
                entity.getVin(),
                entity.getColor(),
                entity.getStatus(),
                entity.getPrice()
        );
    }

    public VehicleEntity mapToEntity(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }

        return new VehicleEntity(
                vehicle.getId(),
                vehicle.getMake(),
                vehicle.getModel(),
                vehicle.getYear(),
                vehicle.getVin(),
                vehicle.getColor(),
                vehicle.getStatus(),
                vehicle.getPrice()
        );
    }

    public Vehicle mapFromCreateDTO(CreateVehicleDTO dto) {
        if (dto == null) {
            return null;
        }

        return new Vehicle(
                null,
                dto.getMake(),
                dto.getModel(),
                dto.getYear(),
                dto.getVin(),
                dto.getColor(),
                dto.getStatus(),
                dto.getPrice()
        );
    }

    public Vehicle mapFromUpdateDTO(UpdateVehicleDTO dto, String vin) {
        if (dto == null) {
            return null;
        }

        return new Vehicle(
                null,
                dto.getMake(),
                dto.getModel(),
                dto.getYear(),
                vin,
                dto.getColor(),
                dto.getStatus(),
                dto.getPrice()
        );
    }
}
