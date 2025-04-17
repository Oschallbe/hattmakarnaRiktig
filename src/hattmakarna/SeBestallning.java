/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hattmakarna;
import hattmakarna.SeVanligOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import oru.inf.InfDB;
import oru.inf.InfException;

/**
 *
 * @author linodeluca
 */
public class SeBestallning extends javax.swing.JFrame {
private String inloggadAnvandare;
private InfDB idb;
private String klickatOrderNr;
    /**
     * Creates new form SeBestallning
     */
    public SeBestallning(InfDB idb, String ePost) {
        initComponents();
        this.idb = idb;
        this.inloggadAnvandare = ePost;
        String fraga = "select BestallningID, Status, Datum, Expressbestallning, TotalPris, Typ, KundID from Bestallning;";
        fyllTable(fraga);
    }
    
    public void fyllTable(String fraga){
        try {
            List<HashMap<String, String>> result = idb.fetchRows(fraga);

            DefaultTableModel model = (DefaultTableModel) BestallningsLista.getModel();
            model.setRowCount(0); // Rensa gamla data
            if (result != null) {
                for (HashMap<String, String> row : result) {
                    String expressHamtning = row.get("Expressbestallning");
                    String expressOmvandling;
                    double totalPris = Double.parseDouble(row.get("TotalPris"));

                    if(expressHamtning.equals("1")){
                        expressOmvandling = "Ja";
                        totalPris = totalPris * 1.2;
                    }
                    else{
                        expressOmvandling = "Nej";
                    }
                    // Lägg till en rad i JTable
                    model.addRow(new Object[]{
                        row.get("Typ"),
                        row.get("BestallningID"),
                        row.get("KundID"),
                        row.get("Status"),
                        totalPris,
                        row.get("Datum"),
                        expressOmvandling
                    });
                }
            } 
            else {
                System.out.println("Ingen data hittades i tabellen.");
            }

        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Fel vid hämtning av data: " + e.getMessage());
        }
    }
    
   
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        BestallningsLista = new javax.swing.JTable();
        tillbaka = new javax.swing.JButton();
        btnSeOrder = new javax.swing.JButton();
        cbStatus = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        txtDatum = new javax.swing.JTextField();
        txtKund = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnSökKund = new javax.swing.JButton();
        btnSökDatum = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        BestallningsLista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Ordertyp", "Ordernummer", "Kundnummer", "Status", "Pris", "Datum", "Expressbestallning"
            }
        ));
        jScrollPane1.setViewportView(BestallningsLista);

        tillbaka.setText("Tillbaka");
        tillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tillbakaActionPerformed(evt);
            }
        });

        btnSeOrder.setText("Se order");
        btnSeOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeOrderActionPerformed(evt);
            }
        });

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Status:", "Packas", "Skickad", "Produktion pågår", "Under behandling", "Levererad" }));
        cbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStatusActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Filtrera efter: ");

        txtDatum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDatumActionPerformed(evt);
            }
        });

        jLabel2.setText("KundID eller för- och efternamn: ");

        jLabel3.setText("Datum:");

        btnSökKund.setText("Sök");
        btnSökKund.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSökKundActionPerformed(evt);
            }
        });

        btnSökDatum.setText("Sök");
        btnSökDatum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSökDatumActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSeOrder)
                        .addGap(47, 47, 47)
                        .addComponent(tillbaka)
                        .addGap(26, 26, 26))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtKund, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDatum, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnSökKund, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(187, 187, 187)
                .addComponent(btnSökDatum, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tillbaka)
                    .addComponent(btnSeOrder))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(txtKund, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDatum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSökKund)
                    .addComponent(btnSökDatum))
                .addGap(48, 48, 48))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tillbakaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tillbakaActionPerformed
        // TODO add your handling code here:
        new HuvudMeny(idb,inloggadAnvandare).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tillbakaActionPerformed

    private void btnSeOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeOrderActionPerformed
        
        try{
            //Hämta raden som har markerats.
            int valdRad = BestallningsLista.getSelectedRow();
            
            //Om en rad inte är vald, visa felmeddelande.
            if(valdRad == -1){
                javax.swing.JOptionPane.showMessageDialog(this, "Markera en beställningsrad för att se ordern.");
                return;
            }
            //Hämtar och lagrar orderns typ från den valda raden i Jtable.
            String ordernsTyp = BestallningsLista.getValueAt(valdRad,0).toString();
            String typ = ordernsTyp;
           
            
            //Hämta Ordernr för den markerade raden och lagrar i klickatOrderNr så det kan skickas vidare till nästa klass.
            String Oid = BestallningsLista.getValueAt(valdRad,1).toString();
            klickatOrderNr = Oid;
            
            //Om det är en standardbeställning skickas man vidare till en gränssnitt som visar en "vanlig order" och detta gränssnitt stängs ner.
            if(typ.contains("Standard")){
                     
                new SeVanligOrder(idb, inloggadAnvandare, klickatOrderNr).setVisible(true);
                this.dispose();
            }
            
            //Om beställningen är en specialbeställning visas gränssnittet för denna typ av beställning.
            else{
                new SeSpecialOrder(idb, inloggadAnvandare, klickatOrderNr).setVisible(true);
                this.dispose();
                
            }
    
        }
        catch(NumberFormatException ex){
            System.out.println(ex);
        }
    }//GEN-LAST:event_btnSeOrderActionPerformed

    private void cbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatusActionPerformed
        String valdStatus = cbStatus.getSelectedItem().toString();

    try {
        String query = "SELECT b.Typ, b.BestallningID, b.KundID, b.Datum, b.Status, b.TotalPris, b.Expressbestallning " +
                       "FROM Bestallning b " +
                       "JOIN Kund k ON k.KundID = b.KundID";

        if (!valdStatus.equalsIgnoreCase("Status")) {
            query += " WHERE b.Status = '" + valdStatus.replace("'", "''") + "'";
        }

        List<HashMap<String, String>> bestallningar = idb.fetchRows(query);

        DefaultTableModel model = (DefaultTableModel) BestallningsLista.getModel();
        model.setRowCount(0); // Rensa tidigare data

        for (HashMap<String, String> rad : bestallningar) {
            model.addRow(new Object[]{
                rad.get("Typ"),
                rad.get("BestallningID"),
                rad.get("KundID"),
                rad.get("Status"),
                rad.get("TotalPris"),
                rad.get("Datum"),
                rad.get("Expressbestallning")
            });
        }

    } catch (InfException e) {
        JOptionPane.showMessageDialog(this, "Fel vid filtrering: " + e.getMessage());
    }
    }//GEN-LAST:event_cbStatusActionPerformed

    private void txtDatumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDatumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDatumActionPerformed

    private void btnSökKundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSökKundActionPerformed
        // 
        String kund = txtKund.getText().trim(); 
        
       if (kund.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Du måste ange KundID eller namn.");
        return;
    }

    try {
        String query = "";

        if (kund.contains(" ")) {
            // Förnamn + Efternamn
            String[] delar = kund.trim().split("\\s+", 2);
            String fornamn = delar[0].toLowerCase().replace("'", "''");
            String efternamn = delar[1].toLowerCase().replace("'", "''");

            query = "SELECT k.KundID, k.Fornamn, k.Efternamn, b.BestallningID, b.Typ, b.Datum, b.Status, b.TotalPris, b.Expressbestallning " +
                    "FROM Kund k " +
                    "JOIN Bestallning b ON k.KundID = b.KundID " +
                    "WHERE LOWER(k.Fornamn) = '" + fornamn + "' " +
                    "AND LOWER(k.Efternamn) = '" + efternamn + "'";
        } else {
            // KundID
            try {
                int kundID = Integer.parseInt(kund);
                query = "SELECT k.KundID, k.Fornamn, k.Efternamn, b.BestallningID, b.Typ , b.Datum, b.Status, b.TotalPris, b.Expressbestallning " +
                        "FROM Kund k " +
                        "JOIN Bestallning b ON k.KundID = b.KundID " +
                        "WHERE k.KundID = " + kundID;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ogiltigt KundID. Ange ett numeriskt värde eller namn.");
                return;
            }
        }

        List<HashMap<String, String>> kunder = idb.fetchRows(query);

        if (kunder == null || kunder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingen kund hittades.");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) BestallningsLista.getModel();
        model.setRowCount(0); // Rensa gammal data

        for (HashMap<String, String> kundData : kunder) {
            model.addRow(new Object[]{
                kundData.get("Typ"),
                kundData.get("BestallningID"),
                kundData.get("KundID"),
                kundData.get("Status"),
                kundData.get("TotalPris"),
                kundData.get("Datum"),
                kundData.get("Expressbestallning")
            });
        }

    } catch (InfException e) {
        JOptionPane.showMessageDialog(this, "Fel vid sökning: " + e.getMessage());
    }
    }//GEN-LAST:event_btnSökKundActionPerformed

    private void btnSökDatumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSökDatumActionPerformed
        String datum = txtDatum.getText().trim();

    // Validera att användaren har skrivit ett korrekt datum
    if (!Validering.valideringDatum(datum)) {
        JOptionPane.showMessageDialog(this, "Datumet måste vara i formatet YYYY-MM-DD.");
        return;
    }

    try {
        String query = "SELECT k.KundID, b.BestallningID, b.Typ, b.Datum, b.Status, b.TotalPris, b.Expressbestallning " +
                       "FROM Kund k " +
                       "JOIN Bestallning b ON k.KundID = b.KundID " +
                       "WHERE b.Datum = '" + datum.replace("'", "''") + "'";

        List<HashMap<String, String>> resultat = idb.fetchRows(query);

        if (resultat == null || resultat.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inga beställningar hittades för det angivna datumet.");
            return;
        }

        // Uppdatera tabellen
        DefaultTableModel model = (DefaultTableModel) BestallningsLista.getModel();
        model.setRowCount(0); // Rensa tidigare data

        for (HashMap<String, String> rad : resultat) {
            model.addRow(new Object[]{
                rad.get("Typ"),
                rad.get("BestallningID"),
                rad.get("KundID"),
                rad.get("Status"),
                rad.get("TotalPris"),
                rad.get("Datum"),
                rad.get("Expressbestallning")
            });
        }

    } catch (InfException e) {
        JOptionPane.showMessageDialog(this, "Fel vid filtrering: " + e.getMessage());
    }
    }//GEN-LAST:event_btnSökDatumActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable BestallningsLista;
    private javax.swing.JButton btnSeOrder;
    private javax.swing.JButton btnSökDatum;
    private javax.swing.JButton btnSökKund;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton tillbaka;
    private javax.swing.JTextField txtDatum;
    private javax.swing.JTextField txtKund;
    // End of variables declaration//GEN-END:variables
}
