package br.com.dealership.modules.sale.application.useCases;

import br.com.dealership.modules.sale.domain.entities.SaleOrder;
import br.com.dealership.modules.sale.domain.entities.SaleStatus;
import br.com.dealership.modules.sale.domain.entities.valueobjects.CPF;
import br.com.dealership.modules.sale.domain.exception.InvalidSaleStatusException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompleteSaleUseCaseTest {

    @Mock
    private SaleRepositoryPort repository;

    @InjectMocks
    private CompleteSaleUseCase useCase;

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
    @DisplayName("Should complete sale successfully when payment is successful")
    void shouldCompleteSaleSuccessfullyWhenPaymentIsSuccessful() {
        when(repository.getSaleById("1")).thenReturn(saleOrder);
        when(repository.save(any(SaleOrder.class))).thenReturn(saleOrder);

        SaleOrder result = useCase.execute("1", true);

        assertNotNull(result);
        assertEquals(SaleStatus.COMPLETED, result.getStatus());
        verify(repository).getSaleById("1");
        verify(repository).save(saleOrder);
    }

    @Test
    @DisplayName("Should cancel sale when payment fails")
    void shouldCancelSaleWhenPaymentFails() {
        when(repository.getSaleById("1")).thenReturn(saleOrder);
        when(repository.save(any(SaleOrder.class))).thenReturn(saleOrder);

        SaleOrder result = useCase.execute("1", false);

        assertNotNull(result);
        assertEquals(SaleStatus.CANCELED, result.getStatus());
        verify(repository).getSaleById("1");
        verify(repository).save(saleOrder);
    }

    @Test
    @DisplayName("Should throw SaleNotFoundException when sale does not exist")
    void shouldThrowSaleNotFoundExceptionWhenSaleDoesNotExist() {
        when(repository.getSaleById("999")).thenReturn(null);

        Exception exception = assertThrows(SaleNotFoundException.class, () -> {
            useCase.execute("999", true);
        });

        assertEquals("Sale with id 999 does not exist", exception.getMessage());
        verify(repository).getSaleById("999");
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidSaleStatusException when sale is not in PENDING status")
    void shouldThrowInvalidSaleStatusExceptionWhenSaleIsNotInPendingStatus() {
        saleOrder.setStatus(SaleStatus.COMPLETED);
        when(repository.getSaleById("1")).thenReturn(saleOrder);

        Exception exception = assertThrows(InvalidSaleStatusException.class, () -> {
            useCase.execute("1", true);
        });

        assertEquals("Sale with id 1 is not in PENDING status. Current status: COMPLETED", exception.getMessage());
        verify(repository).getSaleById("1");
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InvalidSaleStatusException when sale is CANCELED")
    void shouldThrowInvalidSaleStatusExceptionWhenSaleIsCanceled() {
        saleOrder.setStatus(SaleStatus.CANCELED);
        when(repository.getSaleById("1")).thenReturn(saleOrder);

        Exception exception = assertThrows(InvalidSaleStatusException.class, () -> {
            useCase.execute("1", true);
        });

        assertEquals("Sale with id 1 is not in PENDING status. Current status: CANCELED", exception.getMessage());
        verify(repository).getSaleById("1");
        verify(repository, never()).save(any());
    }
}
