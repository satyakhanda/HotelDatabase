package model;

import database.DatabaseConnectionHandler;
import ui.OpeningUI;

import javax.swing.*;


public class Hotel {
    private DatabaseConnectionHandler dbHandler;
    public Hotel() {
        dbHandler = new DatabaseConnectionHandler();
    }

    private void start() {
        login("ora_name88 ", "a43457738");

    }

    private void end() {
        dbHandler.close();
        dbHandler = null;
    }

    public void login(String username, String password) {
        boolean didConnect = dbHandler.login(username, password);

        if (didConnect) {
            java.awt.EventQueue.invokeLater(() -> new OpeningUI(dbHandler).setVisible(true));
        }
    }
    public static void main(String[] args) {
        Hotel hotel = new Hotel();
        hotel.start();

    }

}
