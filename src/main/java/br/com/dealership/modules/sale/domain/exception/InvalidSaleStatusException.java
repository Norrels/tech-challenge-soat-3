package br.com.dealership.modules.sale.domain.exception;

public class InvalidSaleStatusException extends RuntimeException {
    public InvalidSaleStatusException(String message) {
        super(message);
    }
}