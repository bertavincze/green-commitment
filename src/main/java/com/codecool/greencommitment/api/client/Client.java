package com.codecool.greencommitment.api.client;

import com.codecool.greencommitment.api.common.Measurement;
import com.codecool.greencommitment.api.common.MeasurementGenerator;

import java.io.*;
import java.net.Socket;

/**
 * The client application is responsible for generating data that represents some kind of sensor measurement over time.
 * Like an external thermometer, or water level detector in a water container outside your yard, or a CO2 detector.
 * Data is generated every second, minute, etc. (it should be configurable, don't hard-code this into the app).
 * Every generated data-point is sent to the server application via sockets.
 */

public class Client {

    private String hostName;
    private int portNumber;
    private Socket clientSocket;
    private MeasurementGenerator mg;

    public Client(String[] args) {
        this.mg = new MeasurementGenerator();
        this.hostName = args[0];
        this.portNumber = Integer.parseInt(args[1]);
    }

    public void connect() throws IOException {
        clientSocket = new Socket(hostName, portNumber);
    }

    public void disconnect() throws IOException {
        clientSocket.close();
    }

    public void runClientGeneration(int id, int delay, int max) throws IOException, InterruptedException {
        ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
        for (int i = 0; i < max; i++) {
            Measurement measurement = mg.generator(id);
            os.writeObject(measurement.convertToDocument());
            Thread.sleep(delay);
        }
    }
}
