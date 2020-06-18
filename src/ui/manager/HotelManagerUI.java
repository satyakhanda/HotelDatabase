package ui.manager;

import database.DatabaseConnectionHandler;
import model.employees.Employee;

import javax.swing.*;


public class HotelManagerUI extends JFrame {
    private JButton displayRoomButton;
    private JButton displayEmpButton;

    private final Employee manager;
    private final DatabaseConnectionHandler dbHandler;

    public HotelManagerUI(Employee manager, DatabaseConnectionHandler dbHandler) {
        this.manager = manager;
        this.dbHandler = dbHandler;
        initComponents();
    }

    private void initComponents() {

        displayEmpButton = new JButton();
        displayRoomButton = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        displayEmpButton.setText("Display Cleaners");
        displayEmpButton.addActionListener(evt -> displayEmpButtonActionPerformed());

        displayRoomButton.setText("Display Analytics");
        displayRoomButton.addActionListener(evt -> displayRoomButtonActionPerformed());

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(26, Short.MAX_VALUE)
                .addComponent(displayEmpButton)
                .addGap(29, 29, 29)
                .addComponent(displayRoomButton)
                .addGap(38, 38, 38))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(displayEmpButton)
                    .addComponent(displayRoomButton))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }

    private void displayEmpButtonActionPerformed() {
        java.awt.EventQueue.invokeLater(() -> new ChooseDisplayCleaners(manager, dbHandler).setVisible(true));
    }

    private void displayRoomButtonActionPerformed() {
        java.awt.EventQueue.invokeLater(() -> new DisplayRooms(dbHandler.getRoomsForHotel(manager.getHotelID()), dbHandler, manager.getHotelID()).setVisible(true));
    }

}
