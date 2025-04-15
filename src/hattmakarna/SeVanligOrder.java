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

    /**
     * Creates new form SeVanligOrder
     */
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
            String kolumnNamn[] = {"OrderItemID", "AntalProdukter", "AnstalldID", "Namn", "Pris"}; //OBS Namn och Pris ska läggas till
            
            //Skapar en DefaultTableModel som håller kolumnnamnen samt sätter antalet rader till noll.
            DefaultTableModel allaProdukter = new DefaultTableModel(kolumnNamn, 0);
            
            //Hämtar alla anställdas id och lägger det i Arraylistan "id".
            String selectOid = "select OrderItemID from orderitem order by(OrderItemID);";
            ArrayList<String> oid = idb.fetchColumn(selectOid);
            
            //Om AnstalldId inte är tom körs en for-each loop som för varje id hämtar id, förnamn och efternamn om det anställda som placeras i Hashmapen "Info".
            if(oid != null){
                for(String ettOid: oid){
                    String selectInfo = "select OrderItemID, AntalProdukter, AnstalldID, StandardProduktID from orderitem where OrderItemID = " + ettOid + ";"; //OBS Namn och Pris ska läggas till
                    HashMap <String, String> info = idb.fetchRow(selectInfo);
                    
                    String ettProduktID = info.get("StandardProduktID");
                    String selectProdukt = "select Namn, Pris from standardprodukt where StandardProduktID = " + ettProduktID + ";";
                    HashMap<String, String> infoNamnPris = idb.fetchRow(selectProdukt);
                    
                    //Skapar en array som håller data för en rad i tabellen.
                    Object[] enRad = new Object [kolumnNamn.length];
                    enRad[0] = info.get("OrderItemID");
                    enRad[1] = info.get("AntalProdukter");
                    enRad[2] = info.get("AnstalldID");
                    enRad[3] = infoNamnPris.get("Namn");
                    enRad[4] = infoNamnPris.get("Pris");
                    
                    allaProdukter.addRow(enRad);
                    
                    //for-each loop som går igenom varje kolumn i kolumnNamn där värdet för enKolumn läggs till i enRad;
                 //   for(String enKolumn : kolumnNamn){
                  //      enRad[index++] = info.get(enKolumn);
                        
                    //}
                    
                 //enRad läggs till i DefaultTableModel
                // allaProdukter.addRow(enRad);
                 
                }
                
            //Jtable sätts med data från DefaultTableModel.
            tblAllaProdukter.setModel(allaProdukter);
            
            }
            
            tblAllaProdukter.setAutoResizeMode(tblAllaProdukter.AUTO_RESIZE_OFF);
            
            //Sätter storleken på tabellen.
            TableColumn col = tblAllaProdukter.getColumnModel().getColumn(0); //id.
            col.setPreferredWidth(100);
            
            col = tblAllaProdukter.getColumnModel().getColumn(1); //Förnamn.
            col.setPreferredWidth(149);
            
            col = tblAllaProdukter.getColumnModel().getColumn(2); //Efternamn.
            col.setPreferredWidth(149);
        }
        
        catch(InfException ex){
            System.out.println(ex);
        }
    }

    public void hamtaTotalPris(){        
        try{
            
            double totalPris = 0.0;
            DefaultTableModel prisTabell = (DefaultTableModel) tblAllaProdukter.getModel();

            for(int i = 0; i < prisTabell.getRowCount(); i++) {
                Object ettPris = prisTabell.getValueAt(i, 0); //Ändras till fyra när listan är klar!
                Object ettAntal = prisTabell.getValueAt(i, 1);
                System.out.println(ettPris);
                System.out.println(ettAntal);
                
                try {
                    double pris = Double.parseDouble(ettPris.toString());
                    int antal = Integer.parseInt(ettAntal.toString());
                    totalPris = totalPris + (pris * antal);
                }
                
                catch (NumberFormatException ex) {
                    System.out.println(ex);           
                }
                
            
            }
            lblPris.setText(String.valueOf(totalPris));
        }
        catch (NumberFormatException ex){
            System.out.println(ex);
        }    
    }        
           
    
    public void fyllLables(){
        
        try {
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
            
           // String selectPris = "select Status from bestallning where BestallningID = '" + klickatOrderNr + "';";
           // String pris = idb.fetchSingle(selectStatus);
          //  lblPris.setText(pris);
            
           
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
        jLabel11 = new javax.swing.JLabel();
        btnTillbaka = new javax.swing.JButton();

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
        jLabel5.setText("Pris:");

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
        jScrollPane1.setViewportView(tblAllaProdukter);

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("Vald av:");

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
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnTillbaka)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel11)
                            .addGap(36, 36, 36)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                                .addComponent(lblKundNr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(55, Short.MAX_VALUE))
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
                .addGap(11, 11, 11)
                .addComponent(btnTillbaka)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGap(32, 32, 32))
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
    private javax.swing.JButton btnTillbaka;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDatum;
    private javax.swing.JLabel lblKundNr;
    private javax.swing.JLabel lblOrderNr;
    private javax.swing.JLabel lblPris;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTable tblAllaProdukter;
    // End of variables declaration//GEN-END:variables
}
