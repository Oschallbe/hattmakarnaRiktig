/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hattmakarna;

import oru.inf.InfDB;
import oru.inf.InfException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Elin
 */
public class SeSpecialOrder extends javax.swing.JFrame {

    private InfDB idb;
    private String inloggadAnvandare;
    private String klickatOrderNr;

    /**
     * Creates new form SeSpecialOrder
     */
    public SeSpecialOrder(InfDB idb, String ePost, String klickatOrderNr) {
        initComponents();
        this.idb = idb;
        this.inloggadAnvandare = ePost;
        this.klickatOrderNr = klickatOrderNr;
        fyllTabell();
        fyllLabels();
        //hamtaTotalPris();
    }
    
    //Metod för att fylla tabellerna med info från databasen. 
    public void fyllTabell() {
        try{
            //Skapar en array som lagrar kolumnnamnen.
            String kolumnNamn[] = {"OrderItemID", "Storlek", "Pris", "AntalProdukter", "AnstalldID"};
            
            //Skapar en DefaultTableModel som håller kolumnnamnen samt sätter antalet rader till noll.
            DefaultTableModel allaProdukter = new DefaultTableModel(kolumnNamn, 0);
            
            //Hämtar alla anställdas id och lägger det i Arraylistan "id".
            String selectOid = "select OrderItemID from orderitem where BestallningID =" + klickatOrderNr + " order by(OrderItemID);";
            ArrayList<String> oid = idb.fetchColumn(selectOid);
            
            //Om AnstalldId inte är tom körs en for-each loop som för varje id hämtar id, förnamn och efternamn om det anställda som placeras i Hashmapen "Info".
            if(oid != null){
                for(String ettOid: oid){
                    String selectInfo = "select OrderItemID, AntalProdukter, AnstalldID, SpecialProduktID from orderitem where OrderItemID = " + ettOid + ";"; 
                    HashMap <String, String> info = idb.fetchRow(selectInfo);
                    
                    //Hämtar pris och namn för produkten från tabellen "standardprodukt".
                    String ettProduktID = info.get("SpecialProduktID");
                    String selectProdukt = "select Storlek, Pris from specialprodukt where SpecialProduktID = " + ettProduktID + ";";
                    HashMap<String, String> infoNamnPris = idb.fetchRow(selectProdukt);
                    
                    //AnstalldID byts ut mot för- och efternamn på den anställde. 
                    String aid = info.get("AnstalldID");
                    String selectNamnAnstalld = "select Fornamn, Efternamn from anstalld where AnstalldID = " + aid + ";";
                    HashMap<String, String> anstalldFornamnEfternamn = idb.fetchRow(selectNamnAnstalld);
                    String hopslagetNamn = anstalldFornamnEfternamn.get("Fornamn") + " " + anstalldFornamnEfternamn.get("Efternamn");
                    
                    
                    
                    
                    //Skapar en array som håller data för en rad i tabellen.
                    Object[] enRad = new Object [kolumnNamn.length];
                    enRad[0] = info.get("OrderItemID");
                    enRad[1] = infoNamnPris.get("Storlek");
                    enRad[2] = infoNamnPris.get("Pris");
                    enRad[3] = info.get("AntalProdukter");             
                    enRad[4] = hopslagetNamn;
                   
                    allaProdukter.addRow(enRad);
                    
                }
                
            //Jtable sätts med data från DefaultTableModel.
            tblAllaProdukter.setModel(allaProdukter);
            
            }
            
            tblAllaProdukter.setAutoResizeMode(tblAllaProdukter.AUTO_RESIZE_OFF);
            
            //Sätter storleken på tabellen.
            TableColumn col = tblAllaProdukter.getColumnModel().getColumn(0); //id
            col.setPreferredWidth(100);
            
            col = tblAllaProdukter.getColumnModel().getColumn(1); //namn.
            col.setPreferredWidth(75);
            
            col = tblAllaProdukter.getColumnModel().getColumn(2); //pris.
            col.setPreferredWidth(75);
            
            col = tblAllaProdukter.getColumnModel().getColumn(3); //antal.
            col.setPreferredWidth(75);
            
            col = tblAllaProdukter.getColumnModel().getColumn(4); //anstalldID.
            col.setPreferredWidth(154);
            
            
            //Ändrar rubrikerna i tabellen.
            
            tblAllaProdukter.getColumnModel().getColumn(0).setHeaderValue("Artikelnummer");
            tblAllaProdukter.getColumnModel().getColumn(1).setHeaderValue("Storlek");
            tblAllaProdukter.getColumnModel().getColumn(2).setHeaderValue("Pris");
            tblAllaProdukter.getColumnModel().getColumn(3).setHeaderValue("Antal");
            tblAllaProdukter.getColumnModel().getColumn(4).setHeaderValue("Tilldelad:");
            
        }
        
        catch(InfException ex){
            System.out.println(ex);
        }
    }
//    
//    
//    
//    {
//        try {
//            String kolumnNamn[] = {"Namn", "Pris", "Beskrivning"};
//
//            DefaultTableModel allaProdukter = new DefaultTableModel(kolumnNamn, 0);
//
//            String sql = "SELECT sp.Namn, sp.Pris, sp.Beskrivning "
//                    + "FROM orderitem oi "
//                    + "JOIN standardprodukt sp ON oi.StandardProduktID = sp.StandardProduktID "
//                    + "WHERE oi.OrderID = '" + klickatOrderNr + "';";
//
//            ArrayList<HashMap<String, String>> produkter = idb.fetchRows(sql);
//
//            for (HashMap<String, String> produkt : produkter) {
//                Object[] enRad = {
//                    produkt.get("AntalProdukter"),
//                    produkt.get("Namn"),
//                    produkt.get("Pris"),
//                    produkt.get("Beskrivning")
//                };
//                allaProdukter.addRow(enRad);
//            }
//
//            tblTabell.setModel(allaProdukter);
//
//        } catch (InfException ex) {
//            System.out.println("Fel vid hämtning av produkter: " + ex.getMessage());
//        }
//    }
//

    // Metod som fyller i labels med information om en specifik specialprodukt. 
    public void fyllLabels() {
        //Dessa metoder hämtar info från databasen och fyler textfälten. 
        try {
            String selectOrderNr = "select SpecialProduktID from specialprodukt where SpecialProduktID = '" + klickatOrderNr + "';";
            String orderNr = idb.fetchSingle(selectOrderNr);
            lblOrderNr.setText(orderNr);

            String selectTillverkningstid = "select Tillverkningstid from specialprodukt where SpecialProduktID = '" + klickatOrderNr + "';";
            String tillverkningstid = idb.fetchSingle(selectTillverkningstid);
            lblTillverkningstid2.setText(tillverkningstid);

            String selectKundID = "select KundID from bestallning where BestallningID = '" + klickatOrderNr + "';";
            String kundID = idb.fetchSingle(selectKundID);
            lblKundnummer2.setText(kundID);

            String selectStatus = "select Status from bestallning where BestallningID = '" + klickatOrderNr + "';";
            String status = idb.fetchSingle(selectStatus);
            lblStatus2.setText(status);

        } // Om något går fel vid databasanrop skrivs felet ut i konsolen.
        catch (InfException ex) {
            System.out.println(ex);

        }
    }

    /*public void hamtaTotalPris() {
        try {
            double totalPris = 0.0;
            DefaultTableModel prisTabell = (DefaultTableModel) tblTabell.getModel();

            for (int i = 0; i < prisTabell.getRowCount(); i++) {
                Object ettPris = prisTabell.getValueAt(i, 0); //Ändras till fyra när listan är klar!
                Object ettAntal = prisTabell.getValueAt(i, 1);
                System.out.println(ettPris);
                System.out.println(ettAntal);

                try {
                    double pris = Double.parseDouble(ettPris.toString());
                    int antal = Integer.parseInt(ettAntal.toString());
                    totalPris = totalPris + (pris * antal);
                } catch (NumberFormatException ex) {
                    System.out.println(ex);
                }

            }
            lblPris.setText(String.valueOf(totalPris));
        } catch (NumberFormatException ex) {
            System.out.println(ex);
        }
    }
    /*

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblSpecialorder = new javax.swing.JLabel();
        lblOrderNr = new javax.swing.JLabel();
        lblTillverkningstid = new javax.swing.JLabel();
        lblTillverkningstid2 = new javax.swing.JLabel();
        lblKundnummer = new javax.swing.JLabel();
        lblKundnummer2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAllaProdukter = new javax.swing.JTable();
        btnTillbaka = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        lblStatus2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblSpecialorder.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblSpecialorder.setText("Specialorder:");

        lblOrderNr.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblOrderNr.setText("jLabel1");

        lblTillverkningstid.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTillverkningstid.setText("Tillverkningstid:");

        lblTillverkningstid2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTillverkningstid2.setText("jLabel1");

        lblKundnummer.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKundnummer.setText("Kundnummer: ");

        lblKundnummer2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKundnummer2.setText("jLabel1");

        tblAllaProdukter.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        jScrollPane1.setViewportView(tblAllaProdukter);

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
            }
        });

        lblStatus.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblStatus.setText("Status: ");

        lblStatus2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblStatus2.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(64, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblSpecialorder)
                                .addGap(18, 18, 18)
                                .addComponent(lblOrderNr))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTillverkningstid)
                                    .addComponent(lblStatus)
                                    .addComponent(lblKundnummer))
                                .addGap(34, 34, 34)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblKundnummer2)
                                    .addComponent(lblTillverkningstid2)
                                    .addComponent(lblStatus2))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTillbaka)
                        .addGap(45, 45, 45))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSpecialorder)
                    .addComponent(lblOrderNr)
                    .addComponent(btnTillbaka))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTillverkningstid)
                    .addComponent(lblTillverkningstid2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStatus)
                    .addComponent(lblStatus2))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblKundnummer)
                    .addComponent(lblKundnummer2))
                .addGap(49, 49, 49)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTillbakaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTillbakaActionPerformed
        new SeBestallning(idb, inloggadAnvandare).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnTillbakaActionPerformed

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
            java.util.logging.Logger.getLogger(SeSpecialOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SeSpecialOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SeSpecialOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SeSpecialOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //     new SeSpecialOrder().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTillbaka;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblKundnummer;
    private javax.swing.JLabel lblKundnummer2;
    private javax.swing.JLabel lblOrderNr;
    private javax.swing.JLabel lblSpecialorder;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblStatus2;
    private javax.swing.JLabel lblTillverkningstid;
    private javax.swing.JLabel lblTillverkningstid2;
    private javax.swing.JTable tblAllaProdukter;
    // End of variables declaration//GEN-END:variables
}
