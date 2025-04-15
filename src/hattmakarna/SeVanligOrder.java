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
 * @author lisas
 */
public class SeVanligOrder extends javax.swing.JFrame {
    private InfDB idb; 
    private String inloggadAnvandare; 
    private String klickatOrderNr;
    private String express;

    
    public SeVanligOrder(InfDB idb, String ePost, String klickatOrderNr) {
        initComponents();
        this.idb = idb; 
        this.inloggadAnvandare = ePost;
        this.klickatOrderNr = klickatOrderNr;
        fyllTabell();
        fyllLables();
        hamtaTotalPris();
    }
    
    public void fyllTabell(){
        try{
            //Skapar en array som lagrar kolumnnamnen.
            String kolumnNamn[] = {"OrderItemID", "Namn", "Pris", "AntalProdukter", "AnstalldID"};
            
            //Skapar en DefaultTableModel som håller kolumnnamnen samt sätter antalet rader till noll.
            DefaultTableModel allaProdukter = new DefaultTableModel(kolumnNamn, 0);
            
            //Hämtar alla anställdas id och lägger det i Arraylistan "id".
            String selectOid = "select OrderItemID from orderitem where BestallningID =" + klickatOrderNr + " order by(OrderItemID);";
            ArrayList<String> oid = idb.fetchColumn(selectOid);
            
            //Om AnstalldId inte är tom körs en for-each loop som för varje id hämtar id, förnamn och efternamn om det anställda som placeras i Hashmapen "Info".
            if(oid != null){
                for(String ettOid: oid){
                    String selectInfo = "select OrderItemID, AntalProdukter, AnstalldID, StandardProduktID from orderitem where OrderItemID = " + ettOid + ";"; 
                    HashMap <String, String> info = idb.fetchRow(selectInfo);
                    
                    //Hämtar pris och namn för produkten från tabellen "standardprodukt".
                    String ettProduktID = info.get("StandardProduktID");
                    String selectProdukt = "select Namn, Pris from standardprodukt where StandardProduktID = " + ettProduktID + ";";
                    HashMap<String, String> infoNamnPris = idb.fetchRow(selectProdukt);
                    
                    //AnstalldID byts ut mot för- och efternamn på den anställde. 
                    String aid = info.get("AnstalldID");
                    String selectNamnAnstalld = "select Fornamn, Efternamn from anstalld where AnstalldID = " + aid + ";";
                    HashMap<String, String> anstalldFornamnEfternamn = idb.fetchRow(selectNamnAnstalld);
                    String hopslagetNamn = anstalldFornamnEfternamn.get("Fornamn") + " " + anstalldFornamnEfternamn.get("Efternamn");
                    
                    
                    
                    
                    //Skapar en array som håller data för en rad i tabellen.
                    Object[] enRad = new Object [kolumnNamn.length];
                    enRad[0] = info.get("OrderItemID");
                    enRad[1] = infoNamnPris.get("Namn");
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
            col.setPreferredWidth(200);
            
            col = tblAllaProdukter.getColumnModel().getColumn(2); //pris.
            col.setPreferredWidth(75);
            
            col = tblAllaProdukter.getColumnModel().getColumn(3); //antal.
            col.setPreferredWidth(75);
            
            col = tblAllaProdukter.getColumnModel().getColumn(4); //anstalldID.
            col.setPreferredWidth(154);
            
            
            //Ändrar rubrikerna i tabellen.
            
            tblAllaProdukter.getColumnModel().getColumn(0).setHeaderValue("Artikelnummer");
            tblAllaProdukter.getColumnModel().getColumn(1).setHeaderValue("Namn");
            tblAllaProdukter.getColumnModel().getColumn(2).setHeaderValue("Pris");
            tblAllaProdukter.getColumnModel().getColumn(3).setHeaderValue("Antal");
            tblAllaProdukter.getColumnModel().getColumn(4).setHeaderValue("Tilldelad:");
            
        }
        
        catch(InfException ex){
            System.out.println(ex);
        }
    }

    public void hamtaTotalPris(){        
        try{
            
            //Sätter totalpris till 0 och hämtar Jtable och lägger i pristabell.
            double totalPris = 0.0;
            DefaultTableModel prisTabell = (DefaultTableModel) tblAllaProdukter.getModel();
            
            //Loopar igenom pristabellen och hämtar och lagrar pris och antal för varje rad.
            for(int i = 0; i < prisTabell.getRowCount(); i++) {
                Object ettPris = prisTabell.getValueAt(i, 2); 
                Object ettAntal = prisTabell.getValueAt(i, 3);
               
                //Gör om pris och antal till String och beräknar totalpris.
                try {
                    double pris = Double.parseDouble(ettPris.toString());
                    int antal = Integer.parseInt(ettAntal.toString());
                    totalPris = totalPris + (pris * antal);
                }
                
                catch (NumberFormatException ex) {
                    System.out.println(ex);           
                }
                
            
            }
            
            /*if(express.equals("Ja")){
                totalPris = totalPris * 1.2;
            }
            */
            //Sätter label till det totalpris som beräknats.
            lblPris.setText(String.valueOf(totalPris));
        }
        catch (NumberFormatException ex){
            System.out.println(ex);
        }    
    }        
           
    
    public void fyllLables(){
        
        try {
            //Fyller labeles med information från den specifika ordern man klickat på.
            String selectOrderNr = "select BestallningID from bestallning where BestallningID = '" + klickatOrderNr + "';";
            String orderNr = idb.fetchSingle(selectOrderNr);
            lblOrderNr.setText(orderNr);
            
            String selectKund = "select KundID from bestallning where BestallningID = '" + klickatOrderNr + "';";
            String kundNr = idb.fetchSingle(selectKund);
            lblKundNr.setText(kundNr);
            
            String selectDatum = "select Datum from bestallning where BestallningID = '" + klickatOrderNr + "';";
            String datum = idb.fetchSingle(selectDatum);
            lblDatum.setText(datum);
            
            String selectStatus = "select Status from bestallning where BestallningID = '" + klickatOrderNr + "';";
            String status = idb.fetchSingle(selectStatus);
            lblStatus.setText(status);
            
            String selectExpress = "select Expressbestallning from bestallning where BestallningID = '" + klickatOrderNr + "';";
            express = idb.fetchSingle(selectExpress);
            
            if(express.equals("1")){
                express = "Ja";
            }
            
            else{
                express = "Nej";
            }
            lblExpress.setText(express);
                 
    }
        
        catch(InfException ex){
            System.out.println(ex); 
            
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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblOrderNr = new javax.swing.JLabel();
        lblKundNr = new javax.swing.JLabel();
        lblDatum = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        lblPris = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAllaProdukter = new javax.swing.JTable();
        btnTillbaka = new javax.swing.JButton();
        btnAtaProdukt = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        lblExpress = new javax.swing.JLabel();
        btnFraktsedel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Order:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Kund:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Datum:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Status:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Totalpris:");

        lblOrderNr.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblOrderNr.setText("jLabel6");

        lblKundNr.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblKundNr.setText("jLabel7");

        lblDatum.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblDatum.setText("jLabel8");

        lblStatus.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblStatus.setText("jLabel9");

        lblPris.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPris.setText("jLabel10");

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
        tblAllaProdukter.setEnabled(false);
        jScrollPane1.setViewportView(tblAllaProdukter);

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
            }
        });

        btnAtaProdukt.setText("Åta produkt");
        btnAtaProdukt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtaProduktActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Expressleverans:");

        lblExpress.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblExpress.setText("jLabel7");

        btnFraktsedel.setText("Skapa fraktsedel");
        btnFraktsedel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFraktsedelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnFraktsedel)
                        .addGap(18, 18, 18)
                        .addComponent(btnTillbaka))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblDatum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblPris, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblOrderNr, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblKundNr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(lblExpress)))
                    .addComponent(btnAtaProdukt))
                .addContainerGap(50, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblOrderNr))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblKundNr))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblDatum))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblStatus))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblPris))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblExpress))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addComponent(btnAtaProdukt)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTillbaka)
                    .addComponent(btnFraktsedel))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTillbakaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTillbakaActionPerformed
        new SeBestallning(idb, inloggadAnvandare).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnTillbakaActionPerformed

    private void btnAtaProduktActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtaProduktActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAtaProduktActionPerformed

    private void btnFraktsedelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFraktsedelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFraktsedelActionPerformed

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
            java.util.logging.Logger.getLogger(SeVanligOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SeVanligOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SeVanligOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SeVanligOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               // new SeVanligOrder().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtaProdukt;
    private javax.swing.JButton btnFraktsedel;
    private javax.swing.JButton btnTillbaka;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDatum;
    private javax.swing.JLabel lblExpress;
    private javax.swing.JLabel lblKundNr;
    private javax.swing.JLabel lblOrderNr;
    private javax.swing.JLabel lblPris;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tblAllaProdukter;
    // End of variables declaration//GEN-END:variables
}
