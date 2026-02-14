package br.com.dealership.modules.sale.application.useCases;

import br.com.dealership.modules.sale.domain.entities.SaleOrder;
import br.com.dealership.modules.sale.domain.entities.SaleStatus;
import br.com.dealership.modules.sale.domain.entities.valueobjects.CPF;
import br.com.dealership.modules.sale.domain.exception.InvalidSaleException;
import br.com.dealership.modules.sale.domain.ports.out.SaleRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllSaleByCustomerCPFUseCaseTest {

    @Mock
    private SaleRepositoryPort saleRepositoryPort;

    @InjectMocks
    private FindAllSaleByCustomerCPFUseCase useCase;

    @Test
    @DisplayName("Should find all sales by customer CPF successfully")
    void shouldFindAllSalesByCustomerCpfSuccessfully() {
        String validCpf = "12345678909";
        SaleOrder sale1 = new SaleOrder(1L, "John Doe", new CPF(validCpf), "VIN1", 25000.0, UUID.randomUUID(), SaleStatus.PENDING, null);
        SaleOrder sale2 = new SaleOrder(2L, "John Doe", new CPF(validCpf), "VIN2", 30000.0, UUID.randomUUID(), SaleStatus.COMPLETED, null);
        List<SaleOrder> expectedSales = Arrays.asList(sale1, sale2);
        when(saleRepositoryPort.getAllSalesByCustomerCpf(validCpf)).thenReturn(expectedSales);

        List<SaleOrder> result = useCase.execute(validCpf);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedSales, result);
        verify(saleRepositoryPort).getAllSalesByCustomerCpf(validCpf);
    }

    @Test
    @DisplayName("Should find all sales by customer CPF with formatting")
    void shouldFindAllSalesByCustomerCpfWithFormatting() {
        String validCpf = "123.456.789-09";
        SaleOrder sale = new SaleOrder(1L, "Jane Doe", new CPF("12345678909"), "VIN1", 25000.0, UUID.randomUUID(), SaleStatus.PENDING, null);
        List<SaleOrder> expectedSales = Collections.singletonList(sale);
        when(saleRepositoryPort.getAllSalesByCustomerCpf(validCpf)).thenReturn(expectedSales);

        List<SaleOrder> result = useCase.execute(validCpf);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(saleRepositoryPort).getAllSalesByCustomerCpf(validCpf);
    }

    @Test
    @DisplayName("Should return empty list when no sales found for customer")
    void shouldReturnEmptyListWhenNoSalesFoundForCustomer() {
        String validCpf = "12345678909";
        when(saleRepositoryPort.getAllSalesByCustomerCpf(validCpf)).thenReturn(Collections.emptyList());

        List<SaleOrder> result = useCase.execute(validCpf);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(saleRepositoryPort).getAllSalesByCustomerCpf(validCpf);
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when CPF is null")
    void shouldThrowInvalidSaleExceptionWhenCpfIsNull() {
        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            useCase.execute(null);
        });

        assertEquals("Customer CPF cannot be null or empty", exception.getMessage());
        verify(saleRepositoryPort, never()).getAllSalesByCustomerCpf(any());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when CPF is empty")
    void shouldThrowInvalidSaleExceptionWhenCpfIsEmpty() {
        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            useCase.execute("");
        });

        assertEquals("Customer CPF cannot be null or empty", exception.getMessage());
        verify(saleRepositoryPort, never()).getAllSalesByCustomerCpf(any());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when CPF is blank")
    void shouldThrowInvalidSaleExceptionWhenCpfIsBlank() {
        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            useCase.execute("   ");
        });

        assertEquals("Customer CPF cannot be null or empty", exception.getMessage());
        verify(saleRepositoryPort, never()).getAllSalesByCustomerCpf(any());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when CPF format is invalid")
    void shouldThrowInvalidSaleExceptionWhenCpfFormatIsInvalid() {
        String invalidCpf = "12345678900";

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            useCase.execute(invalidCpf);
        });

        assertTrue(exception.getMessage().contains("Invalid CPF format"));
        verify(saleRepositoryPort, never()).getAllSalesByCustomerCpf(any());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when CPF has wrong length")
    void shouldThrowInvalidSaleExceptionWhenCpfHasWrongLength() {
        String invalidCpf = "123456789";

        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            useCase.execute(invalidCpf);
        });

        assertTrue(exception.getMessage().contains("Invalid CPF format"));
        verify(saleRepositoryPort, never()).getAllSalesByCustomerCpf(any());
    }
}
