package com.codecool.greencommitment.cmdProg;

public class Menu {
    
    private String title;
    private String[] options;
    
    public Menu(String title, String[] options) {
        this.title = title;
        this.options = options;
    }
    
    public void displayMenu() {
        System.out.println(title + "\n");
        for (int i = 0; i < options.length; i++) {
            System.out.println(i + 1 + ")" + " " + options[i]);
        }
    }
}
