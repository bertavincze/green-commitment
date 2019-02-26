package com.codecool.greencommitment.common;

import java.util.Random;

public class MeasurementGenerator {
    
    private Random rnd = new Random();
    
    public Measurement generator(){
        return new Measurement(rnd.nextInt(200), rnd.nextInt(100), MeasurementType.CELSIUS);
        
    }
}
