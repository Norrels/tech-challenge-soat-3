package br.com.dealership.modules.sale.adapter.database.converters;

import br.com.dealership.modules.sale.domain.entities.valueobjects.CPF;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CPFConverter implements AttributeConverter<CPF, String> {

    @Override
    public String convertToDatabaseColumn(CPF cpf) {
        if (cpf == null) {
            return null;
        }
        return cpf.getValue();
    }

    @Override
    public CPF convertToEntityAttribute(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return new CPF(value);
    }
}