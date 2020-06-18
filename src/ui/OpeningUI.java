package ui;

import database.DatabaseConnectionHandler;
import ui.customer.CustomerUI;
import ui.manager.ManagerUI;

import javax.swing.*;

public class OpeningUI extends JFrame {
    private JButton custButton;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JButton managerButton;

    private DatabaseConnectionHandler dbHandler;

    public OpeningUI(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
        initComponents();
    }

    private void initComponents() {

        managerButton = new JButton();
        custButton = new JButton();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        managerButton.setText("Manager");
        managerButton.addActionListener(evt -> managerButtonActionPerformed());

        custButton.setText("Customer");
        custButton.addActionListener(evt -> custButtonActionPerformed());
        jLabel1.setText("Please choose an option to continue:");

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel2.setText("                Vacation Booker");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(59, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(managerButton)
                        .addGap(81, 81, 81)
                        .addComponent(custButton)
                        .addGap(60, 60, 60))
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(82, 82, 82))))
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 349, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(managerButton)
                    .addComponent(custButton))
                .addGap(77, 77, 77))
        );

        pack();
    }

    private void custButtonActionPerformed() {
        java.awt.EventQueue.invokeLater(() -> new CustomerUI(dbHandler).setVisible(true));
    }

    private void managerButtonActionPerformed() {
        java.awt.EventQueue.invokeLater(() -> new ManagerUI(dbHandler).setVisible(true));

    }
}
