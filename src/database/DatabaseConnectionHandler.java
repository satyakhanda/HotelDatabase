package database;

import model.amenities.Booking;
import model.amenities.Room;
import model.customers.Account;
import model.customers.Customer;
import model.customers.Payment;
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

            connection = DriverManager.getConnection(ORACLE_URL, "ora_name88", "a43457738");
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
                                                  resultSet.getString("CreditCard"));
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

    public void insertBooking(Booking booking, Customer customer) {
        try {
            try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO booking VALUES (?,?,?,?)");
                ps.setInt(1, booking.getBookingID());
                ps.setDate(2, (Date) booking.getStartDate());
                ps.setDate(3, (Date) booking.getEndDate());
                ps.setInt(4, booking.getSizeOfParty());
                ps.executeUpdate();
                connection.commit();
                ps.close();
            } catch (SQLException e) {
                System.out.println(EXCEPTION_TAG + " " + e.getMessage());
                rollbackConnection();
            }
            PreparedStatement ps1 = connection.prepareStatement("INSERT INTO customerBooking VALUES (?,?)");
            ps1.setString(1, customer.getCreditCard());
            ps1.setInt(2, booking.getBookingID());
            ps1.executeUpdate();
            connection.commit();
            ps1.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateBooking(int creditCard, int bookingId, java.util.Date endDate) {
        try {
            if (endDate == null) {
                cancelBooking(creditCard, bookingId, endDate);
                return;
            }
            Statement stmt = connection.createStatement();
            String query = "UPDATE CutomerBooking cb " + "SET endDate = " + endDate +
                    "WHERE cb.creditCard = " + creditCard + "AND cb.bookingId = " + bookingId;
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            try {
                Statement stmt1 = connection.createStatement();
                String query1 = "UPDATE Booking b " + "SET endDate = " + endDate +
                        "WHERE b.bookingId = " + bookingId;
                ResultSet rs1 = stmt.executeQuery(query1);
                rs1.close();
            } catch (SQLException e) {
                System.out.println(EXCEPTION_TAG + " " + e.getMessage());
                rollbackConnection();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void cancelBooking(int creditCard, int bookingId, java.util.Date endDate) {
        try {
            Statement stmt = connection.createStatement();
            String query = "DELETE Booking b WHERE b.bookingId = " + bookingId;
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            try {
                Statement stmt1 = connection.createStatement();
                String query1 = "DELETE FROM CustomerBooking cb WHERE cb.bookingId = " + bookingId +
                        "AND cb.creditCard = " + creditCard;
                ResultSet rs1 = stmt.executeQuery(query1);
                rs1.close();
            } catch (SQLException e) {
                System.out.println(EXCEPTION_TAG + " " + e.getMessage());
                rollbackConnection();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void makePayment(Customer customer) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT  * from payment p " +
                    "WHERE p.PaymentID = " + customer.getPaymentID());
            while (rs.next()) {
                Payment payment = new Payment(String.valueOf(rs.getInt("PaymentID")),
                        rs.getFloat("RoomCost"), rs.getFloat("AdditionalCosts"));
                float FinalPayment = payment.getRoomCost() + payment.getAdditionalCost();
                Statement stmt1 = connection.createStatement();
                ResultSet rs1 = stmt.executeQuery("SELECT  * from member m " +
                        "WHERE m.CreditCard = " + customer.getCreditCard());
                if (rs.next() != false) {
                    int points = rs.getInt("Points");
                    FinalPayment -= points;
                }
                stmt1.close();
                Statement stmt2 = connection.createStatement();
                ResultSet rs2 = stmt.executeQuery("DELETE FROM Payment p WHERE p.paymentID = " +
                        payment.getPaymentID());
                stmt2.close();
                Statement stmt3 = connection.createStatement();
                ResultSet rs3 = stmt.executeQuery("UPDATE member m SET points = " + FinalPayment + "WHERE member = " +
                        customer.getCreditCard());
                stmt3.close();
                Statement stmt4 = connection.createStatement();
                ResultSet rs4 = stmt.executeQuery("UPDATE customer c SET paymentID = " + null +
                        "WHERE creditcard = " + customer.getCreditCard());
                stmt4.close();
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public List<Employee> getEmployees() {
        List<Employee> allEmployees = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * " +
                    "FROM Employee e");
            while(resultSet.next()) {
                Employee curr = new Employee(resultSet.getInt("EmployeeID"),
                        resultSet.getString("Emp_Name"),
                        resultSet.getInt("HotelID"));
                allEmployees.add(curr);
            }
            resultSet.close();
            stmt.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allEmployees;
    }

    public HashMap<Integer,Integer> averagePartySizePerHotel() {
        HashMap<Integer,Integer> partySizes = new HashMap<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT h.HotelID, AVG(b.SizeOfParty) " +
                    "FROM CustomerMakesBooking cb, Booking b, HotelHasBooking h " +
                    "WHERE b.BookingID = h.BookingID AND cb.BookingID = b.BookingID " +
                    "GROUP BY h.HotelID");
            while(resultSet.next()) {
                partySizes.put(resultSet.getInt("h.HotelID"), resultSet.getInt("AVG(b.SizeOfParty)"));
            }
            resultSet.close();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return partySizes;
    }

    public List<Room> getAvailableRooms() {
        List<Room> availableRooms = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT r.Room_Number, r.Room_Date, r.Rate, r.BookingID " +
                    "FROM Room r, Booking b " +
                    "WHERE r.BookingID IS NULL OR (r.BookingID = b.BookingID AND " +
                    "((b.StartDate > current_date) OR " +
                    "(b.EndDate < current_date))) " +
                    "ORDER BY r.Rate");
            while(resultSet.next()) {
                Room curr = new Room(resultSet.getInt("Room_Number"),
                        resultSet.getDate("Room_Date"),
                        resultSet.getInt("Rate"),
                        bookings.get(resultSet.getInt("BookingID")));
                availableRooms.add(curr);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return availableRooms;
    }


    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }
}
