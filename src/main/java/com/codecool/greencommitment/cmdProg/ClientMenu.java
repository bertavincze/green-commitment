package com.codecool.greencommitment.cmdProg;

import com.codecool.greencommitment.api.client.Client;
import com.codecool.greencommitment.api.common.ChartGenerator;
import com.codecool.greencommitment.api.common.ChartType;
import com.codecool.greencommitment.api.common.CustomException;
import com.codecool.greencommitment.api.common.XMLHandler;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

class ClientMenu {

    private String title;
    private String[] options;
    private final Scanner sc;
    private XMLHandler xmlHandler;
    private Client client;
    
    ClientMenu(String title, String[] options) {
        this.title = title;
        this.options = options;
        this.sc = new Scanner(System.in);
        this.xmlHandler = new XMLHandler();
    }
    
    private void displayMenu() {
        System.out.print(title);
        for (String option:options) {
            if (option.equals(options[options.length-1])) {
                System.out.println(option);
            } else {
                System.out.print(option + ", ");
            }
        }
    }

    private Input getInput() {
        return new Input(sc.nextLine());
    }

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
            } else if (input.isChartRequest()) {
                handleChartRequest();
            }
        }
    }

    private void handleChartRequest() {
        printFileNames();
        System.out.println("\nEnter the id of the sensor: ");
        Input input = getInput();
        if (xmlHandler.getFiles(".").contains(input.getInputString() + ".xml")) {
            try {
                ChartType chartType = getChartType();
                new ChartGenerator().generateChart(input.getInputString(), chartType);
                System.out.println("Chart image generated to root folder as: " + input.getInputString() + ".jpeg");
            } catch (CustomException e) {
                System.out.println("Chart type not recognized!");
            } catch (IOException e) {
                System.out.println("IO Exception occurred while creating chart!");
            }
        } else {
            System.out.println("Sensor id not found!");
        }
    }

    private ChartType getChartType() throws CustomException {
        System.out.println("choose from available chart types: bar, line");
        Input chartType = getInput();
        if (chartType.getInputString().equalsIgnoreCase("bar")) {
            return ChartType.BAR;
        } else if (chartType.getInputString().equalsIgnoreCase("line")) {
            return ChartType.LINE;
        } else {
            throw new CustomException("Invalid chart type!");
        }
    }

    private void printFileNames() {
        List<String> fileNames = xmlHandler.getFiles(".");
        System.out.println("list of sensor ids currently stored: ");
        for (String fileName: fileNames) {
            if (fileName.equals(fileNames.get(fileNames.size()-1))) {
                System.out.print(fileName.replaceFirst(".xml", ""));
            } else {
                System.out.print(fileName.replaceFirst(".xml", "") + ", ");
            }
        }
        System.out.println();
    }

    private void handleConnectionRequest() {
        client = new Client(getConnectionDetails());
        try {
            client.connect();
        } catch (IOException e) {
            System.out.println("IO Exception occurred!");
        }
    }

    private void handleGeneration() {
        try {
            client.runClientGeneration(getSensorId(), getDelay(), getMaxGenerations());
        } catch (CustomException e) {
            System.out.println("Cannot start generation with given data!");
        } catch (InterruptedException e) {
            System.out.println("Thread was interrupted during generation.");
        } catch (IOException e) {
            e.printStackTrace();
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
        System.exit(0);
    }
}
