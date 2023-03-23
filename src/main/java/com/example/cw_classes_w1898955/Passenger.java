/**
 * COPYRIGHT (C) 2022 Chamath Jayasena. All Rights Reserved.
 * Classes version (tasks 2, 3, 4)
 * Name - Rathnayaka Mudiyanselage Chamath Sakuntha Jayasena
 * UoW ID - w1898955
 * IIT ID - 20211387
 */
package com.example.cw_classes_w1898955;

import java.io.Serializable;

/**
 * Implements Serializable to be able to serialize objects of this class.
 * It is important to have getter methods for all the attributes of this class
 * as those methods will be used by the PropertyValueFactory of the JavaFX
 * TableView to fetch the necessary data from the Passenger objects.
 */
public class Passenger implements Serializable {
    private String firstName;
    private String secondName;
    private String vehicleNo;
    private int requiredLiters;

    /**
     * Constructor method
     */
    public Passenger(String firstName, String secondName, String vehicleNo, int requiredLiters)  {
        this.firstName = capitalize(firstName);
        this.secondName = capitalize(secondName);
        this.vehicleNo = vehicleNo;
        this.requiredLiters = requiredLiters;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getVehicleNo() { return vehicleNo; }

    public String getFullName() {
        return firstName + " " + secondName;
    }

    public int getRequiredLiters() {
        return requiredLiters;
    }

    /**
     * Return first letter capitalized of the given string
     */
    private String capitalize(String str) {
        return str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase();
    }

}
