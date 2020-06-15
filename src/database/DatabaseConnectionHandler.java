package database;

import model.amenities.Booking;
import model.amenities.Room;
import model.customers.*;
import model.employees.Employee;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.jdbc.ScriptRunner;

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

            connection = DriverManager.getConnection(ORACLE_URL, "ora_cknee", "a43745280");
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            ScriptRunner sr = new ScriptRunner(connection);
            Reader reader = new BufferedReader(new FileReader("src\\model\\scripts\\hoteldatabase.sql"));
            sr.runScript(reader);

            //getEmployees();
            Customer c = new NonMember("1234567891113434", "dave@gmail.com", "dave123", 100);
            insertBooking(new Booking(160, new Date(2020,02,02), new Date(2020,02,05), 2),c);
            getBooking(c);


            return true;
        } catch (SQLException | FileNotFoundException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }




    public void getBooking(Customer cus) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT c.bookingID, r.room_number, b.SizeOfParty " +
                    "FROM customerBooking c, Booking b, Room r " +
                    "WHERE c.creditCard = " + cus.getCreditCard() + " AND " +
                    "c.bookingID = b.bookingID AND r.bookingID = b.bookingID");

            while(resultSet.next()) {
                System.out.println(resultSet.getString("bookingID"));
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
                    " ((SELECT c1.creditCard FROM customer c1)" +
                    " MINUS" +
                    " (SELECT c2.creditCard FROM customerBooking c2, booking b" +
                    " WHERE c2.bookingID = b.bookingID AND r.bookingID = b.bookingID))");

            while(resultSet.next()) {
                Room curr = new Room(resultSet.getInt("Room_Number"),
                        resultSet.getDate("Room_Date"),
                        resultSet.getInt("Rate"),
                        resultSet.getInt("BookingID"));
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
            ResultSet resultSet = stmt.executeQuery("SELECT r.room_number, r.rate " +
                    "FROM Room r, CustomerBooking c " +
                    "WHERE r.bookingID = c.bookingID " +
                    "GROUP BY r.room_number, r.rate " +
                    "ORDER BY Count(r.room_number)");

            while(resultSet.next()) {
                Room curr = new Room(resultSet.getInt("Room_Number"),
                        null,
                        resultSet.getInt("Rate"),
                        0);
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
                java.sql.Date startDate = java.sql.Date.valueOf(booking.getStartDate().toString());
                java.sql.Date endDate = java.sql.Date.valueOf(booking.getEndDate().toString());
                ps.setDate(2, startDate);
                ps.setDate(3, endDate);
                ps.setInt(4, booking.getSizeOfParty());
                ps.executeUpdate();
                connection.commit();
                ps.close();
            } catch (SQLException e) {
                System.out.println(EXCEPTION_TAG + " " + e.getMessage());
                rollbackConnection();
            }
            PreparedStatement ps1 = connection.prepareStatement("INSERT INTO customerBooking VALUES (?,?, ?)");
            ps1.setString(1, customer.getCreditCard());
            ps1.setInt(2, booking.getBookingID());
            java.sql.Date endDate = java.sql.Date.valueOf(booking.getEndDate().toString());
            ps1.setDate(3, endDate);
            ps1.executeUpdate();
            connection.commit();
            ps1.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void updateBooking(String creditCard, int bookingId, java.sql.Date endDate) {
        try {
            if (endDate == null) {
                cancelBooking(creditCard, bookingId, endDate);
                return;
            }
            Statement stmt = connection.createStatement();
            String query = "UPDATE CustomerBooking cb " + "SET endDate = " + endDate +
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

    public void cancelBooking(String creditCard, int bookingId, java.util.Date endDate) {
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
            ResultSet rs = stmt.executeQuery("SELECT  * from HotelMember m " +
                    "WHERE m.CreditCard = " + customer.getCreditCard());
            if (rs.next() != false) {
                Member m = new Member(rs.getString("CreditCard"), customer.getEmail(),
                        customer.getAccount(), customer.getPaymentID(), rs.getInt("Points"));
                makePaymentMember(m);
            } else {
                NonMember nm = new NonMember(customer.getCreditCard(), customer.getEmail(),
                        customer.getAccount(), customer.getPaymentID());
                makePaymentNonMember(nm);
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    private void makePaymentNonMember(NonMember nm) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT  * from payment p " +
                    "WHERE p.PaymentID = " + nm.getCreditCard());
            while (rs.next()) {
                Payment payment = new Payment(String.valueOf(rs.getInt("PaymentID")),
                        rs.getFloat("RoomCost"), rs.getFloat("AdditionalCosts"));
                float FinalPayment = payment.getRoomCost() + payment.getAdditionalCost();
                Statement stmt2 = connection.createStatement();
                stmt.executeQuery("DELETE FROM Payment p WHERE p.paymentID = " +
                        payment.getPaymentID());
                stmt2.close();
                Statement stmt4 = connection.createStatement();
                ResultSet rs4 = stmt.executeQuery("UPDATE customer c SET paymentID = " + null +
                        "WHERE creditcard = " + nm.getCreditCard());
                stmt4.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    private void makePaymentMember(Member m) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT  * from payment p " +
                    "WHERE p.PaymentID = " + m.getCreditCard());
            while (rs.next()) {
                Payment payment = new Payment(String.valueOf(rs.getInt("PaymentID")),
                        rs.getFloat("RoomCost"), rs.getFloat("AdditionalCosts"));
                float FinalPayment = payment.getRoomCost() + payment.getAdditionalCost();
                float points = rs.getInt("Points");
                if (points > FinalPayment) {
                    points = points - FinalPayment;
                    FinalPayment = 0;
                }
                Statement stmt2 = connection.createStatement();
                ResultSet rs2 = stmt.executeQuery("DELETE FROM Payment p WHERE p.paymentID = " +
                        payment.getPaymentID());
                stmt2.close();
                Statement stmt3 = connection.createStatement();
                ResultSet rs3 = stmt.executeQuery("UPDATE HotelMember m SET points = " + points + "WHERE member = " +
                        m.getCreditCard());
                stmt3.close();
                Statement stmt4 = connection.createStatement();
                ResultSet rs4 = stmt.executeQuery("UPDATE customer c SET paymentID = " + null +
                        "WHERE creditcard = " + m.getCreditCard());
                stmt4.close();
            }
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
                    "FROM CustomerBooking cb, Booking b, HotelHasBooking h " +
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
                        resultSet.getInt("BookingID"));
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
