package com.codecool.greencommitment.cmdProg;

import java.util.Scanner;

class Menu {
    
    private String title;
    private String[] options;
    private final Scanner sc = new Scanner(System.in);
    
    Menu(String title, String[] options) {
        this.title = title;
        this.options = options;
    }
    
    private void displayMenu() {
        System.out.println(title + "\n");
        for (String option:options) {
            System.out.print(option + ", ");
        }
    }

    private Input getInput() {
        return new Input(sc.nextLine());
    }

    void handleMenu() {
        displayMenu();
        Input input = getInput();
        if (input.isExitRequest()) {
            System.exit(0);
        } else if (input.isConnectionRequest()) {
            handleConnectionRequest();
        } else if (input.isId()) {
            handleGeneration();
        } else if (input.isChartRequest()) {
            handleChartRequest();
        }
    }

    private void handleChartRequest() {
    }

    private void handleConnectionRequest() {
    }

    private void handleGeneration() {
    }
}
