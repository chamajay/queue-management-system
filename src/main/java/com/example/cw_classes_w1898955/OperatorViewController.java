/**
 * COPYRIGHT (C) 2022 Chamath Jayasena. All Rights Reserved.
 * Classes version (tasks 2, 3, 4)
 * Name - Rathnayaka Mudiyanselage Chamath Sakuntha Jayasena
 * UoW ID - w1898955
 * IIT ID - 20211387
 */
package com.example.cw_classes_w1898955;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class OperatorViewController {
    private FuelQueue[] queues;
    private CircularWaitingQueue waitingQueue;
    private ObservableList<Passenger> selectedCustomers;

    // JavaFX elements
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Passenger> tableView;
    @FXML
    private TableColumn<Passenger, String> firstNameCol;
    @FXML
    private TableColumn<Passenger, String> secondNameCol;
    @FXML
    private TableColumn<Passenger, String> vehicleNoCol;
    @FXML
    private TableColumn<Passenger, String> requiredLitersCol;

    /**
     * Constructor method
     */
    public OperatorViewController(FuelQueue[] queues, CircularWaitingQueue waitingQueue) {
        this.queues = queues;
        this.waitingQueue = waitingQueue;
    }

    /**
     * initialize() method inside the javafx controller is automatically called by the
     * FXMLoader. This method can be used to initialize javafx controls.
     * https://stackoverflow.com/questions/51392203/what-does-initialize-mean-in-javafx
     */
    @FXML
    private void initialize() {
        // In order to get attributes from an object and add them to the TableView,
        // we have to set the CellValueFactory of each column to a new PropertyValueFactory
        // which references to a getter method of the object class.
        // https://stackoverflow.com/questions/39366828/add-a-simple-row-to-javafx-tableview
        // https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/cell/PropertyValueFactory.html
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        secondNameCol.setCellValueFactory(new PropertyValueFactory<>("secondName"));
        vehicleNoCol.setCellValueFactory(new PropertyValueFactory<>("vehicleNo"));
        requiredLitersCol.setCellValueFactory(new PropertyValueFactory<>("requiredLiters"));

        // Add items to the ComboBox
        comboBox.getItems().add("All Queues");
        for (int i = 1; i < 6; i++) {
            comboBox.getItems().add("Pump " + i);  // Add queues 1-5
        }
        comboBox.getItems().add("Waiting Queue");
        // Set first item as the default selection
        comboBox.getSelectionModel().selectFirst();

        // Add a listener to the searchField to update the tableview with filtered data.
        // Whenever the text of the searchField changes filterList() method is called with the new value of
        // the searchField as an argument, and the TableView is updated with the filtered customers list returned.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<Passenger> filteredCustomers = filterList(newValue);
            if (!filteredCustomers.isEmpty()) {
                tableView.setItems(filteredCustomers);
            }
        });

        // Set selected queue data on startup
        setSelectedCustomers();
    }

    /**
     * This method is called when the comboBox selected value changes.
     * It updates the selectedCustomers ObservableList with the selected queue's customers and
     * then updates the TableView with the data of the SelectedCustomers.
     * https://stackoverflow.com/questions/51392203/what-does-initialize-mean-in-javafx
     */
    @FXML
    private void setSelectedCustomers() {
        int selectedQueueNo = getSelectedQueueNo();
        // Add selected queues' customers to an ArrayList
        ArrayList<Passenger> customers = new ArrayList<>();
        if (selectedQueueNo == 0) {  // 0 - All Queues
            for (FuelQueue queue : queues) {
                if (!queue.isAllEmpty()) {
                    customers.addAll(queue.getCustomerObjects());
                }
                if (!waitingQueue.isEmpty()) {
                    customers.addAll(waitingQueue.getCustomerObjects());
                }
            }
        } else if (selectedQueueNo == 6) {  // 6 - Waiting Queue
            if (!waitingQueue.isEmpty()) {
                customers.addAll(waitingQueue.getCustomerObjects());
            }
        } else {  // 1-5 Pump queues
            FuelQueue selectedQueue = queues[selectedQueueNo - 1];
            if (!selectedQueue.isAllEmpty()) {
                customers.addAll(selectedQueue.getCustomerObjects());
            }
        }

        selectedCustomers = FXCollections.observableList(customers);

        // Set table with the selected customers
        tableView.setItems(selectedCustomers);
    }

    /**
     * Return selected queue number.
     * 0 - All Queues, 1 - Q1, 2 - Q2, 3 - Q3, 4 - Q4, 5 - Q5, 6 - Waiting Queue
     */
    private int getSelectedQueueNo() {
        // Get the selected item from the ComboBox
        String selectedItem = comboBox.getValue();
        int selectedQueueNo;
        try {
            selectedQueueNo = Integer.parseInt(selectedItem.split(" ")[1]);
        } catch (Exception e) {
            if (selectedItem.equals("All Queues")) {
                selectedQueueNo = 0;
            } else {
                selectedQueueNo = 6;
            }
        }
        return selectedQueueNo;
    }

    /**
     * Find the customers whose attributes contain the searchText
     * and return an ObservableList of those customers.
     * Adapted from - https://edencoding.com/search-bar-dynamic-filtering/
     */
    private ObservableList<Passenger> filterList(String searchText) {
        ArrayList<Passenger> filteredList = new ArrayList<>();
        for (Passenger customer : selectedCustomers) {
            if (searchCustomer(customer, searchText)) {
                filteredList.add(customer);
            }
        }

        return FXCollections.observableList(filteredList);
    }

    /**
     * Return whether the customer attributes contain the searchText
     * Adapted from - https://edencoding.com/search-bar-dynamic-filtering/
     */
    private boolean searchCustomer(Passenger passenger, String searchText){
        return (passenger.getFullName().toLowerCase().contains(searchText.toLowerCase())) ||
                (passenger.getVehicleNo().toLowerCase().contains(searchText.toLowerCase())) ||
                Integer.valueOf(passenger.getRequiredLiters()).toString().equals(searchText.toLowerCase());
    }

}