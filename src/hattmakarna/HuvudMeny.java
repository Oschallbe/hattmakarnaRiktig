/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hattmakarna;
import oru.inf.InfDB; 
import oru.inf.InfException; 
import javax.swing.JOptionPane; 
/**
 *
 * @author Fam van L
 */
public class HuvudMeny extends javax.swing.JFrame {
    
    private InfDB idb; 
    private String inloggadAnvandare; 
    /**
     * Creates new form HuvudMeny
     */
    public HuvudMeny(InfDB idb, String ePost) {
        initComponents();
        this.idb = idb; 
        this.inloggadAnvandare = ePost;
        hanteraAnstallda();
    }
    
    public void hanteraAnstallda(){
        try{
            String hamtaAnstallda = "select Behorighet from Anstalld where Epost = '" + inloggadAnvandare + "';";
            String behorighet = idb.fetchSingle(hamtaAnstallda);         
                if(behorighet.equals("1")){
                    HanteraAnstalld.setVisible(false);
                }
                else if(behorighet.equals("2")){
                    HanteraAnstalld.setVisible(true);
                }
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

        Kalender = new javax.swing.JButton();
        SeBestallning = new javax.swing.JButton();
        SeKunder = new javax.swing.JButton();
        SeProdukt = new javax.swing.JButton();
        SkapaBestallning = new javax.swing.JButton();
        SkapaBestallningSpecial = new javax.swing.JButton();
        Forsaljning = new javax.swing.JButton();
        HanteraAnstalld = new javax.swing.JButton();
        LoggaUt = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Kalender.setText("Visa kalenderschema");
        Kalender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KalenderActionPerformed(evt);
            }
        });

        SeBestallning.setText("Se alla beställningar");
        SeBestallning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeBestallningActionPerformed(evt);
            }
        });

        SeKunder.setText("Se alla kunder");
        SeKunder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeKunderActionPerformed(evt);
            }
        });

        SeProdukt.setText("Se alla standardprodukter");
        SeProdukt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeProduktActionPerformed(evt);
            }
        });

        SkapaBestallning.setText("Skapa ny beställning");
        SkapaBestallning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SkapaBestallningActionPerformed(evt);
            }
        });

        SkapaBestallningSpecial.setText("Skapa ny specialbeställning");
        SkapaBestallningSpecial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SkapaBestallningSpecialActionPerformed(evt);
            }
        });

        Forsaljning.setText("Försäljningsstatistik");
        Forsaljning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ForsaljningActionPerformed(evt);
            }
        });

        HanteraAnstalld.setText("Hantera anställda");
        HanteraAnstalld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HanteraAnstalldActionPerformed(evt);
            }
        });

        LoggaUt.setText("Logga ut");
        LoggaUt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoggaUtActionPerformed(evt);
            }
        });

        jLabel1.setText("Huvudmeny");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(HanteraAnstalld)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 145, Short.MAX_VALUE)
                        .addComponent(LoggaUt)
                        .addGap(25, 25, 25))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Forsaljning)
                                .addComponent(SkapaBestallningSpecial)
                                .addComponent(SkapaBestallning)
                                .addComponent(SeProdukt)
                                .addComponent(SeKunder)
                                .addComponent(SeBestallning)
                                .addComponent(Kalender)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(2, 2, 2)
                .addComponent(Kalender)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SeBestallning)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SeKunder)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SeProdukt)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SkapaBestallning)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(SkapaBestallningSpecial)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Forsaljning)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(HanteraAnstalld)
                    .addComponent(LoggaUt))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void KalenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KalenderActionPerformed
        // TODO add your handling code here:
       new KalenderSchema(idb, inloggadAnvandare).setVisible(true);
       this.setVisible(false);
    }//GEN-LAST:event_KalenderActionPerformed

    private void SeBestallningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SeBestallningActionPerformed
        // TODO add your handling code here:
       new SeBestallning(idb, inloggadAnvandare).setVisible(true);
       this.setVisible(false);
    }//GEN-LAST:event_SeBestallningActionPerformed

    private void SeKunderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SeKunderActionPerformed
        // TODO add your handling code here:
       new AllaKunder(idb, inloggadAnvandare).setVisible(true);
       this.setVisible(false);
    }//GEN-LAST:event_SeKunderActionPerformed

    private void SeProduktActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SeProduktActionPerformed
        // TODO add your handling code here:
       new SeAllaProdukter(idb, inloggadAnvandare).setVisible(true);
       this.setVisible(false);
    }//GEN-LAST:event_SeProduktActionPerformed

    private void SkapaBestallningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SkapaBestallningActionPerformed
        // TODO add your handling code here:
       new SkapaVanligOrder(idb, inloggadAnvandare).setVisible(true);
       this.setVisible(false);
    }//GEN-LAST:event_SkapaBestallningActionPerformed

    private void SkapaBestallningSpecialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SkapaBestallningSpecialActionPerformed
        // TODO add your handling code here:
       new SkapaSpecialOrder(idb, inloggadAnvandare).setVisible(true);
       this.setVisible(false);
    }//GEN-LAST:event_SkapaBestallningSpecialActionPerformed

    private void ForsaljningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ForsaljningActionPerformed
        // TODO add your handling code here:
       new SeForsaljningsstatistik(idb, inloggadAnvandare).setVisible(true);
       this.setVisible(false);
    }//GEN-LAST:event_ForsaljningActionPerformed

    private void HanteraAnstalldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HanteraAnstalldActionPerformed
        // TODO add your handling code here:
       new AllaAnstallda(idb, inloggadAnvandare).setVisible(true);
       this.setVisible(false);
    }//GEN-LAST:event_HanteraAnstalldActionPerformed

    private void LoggaUtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoggaUtActionPerformed
        // TODO add your handling code here:
        new Inloggningssida(idb).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_LoggaUtActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Forsaljning;
    private javax.swing.JButton HanteraAnstalld;
    private javax.swing.JButton Kalender;
    private javax.swing.JButton LoggaUt;
    private javax.swing.JButton SeBestallning;
    private javax.swing.JButton SeKunder;
    private javax.swing.JButton SeProdukt;
    private javax.swing.JButton SkapaBestallning;
    private javax.swing.JButton SkapaBestallningSpecial;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
