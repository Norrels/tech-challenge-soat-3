package br.com.dealership.modules.sale.application.services;

import br.com.dealership.modules.sale.application.useCases.*;
import br.com.dealership.modules.sale.domain.entities.SaleOrder;
import br.com.dealership.modules.sale.domain.exception.InvalidSaleException;
import br.com.dealership.modules.sale.domain.ports.in.SaleServicePort;
import br.com.dealership.modules.shared.useCases.FindAvailableVehicleByIdUseCasePort;
import br.com.dealership.modules.shared.useCases.MarkVehicleAsSoldUseCasePort;
import br.com.dealership.modules.vehicle.domain.entities.VehicleStatus;

import java.util.List;

public class SaleService implements SaleServicePort {
    private final CreateSaleUseCase createSaleUseCase;
    private final FindSaleByIdUseCase findSaleByIdUseCase;
    private final FindAllSalesUseCase findAllSalesUseCase;
    private final FindAllSaleByCustomerCPFUseCase findAllSaleByCustomerCPFUseCase;
    private final FindAvailableVehicleByIdUseCasePort findAvailableVehicleByIdUseCase;
    private final MarkVehicleAsSoldUseCasePort markVehicleAsSoldUseCase;
    private final CompleteSaleUseCase completeSaleUseCase;

    public SaleService(CreateSaleUseCase createSaleUseCase, FindSaleByIdUseCase findSaleByIdUseCase, FindAllSalesUseCase findAllSalesUseCase, FindAllSaleByCustomerCPFUseCase findAllSaleByCustomerCPFUseCase, FindAvailableVehicleByIdUseCasePort findAvailableVehicleByIdUseCase, MarkVehicleAsSoldUseCasePort markVehicleAsSoldUseCase, CompleteSaleUseCase completeSaleUseCase) {
        this.createSaleUseCase = createSaleUseCase;
        this.findSaleByIdUseCase = findSaleByIdUseCase;
        this.findAllSalesUseCase = findAllSalesUseCase;
        this.findAllSaleByCustomerCPFUseCase = findAllSaleByCustomerCPFUseCase;
        this.findAvailableVehicleByIdUseCase = findAvailableVehicleByIdUseCase;
        this.markVehicleAsSoldUseCase = markVehicleAsSoldUseCase;
        this.completeSaleUseCase = completeSaleUseCase;
    }

    @Override
    public SaleOrder getSaleById(String id) {
        return findSaleByIdUseCase.execute(id);
    }

    @Override
    public SaleOrder createSale(SaleOrder sale) {
        var vehicle = findAvailableVehicleByIdUseCase.execute(sale.getVehicleVin())
                .orElseThrow(() -> new InvalidSaleException("Vehicle with VIN " + sale.getVehicleVin() + " does not exist"));

        if(vehicle.status() != VehicleStatus.AVAILABLE) {
            throw new InvalidSaleException("Vehicle with VIN " + sale.getVehicleVin() + " is not available for sale");
        }

        sale.setVihicleId(vehicle.vehicleId());
        return createSaleUseCase.execute(sale);
    }

    @Override
    public List<SaleOrder> getAllSales() {
        return findAllSalesUseCase.execute();
    }

    @Override
    public List<SaleOrder> getAllSalesByCustomerCPF(String cpf) {
        return findAllSaleByCustomerCPFUseCase.execute(cpf);
    }

    @Override
    public void paySale(String id, Boolean paymentSuccess) {
        SaleOrder saleOrder = completeSaleUseCase.execute(id, paymentSuccess);
        markVehicleAsSoldUseCase.execute(saleOrder.getVihicleId());
    }
}
