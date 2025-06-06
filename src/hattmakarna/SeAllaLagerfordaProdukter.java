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
public class SeAllaLagerfordaProdukter extends javax.swing.JPanel {

    private InfDB idb;
    private String inloggadAnvandare;

    public SeAllaLagerfordaProdukter(InfDB idb, String ePost) {
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
                    visaMaterialLista(artikelnummer);
                }
            }
        });

    }

    private void seLagerfordaProdukter() {
        try {
            // SQL-fråga för att hämta data från databasen
            String query = "SELECT Artikelnummer, Namn, Pris, Matt, StandardProduktID FROM StandardProdukt WHERE Aktiv = TRUE";

            List<HashMap<String, String>> result = idb.fetchRows(query);

            // Hämta JTable:s modell
            String[] kolumner = {"Art. no", "Namn", "Pris", "Mått", "Materiallista", "ProduktID"};
            DefaultTableModel model = new DefaultTableModel(kolumner, 0);
            jTable1.setModel(model);

            if (result != null) {
                for (HashMap<String, String> row : result) {
                    // Lägg till en rad i JTable
                    model.addRow(new Object[]{
                        row.get("Artikelnummer"),
                        row.get("Namn"),
                        row.get("Pris"),
                        row.get("Matt"),
                        "Se material",
                        row.get("StandardProduktID") // extra kolumn för btnSeProdukt
                    });

                    jTable1.getColumnModel().getColumn(5).setMinWidth(0);
                    jTable1.getColumnModel().getColumn(5).setMaxWidth(0);
                    jTable1.getColumnModel().getColumn(5).setWidth(0);
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

            String fragaMateriallista
                    = "SELECT Material.Namn, Material.Typ, Material.Farg, StandardProdukt_Material.Mängd, Material.Enhet "
                    + "FROM Material "
                    + "JOIN StandardProdukt_Material "
                    + "ON Material.MaterialID = StandardProdukt_Material.MaterialID "
                    + "WHERE StandardProdukt_Material.StandardProduktID = "
                    + "(SELECT StandardProduktID FROM StandardProdukt WHERE StandardProdukt.Artikelnummer = '" + Artikelnummer + "');";
            List<HashMap<String, String>> material = idb.fetchRows(fragaMateriallista);
            StringBuilder info = new StringBuilder();

            if (material != null && !material.isEmpty()) {
                for (HashMap<String, String> rad : material) {
                    info.append(rad.get("Namn"))
                            .append(" – ")
                            .append(rad.get("Typ"))
                            .append(" – ")
                            .append(rad.get("Farg"))
                            .append(" – ")
                            .append("Mängd: ")
                            .append(rad.get("Mängd"))
                            .append(" ")
                            .append(rad.get("Enhet"))
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

        txtSok = new java.awt.TextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnSok = new javax.swing.JButton();
        btnTaBort = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        btnSeProdukt = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        txtSok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSokActionPerformed(evt);
            }
        });

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

        btnTaBort.setText("Ta bort produkt");
        btnTaBort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaBortActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Produktlista");

        jLabel2.setText("Sök produkt efter artikelnummer");

        btnSeProdukt.setText("Se information om produkt");
        btnSeProdukt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeProduktActionPerformed(evt);
            }
        });

        jButton1.setText("Rensa filtrering");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1130, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2))
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(txtSok, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(btnSok)
                .addGap(30, 30, 30)
                .addComponent(jButton1))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(btnSeProdukt)
                .addGap(784, 784, 784)
                .addComponent(btnTaBort))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel2)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(txtSok, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSok)
                        .addComponent(jButton1)))
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSeProdukt)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(btnTaBort))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSokActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSokActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnSokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSokActionPerformed
        String artikelnummer = txtSok.getText().trim();

        try {
            String sql = "SELECT Artikelnummer, Namn, Pris, Matt FROM StandardProdukt "
                    + "WHERE Artikelnummer = '" + artikelnummer + "' AND Aktiv = TRUE";

            List<HashMap<String, String>> produkter = idb.fetchRows(sql);

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0); //Rensa gammal data

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
                JOptionPane.showMessageDialog(this, "Ingen aktiv produkt hittades med det artikelnumret.");
            }

        } catch (InfException e) {
            JOptionPane.showMessageDialog(this, "Fel vid sökning: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSokActionPerformed

    private void btnTaBortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaBortActionPerformed
        int rad = jTable1.getSelectedRow();
        if (rad == -1) {
            JOptionPane.showMessageDialog(this, "Välj en produkt att ta bort.");
            return;
        }

        String artikelnummer = jTable1.getValueAt(rad, 0).toString();

        int svar = JOptionPane.showConfirmDialog(this, "Är du säker på att du vill ta bort produkten?",
                "Bekräfta", JOptionPane.YES_NO_OPTION);

        if (svar == JOptionPane.YES_OPTION) {
            try {
                String sql = "UPDATE StandardProdukt SET Aktiv = FALSE WHERE Artikelnummer = '" + artikelnummer + "'";
                idb.update(sql);
                JOptionPane.showMessageDialog(this, "Produkten togs bort.");
                seLagerfordaProdukter(); //ladda om tabellen
            } catch (InfException e) {
                JOptionPane.showMessageDialog(this, "Fel vid borttagning: " + e.getMessage());

            }
        }


    }//GEN-LAST:event_btnTaBortActionPerformed

    private void btnSeProduktActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeProduktActionPerformed
        try {
            int valdRad = jTable1.getSelectedRow();

            if (valdRad == -1) {
                JOptionPane.showMessageDialog(this, "Markera en rad för att se produkten.");
                return;
            }

            // Hämta StandardProduktID från kolumn 5 (dold)
            String produktID = jTable1.getValueAt(valdRad, 5).toString();

            if (produktID == null || produktID.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingen produktinformation hittades för vald rad.");
                return;
            }

            // Skicka vidare StandardProduktID direkt
            int antal = 1;
            new SeInfoStandardprodukt(idb, inloggadAnvandare, produktID, antal).setVisible(true);

        } catch (Exception ex) {
            System.out.println("Fel i jButton1ActionPerformed: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnSeProduktActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Återställ tabellen
        seLagerfordaProdukter();

        // Töm textfält
        txtSok.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSeProdukt;
    private javax.swing.JButton btnSok;
    private javax.swing.JButton btnTaBort;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private java.awt.TextField txtSok;
    // End of variables declaration//GEN-END:variables
}
