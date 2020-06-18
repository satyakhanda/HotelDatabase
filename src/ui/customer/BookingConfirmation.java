package ui.customer;

import database.DatabaseConnectionHandler;
import model.customers.Customer;

import javax.swing.*;

public class BookingConfirmation extends JFrame {
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel rate;

    public BookingConfirmation(int price, Customer customer, DatabaseConnectionHandler dbHandler) {
        initComponents();
        makePayment(customer, dbHandler);
    }

    public void makePayment(Customer customer, DatabaseConnectionHandler dbHandler) {
        rate.setText("$ " + (dbHandler.makePayment(customer)));
    }

    private void initComponents() {

        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        rate = new JLabel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Thank you, the following will be charged to your credit card:");

        rate.setText("$");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addComponent(rate)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(73, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(rate))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
    }
}
