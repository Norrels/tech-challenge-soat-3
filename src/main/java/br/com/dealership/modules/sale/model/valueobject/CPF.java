package br.com.dealership.modules.sale.model.valueobject;

import java.util.Objects;

public class CPF {
    private final String value;

    public CPF(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("CPF cannot be null or empty");
        }

        String cleanCpf = value.replaceAll("[^0-9]", "");

        if (!isValid(cleanCpf)) {
            throw new IllegalArgumentException("Invalid CPF: " + value);
        }

        this.value = cleanCpf;
    }

    private boolean isValid(String cpf) {
        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int sum = 0;
            for (int i = 0; i < 9; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int firstDigit = 11 - (sum % 11);
            firstDigit = firstDigit >= 10 ? 0 : firstDigit;

            if (Character.getNumericValue(cpf.charAt(9)) != firstDigit) {
                return false;
            }

            sum = 0;
            for (int i = 0; i < 10; i++) {
                sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int secondDigit = 11 - (sum % 11);
            secondDigit = secondDigit >= 10 ? 0 : secondDigit;

            return Character.getNumericValue(cpf.charAt(10)) == secondDigit;
        } catch (Exception e) {
            return false;
        }
    }

    public String getValue() {
        return value;
    }

    public String getFormattedValue() {
        return String.format("%s.%s.%s-%s",
                value.substring(0, 3),
                value.substring(3, 6),
                value.substring(6, 9),
                value.substring(9, 11));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CPF cpf = (CPF) o;
        return Objects.equals(value, cpf.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return getFormattedValue();
    }
}