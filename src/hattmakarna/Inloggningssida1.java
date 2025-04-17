/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package hattmakarna;
import oru.inf.InfDB;
import oru.inf.InfException;
import javax.swing.JOptionPane;

/**
 *
 * @author Elin
 */
public class Inloggningssida1 extends javax.swing.JPanel {

    private Validering validera;
    private InfDB idb;

    /**
     * Creates new form Inloggningssida
     */
    public Inloggningssida1(InfDB idb) {
        initComponents();
        

        this.idb = idb;
        lblFelMeddelande.setVisible(false);
        txtfEmail.setText("karin@hattmakarna.se");
        pswfLosenord.setText("hatt123");
        lblFelMeddelande.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblLosenord = new javax.swing.JLabel();
        pswfLosenord = new javax.swing.JPasswordField();
        btnLoggaIn = new javax.swing.JButton();
        lblFelMeddelande = new javax.swing.JLabel();
        lblInloggning = new javax.swing.JLabel();
        txtfEmail = new javax.swing.JTextField();
        lblEpost = new javax.swing.JLabel();

        lblLosenord.setText("Lösenord");

        pswfLosenord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pswfLosenordActionPerformed(evt);
            }
        });

        btnLoggaIn.setText("Logga in");
        btnLoggaIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoggaInActionPerformed(evt);
            }
        });

        lblFelMeddelande.setText("Felaktig inloggning! ");
        lblFelMeddelande.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblFelMeddelande.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                lblFelMeddelandeComponentHidden(evt);
            }
        });

        lblInloggning.setText("Inloggning");

        txtfEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtfEmailActionPerformed(evt);
            }
        });

        lblEpost.setText("E-post");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEpost)
                            .addComponent(lblLosenord))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtfEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                            .addComponent(pswfLosenord)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(213, 213, 213)
                        .addComponent(btnLoggaIn))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(158, 158, 158)
                        .addComponent(lblFelMeddelande))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(180, 180, 180)
                        .addComponent(lblInloggning, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblInloggning, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEpost))
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLosenord)
                    .addComponent(pswfLosenord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addComponent(btnLoggaIn)
                .addGap(40, 40, 40)
                .addComponent(lblFelMeddelande)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void pswfLosenordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pswfLosenordActionPerformed

    }//GEN-LAST:event_pswfLosenordActionPerformed

    private void btnLoggaInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoggaInActionPerformed
        //Hämtar användarinmatning
        String ePost = txtfEmail.getText();
        char[] losenord = pswfLosenord.getPassword();
        String losen = new String(losenord);

        try {
            lblFelMeddelande.setVisible(false);

            //Kontrollera att fälten inte är tomma
            if (!Validering.faltInteTomt(ePost) || !Validering.faltInteTomt(losen)) {
                lblFelMeddelande.setText("Inget av fälten får vara tomma");
                lblFelMeddelande.setVisible(true);
                return;
            }

            //Kontrollera att e-post är i korrekt format
            if (!Validering.valideringEmail(ePost)) {
                lblFelMeddelande.setText("Ange korrekt format för e-postadress");
                lblFelMeddelande.setVisible(true);
                return;
            }

            //Kolla om e-posten finns i databasen
            if (!Validering.finnsEpost(ePost, idb)) {
                lblFelMeddelande.setText("Finns ingen användare med denna epost");
                lblFelMeddelande.setVisible(true);
                return;
            }

            //Kolla om lösenordet stämmer överrens med angiven epostadress i databasen
            if (!Validering.arLosenordKorrekt(ePost, losen, idb)) {
                lblFelMeddelande.setText("Fel lösenord för denna användare");
                lblFelMeddelande.setVisible(true);
                pswfLosenord.setText("");
                return;
            }

            //Om båda stämmer – logga in
            new HuvudMeny(idb, ePost).setVisible(true);
            this.setVisible(false);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Något gick fel: " + ex.getMessage());
        }

    }//GEN-LAST:event_btnLoggaInActionPerformed

    private void lblFelMeddelandeComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_lblFelMeddelandeComponentHidden

    }//GEN-LAST:event_lblFelMeddelandeComponentHidden

    private void txtfEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtfEmailActionPerformed

    }//GEN-LAST:event_txtfEmailActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLoggaIn;
    private javax.swing.JLabel lblEpost;
    private javax.swing.JLabel lblFelMeddelande;
    private javax.swing.JLabel lblInloggning;
    private javax.swing.JLabel lblLosenord;
    private javax.swing.JPasswordField pswfLosenord;
    private javax.swing.JTextField txtfEmail;
    // End of variables declaration//GEN-END:variables
}
