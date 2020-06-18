package ui.customer;

import database.DatabaseConnectionHandler;
import model.amenities.Booking;
import model.amenities.Room;
import model.customers.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class MakeBookingUI extends JFrame {
    private JTextField dateFrom;
    private JTextField dateUntil;
    private JButton viewBookings;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JScrollPane jScrollPane1;
    private JTable jTable1;
    private JButton makeBooking;
    private JTextField roomNum;
    private JTextField sizeOfParty;
    private JLabel jLabel7;
    private JTextField hotelName;

    private final Customer curr;
    private final DatabaseConnectionHandler dbHandler;

    public MakeBookingUI(Customer curr, DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
        this.curr = curr;
        initComponents();
        fillTable();
    }

    private void fillTable() {
        DefaultTableModel model = (DefaultTableModel) this.jTable1.getModel();
        model.setRowCount(0);
        HashMap<String, List<Room>> rooms = dbHandler.getAvailableRooms();
        for (String key : rooms.keySet()) {
            for(Room curr : rooms.get(key)) {
                model.addRow(new Object[]{key, curr.getRoom_num(), curr.getRate()});
            }
        }
    }

    private void initComponents() {

        jLabel1 = new JLabel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        jLabel2 = new JLabel();
        roomNum = new JTextField();
        jLabel3 = new JLabel();
        dateFrom = new JTextField();
        jLabel4 = new JLabel();
        dateUntil = new JTextField();
        jLabel5 = new JLabel();
        sizeOfParty = new JTextField();
        makeBooking = new JButton();
        jLabel6 = new JLabel();
        viewBookings = new JButton();
        jLabel7 = new JLabel();
        hotelName = new JTextField();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", Font.PLAIN, 18));
        jLabel1.setText("                             Book Your Room");

        jTable1.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null}
                },
                new String [] {
                        "Hotel", "Room #", "Rate ($CAD)"
                }
        ) {
            Class[] types = new Class [] {
                    java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setText("Room#:");

        jLabel3.setText("From:");

        dateFrom.setText("YYYY-MM-DD");

        jLabel4.setText("Until:");

        dateUntil.setText("YYYY-MM-DD");

        jLabel5.setText("Size of Party:");

        makeBooking.setText("Confirm Booking");
        makeBooking.addActionListener(evt -> makeBookingActionPerformed());

        jLabel6.setText("Note: Rooms are sorted by popularity");

        viewBookings.setText("View Your Bookings");
        viewBookings.addActionListener(evt -> viewBookingsActionPerformed());

        jLabel7.setText("Hotel Name:");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(sizeOfParty, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 489, GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel2)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(roomNum, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(47, 47, 47)
                                                                .addComponent(jLabel3)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(dateFrom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(jLabel6)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel7)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(hotelName, GroupLayout.PREFERRED_SIZE, 228, GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(27, 27, 27)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(makeBooking)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addComponent(jLabel4)
                                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(dateUntil, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(16, 16, 16)
                                                                .addComponent(viewBookings, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)))))
                                .addContainerGap(12, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(jLabel1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(roomNum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3)
                                        .addComponent(dateFrom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(dateUntil, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(sizeOfParty, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(makeBooking)
                                        .addComponent(jLabel7)
                                        .addComponent(hotelName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(viewBookings))
                                .addContainerGap())
        );

        pack();
    }

    private void makeBookingActionPerformed() {
            String initialDate = dateFrom.getText();
            String finalDate = dateUntil.getText();
            String hotel = hotelName.getText();
            Booking newBooking = new Booking(1+ dbHandler.getMaxBookingID(), initialDate, finalDate, Integer.parseInt(sizeOfParty.getText())); // THIS LINE IS THE MAIN ONE THAT NEEDS TO BE DONE
            dbHandler.insertBooking(newBooking, curr, Integer.parseInt(roomNum.getText()), hotel);
            java.awt.EventQueue.invokeLater(() -> new BookingConfirmation(dbHandler.getRateForRoom(Integer.parseInt(roomNum.getText())), curr, dbHandler).setVisible(true));
    }


    private void viewBookingsActionPerformed() {
        java.awt.EventQueue.invokeLater(() -> new UpdateBookings(curr, dbHandler).setVisible(true));
    }
}
