/**
 * COPYRIGHT (C) 2022 Chamath Jayasena. All Rights Reserved.
 * Classes version (tasks 2, 3, 4)
 * Name - Rathnayaka Mudiyanselage Chamath Sakuntha Jayasena
 * UoW ID - w1898955
 * IIT ID - 20211387
 */
package com.example.cw_classes_w1898955;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Implements Serializable to be able to serialize objects of this class
 */
public class FuelQueue implements Serializable {
    private ArrayList<Passenger> queue;  // Declare an ArrayList of Passenger objects as the queue
    private int pump;
    private int income;

    /**
     * Constructor method
     */
    public FuelQueue(int size, int pump) {
        // Initialize the queue arraylist with the given size
        // https://stackoverflow.com/questions/15430247/why-start-an-arraylist-with-an-initial-capacity
        this.queue = new ArrayList<>(size);
        this.pump = pump;
    }

    /**
     * Add a customer to the queue if there is space, if not, throw an exception
     */
    public void addCustomer(Passenger customer) throws IndexOutOfBoundsException {
        if (queue.size() < 6) {
            queue.add(customer);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * Remove the given customer from the queue
     */
    public void removeCustomer(Passenger customer) {
        queue.remove(customer);
    }

    /**
     * Return the customer in the give positions.
     * Throw an exception if no customer found in that position.
     */
    public Passenger getCustomer(int position) throws IndexOutOfBoundsException {
        return queue.get(position);
    }

    /**
     * Check whether the queue has empty slots
     */
    public boolean hasEmptySlots() {
        return queue.size() < 6;
    }

    /**
     * Check if the queue is all empty
     */
    public boolean isAllEmpty() {
        return queue.size() == 0;
    }

    /**
     * Return how many customers are in the queue
     */
    public int getLength() {
        return queue.size();
    }

    /**
     * Return the pump number the queue belongs to
     */
    public int getPump() {
        return pump;
    }

    /**
     * Return names of the customers in the queue as an ArrayList
     */
    public ArrayList<String> getCustomerNames() {
        ArrayList<String> customers = new ArrayList<>();
        if (!queue.isEmpty()) {
            for (Passenger customer : queue) {
                customers.add(customer.getFullName());
            }
        }
        return customers;
    }

    /**
     * Return customer names sorted as an ArrayList.
     * A Comparator is used to sort the Passenger objects inside the queue
     * according to their fullName property.
     * */
    public ArrayList<String> getCustomerNamesSorted() throws Exception {
        ArrayList<String> customerNamesSorted = new ArrayList<>(queue.size());
        if (!queue.isEmpty()) {
            // Should not modify the original queue
            ArrayList<Passenger> tmpArr = new ArrayList<>(queue);
            // Sort the tmpArr using customer's full name
            // Use Comparator interface to sort the array based on the customers' full name
            // https://stackoverflow.com/questions/18895915/how-to-sort-an-array-of-objects-in-java
            tmpArr.sort(new Comparator<Passenger>() {
                @Override
                public int compare(Passenger p1, Passenger p2) {
                    return p1.getFullName().compareToIgnoreCase(p2.getFullName());
                }
            });
            // Iterate through the sorted tmpArr and append each customer's full name to the arraylist
            for (Passenger customer : tmpArr) {
                customerNamesSorted.add(customer.getFullName());
            }
        } else {
            throw new Exception();
        }
        return customerNamesSorted;
    }

    /**
     * Return the income of the queue
     */
    public int getIncome() {
        return income;
    }

    /**
     * Update the income of the queue with the given income
     */
    public void updateIncome(int income) {
        this.income += income;
    }

    /**
     * Return the whole queue
     */
    public ArrayList<Passenger> getCustomerObjects() {
        return queue;
    }

}
