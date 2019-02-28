package com.codecool.greencommitment.cmdProg;

import com.codecool.greencommitment.api.common.ChartGenerator;
import com.codecool.greencommitment.api.common.ChartType;
import com.codecool.greencommitment.api.common.CustomException;
import com.codecool.greencommitment.api.common.XMLHandler;
import com.codecool.greencommitment.api.server.Server;

import java.io.IOException;
import java.util.List;

class ServerMenu extends AbstractMenu {

    private Server server;
    private XMLHandler xmlHandler;

    ServerMenu(String title, String[] options, String[] args) throws IOException {
        super(title, options);
        this.server = new Server(args);
        new Thread(() -> {
            try {
                server.runServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        this.xmlHandler = new XMLHandler();
    }

    @Override
    void handleMenu() {
        while (true) {
            System.out.println("Server is running on port number: " + server.getPortNumber());
            displayMenu();
            Input input = getInput();
            if (input.isExitRequest()) {
                handleExitRequest();
            } else if (input.isChartRequest()) {
                handleChartRequest();
            }
        }
    }

    private void handleExitRequest() {
        try {
            server.disconnect();
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Server socket closed.");
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
}
