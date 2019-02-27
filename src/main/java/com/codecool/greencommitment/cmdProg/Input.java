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
        return input.equalsIgnoreCase("chart");
    }

    boolean isValidDelay() {
        return 999 < Integer.parseInt(input) && Integer.parseInt(input) <= 10000;
    }

    boolean isValidMax() {
        return 0 < Integer.parseInt(input) && Integer.parseInt(input) <= 10;
    }

    boolean isGenerationRequest() {
        return input.equalsIgnoreCase("send");
    }

    String getInputString() {
        return input;
    }
}
