/**
 * COPYRIGHT (C) 2022 Chamath Jayasena. All Rights Reserved.
 * Classes version (tasks 2, 3, 4)
 * Name - Rathnayaka Mudiyanselage Chamath Sakuntha Jayasena
 * UoW ID - w1898955
 * IIT ID - 20211387
 */
package com.example.cw_classes_w1898955;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {

    static Scanner sc = new Scanner(System.in);  // Global scanner object

    static int fuelStock = 6600;
    static int fuelLiterPrice = 430;

    static FuelQueue[] queues = new FuelQueue[5];  // Array of 5 FuelQueue objects

    // Circular queue with size 100 as the waiting list queue
    static CircularWaitingQueue waitingQueue = new CircularWaitingQueue(100);

    static boolean javaFxLaunched = false;  // Boolean flag to check if javafx runtime is running

    @Override
    public void start(Stage stage) throws IOException {
        OperatorViewController ovc = new OperatorViewController(queues, waitingQueue);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("operator-view.fxml"));
        fxmlLoader.setController(ovc);
        Scene scene = new Scene(fxmlLoader.load(), 680, 520);
        stage.setTitle("Fuel Queues GUI");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        createQueues(queues.length);
        printMenu();
        while (true) {
            System.out.println();
            System.out.print(">> Enter an option: ");
            String choice = sc.next().toUpperCase();  // Accept lowercase as well
            System.out.println();
            // Enhanced switch statement: https://openjdk.org/jeps/361
            switch (choice) {
                case "100", "VFQ" -> viewFuelQueues();
                case "101", "VEQ" -> viewEmptyQueues();
                case "102", "ACQ" -> addCustomer();
                case "103", "RCQ" -> removeCustomer();
                case "104", "PCQ" -> removeServedCustomer();
                case "105", "VCS" -> viewCustomersSorted();
                case "106", "SPD" -> storeProgramData();
                case "107", "LPD" -> loadProgramData();
                case "108", "STK" -> viewFuelStock();
                case "109", "AFS" -> addFuelStock();
                case "110", "IFQ" -> viewFuelQueueIncome();
                case "111", "VQG" -> showGUI();
                case "999", "EXT" -> {
                    System.out.println("Bye! Have a great day..");
                    // Breaking the loop is not enough here as javafx runtime keeps running
                    // System.exit() will stop javafx runtime and the program
                    System.exit(0);
                }
                default -> System.out.println("Error: Invalid option");
            }
        }
    }

    /**
     * Print console menu options
     */
    public static void printMenu() {
        System.out.println("<<<<<<<<<<< " + coloredText("Fuel Queue Management System V2", 2) +
                " >>>>>>>>>>>>");
        System.out.println("(Hint: Invalid input will get you back to the main menu)");
        System.out.println("--------------------------------------------------------");
        System.out.println("(100/VFQ) - View all fuel queues");
        System.out.println("(101/VEQ) - View all empty queues");
        System.out.println("(102/ACQ) - Add customer to a queue");
        System.out.println("(103/RCQ) - Remove a customer from a queue");
        System.out.println("(104/PCQ) - Remove a served customer");
        System.out.println("(105/VCS) - View customers sorted in alphabetical order");
        System.out.println("(106/SPD) - Store program data into file");
        System.out.println("(107/LPD) - Load program data from file");
        System.out.println("(108/STK) - View remaining fuel stock");
        System.out.println("(109/AFS) - Add fuel stock");
        System.out.println("(110/IFQ) - View fuel queue income");
        System.out.println("(111/VQG) - View & search queues in GUI");
        System.out.println("(999/EXT) - Exit the program");
        System.out.print("--------------------------------------------------------");
    }

    /**
     * Create and add given number of FuelQueue objects to the queues array
     */
    public static void createQueues(int number) {
        for (int i = 0; i < number; i++) {
            queues[i] = new FuelQueue(6, i + 1);
        }
    }

    /**
     * Print all the customers in all the queues including the waiting list queue
     */
    public static void viewFuelQueues() {
        System.out.println("[All Fuel Queues]");
        for (FuelQueue queue : queues) {
            System.out.printf("Pump %d: ", queue.getPump());
            printCustomers(queue.getCustomerNames());
        }
        // Print waiting queue
        System.out.print("Waiting Queue: ");
        printCustomers(waitingQueue.getCustomerNames());
    }

    /**
     * Print queues with empty slots
     */
    public static void viewEmptyQueues() {
        System.out.println("[Empty Queues]");
        for (FuelQueue queue : queues) {
            if (queue.hasEmptySlots()) {
                System.out.printf("Pump %d: ", queue.getPump());
                printCustomers(queue.getCustomerNames());
            }
        }
    }

    /**
     * Add a new customer to the queue with minimum length.
     * If all fuel queues are full, add the customer to the waiting queue
     */
    public static void addCustomer() {
        System.out.println("[Add Customer]");

        // Loop and ask to add more customers
        String enteringAgain;
        do {
            System.out.print("> Enter first name: ");
            String firstName = sc.next();
            System.out.print("> Enter second name: ");
            String secondName = sc.next();
            System.out.print("> Enter vehicle number: ");
            String vehicleNo = sc.next();  // Vehicle number includes letters
            System.out.print("> Enter required liters: ");
            if (!sc.hasNextInt()) {
                System.out.println(coloredText("Error: ", 3) + "Required liters should be a number");
                sc.next();  // Consume leftover input
                return;
            }

            int requiredLiters = sc.nextInt();
            if (requiredLiters > fuelStock) {
                System.out.println(coloredText("Error: ", 3) + "Does not have that much fuel in stock!");
                return;
            }

            // Create a new passenger object with the given input
            Passenger passenger = new Passenger(firstName, secondName, vehicleNo, requiredLiters);

            // If all queues are full, add the customer to the waiting queue
            if (isEveryQueueFull()) {
                waitingQueue.enQueue(passenger);
                System.out.printf(coloredText("Info: ", 2) + "Fuel queues are full. " +
                        "Added '%s' to the waiting queue%n", passenger.getFullName());
            } else {  //  If there are empty queues, the passenger to the queue with the minimum length
                FuelQueue minimumLengthQueue = getMinimumLengthQueue();
                minimumLengthQueue.addCustomer(passenger);
                System.out.printf(coloredText("Success: ", 1) + "Added '%s' to the Pump %d queue%n",
                        passenger.getFullName(), minimumLengthQueue.getPump());
            }

            // Assume the added customer is served with required liters
            fuelStock -= requiredLiters;

            // Show a warning message if fuel stock is 500l or less1
            if (fuelStock <= 500) {
                System.out.println(coloredText("Warning: ", 3) +
                        "Fuel stock is at or less than 500 liters!");
            }

            System.out.print("> Do you want to add another customer (y/n)? ");
            enteringAgain = sc.next();
        } while (enteringAgain.equalsIgnoreCase("y"));
    }

    /**
     * Remove a customer from a specific position in the selected queue and
     * get the next customer in the waiting list queue and add to the fuel queue
     */
    public static void removeCustomer() {
        System.out.println("[Remove Customer]");

        int pumpNum = getPump();
        if (pumpNum == -1) {
            return;
        }

        int position = getPosition();
        if (position == -1) {
            return;
        }

        try {
            FuelQueue queue = queues[pumpNum - 1];  // Get the selected queue
            Passenger customer = queue.getCustomer(position - 1);  // Get the customer in the position
            queue.removeCustomer(customer);

            // Assume reduced liters should be added back because this customer will not be served
            fuelStock += customer.getRequiredLiters();

            System.out.printf(coloredText("Success: ", 1) +
                    "Removed '%s' from Pump %d queue%n", customer.getFullName(), pumpNum);

            // Add the next customer in the waiting queue to the fuel queue
            try {
                // deQueue() method removes and returns the first customer in the waiting queue
                Passenger nextWaitingCustomer = waitingQueue.deQueue();

                // Add that customer to the fuel queue
                queue.addCustomer(nextWaitingCustomer);

                System.out.printf(coloredText("Success: ", 1) + "Added next customer in the " +
                        "waiting queue '%s' to the Pump %d queue%n", nextWaitingCustomer.getFullName(), pumpNum);

            } catch (Exception e) {
                System.out.println(coloredText("Info: ", 2) + "No customers in the waiting queue to add");
            }

        } catch (Exception e) {
            System.out.printf(coloredText("Error: ", 3) +
                    "There is no customer at this position in Pump %d queue%n", pumpNum);
        }
    }

    /**
     * Remove the served customer in a selected queue and get the next
     * customer in the waiting list queue and add to the fuel queue
     */
    public static void removeServedCustomer() {
        System.out.println("[Remove Served Customer]");

        int pumpNum = getPump();
        if (pumpNum == -1) {
            return;
        }

        FuelQueue queue = queues[pumpNum - 1];

        try {
            // Served customer should be the first one in the queue at that time
            Passenger servedCustomer = queue.getCustomer(0);

            queue.removeCustomer(servedCustomer);

            // Update the fuel income of the queue
            queue.updateIncome(servedCustomer.getRequiredLiters() * fuelLiterPrice);

            System.out.printf(coloredText("Success: ", 1) +
                    "Removed the served customer '%s' from Pump %d queue%n",
                    servedCustomer.getFullName(), pumpNum);

            // Add the next customer in the waiting queue to the fuel queue
            try {
                // deQueue() method removes and returns the first customer in the waiting queue
                Passenger nextWaitingCustomer = waitingQueue.deQueue();

                // Add that customer to the fuel queue
                queue.addCustomer(nextWaitingCustomer);

                System.out.printf(coloredText("Success: ", 1) + "Added next customer in the " +
                        "waiting queue '%s' to the Pump %d queue%n", nextWaitingCustomer.getFullName(), pumpNum);

            } catch (Exception e) {
                System.out.println(coloredText("Info: ", 2) + "No customers in the waiting queue to add");
            }
        } catch (Exception e) {
            System.out.printf(coloredText("Error: ", 3) +
                    "There are no customers in Pump %d queue%n", pumpNum);
        }
    }

    /**
     * Print customers of the selected queue in alphabetical order
     */
    public static void viewCustomersSorted() {
        System.out.println("[View Customers Sorted]");
        int pumpNum = getPump();
        if (pumpNum == -1) {
            return;
        }
        FuelQueue queue = queues[pumpNum - 1];
        try {
            System.out.printf("Pump %d (sorted): ", pumpNum);
            printCustomers(queue.getCustomerNamesSorted());
        } catch (Exception e) {
            System.out.printf(coloredText("Error: ", 3) +
                    "There are no customers in Pump %d queue%n", pumpNum);
        }
    }

    /**
     * Serialize program data objects and store in a 'data.ser' file
     */
    public static void storeProgramData() {
        System.out.println("[Store Program Data]");

        System.out.print("This will overwrite previous backup file if it exists. Are you sure? (y/n): ");
        String choice = sc.next();
        if (!choice.equalsIgnoreCase("y")) {
            return;
        }

        // https://metapx.org/serialize-deserialize-object-java/
        // Use a try-with-resources statement so the resources will be automatically closed
        // https://www.baeldung.com/java-try-with-resources
        try (
                FileOutputStream fos = new FileOutputStream("data.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            // Write objects to the file
            oos.writeObject(queues);
            oos.writeObject(waitingQueue);
            oos.writeObject(fuelStock);

            // flush() method ensures java has finished writing the objects before closing the stream
            oos.flush();

            System.out.println(coloredText("Success: ", 1) + "Program data stored in 'data.ser'");
        } catch (Exception e) {
            System.out.println(coloredText("Error: ", 3) + "Could not create the data file");
        }
    }

    /**
     * Deserialize program data in the 'data.ser' file and restore them
     */
    public static void loadProgramData() {
        System.out.println("[Load Program Data]");

        System.out.print("This will overwrite current program data! Are you sure? (y/n): ");
        String choice = sc.next();
        if (!choice.equalsIgnoreCase("y")) {
            return;
        }

        // Deserialize objects in the 'data.ser' file
        // https://metapx.org/serialize-deserialize-object-java/
        try (
                FileInputStream fis = new FileInputStream("data.ser");
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            // Read objects from the file and assign them to program variables
            // Reading order should be the same order as the writing one
            queues = (FuelQueue[]) ois.readObject();
            waitingQueue = (CircularWaitingQueue) ois.readObject();
            fuelStock = (int) ois.readObject();

            System.out.println(coloredText("Success: ", 1) + "Program data loaded from 'data.ser'");
        } catch (Exception e) {
            System.out.println(coloredText("Error: ", 3) + "Data file not found");
        }
    }

    /**
     * Print remaining fuel stock
     */
    public static void viewFuelStock() {
        System.out.println("[Fuel Stock]");
        System.out.printf("Available fuel stock: %d liters%n", fuelStock);
    }

    /**
     * Add fuel stock
     */
    public static void addFuelStock() {
        System.out.println("[Add Fuel Stock]");
        System.out.print("> Enter fuel amount(liters) to add: ");
        int amount;
        if (sc.hasNextInt()) {
            amount = sc.nextInt();
        } else {
            System.out.println(coloredText("Error: ", 3) + "Fuel amount must be a number");
            sc.next();  // Consume leftover input
            return;
        }
        // Assume the stock limit is 6600l
        if (fuelStock == 6600) {
            System.out.println(coloredText("Error: ", 3)  + "Fuel stock is full");
        } else if (amount + fuelStock > 6600) {
            // Show the maximum amount that can be added without exceeding 6600l limit
            System.out.printf(coloredText("Error: ", 3)  +
                    "Maximum that can be added without exceeding the limit is %d liters%n", 6600 - fuelStock);
        } else {
            fuelStock += amount;
            System.out.printf(coloredText("Success: ", 1) + "Added %d liters to the stock%n", amount);
        }
    }

    /**
     * Print the income of fuel queues
     */
    public static void viewFuelQueueIncome() {
        System.out.println("[Fuel Queue Income]");
        for (FuelQueue queue : queues) {
            System.out.printf("Pump %d: ", queue.getPump());
            System.out.println("Rs. " + queue.getIncome());
        }
    }

    /**
     * Check whether all fuel queues are full
     */
    public static boolean isEveryQueueFull() {
        boolean allFull = true;
        for (FuelQueue queue : queues) {
            if (queue.hasEmptySlots()) {
                allFull = false;
            }
        }
        return allFull;
    }

    /**
     * Find and return the queue with minimum length
     */
    public static FuelQueue getMinimumLengthQueue() {
        FuelQueue queue = queues[0];
        for (int i = 1; i < queues.length; i++) {
            if (queues[i].getLength() < queue.getLength()) {
                queue = queues[i];
            }
        }
        return queue;
    }

    /**
     * Validate and get pump number from the user
     */
    public static int getPump() {
        int pumpNum;
        System.out.print("> Enter pump (1-5): ");
        if (sc.hasNextInt()) {
            pumpNum = sc.nextInt();
            if (1 <= pumpNum && pumpNum <= 5) {  // 1 <= pumpNum <= 3
                return pumpNum;
            } else {
                System.out.println(coloredText("Error: ", 3)  + "Pump must be 1, 2, 3, 4, 5");
            }
        } else {
            System.out.println(coloredText("Error: ", 3)  + "Pump must be 1, 2, 3, 4, 5");
            // Consume any leftover input, otherwise the scanner will read this leftover input next time
            // https://stackoverflow.com/questions/45979007/why-does-input-nextint-method-have-a-n-as-a-leftover
            sc.next();
        }
        return -1;
    }

    /**
     * Validate and get customer position from the user
     */
    public static int getPosition() {
        int position;
        System.out.print("> Enter position (1-6): ");
        if (sc.hasNextInt()) {
            position = sc.nextInt();
            if (1 <= position && position <= 6) {
                return position;
            } else {
                System.out.println(coloredText("Error: ", 3)  + "Position must be 1, 2, 3, 4, 5, or 6");
            }
        } else {
            System.out.println(coloredText("Error: ", 3)  + "Position must be 1, 2, 3, 4, 5, or 6");
            sc.next();
        }
        return -1;
    }

    /**
     * Launch the javaFX GUI to view and search customers
     */
    public static void showGUI() {
        System.out.println("[Fuel Queue GUI]");
        System.out.println("Launching GUI..");
        System.out.println(coloredText("Info: ", 2) +
                "To see customers added while the GUI is open, click the refresh button");

        // It is not allowed to call the launch() method to run javafx application more than once,
        // it will throw an exception if you try to. So to be able to show and close a GUI window
        // more than once, we have to keep the javafx runtime running in the background.
        // And the next time we need to show the window, we have to execute it in the javaFX application thread.
        // https://stackoverflow.com/questions/24320014/how-to-call-launch-more-than-once-in-java
        if (!javaFxLaunched) { // First time
            Platform.setImplicitExit(false);  // Do not exit when closing the window
            new Thread(()->Application.launch(Main.class)).start();
            javaFxLaunched = true;
        } else {  // Next time
            Platform.runLater(()->{  // Run it in the application thread
                try {
                    Application application = Main.class.newInstance();
                    Stage primaryStage = new Stage();
                    application.start(primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * Print the given customers ArrayList
     */
    public static void printCustomers(ArrayList<String> customers) {
        if (!customers.isEmpty()) {
            String customersStr = customers.toString();
            // Remove opening and closing square brackets
            String customersStrNoBrackets = customersStr.substring(1, customersStr.length() - 1);
            System.out.println(customersStrNoBrackets);
        } else {
            System.out.println("empty");
        }
    }

    /**
     * Add colors to the given string using ANSI color codes.
     * https://www.geeksforgeeks.org/how-to-print-colored-text-in-java-console/
     */
    public static String coloredText(String txt, int color) {
        // [default, green, yellow, red]
        final String[] COLOURS = {"\u001B[0m", "\u001B[32m", "\u001B[33m", "\u001B[31m"};
        return COLOURS[color] + txt + COLOURS[0];
    }

}
