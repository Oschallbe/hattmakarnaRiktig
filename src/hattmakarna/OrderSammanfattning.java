/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hattmakarna;
import oru.inf.InfDB;
import oru.inf.InfException; 
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
/**
 *
 * @author iftinserar
 */
public class OrderSammanfattning extends javax.swing.JFrame {
    private InfDB idb; 
    private String inloggadAnvandare; 
    private ArrayList<SkapaVanligOrder.Orderrad> orderrader;
    private double totalpris;
    private String kundID;
    private String ordernummer;
    private boolean express = false;
    private boolean redigeringsLage = false;
    private String typ;


    /**
     * Creates new form OrderSammanfattning
     */
    public OrderSammanfattning(InfDB idb, String inloggadAnvandare, ArrayList<SkapaVanligOrder.Orderrad> orderrader, double totalpris, String kundID, String ordernummer, boolean express,  String typ) {
    initComponents();
    this.idb = idb;
    this.inloggadAnvandare = inloggadAnvandare;
    this.orderrader = orderrader;
    this.totalpris = totalpris;
    this.kundID = kundID;
    this.ordernummer = ordernummer;
    this.express = express;
    this.typ = typ;

    // Anropa metod för att sätta textfält och knappar
    setupTextFieldsAndButtons();   
    }

    
    private void setupTextFieldsAndButtons() {
    tfKundID.setText(kundID);
    tfOrdernummer.setText(ordernummer);

    String[] kolumner = {"Produkt", "Antal", "Pris per st", "Totalt"};
    
    DefaultTableModel modell = new DefaultTableModel(kolumner, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return redigeringsLage && column == 1; // Endast kolumn 1 ("Antal") är redigerbar i redigeringsläge
        }
    };

    // Fyll tabellen med data från orderrader
    for (SkapaVanligOrder.Orderrad rad : orderrader) {
        String produktNamn = rad.getNamn();
        int antal = rad.getAntal();
        double prisPerSt = rad.getPris();
        double totalRadPris = rad.totalPris();

        Object[] radData = {
            produktNamn,
            antal,
            String.format("%.2f kr", prisPerSt).replace(",", "."),
            String.format("%.2f kr", totalRadPris).replace(",", ".")
        };
        modell.addRow(radData);
    }

    // Beräkna expressavgift och totalpris
    double expressAvgift = express ? totalpris * 0.2 : 0;
    double slutpris = totalpris + expressAvgift;

    tfExpress.setText(express ? String.format("%.2f kr", expressAvgift).replace(",", ".") : "0 kr");
    tfTotalpris.setText(String.format("%.2f kr", slutpris).replace(",", "."));

    tblOrdersammanfattning.setModel(modell);
    if (tblOrdersammanfattning.getRowCount() > 0) {
        tblOrdersammanfattning.setRowSelectionInterval(0, 0);
    }

    btnSpara.setEnabled(false);
    btnTaBort.setEnabled(false);
    uppdateraTotalpris();
}
    
    private void andraAntal(int andring) {
        if (!redigeringsLage) {
            JOptionPane.showMessageDialog(this, "Klicka på 'Redigera' först innan du kan ändra antal!");
            return;
        }

        int valdRad = tblOrdersammanfattning.getSelectedRow();

        if (valdRad == -1) {
            JOptionPane.showMessageDialog(this, "Välj en produkt först genom att klicka på en rad i tabellen.");
            return;
        }

        SkapaVanligOrder.Orderrad rad = orderrader.get(valdRad);
        int nuvarandeAntal = rad.getAntal(); // Använd getter
        int nyttAntal = nuvarandeAntal + andring;

        if (nyttAntal <= 0) {
            JOptionPane.showMessageDialog(this, "Antal måste vara minst 1.");
            return;
        }

        rad.setAntal(nyttAntal); // Använd setter

        DefaultTableModel modell = (DefaultTableModel) tblOrdersammanfattning.getModel();
        modell.setValueAt(nyttAntal, valdRad, 1);
        modell.setValueAt(String.format("%.2f kr", nyttAntal * rad.getPris()), valdRad, 3);

        uppdateraTotalpris();
    }
    
    private void nollstallOrder() {
    orderrader.clear();
    express = false;
    tfExpress.setText("0 kr");
    tfTotalpris.setText("0 kr");

    DefaultTableModel modell = (DefaultTableModel) tblOrdersammanfattning.getModel();
    modell.setRowCount(0);  // Rensa tabellen

    JOptionPane.showMessageDialog(this, "Varukorgen är tom och expresskostnad har nollställts.");
    }

    private void uppdateraTotalpris() {
        totalpris = 0;
        for (SkapaVanligOrder.Orderrad rad : orderrader) {
            totalpris += rad.totalPris();
        }

        double expressAvgift = express ? totalpris * 0.2 : 0;

        // Visa expressavgiften med punkt som decimaltecken i UI
        tfExpress.setText(String.format("%.2f kr", expressAvgift));

        double slutpris = totalpris + expressAvgift;

        // Visa slutpriset med punkt som decimaltecken i UI
        tfTotalpris.setText(String.format("%.2f kr", slutpris));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOrdersammanfattning = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnBekrafta = new javax.swing.JButton();
        btnTillbaka = new javax.swing.JButton();
        btnRedigera = new javax.swing.JButton();
        btnSpara = new javax.swing.JButton();
        btnTaBort = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        tfOrdernummer = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        tfKundID = new javax.swing.JTextField();
        lblTotalpris = new javax.swing.JLabel();
        tfTotalpris = new javax.swing.JTextField();
        tfExpress = new javax.swing.JTextField();
        lblExpressavgift = new javax.swing.JLabel();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblOrdersammanfattning.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblOrdersammanfattning);

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel1.setText("Orderspecifikation");

        btnBekrafta.setText("Bekräfta");
        btnBekrafta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBekraftaActionPerformed(evt);
            }
        });

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
            }
        });

        btnRedigera.setText("Redigera");
        btnRedigera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRedigeraActionPerformed(evt);
            }
        });

        btnSpara.setText("Spara");
        btnSpara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSparaActionPerformed(evt);
            }
        });

        btnTaBort.setText("Ta bort");
        btnTaBort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaBortActionPerformed(evt);
            }
        });

        jLabel2.setText("Ordernummer:");

        tfOrdernummer.setEnabled(false);

        jLabel3.setText("KundID:");

        tfKundID.setEnabled(false);

        lblTotalpris.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblTotalpris.setText("Totalpris:");

        tfTotalpris.setEnabled(false);

        tfExpress.setEnabled(false);

        lblExpressavgift.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblExpressavgift.setText("Expressavgift:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblExpressavgift)
                .addGap(18, 18, 18)
                .addComponent(tfExpress, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addComponent(btnTillbaka)
                .addGap(18, 18, 18)
                .addComponent(btnRedigera)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSpara)
                    .addComponent(lblTotalpris))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tfTotalpris, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTaBort)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                        .addComponent(btnBekrafta)))
                .addGap(26, 26, 26))
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfKundID, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfOrdernummer, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(tfKundID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(tfOrdernummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfTotalpris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalpris)
                    .addComponent(tfExpress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblExpressavgift))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBekrafta)
                    .addComponent(btnTillbaka)
                    .addComponent(btnRedigera)
                    .addComponent(btnSpara)
                    .addComponent(btnTaBort))
                .addGap(17, 17, 17))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRedigeraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRedigeraActionPerformed
    redigeringsLage = true;
    btnSpara.setEnabled(true);
    btnTaBort.setEnabled(true);
    btnRedigera.setEnabled(false);

    tblOrdersammanfattning.repaint(); // Säkerställ att tabellen uppdateras visuellt
    }//GEN-LAST:event_btnRedigeraActionPerformed

    private void btnTillbakaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTillbakaActionPerformed
   int val = JOptionPane.showConfirmDialog(
        this,
        "Vill du avbryta beställningen och rensa varukorgen?",
        "Avbryt beställning",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );

    if (val == JOptionPane.YES_OPTION) {
        new SkapaVanligOrder(idb, inloggadAnvandare).setVisible(true); 
        this.dispose();     
    }
    // Om användaren väljer "Nej" görs inget alls
    }//GEN-LAST:event_btnTillbakaActionPerformed

    private void btnSparaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSparaActionPerformed
    // Säkerställ att pågående cellredigering avslutas
    if (tblOrdersammanfattning.isEditing()) {
        tblOrdersammanfattning.getCellEditor().stopCellEditing();
    }

    DefaultTableModel modell = (DefaultTableModel) tblOrdersammanfattning.getModel();
    boolean valid = true;

    // Gå igenom alla rader och validera antal
    for (int i = 0; i < modell.getRowCount(); i++) {
        Object cellValue = modell.getValueAt(i, 1); // kolumn "Antal"
        try {
            String cellStr = cellValue.toString().trim();

            // Kontrollera om cellen är tom
            if (cellStr.isEmpty()) {
                throw new NumberFormatException("Tomt antal");
            }

            // Försök konvertera till ett heltal
            int antal = Integer.parseInt(cellStr);

            // Kontrollera om antalet är positivt
            if (antal <= 0) {
                JOptionPane.showMessageDialog(this, "Antalet måste vara ett positivt heltal.");
                valid = false;  // Valideringen misslyckades
                break;  // Avbryt loopen om valideringen misslyckas
            }

            // Om antalet är giltigt, uppdatera orderraden och totalpris
            orderrader.get(i).setAntal(antal); 
            modell.setValueAt(String.format("%.2f kr", antal * orderrader.get(i).getPris()), i, 3); // Uppdatera totalsumma

        } catch (NumberFormatException e) {
            // Visa felmeddelande för ogiltigt antal
            JOptionPane.showMessageDialog(this, "Antalet är inte ett giltigt heltal.");
            valid = false;  // Valideringen misslyckades
            break;  // Avbryt loopen om det finns ogiltigt värde
        }
    }

    // Om alla värden är giltiga, uppdatera totalpris och visa meddelande om sparade ändringar
    if (valid) {
        uppdateraTotalpris();  // Uppdatera totalpris efter validering
        JOptionPane.showMessageDialog(this, "Ändringar har sparats i varukorgen! Klicka på 'Bekräfta' för att skicka beställningen.");
        redigeringsLage = false;
        btnSpara.setEnabled(false);
        btnTaBort.setEnabled(false);
        btnRedigera.setEnabled(true);
    }
    }//GEN-LAST:event_btnSparaActionPerformed

    private void btnTaBortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaBortActionPerformed
    int valdRad = tblOrdersammanfattning.getSelectedRow();

    if (valdRad == -1) {
        JOptionPane.showMessageDialog(this, "Välj en produkt att ta bort.");
        return;
    }

    int bekrafta = JOptionPane.showConfirmDialog(this, 
        "Vill du verkligen ta bort den här produkten?", 
        "Bekräfta borttagning", 
        JOptionPane.YES_NO_OPTION);

    if (bekrafta == JOptionPane.YES_OPTION) {
        DefaultTableModel modell = (DefaultTableModel) tblOrdersammanfattning.getModel();
        modell.removeRow(valdRad);
        orderrader.remove(valdRad);

        uppdateraTotalpris();

        if (orderrader.isEmpty()) {
            nollstallOrder();  // << Nollställ express och totalpris om varukorgen är tom!
        }

        JOptionPane.showMessageDialog(this, "Produkten togs bort.");
    }
    }//GEN-LAST:event_btnTaBortActionPerformed

    private void btnBekraftaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBekraftaActionPerformed
    if (tblOrdersammanfattning.isEditing()) {
        tblOrdersammanfattning.getCellEditor().stopCellEditing();
    }

    // Här gör vi en validering för att säkerställa att alla antal är giltiga innan vi sparar till databasen
    DefaultTableModel modell = (DefaultTableModel) tblOrdersammanfattning.getModel();
    boolean valid = true;

    // Validera alla antal innan vi försöker spara till databasen
    for (int i = 0; i < modell.getRowCount(); i++) {
        Object cellValue = modell.getValueAt(i, 1); // kolumn "Antal"
        try {
            int antal = Integer.parseInt(cellValue.toString());
            if (antal <= 0) {
                JOptionPane.showMessageDialog(this, "Antalet på rad " + (i + 1) + " måste vara ett positivt heltal.");
                valid = false;  // Valideringen misslyckades
                break;
            } else {
                orderrader.get(i).setAntal(antal); // Uppdatera i modellen
                modell.setValueAt(String.format("%.2f kr", antal * orderrader.get(i).getPris()), i, 3); // Uppdatera radens total
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Antalet på rad " + (i + 1) + " är inte ett giltigt heltal.");
            valid = false;  // Valideringen misslyckades
            break;
        }
    }

    // Om all validering lyckades, uppdatera totalpris och spar till databasen
    if (valid) {
        uppdateraTotalpris();  // Uppdatera totalpris och expresskostnad

        // Status och datum
        String status = "Under behandling";
        String datum = java.time.LocalDate.now().toString();
        String expressBool = express ? "1" : "0";

        // Validera och konvertera totalpris
        String totalprisStr = tfTotalpris.getText().replace("kr", "").trim().replace(",", ".");
        double totalprisDouble = Double.parseDouble(totalprisStr);
        String formattedTotalpris = String.format("%.2f", totalprisDouble).replace(",", ".");

        // Skapa och kör SQL
        String sql = String.format(
            "INSERT INTO Bestallning (Status, Datum, Expressbestallning, KundID, FraktsedelID, Totalpris, Typ) " +
            "VALUES ('%s', '%s', %s, %s, NULL, %s, '%s')",
            status, datum, expressBool, kundID, formattedTotalpris, typ
        );

        try {
             idb.insert(sql);  // Spara i databasen
             JOptionPane.showMessageDialog(this, "Ordern har skickats iväg!");
             new SkapaVanligOrder(idb, inloggadAnvandare).setVisible(true);
             this.dispose();
         } catch (InfException ex) {
             // Hantera om det uppstår ett problem med databasoperationen
             JOptionPane.showMessageDialog(this, "Fel vid sparning: " + ex.getMessage());
         }
     }
    }//GEN-LAST:event_btnBekraftaActionPerformed

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
                //new OrderSammanfattning().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBekrafta;
    private javax.swing.JButton btnRedigera;
    private javax.swing.JButton btnSpara;
    private javax.swing.JButton btnTaBort;
    private javax.swing.JButton btnTillbaka;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblExpressavgift;
    private javax.swing.JLabel lblTotalpris;
    private javax.swing.JTable tblOrdersammanfattning;
    private javax.swing.JTextField tfExpress;
    private javax.swing.JTextField tfKundID;
    private javax.swing.JTextField tfOrdernummer;
    private javax.swing.JTextField tfTotalpris;
    // End of variables declaration//GEN-END:variables
}
