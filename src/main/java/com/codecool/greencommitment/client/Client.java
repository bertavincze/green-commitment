package com.codecool.greencommitment.client;

import java.io.IOException;
import java.net.Socket;

/**
 * The client application is responsible for generating data that represents some kind of sensor measurement over time.
 * Like an external thermometer, or water level detector in a water container outside your yard, or a CO2 detector.
 * Data is generated every second, minute, etc. (it should be configurable, don't hard-code this into the app).
 * Every generated data-point is sent to the server application via sockets.
 */

public class Client {

    public Client(String[] args) throws IOException {
        String hostName = args[1];
        int portNumber = Integer.parseInt(args[2]);
        Socket clientSocket = new Socket(hostName, portNumber);
    }
}
