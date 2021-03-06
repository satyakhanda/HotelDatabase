package ui.manager;

import database.DatabaseConnectionHandler;
import model.employees.Employee;

import javax.swing.*;

public class ChooseDisplayCleaners extends JFrame {
    private JButton dsplyID;
    private JButton dsplyNameID;
    private JLabel jLabel1;

    private final Employee manager;
    private final DatabaseConnectionHandler dbHandler;
    public ChooseDisplayCleaners(Employee manager, DatabaseConnectionHandler dbHandler) {
        this.manager = manager;
        this.dbHandler = dbHandler;
        initComponents();
    }

    private void initComponents() {

        dsplyNameID = new JButton();
        dsplyID = new JButton();
        jLabel1 = new JLabel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        dsplyNameID.setText("Name and ID");
        dsplyNameID.addActionListener(evt -> dsplyNameIDActionPerformed());

        dsplyID.setText("ID");
        dsplyID.addActionListener(evt -> dsplyIDActionPerformed());

        jLabel1.setText("Choose what information regarding cleaners you want to see:");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(54, 54, 54)
                                                .addComponent(dsplyNameID)
                                                .addGap(80, 80, 80)
                                                .addComponent(dsplyID))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(14, 14, 14)
                                                .addComponent(jLabel1)))
                                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(dsplyNameID)
                                        .addComponent(dsplyID))
                                .addContainerGap(22, Short.MAX_VALUE))
        );

        pack();
    }

    private void dsplyNameIDActionPerformed() {
        java.awt.EventQueue.invokeLater(() -> new DisplayCleanersBoth(dbHandler.getCleanersForHotel(manager.getHotelID())).setVisible(true));
    }

    private void dsplyIDActionPerformed() {
        java.awt.EventQueue.invokeLater(() -> new DisplayCleanersID(dbHandler.getCleanersIDForHotel(manager.getHotelID())).setVisible(true));
    }
}
