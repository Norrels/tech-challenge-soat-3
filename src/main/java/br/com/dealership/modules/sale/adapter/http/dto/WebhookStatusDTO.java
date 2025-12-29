package br.com.dealership.modules.sale.adapter.http.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data Transfer Object for payment webhook status")
public record WebhookStatusDTO(
        @Schema(description = "Payment success status", example = "true", required = true)
        boolean success,

        @NotBlank(message = "Payer CPF is required")
        @Schema(description = "CPF of the person making the payment", example = "681.702.720-92", required = true)
        String payerCpf
) { }
