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
        /*String sql = "SELECT M.Namn, M.Typ, M.Farg " +
             "FROM Material M " +
             "JOIN StandardProdukt sp ON M.StandardProduktid = sp.StandardProduktID " +
             "WHERE sp.Artikelnummer = '" + Artikelnummer + "'";

        List<HashMap<String, String>> material = idb.fetchRows(sql);
        */
        String fragaMateriallista = 
        "SELECT Material.Namn, Material.Typ, Material.Farg " +
        "FROM Material " +
        "JOIN StandardProdukt_Material " +
        "ON Material.MaterialID = StandardProdukt_Material.MaterialID " +
        "WHERE StandardProdukt_Material.StandardProduktID = " +
        "(SELECT StandardProduktID FROM StandardProdukt WHERE StandardProdukt.Artikelnummer = '" + Artikelnummer + "');";
        List<HashMap<String, String>> material = idb.fetchRows(fragaMateriallista);
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
                "Art. no", "Namn", "Pris", "Mått", "Materiallista"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        btnSok.setText("Sök");
        btnSok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSokActionPerformed(evt);
            }
        });

        btnLaggTill.setText("Lägg till lagerförd hatt");
        btnLaggTill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaggTillActionPerformed(evt);
            }
        });

        btnTaBort.setText("Ta bort");

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
            }
        });

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnLaggTill)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(txtSok, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(btnSok)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTaBort, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnTillbaka, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(34, 34, 34))))
            .addGroup(layout.createSequentialGroup()
                .addGap(206, 206, 206)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtSok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnSok)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(btnTillbaka)))
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTaBort)
                    .addComponent(btnLaggTill))
                .addGap(26, 26, 26))
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

    private void btnLaggTillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaggTillActionPerformed
        new LaggTillProdukt(idb, inloggadAnvandare).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnLaggTillActionPerformed

    private void btnTillbakaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTillbakaActionPerformed
        //Kod för tillbaka kappen som skickar tillbaka användaren till huvudmenyn. 
        new HuvudMeny(idb, inloggadAnvandare).setVisible(true); 
        this.dispose();
    }//GEN-LAST:event_btnTillbakaActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked


    
    
    
    

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
