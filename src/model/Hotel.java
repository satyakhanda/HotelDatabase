package model;

import database.DatabaseConnectionHandler;
import ui.OpeningUI;


public class Hotel {
    private DatabaseConnectionHandler dbHandler = null;
    public Hotel() {
        dbHandler = new DatabaseConnectionHandler();
    }

    private void start() {
        //start login UI
        login(" ", " ");

    }

    private void end() {
        dbHandler.close();
        dbHandler = null;
    }

    public void login(String username, String password) {
        boolean didConnect = dbHandler.login("ora_cknee", "a43457738");

        if (didConnect) {
            //get rid of login ui
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(OpeningUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(OpeningUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(OpeningUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(OpeningUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            //</editor-fold>
            //</editor-fold>

            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new OpeningUI(dbHandler).setVisible(true);
                }
            });
            //new OpeningUI(dbHandler);
        } else {
            //display failed login, retry
        }
    }
    public static void main(String[] args) {

        Hotel hotel = new Hotel();
        hotel.start();
    }

}
