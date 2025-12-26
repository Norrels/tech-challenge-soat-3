package br.com.dealership.modules.vehicle.adapter.database.repositories;

import br.com.dealership.modules.vehicle.adapter.database.models.VehicleEntity;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<VehicleEntity, UUID> {
    Optional<VehicleEntity> findByVin(String vin);
    List<VehicleEntity> findAllByStatusOrderByPriceAsc(VehicleStatus status);
}
