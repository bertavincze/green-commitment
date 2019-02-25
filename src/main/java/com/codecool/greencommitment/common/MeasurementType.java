package com.codecool.greencommitment.common;

public enum MeasurementType {

    CELSIUS("celsius");

    private String value;

    MeasurementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
