package br.com.dealership.integration.sale;

import br.com.dealership.modules.sale.adapter.http.dto.CreateSaleDTO;
import br.com.dealership.modules.sale.adapter.http.dto.WebhookStatusDTO;
import br.com.dealership.modules.sale.domain.entities.SaleStatus;
import br.com.dealership.modules.vehicle.adapter.http.dto.CreateVehicleDTO;
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
class SaleControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private CreateVehicleDTO createVehicleDTO;
    private CreateSaleDTO createSaleDTO;

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

        createSaleDTO = new CreateSaleDTO();
        createSaleDTO.setCustomerName("John Doe");
        createSaleDTO.setCustomerCpf("12345678909");
        createSaleDTO.setVehicleVin("1HGBH41JXMN109186");
        createSaleDTO.setSalePrice(25000.0);
        createSaleDTO.setStatus(SaleStatus.PENDING);
    }

    @Test
    @DisplayName("Should create sale successfully when vehicle is available")
    void shouldCreateSaleSuccessfullyWhenVehicleIsAvailable() throws Exception {
        // Create vehicle first
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVehicleDTO)))
                .andExpect(status().isOk());

        // Create sale
        mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSaleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.customerCpf.value").value("12345678909"))
                .andExpect(jsonPath("$.vehicleVin").value("1HGBH41JXMN109186"))
                .andExpect(jsonPath("$.salePrice").value(25000.0))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.vihicleId").exists());
    }

    @Test
    @DisplayName("Should return 400 when creating sale with non-existent vehicle")
    void shouldReturn400WhenCreatingSaleWithNonExistentVehicle() throws Exception {
        createSaleDTO.setVehicleVin("NONEXISTENT");

        mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSaleDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("NONEXISTENT")))
                .andExpect(jsonPath("$.message").value(containsString("does not exist")));
    }

    @Test
    @DisplayName("Should return 400 when creating sale with sold vehicle")
    void shouldReturn400WhenCreatingSaleWithSoldVehicle() throws Exception {
        // Create sold vehicle
        CreateVehicleDTO soldVehicle = new CreateVehicleDTO(
                "Honda",
                "Civic",
                2023,
                "SOLDVIN123456789",
                "Black",
                VehicleStatus.SOLD,
                new BigDecimal("25000.00")
        );

        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(soldVehicle)))
                .andExpect(status().isOk());

        createSaleDTO.setVehicleVin("SOLDVIN123456789");

        mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSaleDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("not available for sale")));
    }

    @Test
    @DisplayName("Should return 400 when creating sale with invalid CPF")
    void shouldReturn400WhenCreatingSaleWithInvalidCpf() throws Exception {
        // Create vehicle first
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVehicleDTO)))
                .andExpect(status().isOk());

        createSaleDTO.setCustomerCpf("00000000000"); // Invalid CPF

        mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSaleDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Invalid CPF")));
    }

    @Test
    @DisplayName("Should get sale by ID successfully")
    void shouldGetSaleByIdSuccessfully() throws Exception {
        // Create vehicle and sale
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVehicleDTO)))
                .andExpect(status().isOk());

        String createSaleResponse = mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSaleDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String saleId = objectMapper.readTree(createSaleResponse).get("id").asText();

        // Get sale by ID
        mockMvc.perform(get("/sales/{id}", saleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saleId))
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("Should return 404 when sale not found")
    void shouldReturn404WhenSaleNotFound() throws Exception {
        mockMvc.perform(get("/sales/{id}", "999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("999")));
    }

    @Test
    @DisplayName("Should get all sales successfully")
    void shouldGetAllSalesSuccessfully() throws Exception {
        // Create vehicle
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVehicleDTO)))
                .andExpect(status().isOk());

        // Create first sale
        mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSaleDTO)))
                .andExpect(status().isOk());

        // Create second vehicle for second sale
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

        // Create second sale
        CreateSaleDTO secondSale = new CreateSaleDTO();
        secondSale.setCustomerName("Jane Doe");
        secondSale.setCustomerCpf("98765432100");
        secondSale.setVehicleVin("DIFFERENTVIN12345");
        secondSale.setSalePrice(30000.0);
        secondSale.setStatus(SaleStatus.PENDING);

        mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondSale)))
                .andExpect(status().isOk());

        // Get all sales
        mockMvc.perform(get("/sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("Should filter sales by customer CPF successfully")
    void shouldFilterSalesByCustomerCpfSuccessfully() throws Exception {
        // Create vehicle
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVehicleDTO)))
                .andExpect(status().isOk());

        // Create sale for specific customer
        mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSaleDTO)))
                .andExpect(status().isOk());

        // Create another vehicle
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

        // Create sale for different customer
        CreateSaleDTO differentCustomerSale = new CreateSaleDTO();
        differentCustomerSale.setCustomerName("Different Customer");
        differentCustomerSale.setCustomerCpf("11144477735");
        differentCustomerSale.setVehicleVin("DIFFERENTVIN12345");
        differentCustomerSale.setSalePrice(30000.0);
        differentCustomerSale.setStatus(SaleStatus.PENDING);

        mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(differentCustomerSale)))
                .andExpect(status().isOk());

        // Filter sales by first customer CPF
        mockMvc.perform(get("/sales")
                        .param("cpf", "12345678909"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerCpf.value").value("12345678909"));
    }

    @Test
    @DisplayName("Should return 400 when filtering with invalid CPF")
    void shouldReturn400WhenFilteringWithInvalidCpf() throws Exception {
        mockMvc.perform(get("/sales")
                        .param("cpf", "00000000000"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("Invalid CPF")));
    }

    @Test
    @DisplayName("Should complete sale successfully when payment succeeds")
    void shouldCompleteSaleSuccessfullyWhenPaymentSucceeds() throws Exception {
        // Create vehicle and sale
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVehicleDTO)))
                .andExpect(status().isOk());

        String createSaleResponse = mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSaleDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String saleId = objectMapper.readTree(createSaleResponse).get("id").asText();

        // Process payment webhook with success
        WebhookStatusDTO webhookStatus = new WebhookStatusDTO(true);

        mockMvc.perform(post("/sales/payment-webhook/{id}", saleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webhookStatus)))
                .andExpect(status().isOk());

        // Verify sale status changed to COMPLETED
        mockMvc.perform(get("/sales/{id}", saleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        // Verify vehicle status changed to SOLD
        mockMvc.perform(get("/api/v1/vehicles/{vin}", "1HGBH41JXMN109186"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SOLD"));
    }

    @Test
    @DisplayName("Should cancel sale when payment fails")
    void shouldCancelSaleWhenPaymentFails() throws Exception {
        // Create vehicle and sale
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVehicleDTO)))
                .andExpect(status().isOk());

        String createSaleResponse = mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSaleDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String saleId = objectMapper.readTree(createSaleResponse).get("id").asText();

        // Process payment webhook with failure
        WebhookStatusDTO webhookStatus = new WebhookStatusDTO(false);

        mockMvc.perform(post("/sales/payment-webhook/{id}", saleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webhookStatus)))
                .andExpect(status().isOk());

        // Verify sale status changed to CANCELED
        mockMvc.perform(get("/sales/{id}", saleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELED"));

        // Verify vehicle is still AVAILABLE
        mockMvc.perform(get("/api/v1/vehicles/{vin}", "1HGBH41JXMN109186"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AVAILABLE"));
    }

    @Test
    @DisplayName("Should return 409 when trying to pay a non-PENDING sale")
    void shouldReturn409WhenTryingToPayANonPendingSale() throws Exception {
        // Create vehicle and sale
        mockMvc.perform(post("/api/v1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createVehicleDTO)))
                .andExpect(status().isOk());

        String createSaleResponse = mockMvc.perform(post("/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createSaleDTO)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String saleId = objectMapper.readTree(createSaleResponse).get("id").asText();

        // Complete sale first time
        WebhookStatusDTO webhookStatus = new WebhookStatusDTO(true);

        mockMvc.perform(post("/sales/payment-webhook/{id}", saleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webhookStatus)))
                .andExpect(status().isOk());

        // Try to pay again
        mockMvc.perform(post("/sales/payment-webhook/{id}", saleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webhookStatus)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(containsString("not in PENDING status")));
    }
}
