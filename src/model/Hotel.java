package model;

import database.DatabaseConnectionHandler;



public class Hotel {
    private DatabaseConnectionHandler dbHandler = null;
    public Hotel() {
        dbHandler = new DatabaseConnectionHandler();
    }

    private void start() {
        //start login UI
        login(" ", " ");

    }

    private void end() {
        dbHandler.close();
        dbHandler = null;
    }

    public void login(String username, String password) {
        boolean didConnect = dbHandler.login("ora_name88", "a43457738");

        if (didConnect) {
            //get rid of login ui
        } else {
            //display failed login, retry
        }
    }
    public static void main(String[] args) {

        Hotel hotel = new Hotel();
        //hotel.start();
    }

}
