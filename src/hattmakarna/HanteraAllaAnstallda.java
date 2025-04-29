/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hattmakarna;

import oru.inf.InfDB;
import oru.inf.InfException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.TableColumn;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;

/**
 *
 * @author mejaa
 */
public class HanteraAllaAnstallda extends javax.swing.JPanel {

    private static InfDB idb;
    private String inloggadAnvandare;

    /**
     * Creates new form AllaAnstallda
     */
    public HanteraAllaAnstallda(InfDB idb, String ePost) {
        initComponents();
        this.idb = idb;
        this.inloggadAnvandare = ePost;
        fyllTabell();
    }

    public void fyllTabell() {
        try {
            //Skapar en array som lagrar kolumnnamnen.
            String kolumnNamn[] = {"AnstalldID", "Fornamn", "Efternamn"};

            //Skapar en DefaultTableModel som håller kolumnnamnen samt sätter antalet rader till noll.
            DefaultTableModel allaAnstallda = new DefaultTableModel(kolumnNamn, 0);

            //Hämtar alla anställdas id och lägger det i Arraylistan "id".
            String selectId = "select AnstalldID from anstalld order by(AnstalldID);";
            ArrayList<String> id = idb.fetchColumn(selectId);

            //Om AnstalldId inte är tom körs en for-each loop som för varje id hämtar id, förnamn och efternamn om det anställda som placeras i Hashmapen "Info".
            if (id != null) {
                for (String ettId : id) {
                    String selectInfo = "select AnstalldID, Fornamn, Efternamn from anstalld where AnstalldID = " + ettId + ";";
                    HashMap<String, String> info = idb.fetchRow(selectInfo);

                    //Skapar en array som håller data för en rad i tabellen.
                    Object[] enRad = new Object[kolumnNamn.length];
                    int index = 0;

                    //for-each loop som går igenom varje kolumn i kolumnNamn där värdet för enKolumn läggs till i enRad;
                    for (String enKolumn : kolumnNamn) {
                        enRad[index++] = info.get(enKolumn);

                    }

                    //enRad läggs till i DefaultTableModel
                    allaAnstallda.addRow(enRad);

                }

                //Jtable sätts med data från DefaultTableModel.
                tblAllaAnstallda.setModel(allaAnstallda);

            }

            tblAllaAnstallda.setAutoResizeMode(tblAllaAnstallda.AUTO_RESIZE_OFF);

            //Sätter storleken på tabellen.
            TableColumn col = tblAllaAnstallda.getColumnModel().getColumn(0); //id.
            col.setPreferredWidth(100);

            col = tblAllaAnstallda.getColumnModel().getColumn(1); //Förnamn.
            col.setPreferredWidth(149);

            col = tblAllaAnstallda.getColumnModel().getColumn(2); //Efternamn.
            col.setPreferredWidth(149);
        } catch (InfException ex) {
            System.out.println(ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnTaBort = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAllaAnstallda = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        btnTaBort.setText("Ta bort anställd");
        btnTaBort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaBortActionPerformed(evt);
            }
        });

        tblAllaAnstallda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        jScrollPane1.setViewportView(tblAllaAnstallda);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Alla anställda");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(158, 158, 158)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTaBort, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(btnTaBort)
                .addContainerGap(28, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTaBortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaBortActionPerformed
        // Tar bort en anställd genom att välja en rad

        try {
            //Hämta raden som har markerats
            int valdRad = tblAllaAnstallda.getSelectedRow();

            //Om ingen rad är vald
            if (valdRad == -1) {
                javax.swing.JOptionPane.showMessageDialog(this, "Markera en anställd för att kunna ta bort");

                return;
            }

            //Hämta anställningsid för den markerade raden.
            String id = tblAllaAnstallda.getValueAt(valdRad, 0).toString();

            //Ett meddelande visas som bekräftelse på om man verkligen vill ta bort.
            int svar = JOptionPane.showConfirmDialog(this, "Vill du verkligen ta bort den anställda?", "Bekräfta", javax.swing.JOptionPane.YES_NO_OPTION);

            //Om man svarar ja så tas den anställda bort från databasen.
            if (svar == javax.swing.JOptionPane.YES_OPTION) {
                String taBort = "delete from anstalld where AnstalldID = " + id + ";";
                idb.delete(taBort);

                //efter den anställda tagits bort uppdateras tabellen.
                fyllTabell();

                javax.swing.JOptionPane.showMessageDialog(this, "Borttagning lyckades!");
            }
        } catch (InfException ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_btnTaBortActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTaBort;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAllaAnstallda;
    // End of variables declaration//GEN-END:variables
}
