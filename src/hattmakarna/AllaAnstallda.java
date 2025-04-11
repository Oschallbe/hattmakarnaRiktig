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
public class AllaAnstallda extends javax.swing.JFrame {
    private static InfDB idb;
    private String inloggadAnvandare;
    /**
     * Creates new form AllaAnstallda
     */
    public AllaAnstallda(InfDB idb, String ePost) {
        initComponents();
        this.idb=idb;
        this.inloggadAnvandare = ePost;
        fyllTabell();
    }
     public void fyllTabell(){
        try{
            //Skapar en array som lagrar kolumnnamnen.
            String kolumnNamn[] = {"AnstalldID", "Fornamn", "Efternamn"};
            
            //Skapar en DefaultTableModel som håller kolumnnamnen samt sätter antalet rader till noll.
            DefaultTableModel allaAnstallda = new DefaultTableModel(kolumnNamn, 0);
            
            //Hämtar alla anställdas id och lägger det i Arraylistan "id".
            String selectId = "select AnstalldID from anstalld order by(AnstalldID);";
            ArrayList<String> id = idb.fetchColumn(selectId);
            
            //Om AnstalldId inte är tom körs en for-each loop som för varje id hämtar id, förnamn och efternamn om det anställda som placeras i Hashmapen "Info".
            if(id != null){
                for(String ettId: id){
                    String selectInfo = "select AnstalldID, Fornamn, Efternamn from anstalld where AnstalldID = " + ettId + ";";
                    HashMap <String, String> info = idb.fetchRow(selectInfo);
                    
                    //Skapar en array som håller data för en rad i tabellen.
                    Object[] enRad = new Object [kolumnNamn.length];
                    int index = 0;
                    
                    //for-each loop som går igenom varje kolumn i kolumnNamn där värdet för enKolumn läggs till i enRad;
                    for(String enKolumn : kolumnNamn){
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

        btnLaggTill = new javax.swing.JButton();
        btnTaBort = new javax.swing.JButton();
        btnTillbaka = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAllaAnstallda = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnLaggTill.setText("Lägg till");
        btnLaggTill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaggTillActionPerformed(evt);
            }
        });

        btnTaBort.setText("Ta bort");
        btnTaBort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaBortActionPerformed(evt);
            }
        });

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
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

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("Alla anställda");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(158, 158, 158)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnLaggTill)
                                .addGap(26, 26, 26)
                                .addComponent(btnTaBort)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnTillbaka)))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLaggTill)
                    .addComponent(btnTaBort)
                    .addComponent(btnTillbaka))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTillbakaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTillbakaActionPerformed
        //Tillbaka till menyn.
        new HuvudMeny(idb, inloggadAnvandare).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnTillbakaActionPerformed

    private void btnTaBortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaBortActionPerformed
        // Tar bort en anställd genom att välja en rad
        
        try{
            //Hämta raden som har markerats
            int valdRad = tblAllaAnstallda.getSelectedRow();
            
            //Om ingen rad är vald
            if(valdRad == -1){
                javax.swing.JOptionPane.showMessageDialog(this, "Markera en anställd för att kunna ta bort");
                
                return;
            }
            
            //Hämta anställningsid för den markerade raden.
            String id = tblAllaAnstallda.getValueAt(valdRad,0).toString();
            
            //Ett meddelande visas som bekräftelse på om man verkligen vill ta bort.
            int svar = JOptionPane.showConfirmDialog(this, "Vill du verkligen ta bort den anställda?","Bekräfta", javax.swing.JOptionPane.YES_NO_OPTION);
            
            //Om man svarar ja så tas den anställda bort från databasen.
            if(svar==javax.swing.JOptionPane.YES_OPTION){
                String taBort = "delete from anstalld where AnstalldID = " + id + ";";
                idb.delete(taBort);
                
                //efter den anställda tagits bort uppdateras tabellen.
                fyllTabell();
                
                javax.swing.JOptionPane.showMessageDialog(this, "Borttagning lyckades!");
            }
        }
        catch(InfException ex){
            System.out.println(ex);
        }
    }//GEN-LAST:event_btnTaBortActionPerformed

    private void btnLaggTillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaggTillActionPerformed
        //Lägg till ny anställd.
        
    }//GEN-LAST:event_btnLaggTillActionPerformed

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
            java.util.logging.Logger.getLogger(AllaAnstallda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AllaAnstallda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AllaAnstallda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AllaAnstallda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new AllaAnstallda().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLaggTill;
    private javax.swing.JButton btnTaBort;
    private javax.swing.JButton btnTillbaka;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAllaAnstallda;
    // End of variables declaration//GEN-END:variables
}

