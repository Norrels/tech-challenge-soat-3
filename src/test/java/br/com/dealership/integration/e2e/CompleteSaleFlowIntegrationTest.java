package br.com.dealership.integration.e2e;

import br.com.dealership.modules.sale.adapter.http.dto.CreateSaleDTO;
import br.com.dealership.modules.sale.adapter.http.dto.WebhookStatusDTO;
import br.com.dealership.modules.sale.domain.entities.SaleStatus;
import br.com.dealership.modules.vehicle.adapter.http.dto.CreateVehicleDTO;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;
import br.com.dealership.utils.JwtTestHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Complete Sale Flow End-to-End Integration Tests")
class CompleteSaleFlowIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Should complete full sale flow successfully: Create Vehicle -> Create Sale -> Pay -> Verify")
    void shouldCompleteFullSaleFlowSuccessfully() throws Exception {
        // STEP 1: Create a new vehicle
        CreateVehicleDTO vehicleDTO = new CreateVehicleDTO(
                "Honda",
                "Civic EX",
                2024,
                "1HGBH41JXMN109999",
                "Silver",
                VehicleStatus.AVAILABLE,
                new BigDecimal("32000.00")
        );

        String vehicleResponse = mockMvc.perform(post("/api/v1/vehicles")
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String vehicleId = objectMapper.readTree(vehicleResponse).get("id").asText();

        // STEP 2: Verify vehicle is in available vehicles list
        mockMvc.perform(get("/api/v1/vehicles/available")
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.vin == '1HGBH41JXMN109999')]").exists())
                .andExpect(jsonPath("$[?(@.vin == '1HGBH41JXMN109999')].status").value("AVAILABLE"));

        // STEP 3: Create a sale for this vehicle
        CreateSaleDTO saleDTO = new CreateSaleDTO();
        saleDTO.setVehicleVin("1HGBH41JXMN109999");
        saleDTO.setSalePrice(32000.0);

        String saleResponse = mockMvc.perform(post("/api/v1/sales")
                        .with(JwtTestHelper.createAdminJwt("Maria Silva", "12345678909"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Maria Silva"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.vehicleVin").value("1HGBH41JXMN109999"))
                .andExpect(jsonPath("$.vihicleId").value(vehicleId))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String saleId = objectMapper.readTree(saleResponse).get("id").asText();

        // STEP 4: Verify sale appears in all sales
        mockMvc.perform(get("/api/v1/sales")
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == '" + saleId + "')]").exists())
                .andExpect(jsonPath("$[?(@.id == '" + saleId + "')].status").value("PENDING"));

        // STEP 5: Verify sale appears when filtering by customer CPF
        mockMvc.perform(get("/api/v1/sales")
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909"))
                        .param("cpf", "12345678909"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(saleId))
                .andExpect(jsonPath("$[0].customerName").value("Maria Silva"));

        // STEP 6: Process payment webhook with success
        WebhookStatusDTO paymentSuccess = new WebhookStatusDTO(true, "12345678909");

        mockMvc.perform(post("/api/v1/sales/payment-webhook/{id}", saleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentSuccess)))
                .andExpect(status().isOk());

        // STEP 7: Verify sale status changed to COMPLETED
        mockMvc.perform(get("/api/v1/sales/{id}", saleId)
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saleId))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.customerName").value("Maria Silva"))
                .andExpect(jsonPath("$.salePrice").value(32000.0));

        // STEP 8: Verify vehicle status changed to SOLD
        mockMvc.perform(get("/api/v1/vehicles/{vin}", "1HGBH41JXMN109999")
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(vehicleId))
                .andExpect(jsonPath("$.status").value("SOLD"))
                .andExpect(jsonPath("$.make").value("Honda"))
                .andExpect(jsonPath("$.model").value("Civic EX"));

        // STEP 9: Verify vehicle is NO LONGER in available vehicles list
        mockMvc.perform(get("/api/v1/vehicles/available")
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.vin == '1HGBH41JXMN109999')]").doesNotExist());

        // STEP 10: Verify vehicle IS in sold vehicles list
        mockMvc.perform(get("/api/v1/vehicles/sold")
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.vin == '1HGBH41JXMN109999')]").exists())
                .andExpect(jsonPath("$[?(@.vin == '1HGBH41JXMN109999')].status").value("SOLD"));
    }

    @Test
    @DisplayName("Should handle failed payment correctly: Create Vehicle -> Create Sale -> Payment Fails -> Verify")
    void shouldHandleFailedPaymentCorrectly() throws Exception {
        // STEP 1: Create a new vehicle
        CreateVehicleDTO vehicleDTO = new CreateVehicleDTO(
                "Toyota",
                "Corolla",
                2024,
                "TOYOTA123456789VIN",
                "Blue",
                VehicleStatus.AVAILABLE,
                new BigDecimal("28000.00")
        );

        mockMvc.perform(post("/api/v1/vehicles")
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AVAILABLE"));

        // STEP 2: Create a sale
        CreateSaleDTO saleDTO = new CreateSaleDTO();
        saleDTO.setVehicleVin("TOYOTA123456789VIN");
        saleDTO.setSalePrice(28000.0);

        String saleResponse = mockMvc.perform(post("/api/v1/sales")
                        .with(JwtTestHelper.createAdminJwt("João Santos", "98765432100"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String saleId = objectMapper.readTree(saleResponse).get("id").asText();

        // STEP 3: Process payment webhook with FAILURE
        WebhookStatusDTO paymentFailure = new WebhookStatusDTO(false, "98765432100");

        mockMvc.perform(post("/api/v1/sales/payment-webhook/{id}", saleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentFailure)))
                .andExpect(status().isOk());

        // STEP 4: Verify sale status changed to CANCELED
        mockMvc.perform(get("/api/v1/sales/{id}", saleId)
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELED"));

        // STEP 5: Verify vehicle is still AVAILABLE (payment failed, so vehicle was not sold)
        mockMvc.perform(get("/api/v1/vehicles/{vin}", "TOYOTA123456789VIN")
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AVAILABLE"));

        // STEP 6: Verify vehicle is still in available vehicles list
        mockMvc.perform(get("/api/v1/vehicles/available")
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.vin == 'TOYOTA123456789VIN')]").exists())
                .andExpect(jsonPath("$[?(@.vin == 'TOYOTA123456789VIN')].status").value("AVAILABLE"));
    }

    @Test
    @DisplayName("Should prevent creating sale for already sold vehicle")
    void shouldPreventCreatingSaleForAlreadySoldVehicle() throws Exception {
        // STEP 1: Create and sell first vehicle
        CreateVehicleDTO vehicleDTO = new CreateVehicleDTO(
                "BMW",
                "X5",
                2024,
                "BMW123456789VIN",
                "Black",
                VehicleStatus.AVAILABLE,
                new BigDecimal("55000.00")
        );

        mockMvc.perform(post("/api/v1/vehicles")
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleDTO)))
                .andExpect(status().isOk());

        // STEP 2: Create first sale
        CreateSaleDTO firstSale = new CreateSaleDTO();
        firstSale.setVehicleVin("BMW123456789VIN");
        firstSale.setSalePrice(55000.0);

        String firstSaleResponse = mockMvc.perform(post("/api/v1/sales")
                        .with(JwtTestHelper.createAdminJwt("Customer One", "12345678909"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstSale)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String firstSaleId = objectMapper.readTree(firstSaleResponse).get("id").asText();

        // STEP 3: Complete first sale
        WebhookStatusDTO paymentSuccess = new WebhookStatusDTO(true, "12345678909");

        mockMvc.perform(post("/api/v1/sales/payment-webhook/{id}", firstSaleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentSuccess)))
                .andExpect(status().isOk());

        // STEP 4: Verify vehicle is now SOLD
        mockMvc.perform(get("/api/v1/vehicles/{vin}", "BMW123456789VIN")
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SOLD"));

        // STEP 5: Try to create another sale for the same (now sold) vehicle
        CreateSaleDTO secondSale = new CreateSaleDTO();
        secondSale.setVehicleVin("BMW123456789VIN");
        secondSale.setSalePrice(55000.0);

        mockMvc.perform(post("/api/v1/sales")
                        .with(JwtTestHelper.createAdminJwt("Customer Two", "98765432100"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(secondSale)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(containsString("not available for sale")));
    }

    @Test
    @DisplayName("Should handle multiple customers buying different vehicles")
    void shouldHandleMultipleCustomersBuyingDifferentVehicles() throws Exception {
        // Create three vehicles
        String[] vins = {"VIN001", "VIN002", "VIN003"};
        String[] customers = {"Customer A", "Customer B", "Customer C"};
        String[] cpfs = {"12345678909", "98765432100", "11144477735"};

        for (int i = 0; i < 3; i++) {
            // Create vehicle
            CreateVehicleDTO vehicleDTO = new CreateVehicleDTO(
                    "Brand" + i,
                    "Model" + i,
                    2024,
                    vins[i],
                    "Color" + i,
                    VehicleStatus.AVAILABLE,
                    new BigDecimal("30000.00")
            );

            mockMvc.perform(post("/api/v1/vehicles")
                            .with(JwtTestHelper.createAdminJwt("Default User", "12345678909"))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(vehicleDTO)))
                    .andExpect(status().isOk());

            // Create sale
            CreateSaleDTO saleDTO = new CreateSaleDTO();
            saleDTO.setVehicleVin(vins[i]);
            saleDTO.setSalePrice(30000.0);

            String saleResponse = mockMvc.perform(post("/api/v1/sales")
                            .with(JwtTestHelper.createAdminJwt(customers[i], cpfs[i]))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(saleDTO)))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            String saleId = objectMapper.readTree(saleResponse).get("id").asText();

            // Complete payment
            WebhookStatusDTO payment = new WebhookStatusDTO(true, cpfs[i]);

            mockMvc.perform(post("/api/v1/sales/payment-webhook/{id}", saleId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payment)))
                    .andExpect(status().isOk());
        }

        // Verify all sales are completed
        mockMvc.perform(get("/api/v1/sales")
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].status", everyItem(is("COMPLETED"))));

        // Verify all vehicles are sold
        mockMvc.perform(get("/api/v1/vehicles/sold")
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].status", everyItem(is("SOLD"))));

        // Verify no available vehicles
        mockMvc.perform(get("/api/v1/vehicles/available")
                        .with(JwtTestHelper.createAdminJwt("Default User", "12345678909")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
