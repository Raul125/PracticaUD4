package com.raulrh.tiendatelevisiones.base.enums;

/**
 * Enum representing the different types of televisions available.
 * This enum defines the possible types of televisions that can be used in the application.
 */
public enum TelevisionType {
    QLED((short) 0),  // Quantum Dot LED
    OLED((short) 1),  // Organic Light-Emitting Diode
    LED((short) 2),   // Light Emitting Diode
    PLASMA((short) 3), // Plasma Display
    LCD((short) 4);    // Liquid Crystal Display

    private final short code;

    TelevisionType(short code) {
        this.code = code;
    }

    public static TelevisionType fromCode(short code) {
        for (TelevisionType type : values()) {
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