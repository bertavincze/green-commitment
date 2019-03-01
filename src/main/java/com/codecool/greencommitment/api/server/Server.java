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

    private int portNumber;
    private ServerSocket serverSocket;

    public Server(String[] args) throws IOException {
        this.portNumber = Integer.parseInt(args[1]);
        this.serverSocket = new ServerSocket(portNumber);
    }

    public void runServer() throws IOException {
        while (true) {
            final Socket socket = serverSocket.accept();
            new Thread(() -> {
                try {
                    ObjectInputStream is = new ObjectInputStream(socket.getInputStream());

                    while (true) {
                        try {
                            XMLHandler xml = new XMLHandler();
                            Document measurementDoc = (Document)is.readObject();
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

    public void disconnect() throws IOException {
        this.serverSocket.close();
    }

    public int getPortNumber() {
        return portNumber;
    }
}
