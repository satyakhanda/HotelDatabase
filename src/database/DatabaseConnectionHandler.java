package database;

import model.amenities.Booking;
import model.amenities.Room;
import model.customers.Account;
import model.customers.Customer;
import model.employees.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import ca.ubc.cs304.model.BranchModel;
/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
    // Use this version of the ORACLE_URL if you are running the code off of the server
//	private static final String ORACLE_URL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
    // Use this version of the ORACLE_URL if you are tunneling into the undergrad servers
    //jdbc:oracle:thin:@localhost:1522:stu
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    private HashMap<Integer, Booking> bookings;

    public DatabaseConnectionHandler() {
        bookings = new HashMap<Integer, Booking>();
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    public void getBooking(Customer cus) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT c.bookingID, r.room_number, b.SizeOfPary " +
                                                        "FROM customerHasBooking c, Booking b, Room r " +
                                                        "WHERE c.creditCard = " + cus.getCreditCard() + " AND " +
                                                        "c.bookingID = b.bookingID AND r.bookingID = b.bookingID");

            while(resultSet.next()) {
                //use data
            }
            resultSet.close();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Account getAccount(Customer cus) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * " +
                                                        "FROM account " +
                                                        "WHERE creditCard = " + cus.getCreditCard());
            Account custAccount = null;
            while(resultSet.next()) {
                custAccount = new Account(resultSet.getString("Username"),
                                                  resultSet.getString("Password"),
                                                  resultSet.getInt("CreditCard"));
            }
            resultSet.close();
            stmt.close();
            return custAccount;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<Room> bookedByAll() {
        List<Room> allRooms = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * " +
                    "FROM Room r " +
                    "WHERE NOT EXISTS" +
                    " ((SELECT c1.creditCard FROM customer)" +
                    " MINUS" +
                    " (SELECT c2.creditCard FROM customerHasBooking c2, booking b" +
                    " WHERE c2.bookingID = b.bookingID AND r.bookingID = b.bookingID))");

            while(resultSet.next()) {
                Room curr = new Room(resultSet.getInt("Room_Number"),
                                     resultSet.getDate("Room_Date"),
                                     resultSet.getInt("Rate"),
                                     bookings.get(resultSet.getInt("BookingID")));
                allRooms.add(curr);
            }
            resultSet.close();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allRooms;
    }

    public List<Room> mostPopularRooms() {
        List<Room> allRooms = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT r.room_number, r.room_date, r.rate, r.bookingID " +
                    "FROM Room r, CustomerMakesBooking c " +
                    "WHERE r.bookingID = c.bookingID " +
                    "GROUP BY r.room_num " +
                    "ORDER BY Count(r.room_num)");

            while(resultSet.next()) {
                Room curr = new Room(resultSet.getInt("Room_Number"),
                        resultSet.getDate("Room_Date"),
                        resultSet.getInt("Rate"),
                        bookings.get(resultSet.getInt("BookingID")));
                allRooms.add(curr);
            }
            resultSet.close();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allRooms;
    }

    public List<Employee> getEmployees() {
        List<Employee> allEmployees = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * " +
                    "FROM Employee e");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allEmployees;
    }

    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }
}
