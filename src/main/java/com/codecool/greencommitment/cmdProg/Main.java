package com.codecool.greencommitment.cmdProg;

import com.codecool.greencommitment.api.client.Client;
import com.codecool.greencommitment.api.server.Server;
import java.io.IOException;

/**
 * Depending on how the application is started in client mode or server mode
 * (e.g. depending on the first command-line argument passed to the application)
 * start the client's or the server's code.
 */

public class Main {

    public static void main(String[] args) {

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
            e.printStackTrace();
        } 
    }
}
