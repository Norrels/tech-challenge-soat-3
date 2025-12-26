package br.com.dealership.integration.vehicle;

import br.com.dealership.modules.vehicle.adapter.http.dto.CreateVehicleDTO;
import br.com.dealership.modules.vehicle.adapter.http.dto.UpdateVehicleDTO;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class VehicleControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private CreateVehicleDTO createVehicleDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        objectMapper = new ObjectMapper();

        createVehicleDTO = new CreateVehicleDTO(
                "Honda",
                "Civic",
                2023,
                "1HGBH41JXMN109186",
                "Black",
                VehicleStatus.AVAILABLE,
                new BigDecimal("25000.00")
        );
    }

    @Test
    @DisplayName("Should create vehicle successfully")
    void shouldCreateVehicleSuccessfully() throws Exception {
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVehicleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.make").value("Honda"))
                .andExpect(jsonPath("$.model").value("Civic"))
                .andExpect(jsonPath("$.year").value(2023))
                .andExpect(jsonPath("$.vin").value("1HGBH41JXMN109186"))
                .andExpect(jsonPath("$.color").value("Black"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andExpect(jsonPath("$.price").value(25000.00));
    }

    @Test
    @DisplayName("Should return 409 when creating vehicle with duplicate VIN")
    void shouldReturn409WhenCreatingVehicleWithDuplicateVin() throws Exception {
        // First creation
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVehicleDTO)))
                .andExpect(status().isOk());

        // Second creation with same VIN
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVehicleDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(containsString("1HGBH41JXMN109186")));
    }

    @Test
    @DisplayName("Should return 400 when creating vehicle with invalid data")
    void shouldReturn400WhenCreatingVehicleWithInvalidData() throws Exception {
        CreateVehicleDTO invalidVehicle = new CreateVehicleDTO(
                null, // make is null
                "Civic",
                2023,
                "1HGBH41JXMN109186",
                "Black",
                VehicleStatus.AVAILABLE,
                new BigDecimal("25000.00")
        );

        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidVehicle)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should get vehicle by VIN successfully")
    void shouldGetVehicleByVinSuccessfully() throws Exception {
        // Create vehicle first
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVehicleDTO)))
                .andExpect(status().isOk());

        // Get vehicle by VIN
        mockMvc.perform(get("/api/v1/vehicles/{vin}", "1HGBH41JXMN109186"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vin").value("1HGBH41JXMN109186"))
                .andExpect(jsonPath("$.make").value("Honda"))
                .andExpect(jsonPath("$.model").value("Civic"));
    }

    @Test
    @DisplayName("Should return 404 when vehicle VIN not found")
    void shouldReturn404WhenVehicleVinNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/vehicles/{vin}", "NONEXISTENT"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("NONEXISTENT")));
    }

    @Test
    @DisplayName("Should get all available vehicles")
    void shouldGetAllAvailableVehicles() throws Exception {
        // Create two available vehicles
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVehicleDTO)))
                .andExpect(status().isOk());

        CreateVehicleDTO secondVehicle = new CreateVehicleDTO(
                "Toyota",
                "Camry",
                2023,
                "DIFFERENTVIN12345",
                "White",
                VehicleStatus.AVAILABLE,
                new BigDecimal("30000.00")
        );

        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondVehicle)))
                .andExpect(status().isOk());

        // Get all available vehicles - should be ordered by price (ascending)
        mockMvc.perform(get("/api/v1/vehicles/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].status", everyItem(is("AVAILABLE"))))
                .andExpect(jsonPath("$[0].price").value(25000.00))
                .andExpect(jsonPath("$[1].price").value(30000.00));
    }

    @Test
    @DisplayName("Should get all sold vehicles")
    void shouldGetAllSoldVehicles() throws Exception {
        // Create a sold vehicle
        CreateVehicleDTO soldVehicle = new CreateVehicleDTO(
                "Ford",
                "Mustang",
                2023,
                "SOLDVIN123456789",
                "Red",
                VehicleStatus.SOLD,
                new BigDecimal("45000.00")
        );

        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(soldVehicle)))
                .andExpect(status().isOk());

        // Get all sold vehicles
        mockMvc.perform(get("/api/v1/vehicles/sold"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("SOLD"));
    }

    @Test
    @DisplayName("Should update vehicle successfully")
    void shouldUpdateVehicleSuccessfully() throws Exception {
        // Create vehicle first
        String createResponse = mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVehicleDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String vehicleId = objectMapper.readTree(createResponse).get("id").asText();

        // Update vehicle
        UpdateVehicleDTO updateDTO = new UpdateVehicleDTO();
        updateDTO.setMake("Honda");
        updateDTO.setModel("Accord");
        updateDTO.setYear(2024);
        updateDTO.setColor("Blue");
        updateDTO.setStatus(VehicleStatus.AVAILABLE);
        updateDTO.setPrice(new BigDecimal("28000.00"));

        mockMvc.perform(put("/api/v1/vehicles/{id}", vehicleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehicleId))
                .andExpect(jsonPath("$.model").value("Accord"))
                .andExpect(jsonPath("$.year").value(2024))
                .andExpect(jsonPath("$.color").value("Blue"))
                .andExpect(jsonPath("$.price").value(28000.00))
                .andExpect(jsonPath("$.vin").value("1HGBH41JXMN109186")); // VIN should not change
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent vehicle")
    void shouldReturn404WhenUpdatingNonExistentVehicle() throws Exception {
        UpdateVehicleDTO updateDTO = new UpdateVehicleDTO();
        updateDTO.setMake("Honda");
        updateDTO.setModel("Accord");
        updateDTO.setYear(2024);
        updateDTO.setColor("Blue");
        updateDTO.setStatus(VehicleStatus.AVAILABLE);
        updateDTO.setPrice(new BigDecimal("28000.00"));

        String fakeId = "123e4567-e89b-12d3-a456-426614174000";

        mockMvc.perform(put("/api/v1/vehicles/{id}", fakeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should filter vehicles correctly by status")
    void shouldFilterVehiclesCorrectlyByStatus() throws Exception {
        // Create available vehicle
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVehicleDTO)))
                .andExpect(status().isOk());

        // Create sold vehicle
        CreateVehicleDTO soldVehicle = new CreateVehicleDTO(
                "BMW",
                "X5",
                2023,
                "BMWVIN123456789",
                "Gray",
                VehicleStatus.SOLD,
                new BigDecimal("55000.00")
        );

        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(soldVehicle)))
                .andExpect(status().isOk());

        // Get available vehicles - should return 1
        mockMvc.perform(get("/api/v1/vehicles/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"));

        // Get sold vehicles - should return 1
        mockMvc.perform(get("/api/v1/vehicles/sold"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("SOLD"));
    }

    @Test
    @DisplayName("Should return vehicles ordered by price ascending")
    void shouldReturnVehiclesOrderedByPriceAscending() throws Exception {
        // Create multiple vehicles with different prices
        CreateVehicleDTO expensiveVehicle = new CreateVehicleDTO(
                "BMW",
                "X5",
                2023,
                "EXPENSIVE123456",
                "Black",
                VehicleStatus.AVAILABLE,
                new BigDecimal("55000.00")
        );

        CreateVehicleDTO cheapVehicle = new CreateVehicleDTO(
                "Ford",
                "Fiesta",
                2023,
                "CHEAP1234567890",
                "White",
                VehicleStatus.AVAILABLE,
                new BigDecimal("15000.00")
        );

        CreateVehicleDTO midPriceVehicle = new CreateVehicleDTO(
                "Toyota",
                "Corolla",
                2023,
                "MIDPRICE123456",
                "Silver",
                VehicleStatus.AVAILABLE,
                new BigDecimal("28000.00")
        );

        // Create in random order
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expensiveVehicle)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cheapVehicle)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(midPriceVehicle)))
                .andExpect(status().isOk());

        // Get all available vehicles - should be ordered by price (cheapest first)
        mockMvc.perform(get("/api/v1/vehicles/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].price").value(15000.00))
                .andExpect(jsonPath("$[0].model").value("Fiesta"))
                .andExpect(jsonPath("$[1].price").value(28000.00))
                .andExpect(jsonPath("$[1].model").value("Corolla"))
                .andExpect(jsonPath("$[2].price").value(55000.00))
                .andExpect(jsonPath("$[2].model").value("X5"));

        // Create sold vehicles with different prices
        CreateVehicleDTO expensiveSold = new CreateVehicleDTO(
                "Porsche",
                "911",
                2023,
                "SOLDHIGH1234567",
                "Red",
                VehicleStatus.SOLD,
                new BigDecimal("120000.00")
        );

        CreateVehicleDTO cheapSold = new CreateVehicleDTO(
                "Chevrolet",
                "Onix",
                2023,
                "SOLDLOW12345678",
                "Gray",
                VehicleStatus.SOLD,
                new BigDecimal("35000.00")
        );

        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expensiveSold)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cheapSold)))
                .andExpect(status().isOk());

        // Get all sold vehicles - should be ordered by price (cheapest first)
        mockMvc.perform(get("/api/v1/vehicles/sold"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].price").value(35000.00))
                .andExpect(jsonPath("$[0].model").value("Onix"))
                .andExpect(jsonPath("$[1].price").value(120000.00))
                .andExpect(jsonPath("$[1].model").value("911"));
    }
}
