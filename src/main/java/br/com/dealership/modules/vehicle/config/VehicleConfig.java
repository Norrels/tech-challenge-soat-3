package br.com.dealership.modules.vehicle.config;

import br.com.dealership.modules.vehicle.application.services.VehicleService;
import br.com.dealership.modules.vehicle.application.usecases.CreateVehicleUseCase;
import br.com.dealership.modules.vehicle.application.usecases.GetVehicleByStatusUseCase;
import br.com.dealership.modules.vehicle.application.usecases.GetVehicleByVinUseCase;
import br.com.dealership.modules.vehicle.application.usecases.UpdateVehicleUseCase;
import br.com.dealership.modules.vehicle.domain.ports.out.VehicleRepositoryPort;
import br.com.dealership.modules.vehicle.mapper.VehicleMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VehicleConfig {

    @Bean
    public VehicleMapper vehicleMapper() {
        return new VehicleMapper();
    }

    @Bean
    public CreateVehicleUseCase createVehicleUseCase(VehicleRepositoryPort repositoryPort) {
        return new CreateVehicleUseCase(repositoryPort);
    }

    @Bean
    public GetVehicleByVinUseCase getVehicleByVinUseCase(VehicleRepositoryPort repositoryPort) {
        return new GetVehicleByVinUseCase(repositoryPort);
    }

    @Bean
    public GetVehicleByStatusUseCase getVehicleByStatusUseCase(VehicleRepositoryPort repositoryPort) {
        return new GetVehicleByStatusUseCase(repositoryPort);
    }

    @Bean
    public UpdateVehicleUseCase updateVehicleUseCase(VehicleRepositoryPort repositoryPort) {
        return new UpdateVehicleUseCase(repositoryPort);
    }

    @Bean
    public VehicleService vehicleService(CreateVehicleUseCase createVehicleUseCase,
                                         GetVehicleByVinUseCase getVehicleByVinUseCase,
                                         GetVehicleByStatusUseCase getVehicleByStatusUseCase,
                                         UpdateVehicleUseCase updateVehicleUseCase) {
        return new VehicleService(createVehicleUseCase, getVehicleByVinUseCase, getVehicleByStatusUseCase, updateVehicleUseCase);
    }

}