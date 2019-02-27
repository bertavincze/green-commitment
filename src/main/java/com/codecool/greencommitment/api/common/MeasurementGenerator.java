package com.codecool.greencommitment.api.common;

import java.util.Random;

public class MeasurementGenerator {
    
    private Random rnd = new Random();
    
    public Measurement generator(int id){
        return new Measurement(id, rnd.nextInt(70) - 40, MeasurementType.CELSIUS);
    }
}
