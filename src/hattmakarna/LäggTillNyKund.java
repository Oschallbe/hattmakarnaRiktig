/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package hattmakarna;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import oru.inf.InfDB;
import oru.inf.InfException;

/**
 *
 * @author iftinserar
 */
public class LäggTillNyKund extends javax.swing.JPanel {

    private static InfDB idb;
    private String inloggadAnvandare;
    private Validering validera;

    public LäggTillNyKund(InfDB idb) {

        initComponents();
        this.idb = idb;

        comboSamma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SammaAdressCheckbox();
            }
        });
    }

    private void SammaAdressCheckbox() {
        boolean samma = comboSamma.isSelected();

        if (samma) {
            //Kopiera från leveransadress
            txtFakturaAdress.setText(TxtLeveransAdress.getText());
            txtFakturaPostnummer.setText(txtLeveransPostnummer.getText());
            txtFakturaOrt.setText(txtLeveransOrt.getText());
            txtFakturaLand.setText(txtLeveransLand.getText());

            //Lås fälten så att de inte kan ändras
            txtFakturaAdress.setEditable(false);
            txtFakturaPostnummer.setEditable(false);
            txtFakturaOrt.setEditable(false);
            txtFakturaLand.setEditable(false);
        } else {
            //Töm fälten om checkboxen avmarkeras
            txtFakturaAdress.setText("");
            txtFakturaPostnummer.setText("");
            txtFakturaOrt.setText("");
            txtFakturaLand.setText("");

            //Gör fälten redigerbara igen
            txtFakturaAdress.setEditable(true);
            txtFakturaPostnummer.setEditable(true);
            txtFakturaOrt.setEditable(true);
            txtFakturaLand.setEditable(true);
        }
    }

    private void sparaKund() {

        //Hämta inmatade värden
        String fornamn = txtFornamn.getText();
        String efternamn = TxtEfternamn.getText();
        String epost = TxtEpost.getText();
        String telefon = txtTelefonNr.getText();
        String huvudmatt = txtHuvudmått.getText();

        String levAdress = TxtLeveransAdress.getText();
        String levPostNr = txtLeveransPostnummer.getText();
        String levOrt = txtLeveransOrt.getText();
        String levLand = txtLeveransLand.getText();

        String fakAdress = txtFakturaAdress.getText();
        String fakPostNr = txtFakturaPostnummer.getText();
        String fakOrt = txtFakturaOrt.getText();
        String fakLand = txtFakturaLand.getText();

        //Validera att inga fält är tomma
        if (!Validering.faltInteTomt(fornamn) || !Validering.faltInteTomt(efternamn)
                || !Validering.faltInteTomt(epost) || !Validering.faltInteTomt(telefon)
                || !Validering.faltInteTomt(huvudmatt) || !Validering.faltInteTomt(levAdress)
                || !Validering.faltInteTomt(levPostNr) || !Validering.faltInteTomt(levOrt)
                || !Validering.faltInteTomt(levLand)
                || !Validering.faltInteTomt(fakAdress) || !Validering.faltInteTomt(fakPostNr)
                || !Validering.faltInteTomt(fakOrt) || !Validering.faltInteTomt(fakLand)) {

            JOptionPane.showMessageDialog(this, "Alla fält måste fyllas i!", "Fel", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Validera dataformat
        if (!Validering.arEndastBokstaver(fornamn)) {
            visaFel("Förnamn får endast innehålla bokstäver");
            return;
        }

        if (!Validering.arEndastBokstaver(efternamn)) {
            visaFel("Efternamn får endast innehålla bokstäver");
            return;
        }

        if (!Validering.valideringEmail(epost)) {
            visaFel("Ogiltig e-postadress");
            return;
        }

        if (!Validering.valideringTelefon(telefon)) {
            visaFel("Telefonnummer måste börja på +");
            return;
        }

        if (!Validering.arEndastSiffror(huvudmatt)) {
            visaFel("Huvudmått får endast innehålla siffror");
            return;
        }

        if (!Validering.arEndastSiffror(levPostNr) || !Validering.arEndastSiffror(fakPostNr)) {
            visaFel("Postnummer får endast innehålla siffror");
            return;
        }

        try {
            String sql = "INSERT INTO Kund (Fornamn, Efternamn, Matt, Epost, Telefonnummer, LeveransAdress, FakturaAdress, "
                    + "LeveransPostnummer, LeveransOrt, LeveransLand, FakturaPostnummer, FakturaOrt, FakturaLand) VALUES ("
                    + "'" + fornamn.replace("'", "''") + "', "
                    + "'" + efternamn.replace("'", "''") + "', "
                    + "'" + huvudmatt + "', "
                    + "'" + epost.replace("'", "''") + "', "
                    + "'" + telefon + "', "
                    + "'" + levAdress.replace("'", "''") + "', "
                    + "'" + fakAdress.replace("'", "''") + "', "
                    + "'" + levPostNr + "', "
                    + "'" + levOrt.replace("'", "''") + "', "
                    + "'" + levLand.replace("'", "''") + "', "
                    + "'" + fakPostNr + "', "
                    + "'" + fakOrt.replace("'", "''") + "', "
                    + "'" + fakLand.replace("'", "''") + "')";

            idb.insert(sql);
            JOptionPane.showMessageDialog(this, "Kunddata har sparats!");
            rensaFalt();
        } catch (InfException e) {
            visaFel("Fel vid sparning till databas: " + e.getMessage());
        }
    }

    private void visaFel(String meddelande) {
        JOptionPane.showMessageDialog(this, meddelande, "Fel", JOptionPane.ERROR_MESSAGE);
    }

    private void rensaFalt() {
        txtFornamn.setText("");
        TxtEfternamn.setText("");
        TxtEpost.setText("");
        txtTelefonNr.setText("");
        txtHuvudmått.setText("");
        TxtLeveransAdress.setText("");
        txtLeveransPostnummer.setText("");
        txtLeveransOrt.setText("");
        txtLeveransLand.setText("");
        txtFakturaAdress.setText("");
        txtFakturaPostnummer.setText("");
        txtFakturaOrt.setText("");
        txtFakturaLand.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtFornamn = new javax.swing.JTextField();
        txtTelefonNr = new javax.swing.JTextField();
        TxtEpost = new javax.swing.JTextField();
        TxtLeveransAdress = new javax.swing.JTextField();
        txtLeveransPostnummer = new javax.swing.JTextField();
        txtLeveransOrt = new javax.swing.JTextField();
        TxtEfternamn = new javax.swing.JTextField();
        btnSpara = new javax.swing.JButton();
        lblFakturaAdress = new javax.swing.JLabel();
        txtFakturaAdress = new javax.swing.JTextField();
        comboSamma = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        lblFörnamn = new javax.swing.JLabel();
        lblEfternamn = new javax.swing.JLabel();
        lblEpost = new javax.swing.JLabel();
        lblTelefonnr = new javax.swing.JLabel();
        lblOrt = new javax.swing.JLabel();
        lblLeveransAdress = new javax.swing.JLabel();
        lblPostNr = new javax.swing.JLabel();
        lblHuvudmått = new javax.swing.JLabel();
        txtHuvudmått = new javax.swing.JTextField();
        lblPostNrFaktura = new javax.swing.JLabel();
        txtFakturaPostnummer = new javax.swing.JTextField();
        lblOrtFaktura = new javax.swing.JLabel();
        txtFakturaOrt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtLeveransLand = new javax.swing.JTextField();
        txtFakturaLand = new javax.swing.JTextField();

        btnSpara.setText("Spara");
        btnSpara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSparaActionPerformed(evt);
            }
        });

        lblFakturaAdress.setText("Fakturaadress");

        comboSamma.setText("Fakturaadress är samma som leveransadress");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel1.setText("Lägg till ny kund ");

        lblFörnamn.setText("Förnamn");

        lblEfternamn.setText("Efternamn");

        lblEpost.setText("Epost");

        lblTelefonnr.setText("Telefonnummer");

        lblOrt.setText("Ort");

        lblLeveransAdress.setText("Leveransadress");

        lblPostNr.setText("Postnummer");

        lblHuvudmått.setText("Huvudmått");

        lblPostNrFaktura.setText("Postnummer");

        lblOrtFaktura.setText("Ort");

        jLabel2.setText("Land");

        jLabel3.setText("Land");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(comboSamma, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSpara))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblTelefonnr)
                                .addComponent(lblEpost)
                                .addComponent(lblFörnamn)
                                .addComponent(lblEfternamn)
                                .addComponent(lblHuvudmått)
                                .addComponent(lblLeveransAdress)
                                .addComponent(lblFakturaAdress))
                            .addGap(27, 27, 27)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtFakturaAdress, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(TxtLeveransAdress, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtTelefonNr, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(TxtEpost, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtHuvudmått, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(TxtEfternamn, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtFornamn, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(37, 37, 37)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblPostNr)
                                .addComponent(lblPostNrFaktura))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtLeveransPostnummer, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                                .addComponent(txtFakturaPostnummer))
                            .addGap(10, 10, 10)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblOrt)
                                .addComponent(lblOrtFaktura))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtFakturaOrt)
                                .addComponent(txtLeveransOrt, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtLeveransLand, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtFakturaLand)))))
                    .addComponent(jLabel1))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFörnamn)
                    .addComponent(txtFornamn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEfternamn)
                    .addComponent(TxtEfternamn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHuvudmått)
                    .addComponent(txtHuvudmått, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEpost)
                    .addComponent(TxtEpost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTelefonnr)
                    .addComponent(txtTelefonNr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TxtLeveransAdress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPostNr)
                    .addComponent(txtLeveransPostnummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOrt)
                    .addComponent(txtLeveransOrt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblLeveransAdress)
                    .addComponent(jLabel2)
                    .addComponent(txtLeveransLand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFakturaAdress)
                    .addComponent(txtFakturaAdress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPostNrFaktura)
                    .addComponent(txtFakturaPostnummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOrtFaktura)
                    .addComponent(txtFakturaOrt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtFakturaLand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSpara)
                    .addComponent(comboSamma))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSparaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSparaActionPerformed
        sparaKund();
    }//GEN-LAST:event_btnSparaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TxtEfternamn;
    private javax.swing.JTextField TxtEpost;
    private javax.swing.JTextField TxtLeveransAdress;
    private javax.swing.JButton btnSpara;
    private javax.swing.JCheckBox comboSamma;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblEfternamn;
    private javax.swing.JLabel lblEpost;
    private javax.swing.JLabel lblFakturaAdress;
    private javax.swing.JLabel lblFörnamn;
    private javax.swing.JLabel lblHuvudmått;
    private javax.swing.JLabel lblLeveransAdress;
    private javax.swing.JLabel lblOrt;
    private javax.swing.JLabel lblOrtFaktura;
    private javax.swing.JLabel lblPostNr;
    private javax.swing.JLabel lblPostNrFaktura;
    private javax.swing.JLabel lblTelefonnr;
    private javax.swing.JTextField txtFakturaAdress;
    private javax.swing.JTextField txtFakturaLand;
    private javax.swing.JTextField txtFakturaOrt;
    private javax.swing.JTextField txtFakturaPostnummer;
    private javax.swing.JTextField txtFornamn;
    private javax.swing.JTextField txtHuvudmått;
    private javax.swing.JTextField txtLeveransLand;
    private javax.swing.JTextField txtLeveransOrt;
    private javax.swing.JTextField txtLeveransPostnummer;
    private javax.swing.JTextField txtTelefonNr;
    // End of variables declaration//GEN-END:variables
}
