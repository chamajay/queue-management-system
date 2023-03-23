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

/**
 * Circular Queue implementation was adapted from,
 * https://www.programiz.com/dsa/circular-queue
 * Implements Serializable to be able to serialize objects of this class
 */
public class CircularWaitingQueue implements Serializable {
    private int size;
    private int front, rear;
    private Passenger[] queue;  // Declare a Passenger array to be used as the waiting queue

    /**
     * Constructor method
     */
    public CircularWaitingQueue(int size) {
        this.size = size;
        this.queue = new Passenger[size];  // Initialize the queue with the given size
        // Initialize front and rear
        front = -1;
        rear = -1;
    }

    /**
     * Check if the queue is full
     */
    public boolean isFull() {
        if (front == 0 && rear == size - 1) {
            return true;
        }
        return front == rear + 1;
    }

    /**
     * Check if the queue is empty
     */
    public boolean isEmpty() {
        return front == -1;
    }

    /**
     * Add(enqueue) a customer to the queue
     */
    public void enQueue(Passenger customer) {
        if (isFull()) {
            System.out.println("Error: Waiting queue is full");
        } else {
            if (front == -1) {
                front = 0;
            }
            rear = (rear + 1) % size;
            queue[rear] = customer;
        }
    }

    /**
     * Remove(dequeue) a customer from the queue
     */
    public Passenger deQueue() throws Exception {
        if (isEmpty()) {
            throw new Exception();
        } else {
            Passenger customer = queue[front];  // We remove the customer in the front
            if (front == rear) {  // Queue has only one customer, so we reset the queue after deleting it
                front = -1;
                rear = -1;
            } else {
                front = (front + 1) % size;
            }
            return customer;
        }
    }

    /**
     * Return customer names as an arraylist
     */
    public ArrayList<String> getCustomerNames() {
        ArrayList<String> customerNames = new ArrayList<>();
        int i;
        if (!isEmpty()) {
            for (i = front; i != rear; i = (i + 1) % size) {
                customerNames.add(queue[i].getFullName());
            }
            customerNames.add(queue[i].getFullName());
        }
        return customerNames;
    }

    /**
     * Return customer objects as an arraylist
     */
    public ArrayList<Passenger> getCustomerObjects() {
        ArrayList<Passenger> customerObjects = new ArrayList<>();
        int i;
        if (!isEmpty()) {
            for (i = front; i != rear; i = (i + 1) % size) {
                customerObjects.add(queue[i]);
            }
            customerObjects.add(queue[i]);
        }
        return customerObjects;
    }

}
