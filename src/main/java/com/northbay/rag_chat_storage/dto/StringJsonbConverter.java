package com.northbay.rag_chat_storage.dto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.postgresql.util.PGobject;

@Converter
public class StringJsonbConverter implements AttributeConverter<String, PGobject> {

    @Override
    public PGobject convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            PGobject obj = new PGobject();
            obj.setType("jsonb");   // important: tells PG this is jsonb
            obj.setValue(attribute); // attribute is a valid JSON string
            return obj;
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert String to JSONB", e);
        }
    }

    @Override
    public String convertToEntityAttribute(PGobject dbData) {
        return dbData != null ? dbData.getValue() : null;
    }
}
