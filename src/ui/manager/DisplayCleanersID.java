package ui.manager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class DisplayCleanersID extends JFrame {
    private JTable cleanerTable;
    private JScrollPane jScrollPane1;

    private List<Integer> cleanersID;

    public DisplayCleanersID(List<Integer> cleanersID) {
        this.cleanersID = cleanersID;
        initComponents();
        fillTableNameAndID();
    }

    private void fillTableNameAndID() {
        DefaultTableModel model = (DefaultTableModel) this.cleanerTable.getModel();
        model.setRowCount(0);
        for (Integer curr : cleanersID) {
            model.addRow(new Object[] {curr});
        }
    }

    private void initComponents() {

        jScrollPane1 = new JScrollPane();
        cleanerTable = new JTable();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        cleanerTable.setModel(new DefaultTableModel(
                new Object [][] {
                        {null},
                        {null},
                        {null},
                        {null},
                        {null}
                },
                new String [] {
                        "EmployeeID"
                }
        ) {
            Class[] types = new Class [] {
                    java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(cleanerTable);

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 379, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(15, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pack();
    }
}
