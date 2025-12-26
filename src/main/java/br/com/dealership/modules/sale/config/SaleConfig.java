package br.com.dealership.modules.sale.config;

import br.com.dealership.modules.sale.application.services.SaleService;
import br.com.dealership.modules.sale.application.useCases.*;
import br.com.dealership.modules.sale.domain.ports.out.SaleRepositoryPort;
import br.com.dealership.modules.sale.mapper.SaleMapper;
import br.com.dealership.modules.shared.useCases.FindAvaliableVehicleByIdUseCasePort;
import br.com.dealership.modules.shared.useCases.MarkVehicleAsSoldUseCasePort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaleConfig {

    @Bean
    public SaleMapper saleMapper () {
        return new SaleMapper();
    }

    @Bean
    public CreateSaleUseCase createSaleUseCase(SaleRepositoryPort repositoryPort) {
        return new CreateSaleUseCase(repositoryPort);
    }

    @Bean
    public FindAllSaleByCustomerCPFUseCase findAllSaleByCustomerCPFUseCase(SaleRepositoryPort repositoryPort) {
        return new FindAllSaleByCustomerCPFUseCase(repositoryPort);
    }

    @Bean
    public FindSaleByIdUseCase findSaleByIdUseCase(SaleRepositoryPort repositoryPort) {
        return new FindSaleByIdUseCase(repositoryPort);
    }

    @Bean
    public FindAllSalesUseCase findAllSalesUseCase(SaleRepositoryPort repositoryPort) {
        return new FindAllSalesUseCase(repositoryPort);
    }

    @Bean
    public CompleteSaleUseCase completeSaleUseCase(SaleRepositoryPort repositoryPort) {
        return new CompleteSaleUseCase(repositoryPort);
    }

    @Bean
    public SaleService saleService(CreateSaleUseCase createSaleUseCase,
                                   FindSaleByIdUseCase findSaleByIdUseCase,
                                   FindAllSalesUseCase findAllSalesUseCase,
                                   FindAllSaleByCustomerCPFUseCase findAllSaleByCustomerCPFUseCase,
                                   FindAvaliableVehicleByIdUseCasePort findAvaliableVehicleByIdUseCase,
                                   MarkVehicleAsSoldUseCasePort markVehicleAsSoldUseCase,
                                   CompleteSaleUseCase completeSaleUseCase) {
        return new SaleService(createSaleUseCase, findSaleByIdUseCase, findAllSalesUseCase, findAllSaleByCustomerCPFUseCase, findAvaliableVehicleByIdUseCase, markVehicleAsSoldUseCase, completeSaleUseCase);
    }
}
