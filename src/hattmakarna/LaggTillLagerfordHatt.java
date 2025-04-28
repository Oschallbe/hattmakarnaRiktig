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
            txtArtikelnummer.setEditable(false); // Gör det icke-redigerbart
        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Kunde inte generera artikelnummer: " + e.getMessage());
        }
    }

    private void visaEnhetForValtMaterial() {
        try {
            String valtMaterial = (String) comboMaterial.getSelectedItem();

            // Hoppa över dummy-posten
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
    
    // Generera nytt artikelnummer
    try {
        txtArtikelnummer.setText(String.valueOf(genereraNyttArtikelnummer()));
    } catch (InfException e) {
        JOptionPane.showMessageDialog(null, "Kunde inte generera nytt artikelnummer: " + e.getMessage());
    }

    // Rensa material-tabellen
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    model.setRowCount(0);

    // Återställ combo-rutor
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
            return 1000; // Börja på ett basnummer
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

            // Hämta info om materialet från databasen
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

        jLabel2.setText("Mängd");

        comboFunktion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Välj funktion", "Basmaterial", "Innertyg", "Yttertyg", "Innerfoder", "Dekoration", "Stomme" }));

        artikelNummer.setText("Artikelnummer");

        jLabel3.setText("Material");

        namn.setText("Namn");

        jLabel4.setText("Funktion");

        pris.setText("Pris");

        huvudmatt.setText("Huvudmått");

        jLabel5.setText("Färg");

        txtArtikelnummer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtArtikelnummerActionPerformed(evt);
            }
        });

        txtNamn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamnActionPerformed(evt);
            }
        });

        jLabel6.setText("Typ");

        txtPris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrisActionPerformed(evt);
            }
        });

        txtTyp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTypActionPerformed(evt);
            }
        });

        btnLaggTill.setText("Lägg till");
        btnLaggTill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaggTillActionPerformed(evt);
            }
        });

        modell.setText("Modell");

        text.setText("Text");

        laggTillMaterialProdukt.setText("Lägg till material för produkt");
        laggTillMaterialProdukt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laggTillMaterialProduktActionPerformed(evt);
            }
        });

        comboMaterial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMaterialActionPerformed(evt);
            }
        });

        laggTillNyttMaterial.setText("Lägg till nytt material");
        laggTillNyttMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                laggTillNyttMaterialActionPerformed(evt);
            }
        });

        jLabel1.setText("Tillagt material");

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

        jButton1.setText("Ta bort material för produkt");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(artikelNummer, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtArtikelnummer, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(btnLaggTill, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(namn, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNamn, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtFarg, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(pris, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtPris, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTyp, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(huvudmatt, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtHuvudmatt, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(modell, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtModell, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(text, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtText, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(comboFunktion, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(300, 300, 300)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 807, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(laggTillNyttMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(laggTillMaterialProdukt, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtMangd, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lblEnhet, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(388, 388, 388)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtMangd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtArtikelnummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnLaggTill)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(artikelNummer)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNamn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtFarg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(namn)
                                    .addComponent(jLabel5))))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTyp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pris)
                                    .addComponent(jLabel6))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(258, 258, 258)
                                .addComponent(lblEnhet, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addComponent(huvudmatt))
                                    .addComponent(txtHuvudmatt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addComponent(modell))
                                    .addComponent(txtModell, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addComponent(text))
                                    .addComponent(txtText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(23, 23, 23)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(comboMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(laggTillNyttMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(29, 29, 29)
                                        .addComponent(jLabel3)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(27, 27, 27)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(comboFunktion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(laggTillMaterialProdukt)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(32, 32, 32)
                                        .addComponent(jLabel4)))
                                .addGap(24, 24, 24)
                                .addComponent(jButton1)))))
                .addGap(9, 9, 9)
                .addComponent(jLabel1)
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
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

                    // Kontrollera att minst ett material har lagts till
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
                fyllMaterialComboBox(); // Uppdatera material-listan efter stängning
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
