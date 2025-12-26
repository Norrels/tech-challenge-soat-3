package br.com.dealership.modules.shared.useCases;

import java.util.UUID;

public interface MarkVehicleAsSoldUseCasePort {
    void execute(UUID id);
}
