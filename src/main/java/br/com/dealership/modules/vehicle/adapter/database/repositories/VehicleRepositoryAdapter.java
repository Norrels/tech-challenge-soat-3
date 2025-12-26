package br.com.dealership.modules.vehicle.adapter.database.repositories;

import br.com.dealership.modules.vehicle.domain.entities.Vehicle;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;
import br.com.dealership.modules.vehicle.domain.ports.out.VehicleRepositoryPort;
import br.com.dealership.modules.vehicle.mapper.VehicleMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class VehicleRepositoryAdapter implements VehicleRepositoryPort {
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleRepositoryAdapter(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
    }

    @Override
    public List<Vehicle> getAllByStatus(VehicleStatus status) {
        return vehicleRepository.findAllByStatusOrderByPriceAsc(status).stream()
                .map(vehicleMapper::mapToDomain)
                .toList();
    }

    @Override
    public Optional<Vehicle> getVehicleByVin(String id) {
        return vehicleRepository.findByVin(id)
                .map(vehicleMapper::mapToDomain);
    }

    @Override
    public Optional<Vehicle> getVehicleById(UUID id) {
        return vehicleRepository.findById(id)
                .map(vehicleMapper::mapToDomain);
    }

    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleMapper.mapToDomain(
                vehicleRepository.save(
                        vehicleMapper.mapToEntity(vehicle)
                )
        );
    }
}
