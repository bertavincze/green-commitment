package com.codecool.greencommitment.api.common;

import java.io.Serializable;

public class Measurement implements Serializable {
    private static final long serialVersionUID = 158441623L;
    private int id;
    private long time;
    private int value;
    private MeasurementType measurementType;

    public Measurement(int id, int value, MeasurementType measurementType) {
        this.id = id;
        this.time = System.currentTimeMillis();
        this.value = value;
        this.measurementType = measurementType;
    }

    public Measurement(int id, long time, int value, MeasurementType measurementType) {
        this.id = id;
        this.time = time;
        this.value = value;
        this.measurementType = measurementType;
    }

    public int getId() {
        return id;
    }

    public long getTime() {
        return time;
    }

    public int getValue() {
        return value;
    }

    public MeasurementType getMeasurementType() {
        return measurementType;
    }
}
