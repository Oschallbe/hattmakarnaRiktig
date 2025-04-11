/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hattmakarna;

import java.util.HashMap;
import java.util.List;
import oru.inf.InfDB;
import oru.inf.InfException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 *
 * @author oscar
 */
public class SeAllaProdukter extends javax.swing.JFrame {
    
    private InfDB idb;
    private String inloggadAnvandare; 

    public SeAllaProdukter(InfDB idb, String ePost) {
        this.idb = idb;
        this.inloggadAnvandare = ePost;
        initComponents();
        seLagerfordaProdukter();
        
        
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int rad = jTable1.rowAtPoint(evt.getPoint());
            int kolumn = jTable1.columnAtPoint(evt.getPoint());

            // Kolumn 4 = "Materiallista"
            if (kolumn == 4 && rad >= 0) {
                String artikelnummer = jTable1.getValueAt(rad, 0).toString();
                visaMaterialLista(artikelnummer);}}});
        
        
        
    }
    
    private void seLagerfordaProdukter() {
        try {
            // SQL-fråga för att hämta data från databasen
            String query = "SELECT Artikelnummer, Namn, Pris, Matt FROM StandardProdukt";
            List<HashMap<String, String>> result = idb.fetchRows(query);

            // Hämta JTable:s modell
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0); // Rensa gamla data

            if (result != null) {
                for (HashMap<String, String> row : result) {
                    // Lägg till en rad i JTable
                    model.addRow(new Object[]{
                        row.get("Artikelnummer"),
                        row.get("Namn"),
                        row.get("Pris"),
                        row.get("Matt"),
                        "Se material",
                    });
                }
            } else {
                System.out.println("Ingen data hittades i tabellen.");
            }
        } catch (InfException e) {
            System.out.println("Fel vid hämtning av data: " + e.getMessage());
        }
    }
    
    private void visaMaterialLista(String Artikelnummer) {
    try {
        // SQL-fråga: hämta material kopplat till artikelnumret
        String sql = "SELECT M.Namn, M.Typ, M.Farg " +
             "FROM Material M " +
             "JOIN StandardProdukt sp ON M.StandardProduktid = sp.StandardProduktID " +
             "WHERE sp.Artikelnummer = '" + Artikelnummer + "'";

        List<HashMap<String, String>> material = idb.fetchRows(sql);

        StringBuilder info = new StringBuilder();

        if (material != null && !material.isEmpty()) {
            for (HashMap<String, String> rad : material) {
                info.append(rad.get("Namn"))
                    .append(" – ")
                    .append(rad.get("Typ"))
                    .append(" – ")
                    .append(rad.get("Farg"))
                    .append("\n");
            }
        } else {
            info.append("Inget material hittades.");
        }

        JOptionPane.showMessageDialog(this, info.toString(), "Materiallista", JOptionPane.INFORMATION_MESSAGE);

    } catch (InfException e) {
        JOptionPane.showMessageDialog(this, "Fel vid hämtning av material:\n" + e.getMessage(), "Fel", JOptionPane.ERROR_MESSAGE);
    }
}



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnSok = new javax.swing.JButton();
        btnLaggTill = new javax.swing.JButton();
        btnTaBort = new javax.swing.JButton();
        btnTillbaka = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtSok = new java.awt.TextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, "", null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Art. no", "Namn", "Pris", "Huvudmått", "Materiallista"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        btnSok.setText("Sök");
        btnSok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSokActionPerformed(evt);
            }
        });

        btnLaggTill.setText("Lägg till");

        btnTaBort.setText("Ta bort");

        btnTillbaka.setText("Tillbaka");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Produktlista");

        jLabel2.setText("Sök produkt efter artikelnummer");

        txtSok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSokActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLaggTill)
                        .addGap(50, 50, 50)
                        .addComponent(btnTillbaka)
                        .addGap(46, 46, 46))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnTaBort)
                .addGap(98, 98, 98))
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtSok, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSok)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnTillbaka)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSok)
                            .addComponent(txtSok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTaBort))
                    .addComponent(btnLaggTill, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSokActionPerformed
    
    String artikelnummer = txtSok.getText().trim();

    try {
        String sql = "SELECT Artikelnummer, Namn, Pris, Matt FROM StandardProdukt WHERE Artikelnummer = '" + artikelnummer + "'";
        List<HashMap<String, String>> produkter = idb.fetchRows(sql);

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Rensa gammal data

        if (produkter != null && !produkter.isEmpty()) {
            for (HashMap<String, String> produkt : produkter) {
                model.addRow(new Object[]{
                    produkt.get("Artikelnummer"),
                    produkt.get("Namn"),
                    produkt.get("Pris"),
                    produkt.get("Matt"),
                    "Se material"
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Ingen produkt hittades med det artikelnumret.");
        }

    } catch (InfException e) {
        JOptionPane.showMessageDialog(this, "Fel vid sökning: " + e.getMessage());
    }
    
    }//GEN-LAST:event_btnSokActionPerformed

    private void txtSokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSokActionPerformed


    
    
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLaggTill;
    private javax.swing.JButton btnSok;
    private javax.swing.JButton btnTaBort;
    private javax.swing.JButton btnTillbaka;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private java.awt.TextField txtSok;
    // End of variables declaration//GEN-END:variables
}
