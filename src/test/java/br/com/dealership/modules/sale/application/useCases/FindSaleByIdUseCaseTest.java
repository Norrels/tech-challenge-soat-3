package br.com.dealership.modules.sale.application.useCases;

import br.com.dealership.modules.sale.domain.entities.SaleOrder;
import br.com.dealership.modules.sale.domain.entities.SaleStatus;
import br.com.dealership.modules.sale.domain.entities.valueobjects.CPF;
import br.com.dealership.modules.sale.domain.exception.InvalidSaleException;
import br.com.dealership.modules.sale.domain.exception.SaleNotFoundException;
import br.com.dealership.modules.sale.domain.ports.out.SaleRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindSaleByIdUseCaseTest {

    @Mock
    private SaleRepositoryPort saleRepositoryPort;

    @InjectMocks
    private FindSaleByIdUseCase useCase;

    private SaleOrder saleOrder;

    @BeforeEach
    void setUp() {
        saleOrder = new SaleOrder(
                1L,
                "John Doe",
                new CPF("12345678909"),
                "1HGBH41JXMN109186",
                25000.0,
                UUID.randomUUID(),
                SaleStatus.PENDING
        );
    }

    @Test
    @DisplayName("Should find sale by ID successfully")
    void shouldFindSaleByIdSuccessfully() {
        when(saleRepositoryPort.getSaleById("1")).thenReturn(saleOrder);

        SaleOrder result = useCase.execute("1");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(saleRepositoryPort).getSaleById("1");
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when ID is null")
    void shouldThrowInvalidSaleExceptionWhenIdIsNull() {
        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            useCase.execute(null);
        });

        assertEquals("Sale ID cannot be null or empty", exception.getMessage());
        verify(saleRepositoryPort, never()).getSaleById(any());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when ID is empty")
    void shouldThrowInvalidSaleExceptionWhenIdIsEmpty() {
        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            useCase.execute("");
        });

        assertEquals("Sale ID cannot be null or empty", exception.getMessage());
        verify(saleRepositoryPort, never()).getSaleById(any());
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when ID is blank")
    void shouldThrowInvalidSaleExceptionWhenIdIsBlank() {
        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            useCase.execute("   ");
        });

        assertEquals("Sale ID cannot be null or empty", exception.getMessage());
        verify(saleRepositoryPort, never()).getSaleById(any());
    }

    @Test
    @DisplayName("Should throw SaleNotFoundException when sale does not exist")
    void shouldThrowSaleNotFoundExceptionWhenSaleDoesNotExist() {
        when(saleRepositoryPort.getSaleById("non-existent-id")).thenReturn(null);

        Exception exception = assertThrows(SaleNotFoundException.class, () -> {
            useCase.execute("non-existent-id");
        });

        assertEquals("Sale not found with ID: non-existent-id", exception.getMessage());
        verify(saleRepositoryPort).getSaleById("non-existent-id");
    }
}
