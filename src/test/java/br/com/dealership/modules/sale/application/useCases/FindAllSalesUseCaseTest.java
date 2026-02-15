package br.com.dealership.modules.sale.application.useCases;

import br.com.dealership.modules.sale.domain.entities.SaleOrder;
import br.com.dealership.modules.sale.domain.entities.SaleStatus;
import br.com.dealership.modules.sale.domain.entities.valueobjects.CPF;
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
class FindAllSalesUseCaseTest {

    @Mock
    private SaleRepositoryPort saleRepositoryPort;

    @InjectMocks
    private FindAllSalesUseCase useCase;

    @Test
    @DisplayName("Should find all sales successfully")
    void shouldFindAllSalesSuccessfully() {
        SaleOrder sale1 = new SaleOrder(1L, "John Doe", new CPF("12345678909"), "VIN1", 25000.0, UUID.randomUUID(), SaleStatus.PENDING, null);
        SaleOrder sale2 = new SaleOrder(2L, "Jane Doe", new CPF("98765432100"), "VIN2", 30000.0, UUID.randomUUID(), SaleStatus.COMPLETED, null);
        SaleOrder sale3 = new SaleOrder(3L, "Bob Smith", new CPF("11144477735"), "VIN3", 35000.0, UUID.randomUUID(), SaleStatus.CANCELED, null);
        List<SaleOrder> expectedSales = Arrays.asList(sale1, sale2, sale3);
        when(saleRepositoryPort.getAllSales()).thenReturn(expectedSales);

        List<SaleOrder> result = useCase.execute();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(expectedSales, result);
        verify(saleRepositoryPort).getAllSales();
    }

    @Test
    @DisplayName("Should return empty list when no sales exist")
    void shouldReturnEmptyListWhenNoSalesExist() {
        when(saleRepositoryPort.getAllSales()).thenReturn(Collections.emptyList());

        List<SaleOrder> result = useCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(saleRepositoryPort).getAllSales();
    }

    @Test
    @DisplayName("Should call repository only once")
    void shouldCallRepositoryOnlyOnce() {
        SaleOrder sale = new SaleOrder(1L, "John Doe", new CPF("12345678909"), "VIN1", 25000.0, UUID.randomUUID(), SaleStatus.PENDING, null);
        List<SaleOrder> expectedSales = Collections.singletonList(sale);
        when(saleRepositoryPort.getAllSales()).thenReturn(expectedSales);

        useCase.execute();

        verify(saleRepositoryPort, times(1)).getAllSales();
    }
}
