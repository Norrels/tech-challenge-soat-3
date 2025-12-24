package br.com.dealership.modules.sale.domain.exception;

public class InvalidSaleException extends RuntimeException {
    public InvalidSaleException(String message) {
        super(message);
    }
}