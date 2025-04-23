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
import javax.swing.ImageIcon;

/**
 *
 * @author teami
 */
public class SeSpecifikProdukt extends javax.swing.JFrame {
    private InfDB idb;
    private String inloggadAnvandare;
    private String produktID; // Viktigt! Spara riktiga ID:t
    private boolean arStandardprodukt;
    private int antalProdukter;
    
    

    public SeSpecifikProdukt(InfDB idb, String ePost, String produktID, boolean arStandardprodukt, int antalProdukter) {
        this.idb = idb;
        this.inloggadAnvandare = ePost;
        this.produktID = produktID;
        this.arStandardprodukt = arStandardprodukt;
        this.antalProdukter = antalProdukter;
        initComponents();
        
   Tabell.setModel(new javax.swing.table.DefaultTableModel(
            new Object[][] {},
            new String[] { "Artikelnummer", "Namn", "Pris", "Antal", "Mått", "Materiallista" }
        ));

        visaSpecifikProdukt(produktID);
        
 Tabell.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int rad = Tabell.rowAtPoint(evt.getPoint());
                int kolumn = Tabell.columnAtPoint(evt.getPoint());

                if (kolumn == 5 && rad >= 0) {
                    visaMaterialForProdukt(); // Använder nu sparat ID
                }
            }
        });
        

    }
    
    
    private void visaSpecifikProdukt(String artikelnummer) {
    try {
        String fraga = "SELECT * FROM StandardProdukt WHERE StandardProduktID = '" + artikelnummer + "'";
        HashMap<String, String> resultat = idb.fetchRow(fraga);

        if (resultat != null) {
            // Visa data i tabell eller textfält
            DefaultTableModel model = (DefaultTableModel) Tabell.getModel();
            model.setRowCount(0);
            model.addRow(new Object[]{
                resultat.get("Namn"),
                resultat.get("Pris"),
                resultat.get("Text"),
                resultat.get("Typ")
            });
       //bildUrl != null
       // bildVag != null && !bildVag.isEmpty()
            // 🖼️ Visa bilden
String bildVag = resultat.get("BildVag");
if (bildVag != null && !bildVag.isEmpty()) {
    java.net.URL bildUrl = getClass().getClassLoader().getResource(bildVag);
    if (bildUrl != null) {
        ImageIcon ikon = new ImageIcon(bildUrl);
        java.awt.Image scaledImage = ikon.getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
        ikon = new ImageIcon(scaledImage);
        jLabelBild.setIcon(ikon);
    } else {
        jLabelBild.setIcon(null);
        System.err.println("🚫 Kunde inte hitta bild i resurser: " + bildVag);
    }
} else {
    jLabelBild.setIcon(null);
}

   

    }} catch (InfException e) {
        JOptionPane.showMessageDialog(this, "Fel vid hämtning: " + e.getMessage());
    }
}
    
    
    
    
    
/*
 private void visaSpecifikProdukt() {
        try {
            String query;
            if (arStandardprodukt) {
                query = "SELECT Artikelnummer, Namn, Pris, Matt FROM StandardProdukt WHERE StandardProduktID = " + produktID;
            } else {
                query = "SELECT SpecialProduktID AS Artikelnummer, Beskrivning AS Namn, Pris, Matt FROM SpecialProdukt WHERE SpecialProduktID = " + produktID;
            }

            HashMap<String, String> produkt = idb.fetchRow(query);
            DefaultTableModel model = (DefaultTableModel) Tabell.getModel();
            model.setRowCount(0);

            if (produkt != null) {
                model.addRow(new Object[]{
                    produkt.getOrDefault("Artikelnummer", "-"),
                    produkt.getOrDefault("Namn", "-"),
                    produkt.getOrDefault("Pris", "-"),
                    antalProdukter,
                    produkt.getOrDefault("Matt", "-"),
                    "Se material"
                });
            } else {
                JOptionPane.showMessageDialog(this, "Kunde inte hitta produktinformation.");
            }
        } catch (InfException e) {
            JOptionPane.showMessageDialog(this, "Fel vid hämtning av produkt:\n" + e.getMessage(), "Fel", JOptionPane.ERROR_MESSAGE);
        }
    }
*/
    
   private void visaMaterialForProdukt() {
        try {
            List<HashMap<String, String>> material;
            String query;

            if (arStandardprodukt) {
                query = "SELECT m.Namn, m.Typ, m.Farg, spm.Mängd, m.Enhet, spm.Funktion " +
                        "FROM Material m " +
                        "JOIN StandardProdukt_Material spm ON m.MaterialID = spm.MaterialID " +
                        "WHERE spm.StandardProduktID = " + produktID;
            } else {
                query = "SELECT m.Namn, m.Typ, m.Farg, spm.Mängd, m.Enhet, spm.Funktion " +
                        "FROM Material m " +
                        "JOIN SpecialProdukt_Material spm ON m.MaterialID = spm.MaterialID " +
                        "WHERE spm.SpecialProduktID = " + produktID;
            }

            material = idb.fetchRows(query);
            StringBuilder info = new StringBuilder();

            if (material != null && !material.isEmpty()) {
                for (HashMap<String, String> rad : material) {
                    info.append(rad.get("Namn")).append(" – ")
                        .append(rad.get("Typ")).append(" – ")
                        .append(rad.get("Farg")).append(" – Mängd: ")
                        .append(rad.get("Mängd")).append(" ")
                        .append(rad.get("Enhet")).append(" – Funktion: ")
                        .append(rad.get("Funktion")).append("\n");
                }
            } else {
                info.append("Inget material hittades.");
            }

            JOptionPane.showMessageDialog(this, info.toString(), "Material för produkt", JOptionPane.INFORMATION_MESSAGE);
        } catch (InfException e) {
            JOptionPane.showMessageDialog(this, "Fel vid hämtning av material:\n" + e.getMessage(), "Fel", JOptionPane.ERROR_MESSAGE);
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Tabell = new javax.swing.JTable();
        btnTillbaka = new javax.swing.JButton();
        labelBild = new javax.swing.JLabel();
        jLabelBild = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        jLabel1.setText("Specifik produkt");

        Tabell.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Art. no", "Namn", "Pris", "Antal", "Mått", "Materiallista"
            }
        ));
        Tabell.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(Tabell);
        Tabell.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
            }
        });

        labelBild.setText("Bild på produkt:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 637, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(197, 197, 197)
                        .addComponent(btnTillbaka))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(labelBild)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(173, 173, 173)
                .addComponent(jLabelBild)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnTillbaka)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(labelBild)
                .addGap(75, 75, 75)
                .addComponent(jLabelBild)
                .addContainerGap(137, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTillbakaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTillbakaActionPerformed
       //Kod för tillbaka kappen som skickar tillbaka användaren till huvudmenyn. 
        new SeAllaLagerfordaProdukter(idb, inloggadAnvandare).setVisible(true); 
        this.dispose();
    }//GEN-LAST:event_btnTillbakaActionPerformed


    private void TabellMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabellMouseClicked
     
    }//GEN-LAST:event_TabellMouseClicked

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
            java.util.logging.Logger.getLogger(SeSpecifikProdukt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SeSpecifikProdukt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SeSpecifikProdukt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SeSpecifikProdukt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // new SeSpecifikProdukt().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabell;
    private javax.swing.JButton btnTillbaka;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelBild;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelBild;
    // End of variables declaration//GEN-END:variables
}
