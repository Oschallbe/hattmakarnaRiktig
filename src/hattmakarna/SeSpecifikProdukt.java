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
 * @author teami
 */
public class SeSpecifikProdukt extends javax.swing.JFrame {
    private InfDB idb;
    private String inloggadAnvandare;
    private String artikelnummer;

    /**
     * Creates new form SeSpecifikProdukt
     */
    public SeSpecifikProdukt(InfDB idb, String ePost, String artikelnummer) {
        this.idb = idb;
        this.inloggadAnvandare = ePost;
        this.artikelnummer = artikelnummer;
        
        initComponents();
        
        Tabell.setModel(new javax.swing.table.DefaultTableModel(
        new Object [][] {},
        new String [] {
            "Artikelnummer", "Namn", "Pris", "Antal", "Matt", "Material"
        }
    ));
        visaSpecifikProdukt(); 
        
        Tabell.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
             if(evt.getSource() == Tabell) {
            int rad = Tabell.rowAtPoint(evt.getPoint());
            int kolumn = Tabell.columnAtPoint(evt.getPoint());
            
            // Kolumnindex 0 = "Artikelnummer"
            if (kolumn == 0 && rad >= 0) {
                String artikelnummer = Tabell.getValueAt(rad, 0).toString();
                visaMaterialLista(artikelnummer);
                
                // Öppna samma fönster igen med den produkten
                new SeSpecifikProdukt(idb, inloggadAnvandare, artikelnummer).setVisible(true);
                SeSpecifikProdukt.this.dispose(); // Stäng nuvarande fönster
            }
        }
        }
    });
}

    private void visaSpecifikProdukt() {
        try {
            // SQL-fråga för att hämta data från databasen
            String query = "SELECT sp.Artikelnummer, sp.Namn, sp.Pris, sp.Matt, mb.Antal " +
            "FROM StandardProdukt sp " +
            "LEFT JOIN MaterialBestallning mb ON sp.Namn = mb.Namn " +
            "WHERE sp.Artikelnummer = '" + artikelnummer + "'";
            List<HashMap<String, String>> result = idb.fetchRows(query);

            // Hämta JTable:s modell
            DefaultTableModel model = (DefaultTableModel) Tabell.getModel();
            model.setRowCount(0); // Rensa gamla data

            if (result != null) {
                for (HashMap<String, String> row : result) {
                    // Lägg till en rad i JTable
                    model.addRow(new Object[]{
                        row.get("Artikelnummer"),
                        row.get("Namn"),
                        row.get("Pris"),
                        row.get("Antal")!= null ? row.get("Antal") : "0",
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
        "SELECT Material.Namn, Material.Typ, Material.Farg, StandardProdukt_Material.Mängd, Material.Enhet " +
        "FROM Material " +
        "JOIN StandardProdukt_Material " +
        "ON Material.MaterialID = StandardProdukt_Material.MaterialID " +
        "WHERE StandardProdukt_Material.StandardProduktID = " +
        "(SELECT StandardProduktID FROM StandardProdukt WHERE StandardProdukt.Artikelnummer = '" + artikelnummer + "');";
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
        Tabell.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabellMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Tabell);
        Tabell.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(119, 119, 119)
                        .addComponent(btnTillbaka)))
                .addContainerGap())
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(185, Short.MAX_VALUE))
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
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
