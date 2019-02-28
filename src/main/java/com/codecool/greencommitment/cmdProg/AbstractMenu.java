package com.codecool.greencommitment.cmdProg;

import java.util.Scanner;

abstract class AbstractMenu {

    private String title;
    private String[] options;
    private final Scanner sc = new Scanner(System.in);

    AbstractMenu(String title, String[] options) {
        this.title = title;
        this.options = options;
    }

    Input getInput() {
        return new Input(sc.nextLine());
    }

    void displayMenu() {
        System.out.print(title);
        for (String option:options) {
            if (option.equals(options[options.length-1])) {
                System.out.println(option);
            } else {
                System.out.print(option + ", ");
            }
        }
    }

    abstract void handleMenu();
}
