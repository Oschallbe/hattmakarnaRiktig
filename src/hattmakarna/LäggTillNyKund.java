/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hattmakarna;

import oru.inf.InfDB;
import oru.inf.InfException;
import javax.swing.JOptionPane;
import java.awt.*;

/**
 *
 * @author helinakravi
 */
public class LäggTillNyKund extends javax.swing.JFrame {

    private static InfDB idb;
    private String inloggadAnvandare;
    private Validering validera;

    public LäggTillNyKund(InfDB idb, String inloggadAnvandare) {

        initComponents();
        this.idb = idb;
        this.inloggadAnvandare = inloggadAnvandare;
        initListeners();
    }

    private void initListeners() {

        btnSpara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sparaKund();
            }
        });

// Kod för tillbaka-knappen som skickar användaren till huvudmenyn.
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new HuvudMeny(idb, inloggadAnvandare).setVisible(true);
                setVisible(false);
            }

        });
    }

//skyddar mot sql-injektion, genom att byta ut ' mot två'' (så att databasen inte tror att man avslutar koden tex)
    private void sparaKund() {
        String telefonNummer = (txtTelefonNr.getText());
        String fakturaAdress = (txtFakturaAdress.getText());
        String leveransAdress = (TxtLeveransAdress.getText());
        String postNummer = (txtPostNr.getText());
        String ort = (txtOrt.getText());
        String förnamn = (txtFornamn.getText());
        String efternamn = (TxtEfternamn.getText());
        String matt = (txtHuvudmått.getText());
        String epost = (TxtEpost.getText());

//felmedelande som dyker upp ifall en ruta inte fylls i, går ej att spara då 
        if (!Validering.faltInteTomt(förnamn) || !Validering.faltInteTomt(efternamn)
                || !Validering.faltInteTomt(epost) || !Validering.faltInteTomt(matt) || !Validering.faltInteTomt(telefonNummer)
                || !Validering.faltInteTomt(fakturaAdress) || !Validering.faltInteTomt(postNummer)
                || !Validering.faltInteTomt(ort) || !Validering.faltInteTomt(leveransAdress)) {

            JOptionPane.showMessageDialog(this, "Alla fält måste fyllas i!", "Fel", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Validering.arEndastBokstaver(förnamn)) {
            JOptionPane.showMessageDialog(this, "Förnamn får endast innehålla bokstäver", "Fel", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Validering.arEndastBokstaver(efternamn)) {
            JOptionPane.showMessageDialog(this, "Efternamn får endast innehålla bokstäver", "Fel", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Validering.arEndastSiffror(matt)) {
            JOptionPane.showMessageDialog(this, "Huvudmått får endast innehålla siffror", "Fel", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Validering.valideringEmail(epost)) {
            JOptionPane.showMessageDialog(this, "Ogiltig e-postadress", "Fel", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Validering.valideringTelefon(telefonNummer)) {
            JOptionPane.showMessageDialog(this, "Telefonnummer måste ha formatet XXX-XXX-XXXX och enbart siffror", "Fel", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Validering.arEndastSiffror(postNummer)) {
            JOptionPane.showMessageDialog(this, "Postnummer får endast innehålla siffror", "Fel", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String fraga = "INSERT INTO Kund (Fornamn, Efternamn, Matt, Epost, Telefonnummer, LeveransAdress, FakturaAdress, LeveransPostnummer, LeveransOrt) "
                    + "VALUES ('" + förnamn + "', '" + efternamn + "', '" + matt + "', '" + epost + "', '" + telefonNummer + "', '" + leveransAdress + "', '" + fakturaAdress + "', '" + postNummer + "', '" + ort + "')";

//kör sql-frågan och ser till att spara datan i databasen
            idb.insert(fraga);
            JOptionPane.showMessageDialog(this, "Kunddata har sparats!");
        } //om något går fel visas detta felmeddelande
        catch (InfException ex) {
            JOptionPane.showMessageDialog(this, "Fel vid sparning till databas: " + ex.getMessage(), "Fel", JOptionPane.ERROR_MESSAGE);
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

        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField7 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lblFörnamn = new javax.swing.JLabel();
        lblEfternamn = new javax.swing.JLabel();
        lblEpost = new javax.swing.JLabel();
        lblTelefonnr = new javax.swing.JLabel();
        lblOrt = new javax.swing.JLabel();
        lblLeveransAdress = new javax.swing.JLabel();
        lblPostNr = new javax.swing.JLabel();
        txtFornamn = new javax.swing.JTextField();
        txtTelefonNr = new javax.swing.JTextField();
        TxtEpost = new javax.swing.JTextField();
        TxtLeveransAdress = new javax.swing.JTextField();
        txtPostNr = new javax.swing.JTextField();
        txtOrt = new javax.swing.JTextField();
        TxtEfternamn = new javax.swing.JTextField();
        btnTillbaka = new javax.swing.JButton();
        btnSpara = new javax.swing.JButton();
        lblFakturaAdress = new javax.swing.JLabel();
        txtFakturaAdress = new javax.swing.JTextField();
        lblHuvudmått = new javax.swing.JLabel();
        txtHuvudmått = new javax.swing.JTextField();

        jLabel8.setText("jLabel8");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextField7.setText("jTextField1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Lägg till ny kund ");

        lblFörnamn.setText("Förnamn");

        lblEfternamn.setText("Efternamn");

        lblEpost.setText("Epost");

        lblTelefonnr.setText("Telefonnummer");

        lblOrt.setText("Ort");

        lblLeveransAdress.setText("Leveransadress");

        lblPostNr.setText("Postnummer");

        txtFornamn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFornamnActionPerformed(evt);
            }
        });

        txtTelefonNr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonNrActionPerformed(evt);
            }
        });

        TxtLeveransAdress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtLeveransAdressActionPerformed(evt);
            }
        });

        txtPostNr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPostNrActionPerformed(evt);
            }
        });

        txtOrt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOrtActionPerformed(evt);
            }
        });

        TxtEfternamn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtEfternamnActionPerformed(evt);
            }
        });

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
            }
        });

        btnSpara.setText("Spara");
        btnSpara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSparaActionPerformed(evt);
            }
        });

        lblFakturaAdress.setText("Fakturaadress");

        lblHuvudmått.setText("Huvudmått");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTelefonnr)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblFörnamn)
                            .addComponent(lblEpost)
                            .addComponent(jLabel1)
                            .addComponent(lblLeveransAdress)
                            .addComponent(lblFakturaAdress))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 198, Short.MAX_VALUE)
                        .addComponent(lblPostNr)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtPostNr, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30)
                        .addComponent(lblOrt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtOrt, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(87, 87, 87))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtHuvudmått)
                            .addComponent(TxtEpost, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTelefonNr, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtEfternamn, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtFornamn, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TxtLeveransAdress, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                            .addComponent(txtFakturaAdress, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTillbaka)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSpara)
                        .addGap(20, 20, 20))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblEfternamn)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblHuvudmått)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFornamn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFörnamn))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEfternamn)
                    .addComponent(TxtEfternamn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHuvudmått)
                    .addComponent(txtHuvudmått, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEpost)
                    .addComponent(TxtEpost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTelefonNr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTelefonnr))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtLeveransAdress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLeveransAdress)
                    .addComponent(lblPostNr)
                    .addComponent(txtPostNr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOrt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOrt))
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFakturaAdress)
                    .addComponent(txtFakturaAdress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTillbaka)
                    .addComponent(btnSpara))
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtEfternamnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtEfternamnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtEfternamnActionPerformed

    private void btnSparaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSparaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSparaActionPerformed

    private void btnTillbakaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTillbakaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTillbakaActionPerformed

    private void txtTelefonNrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonNrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonNrActionPerformed

    private void txtFornamnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFornamnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFornamnActionPerformed

    private void TxtLeveransAdressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtLeveransAdressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtLeveransAdressActionPerformed

    private void txtPostNrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPostNrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPostNrActionPerformed

    private void txtOrtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOrtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtOrtActionPerformed

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
            java.util.logging.Logger.getLogger(LäggTillNyKund.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LäggTillNyKund.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LäggTillNyKund.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LäggTillNyKund.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new LäggTillNyKund().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TxtEfternamn;
    private javax.swing.JTextField TxtEpost;
    private javax.swing.JTextField TxtLeveransAdress;
    private javax.swing.JButton btnSpara;
    private javax.swing.JButton btnTillbaka;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JLabel lblEfternamn;
    private javax.swing.JLabel lblEpost;
    private javax.swing.JLabel lblFakturaAdress;
    private javax.swing.JLabel lblFörnamn;
    private javax.swing.JLabel lblHuvudmått;
    private javax.swing.JLabel lblLeveransAdress;
    private javax.swing.JLabel lblOrt;
    private javax.swing.JLabel lblPostNr;
    private javax.swing.JLabel lblTelefonnr;
    private javax.swing.JTextField txtFakturaAdress;
    private javax.swing.JTextField txtFornamn;
    private javax.swing.JTextField txtHuvudmått;
    private javax.swing.JTextField txtOrt;
    private javax.swing.JTextField txtPostNr;
    private javax.swing.JTextField txtTelefonNr;
    // End of variables declaration//GEN-END:variables
}
