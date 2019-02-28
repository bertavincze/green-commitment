package com.codecool.greencommitment.cmdProg;

import com.codecool.greencommitment.api.client.Client;
import com.codecool.greencommitment.api.common.ChartGenerator;
import com.codecool.greencommitment.api.common.ChartType;
import com.codecool.greencommitment.api.common.CustomException;
import com.codecool.greencommitment.api.common.XMLHandler;

import java.io.IOException;
import java.util.List;

class ClientMenu extends AbstractMenu {

    private Client client;
    
    ClientMenu(String title, String[] options) {
        super(title, options);
    }

    @Override
    void handleMenu() {
        while (true) {
            displayMenu();
            Input input = getInput();
            if (input.isExitRequest()) {
                handleExitRequest();
            } else if (input.isConnectionRequest()) {
                handleConnectionRequest();
            } else if (input.isGenerationRequest()) {
                handleGeneration();
            }
        }
    }
    
    private void handleConnectionRequest() {
        client = new Client(getConnectionDetails());
        try {
            client.connect();
        } catch (IOException e) {
            System.out.println("IO Exception occurred: " + e.getMessage());
        }
    }

    private void handleGeneration() {
        try {
            client.runClientGeneration(getSensorId(), getDelay(), getMaxGenerations());
        } catch (CustomException e) {
            System.out.println("Cannot start generation with given data!");
        }
    }

    private int getMaxGenerations() throws CustomException {
        System.out.println("Enter the maximum amount of measurements to send: ");
        Input input = getInput();
        if (input.isValidMax()) {
            return Integer.parseInt(input.getInputString());
        } else {
            throw new CustomException("Invalid input!");
        }
    }

    private int getDelay() throws CustomException {
        System.out.println("Enter the delay in milliseconds (1000-10000): ");
        Input input = getInput();
        if (input.isValidDelay()) {
            return Integer.parseInt(input.getInputString());
        } else {
            throw new CustomException("Invalid input!");
        }
    }

    private Integer getSensorId() throws CustomException {
        System.out.println("enter an id for the sensor: ");
        Input input = getInput();
        if (input.isId()) {
            return Integer.parseInt(input.getInputString());
        } else {
            throw new CustomException("Invalid input!");
        }
    }

    private String[] getConnectionDetails() {
        System.out.println("Enter hostname / IP address: ");
        String hostName = getInput().getInputString();
        System.out.println("Enter port number: ");
        String portNumber = getInput().getInputString();
        return new String[]{hostName, portNumber};
    }

    private void handleExitRequest() {
        try {
            client.disconnect();
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Client socket closed.");
        }
    }
}
