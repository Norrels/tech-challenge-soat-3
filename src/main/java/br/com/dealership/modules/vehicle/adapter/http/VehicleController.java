package br.com.dealership.modules.vehicle.adapter.http;

import br.com.dealership.modules.vehicle.adapter.http.dto.CreateVehicleDTO;
import br.com.dealership.modules.vehicle.domain.entities.Vehicle;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;
import br.com.dealership.modules.vehicle.domain.exception.VehicleNotFoundException;
import br.com.dealership.modules.vehicle.domain.ports.in.VehicleServicePort;
import br.com.dealership.modules.vehicle.mapper.VehicleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import br.com.dealership.exception.ErrorResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicles")
@Tag(name = "Vehicles", description = "Vehicle management endpoints")
public class VehicleController {

    private final VehicleServicePort vehicleServicePort;
    private final VehicleMapper vehicleMapper;

    public VehicleController(VehicleServicePort vehicleServicePort, VehicleMapper vehicleMapper) {
        this.vehicleServicePort = vehicleServicePort;
        this.vehicleMapper = vehicleMapper;
    }

    @PostMapping()
    @Operation(summary = "Create a new vehicle", description = "Creates a new vehicle in the system")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vehicle created successfully",
                    content = @Content(schema = @Schema(implementation = Vehicle.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid vehicle data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Vehicle with this VIN already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Vehicle> createVehicle(@Valid @RequestBody CreateVehicleDTO createVehicleDTO) {
        Vehicle vehicle = vehicleMapper.mapFromCreateDTO(createVehicleDTO);
        Vehicle createdVehicle = vehicleServicePort.createVehicle(vehicle);
        return ResponseEntity.ok(createdVehicle);
    }

    @GetMapping("/{vin}")
    @Operation(summary = "Get vehicle by VIN", description = "Retrieves a vehicle by its VIN (Vehicle Identification Number)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vehicle found",
                    content = @Content(schema = @Schema(implementation = Vehicle.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Vehicle not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid VIN parameter",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<Vehicle> getVehicleByVin(
            @Parameter(description = "Vehicle Identification Number") @PathVariable String vin) {
        Vehicle vehicle = vehicleServicePort.getVehicleByVin(vin)
                .orElseThrow(() -> new VehicleNotFoundException(vin, "VIN"));
        return ResponseEntity.ok(vehicle);
    }

    @GetMapping("/available")
    @Operation(summary = "Get all available vehicles", description = "Retrieves all vehicles with AVAILABLE status")
    @ApiResponse(
            responseCode = "200",
            description = "List of available vehicles",
            content = @Content(schema = @Schema(implementation = Vehicle.class))
    )
    public ResponseEntity<List<Vehicle>> getAllAvailableVehicles() {
        List<Vehicle> availableVehicles = vehicleServicePort.getAllVehiclesByStatus(VehicleStatus.AVAILABLE);
        return ResponseEntity.ok(availableVehicles);
    }

    @GetMapping("/sold")
    @Operation(summary = "Get all sold vehicles", description = "Retrieves all vehicles with SOLD status")
    @ApiResponse(
            responseCode = "200",
            description = "List of sold vehicles",
            content = @Content(schema = @Schema(implementation = Vehicle.class))
    )
    public ResponseEntity<List<Vehicle>> getAllSoldVehicles() {
        List<Vehicle> soldVehicles = vehicleServicePort.getAllVehiclesByStatus(VehicleStatus.SOLD);
        return ResponseEntity.ok(soldVehicles);
    }


}
