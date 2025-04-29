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
import java.util.ArrayList;

/**
 *
 * @author oscar
 */
public class LaggTillLagerfordHatt extends javax.swing.JPanel {

    private InfDB idb;
    private String inloggadAnvandare;

    /*
     * Creates new form LaggTillProdukt
     */
    public LaggTillLagerfordHatt(InfDB idb, String inloggadAnvandare) {
        initComponents();
        this.idb = idb;
        this.inloggadAnvandare = inloggadAnvandare;
        fyllMaterialComboBox();
        jTable1.setRowHeight(30);
    }

    private void fyllMaterialComboBox() {
        try {

            comboMaterial.removeAllItems(); // Töm först
            comboMaterial.addItem("Välj material"); // Dummy-post först
            String sqlFraga = "SELECT Namn FROM Material";
            ArrayList<String> materialLista = idb.fetchColumn(sqlFraga);

            java.util.Collections.sort(materialLista, String.CASE_INSENSITIVE_ORDER);

            for (String namn : materialLista) {
                comboMaterial.addItem(namn);
            }

        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Fel vid hämtning av material " + e.getMessage());
        }

        comboMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visaEnhetForValtMaterial();
            }
        });

        try {
            txtArtikelnummer.setText(String.valueOf(genereraNyttArtikelnummer()));
            txtArtikelnummer.setEditable(false); //Gör det icke-redigerbart
        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Kunde inte generera artikelnummer: " + e.getMessage());
        }
    }

    private void visaEnhetForValtMaterial() {
        try {
            String valtMaterial = (String) comboMaterial.getSelectedItem();

            //Hoppa över dummy-posten
            if (valtMaterial == null || valtMaterial.equals("Välj material")) {
                lblEnhet.setText("");
                return;
            }

            String sql = "SELECT Enhet FROM Material WHERE Namn = '" + valtMaterial + "'";
            String enhet = idb.fetchSingle(sql);

            if (enhet != null && !enhet.isEmpty()) {
                lblEnhet.setText(enhet);
            } else {
                lblEnhet.setText("Okänd enhet");
            }

        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Kunde inte hämta enhet: " + e.getMessage());
        }
    }

    private void rensaFormulär() {
        txtNamn.setText("");
        txtPris.setText("");
        txtHuvudmatt.setText("");
        txtModell.setText("");
        txtText.setText("");
        txtFarg.setText("");
        txtTyp.setText("");

        //Generera nytt artikelnummer
        try {
            txtArtikelnummer.setText(String.valueOf(genereraNyttArtikelnummer()));
        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Kunde inte generera nytt artikelnummer: " + e.getMessage());
        }

        //Rensa material-tabellen
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        
        model.setRowCount(0);

        //Återställ combo-rutor
        comboMaterial.setSelectedIndex(0);
        comboFunktion.setSelectedIndex(0);
        lblEnhet.setText("");
    }

    private int genereraNyttArtikelnummer() throws InfException {
        String sql = "SELECT MAX(Artikelnummer) FROM StandardProdukt";
        String maxArtikelnummer = idb.fetchSingle(sql);

        if (maxArtikelnummer != null) {
            return Integer.parseInt(maxArtikelnummer) + 1;
        } else {
            return 1000; //Börja på ett basnummer
        }
    }

    private void laggTillMaterialIRuta() {
        try {
            String valtMaterial = (String) comboMaterial.getSelectedItem();
            String mangdText = txtMangd.getText().trim();
            String valjFunktion = (String) comboFunktion.getSelectedItem();

            if (!Validering.faltInteTomt(mangdText)) {
                JOptionPane.showMessageDialog(null, "Vänligen fyll i mängd material");
                return;
            }

            if (!Validering.arGiltigtDouble(mangdText)) {
                JOptionPane.showMessageDialog(null, "En mängd måste bestå av siffror");
                return;
            }

            if (valjFunktion.equals("Välj funktion")) {
                JOptionPane.showMessageDialog(null, "Välj en funktion för materialet innan du lägger till det.");
                return;
            }
            if (valtMaterial.equals("Välj material")) {
                JOptionPane.showMessageDialog(null, "Välj vilket material innan du lägger till det.");
                return;
            }

            //Hämta info om materialet från databasen
            String sql = "SELECT Namn, Typ, Farg, Pris, Enhet FROM Material WHERE Namn = '" + valtMaterial + "'";
            HashMap<String, String> rad = idb.fetchRow(sql);

            if (rad != null && !rad.isEmpty()) {
                javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
                model.addRow(new Object[]{
                    rad.get("Namn"),
                    rad.get("Typ"),
                    rad.get("Farg"),
                    rad.get("Pris"),
                    mangdText,
                    rad.get("Enhet"),
                    valjFunktion

                });
            }

        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Fel vid tilläggning av material " + e.getMessage());
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

        jLabel2 = new javax.swing.JLabel();
        txtMangd = new javax.swing.JTextField();
        comboFunktion = new javax.swing.JComboBox<>();
        artikelNummer = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        namn = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        pris = new javax.swing.JLabel();
        huvudmatt = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtArtikelnummer = new javax.swing.JTextField();
        txtFarg = new javax.swing.JTextField();
        txtNamn = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPris = new javax.swing.JTextField();
        txtHuvudmatt = new javax.swing.JTextField();
        txtTyp = new javax.swing.JTextField();
        btnLaggTill = new javax.swing.JButton();
        modell = new javax.swing.JLabel();
        text = new javax.swing.JLabel();
        txtModell = new javax.swing.JTextField();
        txtText = new javax.swing.JTextField();
        laggTillMaterialProdukt = new javax.swing.JButton();
        comboMaterial = new javax.swing.JComboBox<>();
        laggTillNyttMaterial = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lblEnhet = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();

        jLabel2.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        jLabel2.setText("Mängd");

        txtMangd.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N

        comboFunktion.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        comboFunktion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Välj funktion", "Basmaterial", "Innertyg", "Yttertyg", "Innerfoder", "Dekoration", "Stomme" }));

        artikelNummer.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        artikelNummer.setText("Artikelnummer");

        jLabel3.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        jLabel3.setText("Material");

        namn.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        namn.setText("Namn");

        jLabel4.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        jLabel4.setText("Funktion");

        pris.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        pris.setText("Pris");

        huvudmatt.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        huvudmatt.setText("Huvudmått");

        jLabel5.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        jLabel5.setText("Färg");

        txtArtikelnummer.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        txtArtikelnummer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtArtikelnummerActionPerformed(evt);
            }
        });

        txtFarg.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        txtFarg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFargActionPerformed(evt);
            }
        });

        txtNamn.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        txtNamn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamnActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        jLabel6.setText("Typ");

        txtPris.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        txtPris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrisActionPerformed(evt);
            }
        });

        txtHuvudmatt.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N

        txtTyp.setFont(new java.awt.Font("Centaur", 0, 14)); // NOI18N
        txtTyp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTypActionPerformed(evt);
            }
        });

        btnLaggTill.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        btnLaggTill.setText("Lägg till produkt");
        btnLaggTill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaggTillActionPerformed(evt);
            }
        });

        modell.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        modell.setText("Modell");

        text.setFont(new java.awt.Font("Century Gothic", 0, 18)); // NOI18N
        text.setText("Text");

        txtModell.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N

        txtText.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N

        laggTillMaterialProdukt.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        laggTillMaterialProdukt.setText("Lägg till material för produkten");
        laggTillMaterialProdukt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laggTillMaterialProduktActionPerformed(evt);
            }
        });

        comboMaterial.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        comboMaterial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMaterialActionPerformed(evt);
            }
        });

        laggTillNyttMaterial.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        laggTillNyttMaterial.setText("Lägg till nytt material");
        laggTillNyttMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laggTillNyttMaterialActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Century Gothic", 1, 30)); // NOI18N
        jLabel1.setText("Tillagt material");

        jTable1.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Namn", "Typ", "Färg", "Pris", "Mängd", "Enhet", "Funktion"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, true, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("Century Gothic", 0, 14)); // NOI18N
        jButton1.setText("Ta bort material");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Century Gothic", 1, 30)); // NOI18N
        jLabel7.setText("Lägg till hatt");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(laggTillMaterialProdukt)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(text)
                                        .addGap(48, 48, 48)
                                        .addComponent(lblEnhet, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(namn)
                                                    .addComponent(pris)
                                                    .addComponent(huvudmatt, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel6)
                                                    .addComponent(jLabel5)
                                                    .addComponent(jLabel2)
                                                    .addComponent(jLabel4)
                                                    .addComponent(modell)
                                                    .addComponent(jLabel3))
                                                .addGap(41, 41, 41))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(artikelNummer)
                                                .addGap(18, 18, 18)))
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtFarg)
                                            .addComponent(txtHuvudmatt, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtPris, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtNamn, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtArtikelnummer, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtModell, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtText, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtMangd, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(comboMaterial, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(comboFunktion, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtTyp))
                                        .addGap(30, 30, 30)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(laggTillNyttMaterial)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnLaggTill))
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 807, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(30, 30, 30))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7))
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtArtikelnummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(artikelNummer))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(namn)
                            .addComponent(txtNamn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pris)
                            .addComponent(txtPris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(huvudmatt)
                            .addComponent(txtHuvudmatt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(modell)
                            .addComponent(txtModell, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(laggTillNyttMaterial)
                            .addComponent(btnLaggTill)
                            .addComponent(jButton1))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(text)
                            .addComponent(txtText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(comboMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(lblEnhet, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(comboFunktion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMangd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtFarg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtTyp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(laggTillMaterialProdukt)
                .addContainerGap(78, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtArtikelnummerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtArtikelnummerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtArtikelnummerActionPerformed

    private void txtNamnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamnActionPerformed

    private void txtPrisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrisActionPerformed

    private void txtTypActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTypActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTypActionPerformed

    private void btnLaggTillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaggTillActionPerformed
        // TODO add your handling code here:
        String namn = txtNamn.getText();
        String textPris = txtPris.getText();
        String textHuvudmatt = txtHuvudmatt.getText();
        String modell = txtModell.getText();
        String text = txtText.getText();
        String farg = txtFarg.getText().trim();
        String typ = txtTyp.getText().trim();

        try {
            int pris = Integer.parseInt(textPris);
            int huvudmatt = Integer.parseInt(textHuvudmatt);
            int artikelNummer = Integer.parseInt(txtArtikelnummer.getText());

            if (Validering.faltInteTomt(namn)
                    && Validering.faltInteTomt(textPris)
                    && Validering.faltInteTomt(textHuvudmatt)
                    && Validering.faltInteTomt(modell)
                    && Validering.faltInteTomt(text)
                    && Validering.faltInteTomt(farg)
                    && Validering.faltInteTomt(typ)) {
                try {
                    String hamtaID = "Select max(StandardProduktID) from StandardProdukt;";
                    String nyHamtaID = idb.fetchSingle(hamtaID);
                    int nyID = Integer.parseInt(nyHamtaID) + 1;

                    String fragaLaggTill = "INSERT INTO StandardProdukt "
                            + "(StandardProduktID, Namn, Modell, Typ, Farg, Text, Matt, Pris, Artikelnummer) "
                            + "VALUES (" + nyID + ", '" + namn + "', '" + modell + "', '" + typ + "', '" + farg + "', '" + text
                            + "', " + huvudmatt + ", " + pris + ", '" + artikelNummer + "');";

                    idb.insert(fragaLaggTill);

                    //Kolla att minst ett material har lagts till
                    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                    int rowCount = model.getRowCount();
                    if (rowCount == 0) {
                        JOptionPane.showMessageDialog(null, "Vänligen lägg till minst ett material till produkten.");
                        return;
                    }
                    for (int i = 0; i < rowCount; i++) {
                        String materialNamn = (String) model.getValueAt(i, 0);
                        String mangd = (String) model.getValueAt(i, 4);
                        String funktion = (String) model.getValueAt(i, 6);

                        String materialID = idb.fetchSingle("SELECT MaterialID FROM Material WHERE Namn = '" + materialNamn + "'");

                        if (funktion.equals("Välj funktion")) {
                            JOptionPane.showMessageDialog(null, "Vänligen välj en giltig funktion för materialet: " + materialNamn);
                            return;
                        }

                        String insertMaterial = "INSERT INTO StandardProdukt_Material (StandardProduktID, MaterialID, Mängd, Funktion) "
                                + "VALUES (" + nyID + ", " + materialID + ", " + mangd + ", '" + funktion + "')";

                        idb.insert(insertMaterial);
                    }
                    JOptionPane.showMessageDialog(null, "Produkt är tillagd!");
                    rensaFormulär();

                } catch (InfException e) {
                    JOptionPane.showMessageDialog(null, "Misslyckade att spara" + e.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Fälten får inte vara tomma!");
            }
        } catch (NumberFormatException numb) {
            JOptionPane.showMessageDialog(null, "Pris, huvudmått och artikelnummer måste innehålla endast siffror!");
        }
    }//GEN-LAST:event_btnLaggTillActionPerformed

    private void laggTillMaterialProduktActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_laggTillMaterialProduktActionPerformed
        // TODO add your handling code here:
        laggTillMaterialIRuta();
    }//GEN-LAST:event_laggTillMaterialProduktActionPerformed

    private void laggTillNyttMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_laggTillNyttMaterialActionPerformed
        LaggTillMaterial nyttMaterialFönster = new LaggTillMaterial(idb, inloggadAnvandare);

        nyttMaterialFönster.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                fyllMaterialComboBox(); //Uppdatera material-listan efter stängning
            }
        });

        nyttMaterialFönster.setVisible(true);
    }//GEN-LAST:event_laggTillNyttMaterialActionPerformed

    private void comboMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboMaterialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboMaterialActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        try {
            int valdRad = jTable1.getSelectedRow();

            if (valdRad == -1) {
                JOptionPane.showMessageDialog(this, "Markera ett material att ta bort.");
                return;
            }

            int svar = JOptionPane.showConfirmDialog(this, "Vill du verkligen ta bort det valda materialet?", "Bekräfta borttagning", JOptionPane.YES_NO_OPTION);

            if (svar == JOptionPane.YES_OPTION) {
                javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
                model.removeRow(valdRad);

                JOptionPane.showMessageDialog(this, "Materialet har tagits bort.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Något gick fel vid borttagning: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtFargActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFargActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFargActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel artikelNummer;
    private javax.swing.JButton btnLaggTill;
    private javax.swing.JComboBox<String> comboFunktion;
    private javax.swing.JComboBox<String> comboMaterial;
    private javax.swing.JLabel huvudmatt;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JButton laggTillMaterialProdukt;
    private javax.swing.JButton laggTillNyttMaterial;
    private javax.swing.JLabel lblEnhet;
    private javax.swing.JLabel modell;
    private javax.swing.JLabel namn;
    private javax.swing.JLabel pris;
    private javax.swing.JLabel text;
    private javax.swing.JTextField txtArtikelnummer;
    private javax.swing.JTextField txtFarg;
    private javax.swing.JTextField txtHuvudmatt;
    private javax.swing.JTextField txtMangd;
    private javax.swing.JTextField txtModell;
    private javax.swing.JTextField txtNamn;
    private javax.swing.JTextField txtPris;
    private javax.swing.JTextField txtText;
    private javax.swing.JTextField txtTyp;
    // End of variables declaration//GEN-END:variables
}
