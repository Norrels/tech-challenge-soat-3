package br.com.dealership.modules.sale.adapter.http;

import br.com.dealership.exception.ErrorResponse;
import br.com.dealership.modules.sale.application.services.SaleService;
import br.com.dealership.modules.sale.domain.entities.SaleOrder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
@Tag(name = "Sales", description = "Sales management endpoints")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping()
    @Operation(summary = "Create a new sale", description = "Creates a new sale order in the system")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sale created successfully",
                    content = @Content(schema = @Schema(implementation = SaleOrder.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid sale data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<SaleOrder> createSaleOrder(@RequestBody SaleOrder saleOrder) {
        return ResponseEntity.ok(saleService.createSale(saleOrder));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sale by ID", description = "Retrieves a sale order by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Sale found",
                    content = @Content(schema = @Schema(implementation = SaleOrder.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sale not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid ID parameter",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<SaleOrder> getSaleOrderById(
            @Parameter(description = "Sale Order ID") @PathVariable String id) {
        return ResponseEntity.ok(saleService.getSaleById(id));
    }

    @PostMapping("/payment-webhook/{id}")
    @Operation(summary = "Payment webhook", description = "Webhook endpoint for payment status updates")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Payment status updated successfully",
                    content = @Content(schema = @Schema(implementation = SaleOrder.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid payment data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Sale not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<SaleOrder> updateSaleOrder(
            @Parameter(description = "Sale Order ID") @PathVariable String id,
            @RequestBody SaleOrder saleOrder) {
        // TODO Implementar validação do webhook
        return ResponseEntity.ok(saleOrder);
    }

    @GetMapping()
    @Operation(summary = "Get all sales", description = "Retrieves all sale orders, optionally filtered by customer CPF")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List of sales retrieved successfully",
                    content = @Content(schema = @Schema(implementation = SaleOrder.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid CPF parameter",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })

    public ResponseEntity<List<SaleOrder>> getAllSaleOrders(
            @Parameter(description = "Customer CPF (optional)") @RequestParam(required = false) String cpf) {
        if (cpf != null && !cpf.isBlank()) {
            return ResponseEntity.ok(saleService.getAllSalesByCustomerCPF(cpf));
        }
        return ResponseEntity.ok(saleService.getAllSales());
    }
}
