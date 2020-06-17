package database;

import model.amenities.Booking;
import model.amenities.Room;
import model.customers.*;
import model.employees.Cleaner;
import model.employees.Employee;

import java.awt.print.Book;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.employees.Manager;
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

            connection = DriverManager.getConnection(ORACLE_URL, "ora_name88", "a43457738");
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            ScriptRunner sr = new ScriptRunner(connection);
            Reader reader = new BufferedReader(new FileReader("src/model/scripts/hoteldatabase.sql"));
            sr.runScript(reader);

            return true;
        } catch (SQLException | FileNotFoundException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }




    public List<List<Object>> getBooking(Customer cus) {
        List<List<Object>> retList = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT c.bookingID, r.room_number, b.SizeOfParty, b.StartDate, b.EndDate " +
                    "FROM customerBooking c, Booking b, Room r " +
                    "WHERE c.creditCard = " + cus.getCreditCard() + " AND " +
                    "c.bookingID = b.bookingID AND r.bookingID = b.bookingID");
            while(resultSet.next()) {
                List<Object> temp = new ArrayList<Object>();
                temp.add(resultSet.getInt("BookingID"));
               // temp.add(resultSet.getInt("Room_Number"));
                temp.add(resultSet.getInt("SizeOfParty"));
                temp.add(resultSet.getDate("StartDate"));
                temp.add(resultSet.getDate("EndDate"));
                retList.add(temp);
            }
            resultSet.close();
            stmt.close();
            return retList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return retList;
    }

//    public Customer getCustomer(String username, String password) {
//        try {
//            Statement stmt = connection.createStatement();
//            ResultSet resultSet = stmt.executeQuery("SELECT c.CreditCard, c.Account, c.Email, m.Points " +
//                    "FROM account a, customer c, hotelMember m " +
//                    "WHERE username = " + username + " AND password = " + password + " AND c.account = " + username +
//                    " AND m.creditCard = c.creditCard ");
//            Customer custAccount = null;
//            while(resultSet.next()) {
//                custAccount = new Member(resultSet.getString("CrediCard"),
//                        resultSet.getString("Email"),
//                        resultSet.getString("Account"), 0,
//                        resultSet.getInt("Points"));
//            }
//            resultSet.close();
//            stmt.close();
//            return custAccount;
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        return null;
//    }

    public Customer getCustomer(String username, String password) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT c.CreditCard, c.Account, c.Email, m.Points " +
                    "FROM account a, customer c, hotelMember m " +
                    "WHERE a.username = '" + username + "' AND a.password = '" + password + "' AND c.account = '" + username +
                    "' AND m.creditCard = c.creditCard ");
            Customer custAccount = null;
            while(resultSet.next()) {
                custAccount = new Member(resultSet.getString("CreditCard"),
                        resultSet.getString("Email"),
                        resultSet.getString("Account"), 0,
                        resultSet.getInt("Points"));
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
                        resultSet.getString("Room_Date"),
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

    public void insertBooking(Booking booking, Customer customer, int roomNum) {
        try {
                PreparedStatement ps = connection.prepareStatement("INSERT INTO booking VALUES (?,?,?,?)");
                ps.setInt(1, booking.getBookingID());
                String initDate = booking.getStartDate();
                String finDate = booking.getEndDate();
                java.sql.Date startDate = java.sql.Date.valueOf(initDate);
                java.sql.Date endDate = java.sql.Date.valueOf(finDate);
                ps.setDate(2, startDate);
                ps.setDate(3, endDate);
                ps.setInt(4, booking.getSizeOfParty());
                ps.executeUpdate();
                connection.commit();
                ps.close();
                Statement stmt = connection.createStatement();
                String query = "UPDATE Room r " + "SET Room_Date = " + finDate +
                        ", bookingID = " + booking.getBookingID() + " WHERE r.room_number = " + roomNum;
                ResultSet rs = stmt.executeQuery(query);
                rs.close();

            PreparedStatement ps1 = connection.prepareStatement("INSERT INTO customerBooking VALUES (?,?, ?)");
            ps1.setString(1, customer.getCreditCard());
            ps1.setInt(2, booking.getBookingID());
            java.sql.Date endDate1 = java.sql.Date.valueOf(booking.getEndDate().toString());
            ps1.setDate(3, endDate1);
            ps1.executeUpdate();
            connection.commit();
            ps1.close();


            List<Float> temp = getBasicCost();
            int roomCost = getRateForRoom(roomNum);
            if (!temp.contains((float) roomCost)) {
                PreparedStatement ps3 = connection.prepareStatement("INSERT INTO BasicCost VALUES (?,?)");
                ps3.setFloat(1, getRateForRoom(roomNum));
                ps3.setInt(2, 10 * getRateForRoom(roomNum));
                ps3.executeUpdate();
                connection.commit();
                ps3.close();
            }
            int maxPID = getMaxPaymentID() + 1;
            PreparedStatement ps2 = connection.prepareStatement("INSERT INTO payment VALUES (?,?,?)");
            ps2.setInt(1, maxPID);
            ps2.setFloat(2, (float) roomCost);
            ps2.setFloat(3, (float) 0.0);
            ps2.executeUpdate();
            connection.commit();
            ps2.close();

            Statement stmt1 = connection.createStatement();
            String query1 = "UPDATE Customer c SET c.PaymentID = " + maxPID + "WHERE c.CreditCard = " + customer.getCreditCard();
            ResultSet resultSet = stmt1.executeQuery(query1);
            resultSet.close();
            stmt1.close();
            customer.setPaymentID(maxPID);
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public List<Float> getBasicCost() {
        List<Float> basicCostList = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT RoomCost " +
                    "FROM BasicCost bc");
            while(resultSet.next()) {
                float curr = (resultSet.getFloat("RoomCost"));
                basicCostList.add(curr);
            }
            resultSet.close();
            stmt.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return basicCostList;
    }

    public void updateBooking(String creditCard, int bookingId, String endDate) {
        try {
            if (endDate == null) {
                cancelBooking(creditCard, bookingId, endDate);
                return;
            }
            Statement stmt = connection.createStatement();
            String query = "UPDATE CustomerBooking cb " + "SET endDate = " + java.sql.Date.valueOf(endDate) +
                    " WHERE cb.creditCard = " + creditCard + " AND cb.bookingId = " + bookingId;
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            try {
                Statement stmt1 = connection.createStatement();

                String query1 = "UPDATE Booking b " + "SET endDate = to_date('" + endDate +
                        "', 'YYYY-MM-DD') WHERE b.bookingId = " + bookingId;
                ResultSet rs1 = stmt.executeQuery(query1);
                rs1.close();
                Statement stmt2 = connection.createStatement();
                String query2 = "UPDATE Room r " + "SET Room_Date = " + endDate + " WHERE r.bookingID = " + bookingId;
                ResultSet rs2 = stmt.executeQuery(query2);
                rs2.close();
            } catch (SQLException e) {
                System.out.println(EXCEPTION_TAG + " " + e.getMessage());
                rollbackConnection();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public void cancelBooking(String creditCard, int bookingId, String endDate) {
        try {
            Statement stmt = connection.createStatement();
            String query = "DELETE Booking b WHERE b.bookingId = " + bookingId;
            ResultSet rs = stmt.executeQuery(query);
            rs.close();
            try {
                Statement stmt1 = connection.createStatement();
                String query1 = "DELETE FROM CustomerBooking cb WHERE cb.bookingId = " + bookingId +
                        " AND cb.creditCard = " + creditCard;
                ResultSet rs1 = stmt1.executeQuery(query1);
                rs1.close();
                Statement stmt2 = connection.createStatement();
                String query2 = "UPDATE Room r " + "SET Room_Date = NULL, r.bookingID = NULL " +
                        "WHERE r.bookingID = " + bookingId;
                ResultSet rs2 = stmt2.executeQuery(query2);
                rs2.close();
            } catch (SQLException e) {
                System.out.println(EXCEPTION_TAG + " " + e.getMessage());
                rollbackConnection();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public float makePayment(Customer customer) {
        try {
            float finalPayment = 0;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from HotelMember m " +
                    "WHERE m.CreditCard = " + customer.getCreditCard());
            if (rs.next() != false) {
                Customer m = new Member(rs.getString("CreditCard"), customer.getEmail(),
                        customer.getAccount(), customer.getPaymentID(), rs.getInt("Points"));
                finalPayment = makePaymentMember(m);
            } else {
                NonMember nm = new NonMember(customer.getCreditCard(), customer.getEmail(),
                        customer.getAccount(), customer.getPaymentID());
                finalPayment = makePaymentNonMember(nm);
            }
            if (finalPayment == -1.0 ) System.out.println("Messed up in Non mem");
            return finalPayment;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return -1;
    }

    private float makePaymentNonMember(NonMember nm) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from payment p " +
                    "WHERE p.PaymentID = " + nm.getPaymentID());
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
                        " WHERE creditcard = " + nm.getCreditCard());
                stmt4.close();
                return FinalPayment;
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return -1;
    }

    private float makePaymentMember(Customer m) {
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Payment p " +
                    "WHERE p.PaymentID = " + m.getPaymentID());
            while (rs.next()) {
                Payment payment = new Payment(String.valueOf(rs.getInt("PaymentID")),
                        rs.getFloat("RoomCost"), rs.getFloat("AdditionalCosts"));
                float FinalPayment = payment.getRoomCost() + payment.getAdditionalCost();
                float points = rs.getInt("Points");
                if (points > 100*FinalPayment) {
                    points = points - 100*FinalPayment;
                    FinalPayment = 0;
                }
                Statement stmt2 = connection.createStatement();
                ResultSet rs2 = stmt2.executeQuery("DELETE FROM Payment p WHERE p.paymentID = " +
                        payment.getPaymentID());
                rs2.close();
                stmt2.close();
                Statement stmt3 = connection.createStatement();
                ResultSet rs3 = stmt3.executeQuery("UPDATE HotelMember m SET points = " + points + " WHERE member = " +
                        m.getCreditCard());
                rs3.close();
                stmt3.close();
                Statement stmt4 = connection.createStatement();
                ResultSet rs4 = stmt4.executeQuery("UPDATE customer c SET paymentID = NULL" +
                        " WHERE creditcard = " + m.getCreditCard());
                rs4.close();
                stmt4.close();
                return FinalPayment;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
        return -1;
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

    public List<Manager> getManagers() {
        List<Manager> allManagers = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * " +
                    "FROM Manager m");
            while(resultSet.next()) {
                Manager curr = new Manager(resultSet.getInt("EmployeeID"),
                        resultSet.getString("Mng_Name"),
                        resultSet.getInt("HotelID"));
                allManagers.add(curr);
            }
            resultSet.close();
            stmt.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allManagers;
    }

    public List<Cleaner> getCleanersForHotel(int hotelID) {
        List<Cleaner> allCleaners = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * " +
                    "FROM Cleaner c WHERE c.HotelID = " + hotelID);
            while(resultSet.next()) {
                Cleaner curr = new Cleaner(resultSet.getInt("EmployeeID"),
                        resultSet.getString("Cln_Name"),
                        resultSet.getInt("HotelID"));
                allCleaners.add(curr);
            }
            resultSet.close();
            stmt.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allCleaners;

    }

    public HashMap<Integer,Integer> averagePartySizePerHotel() {
        HashMap<Integer,Integer> partySizes = new HashMap<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT h.HotelID, AVG(b.SizeOfParty) AS SizeOfParty " +
                    "FROM CustomerBooking cb, Booking b, HotelHasBooking h " +
                    "WHERE b.BookingID = h.BookingID AND cb.BookingID = b.BookingID " +
                    "GROUP BY h.HotelID");
            while(resultSet.next()) {
                partySizes.put(resultSet.getInt("HotelID"), resultSet.getInt("SizeOfParty"));
            }
            resultSet.close();
            stmt.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return partySizes;
    }

    public int getMaxBookingID() {
        int bookingIDs = 0;
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT MAX(BookingID) AS BookingID " +
                    "FROM Booking b");
            while(resultSet.next()) {
                bookingIDs = (resultSet.getInt("BookingID"));
            }
            resultSet.close();
            stmt.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return bookingIDs;
    }

    public int getMaxPaymentID() {
        int paymentID = 0;
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT MAX(PaymentID) AS PaymentID " +
                    "FROM Payment p");
            while(resultSet.next()) {
                paymentID = (resultSet.getInt("PaymentID"));
            }
            resultSet.close();
            stmt.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return paymentID;
    }

    public HashMap<String, List<Room>> getAvailableRooms() {
        HashMap<String, List<Room>> hotelNameAndRooms = new HashMap<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT r.Room_Number, r.Room_Date, r.Rate, r.BookingID, h.HotelName " +
                    "FROM Room r, Booking b, HotelHasBooking hb, Hotel h " +
                    "WHERE r.BookingID IS NULL OR (r.BookingID = b.BookingID AND b.BookingID = hb.BookingID AND hb.HotelID = h.HotelID AND " +
                    "((b.StartDate > current_date) OR " +
                    "(b.EndDate < current_date))) " +
                    "ORDER BY r.Rate");
            while(resultSet.next()) {
                List<Room> rooms = hotelNameAndRooms.getOrDefault(resultSet.getString("HotelName"), new ArrayList<>());
                Room curr = new Room(resultSet.getInt("Room_Number"),
                        resultSet.getString("Room_Date"),
                        resultSet.getInt("Rate"),
                        resultSet.getInt("BookingID"));
                rooms.add(curr);
                hotelNameAndRooms.put(resultSet.getString("HotelName"), rooms);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return hotelNameAndRooms;
    }

    public List<Room> getRoomsForHotel(int hotelID) {
        List<Room> allRooms = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT r.Room_Number, r.Room_Date, r.Rate, r.BookingID " +
                    "FROM Room r, Booking b, HotelHasBooking hb " +
                    "WHERE r.BookingID IS NULL AND hb.HotelID = " + hotelID + " OR (r.BookingID = b.BookingID AND b.BookingID = hb.BookingID AND hb.HotelID ="+ hotelID +") " +
                    "ORDER BY r.Rate");
            while(resultSet.next()) {
                Room curr = new Room(resultSet.getInt("Room_Number"),
                        resultSet.getString("Room_Date"),
                        resultSet.getInt("Rate"),
                        resultSet.getInt("BookingID"));
                allRooms.add(curr);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allRooms;
    }

    public void insertCustomer(Customer customer, Boolean b, String password, String Name,
                               int Age, String Address) {
        try {
//            PreparedStatement ps = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?,?)");
//            ps.setString(1, customer.getCreditCard());
//            ps.setString(2, customer.getEmail());
//            ps.setString(3, customer.getAccount());
//            ps.setInt(4, customer.getPaymentID());
//            ps.executeUpdate();
//            connection.commit();
//            ps.close();
            PreparedStatement ps1 = connection.prepareStatement("INSERT INTO CustomerDetails VALUES (?,?,?,?)");
            ps1.setString(1, customer.getEmail());
            ps1.setString(2, Name);
            ps1.setInt(3, Age);
            ps1.setString(4,Address);
            ps1.executeUpdate();
            connection.commit();
            ps1.close();
            insertAccount(customer, password);
            PreparedStatement ps = connection.prepareStatement("INSERT INTO customer VALUES (?,?,?,?)");
            ps.setString(1, customer.getCreditCard());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getAccount());
            if (customer.getPaymentID() == null) {
                ps.setNull(4, Types.NULL);
            } else {
                ps.setInt(4, customer.getPaymentID());
            }
            ps.executeUpdate();
            connection.commit();
            ps.close();
            if (b) {
                insertMember(customer);
            } else {
                insertNonmember(customer);
            }
            //insertAccount(customer, password);
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    private void insertMember(Customer customer) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO HotelMember VALUES (?,?,?,?, ?)");
            ps.setString(1, customer.getCreditCard());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getAccount());
            if (customer.getPaymentID() == null) {
                ps.setNull(4, Types.NULL);
            } else {
                ps.setInt(4, customer.getPaymentID());
            }
            ps.setInt(5, 0);
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    private void insertNonmember(Customer customer) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO NonMember VALUES (?,?,?,?, ?)");
            ps.setString(1, customer.getCreditCard());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getAccount());
            if (customer.getPaymentID() == null) {
                ps.setNull(4, Types.NULL);
            } else {
                ps.setInt(4, customer.getPaymentID());
            }
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    private void insertAccount(Customer customer, String password) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO account VALUES (?,?,?)");
            ps.setString(1, customer.getAccount());
            ps.setString(2, password);
            ps.setString(3, customer.getCreditCard());
            ps.executeUpdate();
            connection.commit();
            ps.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            rollbackConnection();
        }
    }

    public int getRateForRoom(int roomNum) {

        try {
            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT Rate FROM Room WHERE Room_Number = " + roomNum);
            while (resultSet.next()) {
                return resultSet.getInt("Rate");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }


    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }
}
