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

/**
 *
 * @author oscar
 */
public class SeInfoSpecialprodukt extends javax.swing.JFrame {

    private InfDB idb;
    private String inloggadAnvandare;
    private String produktID;
    private int antalProdukter;

    public SeInfoSpecialprodukt(InfDB idb, String ePost, String produktID, int antalProdukter) {
        initComponents();
        this.idb = idb;
        this.inloggadAnvandare = ePost;
        this.produktID = produktID;
        this.antalProdukter = antalProdukter;

        Tabell.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"Beskrivning", "Pris", "Text", "Tillverkningstid", "Mått", "Höjd", "Bredd", "Djup", "Materiallista"}
        ));

        visaSpecifikProdukt(produktID);

        Tabell.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int rad = Tabell.rowAtPoint(evt.getPoint());
                int kolumn = Tabell.columnAtPoint(evt.getPoint());
                if (kolumn == 8 && rad >= 0) {
                    visaMaterialForProdukt();
                }
            }
        });
    }

    private void visaSpecifikProdukt(String produktID) {
        try {

            String query = "SELECT Beskrivning, Pris, Text, Tillverkningstid, Matt, Hojd, Bredd, Djup "
                    + "FROM SpecialProdukt WHERE SpecialProduktID = " + produktID;

            HashMap<String, String> produkt = idb.fetchRow(query);
            DefaultTableModel model = (DefaultTableModel) Tabell.getModel();
            model.setRowCount(0);

            if (produkt != null) {
                model.addRow(new Object[]{
                    produkt.get("Beskrivning"),
                    produkt.get("Pris"),
                    produkt.get("Text"),
                    produkt.get("Tillverkningstid"),
                    produkt.get("Matt"),
                    produkt.get("Hojd"),
                    produkt.get("Bredd"),
                    produkt.get("Djup"),
                    "Se material"
                });
            } else {
                JOptionPane.showMessageDialog(this, "Kunde inte hitta produktinformation.");
            }
        } catch (InfException e) {
            JOptionPane.showMessageDialog(this, "Fel vid hämtning av produkt:\n" + e.getMessage(), "Fel", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void visaMaterialForProdukt() {
        try {
            String query = "SELECT m.Namn, m.Typ, m.Farg, spm.Mängd, m.Enhet, spm.Funktion "
                    + "FROM Material m "
                    + "JOIN SpecialProdukt_Material spm ON m.MaterialID = spm.MaterialID "
                    + "WHERE spm.SpecialProduktID = " + produktID;

            List<HashMap<String, String>> material = idb.fetchRows(query);
            StringBuilder info = new StringBuilder();

            if (material != null && !material.isEmpty()) {
                for (HashMap<String, String> rad : material) {
                    String namn = rad.get("Namn");
                    String typ = rad.get("Typ");
                    String farg = rad.get("Farg");
                    String mangd = rad.get("Mängd") != null ? rad.get("Mängd") : "-";
                    String enhet = rad.get("Enhet") != null ? rad.get("Enhet") : "";
                    String funktion = rad.get("Funktion") != null ? rad.get("Funktion") : "";

                    info.append(namn).append(" – ")
                            .append(typ).append(" – ")
                            .append(farg).append(" – Mängd: ")
                            .append(mangd).append(" ").append(enhet)
                            .append(" – Funktion: ").append(funktion)
                            .append("\n");
                }
            } else {
                info.append("Inget material hittades.");
            }

            JOptionPane.showMessageDialog(this, info.toString(), "Material för produkt", JOptionPane.INFORMATION_MESSAGE);

        } catch (InfException e) {
            JOptionPane.showMessageDialog(this, "Fel vid hämtning av material" + e.getMessage(), "Fel", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        Tabell = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        Tabell.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Beskrivning", "Pris", "Text", "Tillverkningstid", "Mått", "Höjd", "Bredd", "Djup", "Materiallista"
            }
        ));
        Tabell.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(Tabell);
        Tabell.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Specifik produkt");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 736, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(304, 304, 304)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(SeInfoSpecialprodukt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SeInfoSpecialprodukt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SeInfoSpecialprodukt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SeInfoSpecialprodukt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new SeInfoSpecialprodukt().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Tabell;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
