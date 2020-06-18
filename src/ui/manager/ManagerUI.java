package ui.manager;

import database.DatabaseConnectionHandler;
import model.employees.Manager;

import javax.swing.*;
import java.util.List;

public class ManagerUI extends JFrame {
    private JTextField enteredEmpID;
    private JButton submitEmpID;
    private JLabel jLabel1;
    private JScrollPane jScrollPane1;
    private JTextArea jTextArea1;

    private DatabaseConnectionHandler dbHandler;

    public ManagerUI(DatabaseConnectionHandler dbHandler) {
        this.dbHandler = dbHandler;
        initComponents();
    }

    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        jTextArea1 = new JTextArea();
        enteredEmpID = new JTextField();
        jLabel1 = new JLabel();
        submitEmpID = new JButton();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Please enter your EmployeeID");

        submitEmpID.setText("Submit");
        submitEmpID.addActionListener(evt -> submitEmpIDActionPerformed());

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(enteredEmpID, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
                        .addGap(61, 61, 61)
                        .addComponent(submitEmpID)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(enteredEmpID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(submitEmpID))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        pack();
    }

    private void submitEmpIDActionPerformed() {
        String empID = enteredEmpID.getText();
        List<Manager> managers = dbHandler.getManagers();
        for (Manager m : managers) {
            if (m.getEmployeeID() == Integer.parseInt(empID)) {
                java.awt.EventQueue.invokeLater(() -> new HotelManagerUI(m, dbHandler).setVisible(true));
                this.setVisible(false);
            }
        }
        enteredEmpID.setText("");
    }
}
