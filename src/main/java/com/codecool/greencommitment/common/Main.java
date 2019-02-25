package com.codecool.greencommitment.common;

import com.codecool.greencommitment.client.Client;
import com.codecool.greencommitment.server.Server;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Depending on how the application is started in client mode or server mode
 * (e.g. depending on the first command-line argument passed to the application)
 * start the client's or the server's code.
 */

public class Main {

    public static void main(String[] args) {
        /*
        try {
            if (args[0].equalsIgnoreCase("server")) {
                new Server(args);
            } else if (args[0].equalsIgnoreCase("client")) {
                new Client(args);
            } else {
                System.exit(-1);
            }
        } catch (IOException e) {
            // TODO: Handle Exception
        } */

        XMLHandler xml = new XMLHandler();
        Measurement m = new Measurement(123, 30, MeasurementType.CELSIUS);
        Measurement m2 = new Measurement(124, 32, MeasurementType.CELSIUS);
        Measurement m3 = new Measurement(123, 31, MeasurementType.CELSIUS);
        List<Measurement> measurements = new ArrayList<>();
        measurements.add(m);
        measurements.add(m2);
        measurements.add(m3);
        for (Measurement measurement : measurements) {
            xml.handleXml(measurement);
        }

    }
}
