package br.com.dealership.modules.sale.application.useCases;

import br.com.dealership.modules.sale.domain.entities.SaleOrder;
import br.com.dealership.modules.sale.domain.exception.InvalidSaleException;
import br.com.dealership.modules.sale.domain.ports.out.SaleRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateSaleUseCaseTest {

    @Mock
    private SaleRepositoryPort saleRepositoryPort;

    @InjectMocks
    private CreateSaleUseCase useCase;

    private SaleOrder saleOrder;

    @BeforeEach
    void setUp() {
        saleOrder = mock(SaleOrder.class);
    }

    @Test
    @DisplayName("Should create sale successfully when sale order is valid")
    void shouldCreateSaleSuccessfullyWhenSaleOrderIsValid() {
        when(saleRepositoryPort.save(saleOrder)).thenReturn(saleOrder);
        doNothing().when(saleOrder).validate();

        SaleOrder result = useCase.execute(saleOrder);

        assertNotNull(result);
        assertEquals(saleOrder, result);
        verify(saleOrder).validate();
        verify(saleRepositoryPort).save(saleOrder);
    }

    @Test
    @DisplayName("Should throw InvalidSaleException when sale order is null")
    void shouldThrowInvalidSaleExceptionWhenSaleOrderIsNull() {
        Exception exception = assertThrows(InvalidSaleException.class, () -> {
            useCase.execute(null);
        });

        assertEquals("Sale order cannot be null", exception.getMessage());
        verify(saleRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should call validate on sale order")
    void shouldCallValidateOnSaleOrder() {
        when(saleRepositoryPort.save(saleOrder)).thenReturn(saleOrder);
        doNothing().when(saleOrder).validate();

        useCase.execute(saleOrder);

        verify(saleOrder).validate();
    }

    @Test
    @DisplayName("Should propagate validation exception when sale order is invalid")
    void shouldPropagateValidationExceptionWhenSaleOrderIsInvalid() {
        doThrow(new IllegalArgumentException("Invalid sale data")).when(saleOrder).validate();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            useCase.execute(saleOrder);
        });

        assertEquals("Invalid sale data", exception.getMessage());
        verify(saleOrder).validate();
        verify(saleRepositoryPort, never()).save(any());
    }
}
