package com.raulrh.tiendatelevisiones.base.enums;

/**
 * Enum representing different types of customers.
 * This enum defines the possible categories of customers in the application.
 */
public enum CustomerType {
    DESEMPLEADO((short) 0),  // Unemployed
    JUBILADO((short) 1),     // Retired
    ESTUDIANTE((short) 2),   // Student
    TRABAJADOR((short) 3);     // Worker

    private final short code;

    CustomerType(short code) {
        this.code = code;
    }

    public static CustomerType fromCode(short code) {
        for (CustomerType type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return null;
    }

    public short getCode() {
        return code;
    }
}