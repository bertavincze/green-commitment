package com.codecool.greencommitment.api.server;

import com.codecool.greencommitment.api.common.XMLHandler;
import org.w3c.dom.Document;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The server application is responsible for accepting/reading and accumulating data-points sent by the client applications.
 * Can have more than 1 client. Multiple client applications can connect the server, each acts as a different sensor.
 * The server collects data over time from every sensor that connects to it.
 * The server stores collected data in an XML file. The file is only updated when the user exits the server application
 * (until then the server collects data in memory).
 * While the server's running the user of the application has the option to generate a chart (image) from the data
 * collected so far. Users are able to select which sensor's data to use to generate the chart. The generated chart
 * should be saved in a directory/file specified by the user. The chart has two axes, time and measured value.
 * In the case of a thermometer measured value is in celsius degrees, in the case of a water level detector
 * it's in meters or centimeters, etc.
 */

public class Server {

    public Server(String[] args) throws IOException {
        runServer(args[1]);
    }

    private void runServer(String arg) throws IOException {
        int portNumber = Integer.parseInt(arg);
        ServerSocket ss = new ServerSocket(portNumber);
        System.out.println("The server is ready for the measurements: ");
        while (true) {
            final Socket socket = ss.accept();
            new Thread(() -> {
                try {
                    ObjectInputStream is = new ObjectInputStream(socket.getInputStream());

                    Document measurementDoc = null;
                    while (true) {
                        try {
                            XMLHandler xml = new XMLHandler();
                            measurementDoc = (Document)is.readObject();
                            xml.handleXml(measurementDoc);
                        } catch (EOFException e) {
                            break;
                        }
                    }
                } catch (IOException | ClassNotFoundException e ) {
                    e.printStackTrace();
                }
            }).start();

        }
    }

}
