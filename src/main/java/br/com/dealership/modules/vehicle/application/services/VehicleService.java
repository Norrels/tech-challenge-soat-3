package br.com.dealership.modules.vehicle.application.services;

import br.com.dealership.modules.vehicle.application.usecases.CreateVehicleUseCase;
import br.com.dealership.modules.vehicle.application.usecases.GetVehicleByStatusUseCase;
import br.com.dealership.modules.vehicle.application.usecases.GetVehicleByVinUseCase;
import br.com.dealership.modules.vehicle.domain.entities.Vehicle;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;
import br.com.dealership.modules.vehicle.domain.ports.in.VehicleServicePort;

import java.util.List;
import java.util.Optional;

public class VehicleService implements VehicleServicePort {
    private final CreateVehicleUseCase createVehicleUseCase;
    private final GetVehicleByVinUseCase getVehicleByVinUseCase;
    private final GetVehicleByStatusUseCase getVehicleByStatusUseCase;

    public VehicleService(CreateVehicleUseCase createVehicleUseCase, GetVehicleByVinUseCase getVehicleByVinUseCase, GetVehicleByStatusUseCase getVehicleByStatusUseCase) {
        this.createVehicleUseCase = createVehicleUseCase;
        this.getVehicleByVinUseCase = getVehicleByVinUseCase;
        this.getVehicleByStatusUseCase = getVehicleByStatusUseCase;
    }

    @Override
    public List<Vehicle> getAllVehiclesByStatus(VehicleStatus status) {
        return getVehicleByStatusUseCase.execute(status);
    }

    @Override
    public Optional<Vehicle> getVehicleByVin(String vin) {
        return getVehicleByVinUseCase.execute(vin);
    }

    @Override
    public Vehicle createVehicle(Vehicle vehicle) {
        return createVehicleUseCase.execute(vehicle);
    }
}
