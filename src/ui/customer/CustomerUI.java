package ui.customer;

import database.DatabaseConnectionHandler;
import model.customers.Customer;

import javax.swing.*;
public class CustomerUI extends JFrame {
    private JButton createAccount;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JButton login;
    private JPasswordField password;
    private JTextField username;

    private final DatabaseConnectionHandler dbHandler;

    public CustomerUI(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
        initComponents();
    }

   private void initComponents() {

        jLabel1 = new JLabel();
        login = new JButton();
        username = new JTextField();
        jLabel2 = new JLabel();
        password = new JPasswordField();
        jLabel3 = new JLabel();
        createAccount = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Before making a booking, please login or create an account");

        login.setText("Login");
        login.addActionListener(evt -> loginActionPerformed());

        jLabel2.setText("Password");

        jLabel3.setText("Username");

        createAccount.setText("Create New Account");
        createAccount.addActionListener(evt -> createAccountActionPerformed());

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(username, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(password, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
                        .addGap(35, 35, 35))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(login)
                        .addGap(18, 18, 18)
                        .addComponent(createAccount)
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(username, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(login)
                    .addComponent(createAccount))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        pack();
    }

    private void loginActionPerformed() {
        Customer curr = dbHandler.getCustomer(username.getText(), new String(password.getPassword()));

        if (curr == null) {
            username.setText("");
            password.setText("");
        } else {
            java.awt.EventQueue.invokeLater(() -> new MakeBookingUI(curr, dbHandler).setVisible(true));
        }
    }

    private void createAccountActionPerformed() {
        java.awt.EventQueue.invokeLater(() -> new CreateAccountUI(dbHandler).setVisible(true));
    }
}
