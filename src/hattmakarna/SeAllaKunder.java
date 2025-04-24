/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hattmakarna;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import oru.inf.InfDB; //importeras i alla klasser som vi ska använda
import oru.inf.InfException; //importeras i alla klasser som vi ska använda
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
/**
 *
 * @author iftinserar
 */
public class SeAllaKunder extends javax.swing.JPanel {
    private static InfDB idb;
    private String inloggadAnvandare; 
    private boolean harFiltrerat = false;
    /**
     * Creates new form AllaKunder
     */
    public SeAllaKunder(InfDB idb, String inloggadAnvandare) {
        this.inloggadAnvandare = inloggadAnvandare;
        this.idb = idb;
        initComponents();
        hanteraSearchListener(); // separat metod för sök
        fyllKundTabell();
    }
    
     // Laddar alla kunder till tabellen
    public void fyllKundTabell() {
        try {
            // Kolumnnamn för tabellen (endast Fornamn och Efternamn visas – KundID används men döljs)
            String[] kolumnNamn = {"KundID", "Fornamn", "Efternamn"};
            DefaultTableModel kundModel = new DefaultTableModel(kolumnNamn, 0);

            String selectKundID = "SELECT KundID FROM kund ORDER BY KundID ASC;";
            ArrayList<String> kundIDList = idb.fetchColumn(selectKundID);

            if (kundIDList != null) {
                for (String ettID : kundIDList) {
                    String selectInfo = "SELECT KundID, Fornamn, Efternamn FROM kund WHERE KundID = " + ettID + ";";
                    HashMap<String, String> info = idb.fetchRow(selectInfo);

                    String fornamn = info.get("Fornamn");
                    String efternamn = info.get("Efternamn");

                    // Hoppa över kunder som saknar namn
                    if (fornamn == null || efternamn == null || fornamn.trim().isEmpty() || efternamn.trim().isEmpty()) {
                        continue;
                    }

                    Object[] enRad = new Object[kolumnNamn.length];
                    enRad[0] = info.get("KundID");
                    enRad[1] = fornamn;
                    enRad[2] = efternamn;

                    kundModel.addRow(enRad);
                }

                TblAllaKunder.setModel(kundModel);
            }

            // Ställ in kolumnstorlekar
            TblAllaKunder.setAutoResizeMode(TblAllaKunder.AUTO_RESIZE_OFF);
            TblAllaKunder.getColumnModel().getColumn(0).setPreferredWidth(100); // KundID
            TblAllaKunder.getColumnModel().getColumn(1).setPreferredWidth(250); // Förnamn
            TblAllaKunder.getColumnModel().getColumn(2).setPreferredWidth(250); // Efternamn

            // Dölj KundID-kolumnen men behåll datan i modellen
            TblAllaKunder.getColumnModel().getColumn(0).setMinWidth(0);
            TblAllaKunder.getColumnModel().getColumn(0).setMaxWidth(0);
            TblAllaKunder.getColumnModel().getColumn(0).setWidth(0);

        } catch (InfException ex) {
            System.out.println("Fel vid hämtning av kunddata: " + ex.getMessage());
        }
    }

    private void hanteraSearchListener() {
    BtnSok.addActionListener((ActionEvent e) -> {
        // Be användaren om sökterm
        String sokTerm = JOptionPane.showInputDialog("Ange KundID, eller Förnamn och Efternamn, för att söka efter en kund:");

            if (sokTerm == null) {
                // Användaren tryckte på Avbryt — gör inget
                return;
            }

            if (sokTerm.trim().isEmpty()) {
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
                harFiltrerat = false;  // Se till att vi återställer harFiltrerat till false
                return; // Avsluta sökfunktionen så att ingen vidare kod körs
            }

            // Om resultatet inte är tomt, sätt harFiltrerat till true
            harFiltrerat = true;

            // Skapa en ny tabellmodell för att visa resultatet
            String[] kolumnNamn = {
                    "KundID", "Förnamn", "Efternamn"
            };
            DefaultTableModel filtreradModell = new DefaultTableModel(kolumnNamn, 0);

            // Lägg till rader i modellen baserat på resultatet
            for (HashMap<String, String> rad : resultat) {
                filtreradModell.addRow(new Object[]{
                rad.get("KundID") != null ? rad.get("KundID") : "Saknas",
                rad.get("Fornamn") != null ? rad.get("Fornamn") : "Saknas",
                rad.get("Efternamn") != null ? rad.get("Efternamn") : "Saknas"
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

        btnLaggTill = new javax.swing.JButton();
        BtnSok = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TblAllaKunder = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        btnLaggTill.setText("Lägg till ny kund");
        btnLaggTill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaggTillActionPerformed(evt);
            }
        });

        BtnSok.setText("Sök");

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setForeground(new java.awt.Color(255, 255, 255));

        TblAllaKunder.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Table 3"
            }
        ));
        TblAllaKunder.setEnabled(false);
        TblAllaKunder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TblAllaKunderMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(TblAllaKunder);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Alla kunder");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnLaggTill)
                                .addGap(314, 314, 314)
                                .addComponent(BtnSok))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(244, 244, 244)
                        .addComponent(jLabel1)))
                .addGap(0, 48, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLaggTill)
                    .addComponent(BtnSok))
                .addGap(49, 49, 49))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnLaggTillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaggTillActionPerformed
    try {
        // Skapa en instans av LäggTillNyKund som en JPanel
        LäggTillNyKund laggTillNyKundPanel = new LäggTillNyKund(idb, inloggadAnvandare);

        // Lägg till panelen i MainFrame
        MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
        mainFrame.addPanelToCardLayout(laggTillNyKundPanel, "laggTillNyKund");
        mainFrame.showPanel("laggTillNyKund");
    } catch (Exception e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnLaggTillActionPerformed

    private void TblAllaKunderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TblAllaKunderMouseClicked
    if(evt.getSource() == TblAllaKunder) {
        int row = TblAllaKunder.rowAtPoint(evt.getPoint());
        int column = TblAllaKunder.columnAtPoint(evt.getPoint());

        if (column == 0 || column == 1 || column == 2) {
            int kundID = Integer.parseInt(TblAllaKunder.getValueAt(row, 0).toString());

            // Skicka med en flagga om varifrån vi kommer, t.ex. "SeAllaKunder"
            String previousPanel = "SeAllaKunder"; // Kan vara "SeVanligOrder" eller andra paneler

            // Skapa instansen av SpecifikKund och skicka med previousPanel
            SpecifikKund specifikKundPanel = new SpecifikKund(idb, inloggadAnvandare, kundID, previousPanel);

            MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
            JPanel wrapper = new JPanel(new GridBagLayout());
            wrapper.add(specifikKundPanel);

            mainFrame.addPanelToCardLayout(wrapper, "SpecifikKund" + kundID);
            mainFrame.showPanel("SpecifikKund" + kundID);
        }
    }
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

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // new SeSpecifikProdukt().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnSok;
    private javax.swing.JTable TblAllaKunder;
    private javax.swing.JButton btnLaggTill;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
