package main;

import controller.BankController;
import controller.BankControllerManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Client");
        System.out.println("2. Manager");
        System.out.print("Choose which operation you want to perform: ");
        int choice = scanner.nextInt();

        if (choice == 1) {
            BankController bankController = new BankController();
            bankController.getLoginScreen().setVisible(true);
        } else if (choice == 2) {
            BankControllerManager bankControllerManager = new BankControllerManager();
            bankControllerManager.getLogInScreenManager().setVisible(true);
        } else {
            System.out.println("Invalid choice! Ending program!");
        }
    }
}
