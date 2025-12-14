package br.com.dealership.modules.sale.model.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class CPFTest {

    @Test
    @DisplayName("Should create CPF with valid number without formatting")
    void shouldCreateCpfWithValidNumberWithoutFormatting() {
        String validCpf = "12345678909";
        CPF cpf = new CPF(validCpf);

        assertNotNull(cpf);
        assertEquals("12345678909", cpf.getValue());
        assertEquals("123.456.789-09", cpf.getFormattedValue());
    }

    @Test
    @DisplayName("Should create CPF with valid number with formatting")
    void shouldCreateCpfWithValidNumberWithFormatting() {
        String validCpf = "123.456.789-09";
        CPF cpf = new CPF(validCpf);

        assertNotNull(cpf);
        assertEquals("12345678909", cpf.getValue());
        assertEquals("123.456.789-09", cpf.getFormattedValue());
    }

    @Test
    @DisplayName("Should create CPF with another valid number")
    void shouldCreateCpfWithAnotherValidNumber() {
        String validCpf = "111.444.777-35";
        CPF cpf = new CPF(validCpf);

        assertNotNull(cpf);
        assertEquals("11144477735", cpf.getValue());
    }

    @Test
    @DisplayName("Should throw exception when CPF is null")
    void shouldThrowExceptionWhenCpfIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CPF(null);
        });

        assertEquals("CPF cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when CPF is empty")
    void shouldThrowExceptionWhenCpfIsEmpty() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CPF("");
        });

        assertEquals("CPF cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when CPF is blank")
    void shouldThrowExceptionWhenCpfIsBlank() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CPF("   ");
        });

        assertEquals("CPF cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when CPF has wrong length")
    void shouldThrowExceptionWhenCpfHasWrongLength() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CPF("123456789");
        });

        assertTrue(exception.getMessage().contains("Invalid CPF"));
    }

    @Test
    @DisplayName("Should throw exception when CPF has all same digits")
    void shouldThrowExceptionWhenCpfHasAllSameDigits() {
        String[] invalidCpfs = {
            "00000000000",
            "11111111111",
            "22222222222",
            "33333333333",
            "44444444444",
            "55555555555",
            "66666666666",
            "77777777777",
            "88888888888",
            "99999999999"
        };

        for (String invalidCpf : invalidCpfs) {
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                new CPF(invalidCpf);
            });

            assertTrue(exception.getMessage().contains("Invalid CPF"));
        }
    }

    @Test
    @DisplayName("Should throw exception when CPF has invalid check digits")
    void shouldThrowExceptionWhenCpfHasInvalidCheckDigits() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CPF("12345678900");
        });

        assertTrue(exception.getMessage().contains("Invalid CPF"));
    }

    @Test
    @DisplayName("Should format CPF correctly")
    void shouldFormatCpfCorrectly() {
        CPF cpf = new CPF("12345678909");

        assertEquals("123.456.789-09", cpf.getFormattedValue());
        assertEquals("123.456.789-09", cpf.toString());
    }

    @Test
    @DisplayName("Should return true when comparing equal CPFs")
    void shouldReturnTrueWhenComparingEqualCpfs() {
        CPF cpf1 = new CPF("12345678909");
        CPF cpf2 = new CPF("123.456.789-09");

        assertEquals(cpf1, cpf2);
        assertEquals(cpf1.hashCode(), cpf2.hashCode());
    }

    @Test
    @DisplayName("Should return false when comparing different CPFs")
    void shouldReturnFalseWhenComparingDifferentCpfs() {
        CPF cpf1 = new CPF("12345678909");
        CPF cpf2 = new CPF("11144477735");

        assertNotEquals(cpf1, cpf2);
    }

    @Test
    @DisplayName("Should return false when comparing CPF with null")
    void shouldReturnFalseWhenComparingCpfWithNull() {
        CPF cpf = new CPF("12345678909");

        assertNotEquals(cpf, null);
    }

    @Test
    @DisplayName("Should return false when comparing CPF with different object type")
    void shouldReturnFalseWhenComparingCpfWithDifferentObjectType() {
        CPF cpf = new CPF("12345678909");

        assertNotEquals(cpf, "12345678909");
    }

    @Test
    @DisplayName("Should return true when comparing CPF with itself")
    void shouldReturnTrueWhenComparingCpfWithItself() {
        CPF cpf = new CPF("12345678909");

        assertEquals(cpf, cpf);
    }
}