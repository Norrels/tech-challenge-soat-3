package br.com.dealership.modules.sale.domain.entities;

import br.com.dealership.modules.sale.domain.entities.valueobjects.CPF;
import br.com.dealership.modules.sale.domain.exception.InvalidSaleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SaleOrderTest {

    private SaleOrder saleOrder;
    private CPF validCpf;
    private UUID vehicleId;

    @BeforeEach
    void setUp() {
        validCpf = new CPF("12345678909");
        vehicleId = UUID.randomUUID();
        saleOrder = new SaleOrder(
                1L,
                "John Doe",
                validCpf,
                "1HGBH41JXMN109186",
                25000.0,
                vehicleId,
                SaleStatus.PENDING
        );
    }

    @Test
    @DisplayName("Should create sale order with valid data")
    void shouldCreateSaleOrderWithValidData() {
        assertNotNull(saleOrder);
        assertEquals(1L, saleOrder.getId());
        assertEquals("John Doe", saleOrder.getCustomerName());
        assertEquals(validCpf, saleOrder.getCustomerCpf());
        assertEquals("1HGBH41JXMN109186", saleOrder.getVehicleVin());
        assertEquals(25000.0, saleOrder.getSalePrice());
        assertEquals(vehicleId, saleOrder.getVihicleId());
        assertEquals(SaleStatus.PENDING, saleOrder.getStatus());
    }

    @Test
    @DisplayName("Should validate sale order successfully with all valid data")
    void shouldValidateSaleOrderSuccessfullyWithAllValidData() {
        assertDoesNotThrow(() -> saleOrder.validate());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when customer name is null")
    void shouldThrowInvalidSaleExceptionWhenCustomerNameIsNull() {
        saleOrder.setCustomerName(null);

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            saleOrder.validate();
        });

        assertEquals("Customer name is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when customer name is empty")
    void shouldThrowInvalidSaleExceptionWhenCustomerNameIsEmpty() {
        saleOrder.setCustomerName("");

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            saleOrder.validate();
        });

        assertEquals("Customer name is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when customer name is blank")
    void shouldThrowInvalidSaleExceptionWhenCustomerNameIsBlank() {
        saleOrder.setCustomerName("   ");

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            saleOrder.validate();
        });

        assertEquals("Customer name is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when customer CPF is null")
    void shouldThrowInvalidSaleExceptionWhenCustomerCpfIsNull() {
        saleOrder.setCustomerCpf(null);

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            saleOrder.validate();
        });

        assertEquals("Customer CPF is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when vehicle VIN is null")
    void shouldThrowInvalidSaleExceptionWhenVehicleVinIsNull() {
        saleOrder.setVehicleVin(null);

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            saleOrder.validate();
        });

        assertEquals("Vehicle VIN is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when vehicle VIN is empty")
    void shouldThrowInvalidSaleExceptionWhenVehicleVinIsEmpty() {
        saleOrder.setVehicleVin("");

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            saleOrder.validate();
        });

        assertEquals("Vehicle VIN is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when vehicle VIN is blank")
    void shouldThrowInvalidSaleExceptionWhenVehicleVinIsBlank() {
        saleOrder.setVehicleVin("   ");

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            saleOrder.validate();
        });

        assertEquals("Vehicle VIN is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when vehicle ID is null")
    void shouldThrowInvalidSaleExceptionWhenVehicleIdIsNull() {
        saleOrder.setVihicleId(null);

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            saleOrder.validate();
        });

        assertEquals("Vehicle ID is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when sale price is null")
    void shouldThrowInvalidSaleExceptionWhenSalePriceIsNull() {
        saleOrder.setSalePrice(null);

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            saleOrder.validate();
        });

        assertEquals("Sale price must be greater than zero", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when sale price is zero")
    void shouldThrowInvalidSaleExceptionWhenSalePriceIsZero() {
        saleOrder.setSalePrice(0.0);

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            saleOrder.validate();
        });

        assertEquals("Sale price must be greater than zero", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when sale price is negative")
    void shouldThrowInvalidSaleExceptionWhenSalePriceIsNegative() {
        saleOrder.setSalePrice(-100.0);

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            saleOrder.validate();
        });

        assertEquals("Sale price must be greater than zero", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when status is null")
    void shouldThrowInvalidSaleExceptionWhenStatusIsNull() {
        saleOrder.setStatus(null);

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            saleOrder.validate();
        });

        assertEquals("Sale status is required", exception.getMessage());
    }

    @Test
    @DisplayName("Should mark sale as paid successfully when status is PENDING")
    void shouldMarkSaleAsPaidSuccessfullyWhenStatusIsPending() {
        assertEquals(SaleStatus.PENDING, saleOrder.getStatus());

        saleOrder.markAsPaid();

        assertEquals(SaleStatus.COMPLETED, saleOrder.getStatus());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when trying to mark as paid a COMPLETED sale")
    void shouldThrowInvalidSaleExceptionWhenTryingToMarkAsPaidACompletedSale() {
        saleOrder.setStatus(SaleStatus.COMPLETED);

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            saleOrder.markAsPaid();
        });

        assertEquals("Only pending sales can be marked as paid", exception.getMessage());
        assertEquals(SaleStatus.COMPLETED, saleOrder.getStatus());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when trying to mark as paid a CANCELED sale")
    void shouldThrowInvalidSaleExceptionWhenTryingToMarkAsPaidACanceledSale() {
        saleOrder.setStatus(SaleStatus.CANCELED);

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            saleOrder.markAsPaid();
        });

        assertEquals("Only pending sales can be marked as paid", exception.getMessage());
        assertEquals(SaleStatus.CANCELED, saleOrder.getStatus());
    }

    @Test
    @DisplayName("Should set and get all properties correctly")
    void shouldSetAndGetAllPropertiesCorrectly() {
        Long newId = 2L;
        String newName = "Jane Doe";
        CPF newCpf = new CPF("98765432100");
        String newVin = "NEWVIN123456789";
        Double newPrice = 30000.0;
        UUID newVehicleId = UUID.randomUUID();
        SaleStatus newStatus = SaleStatus.COMPLETED;

        saleOrder.setId(newId);
        saleOrder.setCustomerName(newName);
        saleOrder.setCustomerCpf(newCpf);
        saleOrder.setVehicleVin(newVin);
        saleOrder.setSalePrice(newPrice);
        saleOrder.setVihicleId(newVehicleId);
        saleOrder.setStatus(newStatus);

        assertEquals(newId, saleOrder.getId());
        assertEquals(newName, saleOrder.getCustomerName());
        assertEquals(newCpf, saleOrder.getCustomerCpf());
        assertEquals(newVin, saleOrder.getVehicleVin());
        assertEquals(newPrice, saleOrder.getSalePrice());
        assertEquals(newVehicleId, saleOrder.getVihicleId());
        assertEquals(newStatus, saleOrder.getStatus());
    }
}
