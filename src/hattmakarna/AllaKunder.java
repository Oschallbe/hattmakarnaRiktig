/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hattmakarna;
import java.awt.event.ActionEvent;
import oru.inf.InfDB; //importeras i alla klasser som vi ska använda
import oru.inf.InfException; //importeras i alla klasser som vi ska använda
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
/**
 *
 * @author iftinserar
 */
public class AllaKunder extends javax.swing.JFrame {
    private static InfDB idb;
    private String inloggadAnvandare;  
    /**
     * Creates new form AllaKunder
     */
    public AllaKunder(InfDB idb, String inloggadAnvandare) {
        this.inloggadAnvandare = inloggadAnvandare;
        this.idb = idb;
        initComponents();
        hanteraSearchListener(); // separat metod för sök
        fyllKundTabell();
    }
    
     // Laddar alla kunder till tabellen
    public void fyllKundTabell() {
        try {
            // Kolumnnamn för tabellen (endast KundID, Fornamn och Efternamn)
            String[] kolumnNamn = {
                "KundID", "Fornamn", "Efternamn"
            };

            // Skapar en DefaultTableModel
            DefaultTableModel kundModel = new DefaultTableModel(kolumnNamn, 0);

            // Hämtar alla KundID i ordning
            String selectKundID = "SELECT KundID FROM kund ORDER BY KundID ASC;";
            ArrayList<String> kundIDList = idb.fetchColumn(selectKundID);

            // Om vi har några KundID, börjar vi lägga till data i tabellen
            if (kundIDList != null) {
                for (String ettID : kundIDList) {
                    // Hämtar all data för varje KundID
                    String selectInfo = "SELECT KundID, Fornamn, Efternamn FROM kund WHERE KundID = " + ettID + ";";
                    HashMap<String, String> info = idb.fetchRow(selectInfo);

                    // Skapa en rad för tabellen baserat på hämtad data
                    Object[] enRad = new Object[kolumnNamn.length];
                    enRad[0] = info.get("KundID");          // KundID
                    enRad[1] = info.get("Fornamn");         // Fornamn
                    enRad[2] = info.get("Efternamn");       // Efternamn

                    // Lägg till raden i DefaultTableModel
                    kundModel.addRow(enRad);
                }

                // Sätt modellen för tabellen
                TblAllaKunder.setModel(kundModel);
            }

            // Ställ in automatisk storleksändring för tabellen
            TblAllaKunder.setAutoResizeMode(TblAllaKunder.AUTO_RESIZE_OFF);

            // Ställ in kolumnbredder
            TableColumn col = TblAllaKunder.getColumnModel().getColumn(0); // KundID
            col.setPreferredWidth(60);
            col = TblAllaKunder.getColumnModel().getColumn(1); // Förnamn
            col.setPreferredWidth(100);
            col = TblAllaKunder.getColumnModel().getColumn(2); // Efternamn
            col.setPreferredWidth(100);

        } catch (InfException ex) {
            System.out.println("Fel vid hämtning av kunddata: " + ex.getMessage());
        }
    }

    private void hanteraSearchListener() {
        BtnSok.addActionListener((ActionEvent e) -> {
            // Be användaren om sökterm
            String sokTerm = JOptionPane.showInputDialog("Ange KundID, eller Förnamn och Efternamn, för att söka efter en kund:");

            if (sokTerm == null || sokTerm.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Du måste ange en sökterm!");
                return;
            }

            try {
                String query = "";
                DefaultTableModel tomModell = new DefaultTableModel(new String[]{
                        "KundID", "Fornamn", "Efternamn"
                }, 0);

                // Om sökterm innehåller ett mellanslag, anta att det är förnamn + efternamn
                if (sokTerm.contains(" ")) {
                    String[] namnDelar = sokTerm.split(" ", 2);
                    String fornamn = namnDelar[0].trim().toLowerCase();
                    String efternamn = namnDelar[1].trim().toLowerCase();

                    query = "SELECT KundID, Fornamn, Efternamn FROM kund " +
                            "WHERE LOWER(fornamn) = '" + fornamn + "' " +
                            "AND LOWER(efternamn) = '" + efternamn + "';"; // Sök efter kund baserat på förnamn och efternamn
                } else {
                    // Om sökterm inte innehåller mellanslag, anta att det är KundID
                    // Kontrollera om sökterm är ett giltigt nummer
                    try {
                        int kundID = Integer.parseInt(sokTerm.trim()); // Försök att konvertera till ett heltal
                        query = "SELECT KundID, Fornamn, Efternamn FROM kund WHERE KundID = " + kundID + ";"; // Sök efter kund baserat på KundID
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Ogiltigt KundID. Var god och ange ett giltigt numeriskt KundID.");
                        return; // Avsluta om det inte är ett giltigt KundID
                    }
                }

                // Hämta resultat från databasen
                ArrayList<HashMap<String, String>> resultat = idb.fetchRows(query);

                if (resultat == null || resultat.isEmpty()) {
                    // Om ingen kund hittas, visa meddelande och återställ tabellen till den normala listan
                    JOptionPane.showMessageDialog(null, "Ingen kund hittades med den angivna termen.");

                    // Återställ tabellen genom att fylla den med alla kunder
                    fyllKundTabell();
                    return; // Avsluta sökfunktionen så att ingen vidare kod körs
                }

                // Skapa en ny tabellmodell för att visa resultatet
                String[] kolumnNamn = {
                        "KundID", "Förnamn", "Efternamn"
                };
                DefaultTableModel filtreradModell = new DefaultTableModel(kolumnNamn, 0);

                // Lägg till rader i modellen baserat på resultatet
                for (HashMap<String, String> rad : resultat) {
                    filtreradModell.addRow(new Object[]{
                            rad.get("KundID"),
                            rad.get("Fornamn"),
                            rad.get("Efternamn")
                    });
                }

                // Uppdatera tabellen med den filtrerade modellen
                TblAllaKunder.setModel(filtreradModell);

                // Automatisk justering av tabellstorlek
                TblAllaKunder.setAutoResizeMode(TblAllaKunder.AUTO_RESIZE_OFF);

                // Ställ in kolumnbredder
                TableColumn col = TblAllaKunder.getColumnModel().getColumn(0); // KundID
                col.setPreferredWidth(60);
                col = TblAllaKunder.getColumnModel().getColumn(1); // Förnamn
                col.setPreferredWidth(100);
                col = TblAllaKunder.getColumnModel().getColumn(2); // Efternamn
                col.setPreferredWidth(100);

            } catch (InfException ex) {
                JOptionPane.showMessageDialog(null, "Ett fel inträffade vid sökningen: " + ex.getMessage());
            }
        });
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnTillbaka = new javax.swing.JButton();
        btnLaggTill = new javax.swing.JButton();
        BtnSok = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TblAllaKunder = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
            }
        });

        btnLaggTill.setText("Lägg till ny kund");
        btnLaggTill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaggTillActionPerformed(evt);
            }
        });

        BtnSok.setText("Sök");

        TblAllaKunder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        TblAllaKunder.setEnabled(false);
        TblAllaKunder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblAllaKunderMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TblAllaKunder);

        jLabel1.setText("Alla kunder");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnLaggTill)
                                .addGap(47, 47, 47)
                                .addComponent(BtnSok)
                                .addGap(37, 37, 37)
                                .addComponent(btnTillbaka)))))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTillbaka)
                    .addComponent(btnLaggTill)
                    .addComponent(BtnSok))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTillbakaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTillbakaActionPerformed
    new HuvudMeny(idb, inloggadAnvandare).setVisible(true);
    this.dispose();
    }//GEN-LAST:event_btnTillbakaActionPerformed

    private void btnLaggTillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaggTillActionPerformed
    //new LaggTillKund(idb, inloggadAnvandare).setVisible(true);
    //this.setVisible(false);
    }//GEN-LAST:event_btnLaggTillActionPerformed

    private void TblAllaKunderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblAllaKunderMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TblAllaKunderMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AllaKunder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AllaKunder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AllaKunder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AllaKunder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new AllaKunder().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnSok;
    private javax.swing.JTable TblAllaKunder;
    private javax.swing.JButton btnLaggTill;
    private javax.swing.JButton btnTillbaka;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
