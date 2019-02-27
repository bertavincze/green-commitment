package com.codecool.greencommitment.api.common;

import java.util.Random;

public class MeasurementGenerator {
    
    private Random rnd = new Random();
    
    public Measurement generator(){
        return new Measurement(rnd.nextInt(150), rnd.nextInt(70) - 40, MeasurementType.CELSIUS);
    }
}
