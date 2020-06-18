package ui.customer;

import database.DatabaseConnectionHandler;
import model.customers.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class UpdateBookings extends JFrame {
    private JButton cancelBooking;
    private JButton updateBooking;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JScrollPane jScrollPane1;
    private JTable jTable1;
    private JTextField jTextField1;

    private final DatabaseConnectionHandler dbHandler;
    private final Customer customer;
    private Object booking;

    public UpdateBookings(Customer customer, DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
        this.customer = customer;
        initComponents();
        fillTable();
    }

    private void fillTable() {
        DefaultTableModel model = (DefaultTableModel) this.jTable1.getModel();
        model.setRowCount(0);
        List<List<Object>> bookings = dbHandler.getBooking(customer);
        if (!(bookings.size() == 0)) {
            booking = bookings.get(0).get(0);
            for (List<Object> curr : bookings) {
                model.addRow(new Object[]{curr.get(0), curr.get(1), curr.get(2), curr.get(3)});
            }
        }
    }

    private void initComponents() {

        jLabel1 = new JLabel();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        updateBooking = new JButton();
        jLabel2 = new JLabel();
        jTextField1 = new JTextField();
        cancelBooking = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Your Booking");

        jTable1.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null},
                        {null, null, null, null}
                },
                new String [] {
                        "BookingID", "Size of Party", "From", "Until"
                }
        ) {
            Class[] types = new Class [] {
                    java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        updateBooking.setText("Update Booking");
        updateBooking.addActionListener(evt -> updateBookingActionPerformed());

        jLabel2.setText("New End Date:");

        jTextField1.setText("YYYY-MM-DD");

        cancelBooking.setText("Cancel Booking");
        cancelBooking.addActionListener(evt -> cancelBookingActionPerformed());

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(6, 6, 6)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 373, GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addComponent(jLabel2)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(updateBooking, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                        .addComponent(cancelBooking, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel1)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(updateBooking)
                                        .addComponent(jLabel2)
                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cancelBooking)
                                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }

    private void updateBookingActionPerformed() {
        String creditCard = this.customer.getCreditCard();
        int bookingID = (int) booking;
        dbHandler.updateBooking(creditCard, bookingID, jTextField1.getText());
        fillTable();
    }

    private void cancelBookingActionPerformed() {
        String creditCard = this.customer.getCreditCard();
        int bookingID = (int) booking;
        dbHandler.updateBooking(creditCard, bookingID, null);
        fillTable();
    }

}
