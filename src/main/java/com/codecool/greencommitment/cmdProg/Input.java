package com.codecool.greencommitment.cmdProg;

class Input {

    private final String input;

    Input(String input) {
        this.input = input;
    }

    boolean isExitRequest() {
        return input.equalsIgnoreCase("exit");
    }

    boolean isConnectionRequest() {
        return input.equalsIgnoreCase("connect");
    }

    boolean isId() {
        try {
            int i = Integer.parseInt(input);
            return 0 <= i && i <= 999;

        } catch (NumberFormatException e) {
            System.out.println("Must be a number!");
            return false;
        }
    }

    boolean isChartRequest() {
        return input.matches("(^chart\\s\\d{1,3})");
    }
}
