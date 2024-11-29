package com.example.librarysystem;

import java.util.Scanner;

public class InputValidation {

    public static boolean validateInput(int userInput, int min, int max) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.print("Enter a number between " + min + " and " + max + ": ");
                userInput = scanner.nextInt();

                if (userInput >= min && userInput <= max) {
                    return true;
                } else {
                    System.out.println("Invalid input. Please enter a number within the specified range.");
                }
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.next();
            }
        }
    }
}