package ui.manager;

import database.DatabaseConnectionHandler;
import model.amenities.Room;
import model.employees.Cleaner;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.List;

public class DisplayRooms extends JFrame {
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel avgNum;
    private JLabel jLabel5;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JScrollPane jScrollPane3;
    private JTable allRooms;
    private JTable cleanedAll;
    private JTable mostPopular;

    private final List<Room> rooms;
    private final DatabaseConnectionHandler dbHandler;

    public DisplayRooms(List<Room> roomsForHotel, DatabaseConnectionHandler dbHandler, int hotelID) {
        this.dbHandler = dbHandler;
        this.rooms = roomsForHotel;
        initComponents();
        fillTableAllRooms();
        fillTableCleanedAll();
        fillTableMostPopular();
        getAVGPartySize(hotelID);
    }

    private void getAVGPartySize(int hotelID) {
        HashMap<Integer, Integer> partySizes = dbHandler.averagePartySizePerHotel();
        avgNum.setText(partySizes.getOrDefault(hotelID, 0) + " people");

    }

    private void fillTableMostPopular() {
        DefaultTableModel model = (DefaultTableModel) this.mostPopular.getModel();
        model.setRowCount(0);
        List<Room> byAll = dbHandler.mostPopularRooms();
        for (Room curr : byAll) {
            model.addRow(new Object[] {curr.getRoom_num(), curr.getRate()});
        }
    }

    private void fillTableCleanedAll() {
        DefaultTableModel model = (DefaultTableModel) this.cleanedAll.getModel();
        model.setRowCount(0);
        List<Cleaner> cleaners = dbHandler.cleanedAllBooked();
        for (Cleaner curr : cleaners) {
            model.addRow(new Object[] {curr.getEmployeeName(), curr.getEmployeeID()});
        }
    }

    private void fillTableAllRooms() {
        DefaultTableModel model = (DefaultTableModel) this.allRooms.getModel();
        model.setRowCount(0);
        for (Room curr : rooms) {
            model.addRow(new Object[] {curr.getRoom_num(), curr.getRate()});
        }
    }

    private void initComponents() {

            jScrollPane1 = new JScrollPane();
            allRooms = new JTable();
            jLabel1 = new JLabel();
            jLabel2 = new JLabel();
            jScrollPane2 = new JScrollPane();
            cleanedAll = new JTable();
            jLabel3 = new JLabel();
            avgNum = new JLabel();
            jScrollPane3 = new JScrollPane();
            mostPopular = new JTable();
            jLabel5 = new JLabel();

            setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            allRooms.setModel(new DefaultTableModel(
                    new Object [][] {
                            {null, null},
                            {null, null},
                            {null, null},
                            {null, null}
                    },
                    new String [] {
                            "Number", "Rate ($CAD)"
                    }
            ) {
                Class[] types = new Class [] {
                        java.lang.Integer.class, java.lang.Integer.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jScrollPane1.setViewportView(allRooms);

            jLabel1.setText("All Rooms:");

            jLabel2.setText("Cleaners that have Cleaned all Booked Rooms:");

            cleanedAll.setModel(new DefaultTableModel(
                    new Object [][] {
                            {null, null},
                            {null, null},
                            {null, null},
                            {null, null}
                    },
                    new String [] {
                            "Name", "Employee ID"
                    }
            ) {
                Class[] types = new Class [] {
                        java.lang.Integer.class, java.lang.Integer.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jScrollPane2.setViewportView(cleanedAll);

            jLabel3.setText("Your Hotel's Average Party Size:");

            avgNum.setText(" people");

            mostPopular.setModel(new DefaultTableModel(
                    new Object [][] {
                            {null, null},
                            {null, null},
                            {null, null},
                            {null, null}
                    },
                    new String [] {
                            "Number", "Rate ($CAD)"
                    }
            ) {
                Class[] types = new Class [] {
                        java.lang.Integer.class, java.lang.Integer.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jScrollPane3.setViewportView(mostPopular);

            jLabel5.setText("Most Popular Rooms:");

            GroupLayout layout = new GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5)
                                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jLabel1)
                                                    .addComponent(jLabel2)
                                                    .addComponent(jScrollPane2)
                                                    .addComponent(jScrollPane1)
                                                    .addComponent(jScrollPane3)
                                                    .addGroup(layout.createSequentialGroup()
                                                            .addComponent(jLabel3)
                                                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(avgNum))))
                                    .addGap(10, 10, 10))
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addGap(11, 11, 11)
                                    .addComponent(jLabel1)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel2)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, 112, GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel3)
                                            .addComponent(avgNum))
                                    .addGap(19, 19, 19))
            );

            pack();
        }
}
