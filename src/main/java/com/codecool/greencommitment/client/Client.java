package com.codecool.greencommitment.client;

import com.codecool.greencommitment.common.Measurement;
import com.codecool.greencommitment.common.MeasurementType;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * The client application is responsible for generating data that represents some kind of sensor measurement over time.
 * Like an external thermometer, or water level detector in a water container outside your yard, or a CO2 detector.
 * Data is generated every second, minute, etc. (it should be configurable, don't hard-code this into the app).
 * Every generated data-point is sent to the server application via sockets.
 */

public class Client {

    public Client(String[] args) throws IOException, FileNotFoundException {

        runClient(args);
    }

    private void runClient(String[] args) throws IOException {
        String hostName = args[1];
        int portNumber = Integer.parseInt(args[2]);
        Socket clientSocket = new Socket(hostName, portNumber);
        Scanner reader = new Scanner(System.in);
        int id = Integer.valueOf(reader.nextLine());
        int value = Integer.valueOf(reader.nextLine());
        MeasurementType measurementType = MeasurementType.valueOf(reader.nextLine());
        Measurement m = new Measurement(id, value, measurementType);
        System.out.println(m.getId());
        try {
            // Serialize today's date to a outputstream associated to the socket
            ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println(m.getId());
            os.writeObject(m);
            Measurement rm = (Measurement) is.readObject();
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
