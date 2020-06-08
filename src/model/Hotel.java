package model;

import database.DatabaseConnectionHandler;

public class Hotel {
    private DatabaseConnectionHandler dbHandler = null;
    public Hotel() {
        dbHandler = new DatabaseConnectionHandler();
    }
    public static void main(String[] args) {
        Hotel hotel = new Hotel();

    }

    //PUT ALL QUERIES HERE
}
