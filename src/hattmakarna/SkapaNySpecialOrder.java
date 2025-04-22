/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hattmakarna;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import oru.inf.InfDB;
import oru.inf.InfException;

/**
 *
 * @author mejaa
 */
public class SkapaNySpecialOrder extends javax.swing.JPanel {

    private static InfDB idb;
    private String inloggadAnvandare;

    /**
     * Creates new form SkapaSpecialOrder
     */
    public SkapaNySpecialOrder(InfDB idb, String ePost) {
        initComponents();
        this.idb = idb;
        this.inloggadAnvandare = ePost;
        fyllMaterialComboBox();
        fyllKundComboBox();
        seOrderNummer();
        txtDatum.setText(java.time.LocalDate.now().toString());

        jComboKund.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kundValdOchHamtaMatt();
            }

        });
        comboMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                visaEnhetForValtMaterial();
            }
        });

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

    private void kundValdOchHamtaMatt() {
        try {
            String valdKund = (String) jComboKund.getSelectedItem();

            // Säkerställ att vi inte försöker på dummy-posten
            if (valdKund == null || valdKund.equals("Välj kund")) {
                txtHuvudMatt.setText("");
                return;
            }

            // Exempel: "3 - Agnes Eriksson" → plocka ut "3"
            int kundID = Integer.parseInt(valdKund.split(" - ")[0]);

            String sql = "SELECT Matt FROM Kund WHERE KundID = " + kundID;
            String huvudmatt = idb.fetchSingle(sql);

            if (huvudmatt != null) {
                txtHuvudMatt.setText(huvudmatt);
            } else {
                txtHuvudMatt.setText("");
            }

        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Kunde inte hämta huvudmått: " + e.getMessage());
        }
    }

    public void fyllMaterialComboBox() {
        try {

            comboMaterial.removeAllItems(); // Töm först
            comboMaterial.addItem("Välj material"); // Dummy-post först
            String sqlFraga = "SELECT Namn FROM Material";
            ArrayList<String> materialLista = idb.fetchColumn(sqlFraga);

            for (String namn : materialLista) {
                comboMaterial.addItem(namn);
            }

        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Fel vid hämtning av material " + e.getMessage());
        }
    }

    //Enkel metod för att visa OrderID, vilket i databasen är BeställningsID (Har ingen annan funktion än visuellt)
    private void seOrderNummer() {

        try {
            String sqlfraga = idb.fetchSingle("SELECT MAX(BestallningID) FROM Bestallning");
            int ordernummer = Integer.parseInt(sqlfraga) + 1;
            String bestallningsID = String.valueOf(ordernummer);
            txtOrderID.setText(bestallningsID);

        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Fel vid hämtning av material " + e.getMessage());
        }
    }

    private void fyllKundComboBox() {
        try {

            jComboKund.removeAllItems(); // Töm först
            jComboKund.addItem("Välj kund"); // Dummy-post först
            String sqlFraga = "SELECT KundID, Fornamn, Efternamn FROM Kund";
            ArrayList<HashMap<String, String>> kunder = idb.fetchRows(sqlFraga);

            for (HashMap<String, String> kund : kunder) {
                String kundID = kund.get("KundID");
                String namn = kund.get("Fornamn") + " " + kund.get("Efternamn");
                jComboKund.addItem(kundID + " - " + namn);
            }

        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Fel vid hämtning av kund " + e.getMessage());
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

        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtDatum = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPris = new javax.swing.JTextField();
        btnSpara = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        txtMangd = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        lblEnhet = new javax.swing.JLabel();
        txtBeskrivning = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        comboFunktion = new javax.swing.JComboBox<>();
        txtOrderID = new javax.swing.JLabel();
        jComboKund = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        txtText = new javax.swing.JTextField();
        txtHuvudMatt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtHojd = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtBredd = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        txtTillverkningsTid = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtDjup = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        comboMaterial = new javax.swing.JComboBox<>();
        jLabel23 = new javax.swing.JLabel();
        btnLaggTillMaterialOrder = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        btnLaggTillNyttMaterial = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        jLabel3.setText("Kundnummer:");

        jLabel4.setText("Datum:");

        txtDatum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDatumActionPerformed(evt);
            }
        });

        jLabel5.setText("Totalt pris:");

        txtPris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrisActionPerformed(evt);
            }
        });

        btnSpara.setText("Spara");
        btnSpara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSparaActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Ja");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel6.setText("Expressleverans?");

        txtMangd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMangdActionPerformed(evt);
            }
        });

        jLabel7.setText("Beskrivning:");

        lblEnhet.setText("Enhet");

        jLabel25.setText("Funktion");

        jLabel12.setText("Tillagt material");

        comboFunktion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Välj funktion", "Basmaterial", "Innertyg", "Yttertyg", "Innerfoder", "Dekoration", "Stomme" }));
        comboFunktion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboFunktionActionPerformed(evt);
            }
        });

        txtOrderID.setText("txtOrderID");

        jComboKund.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel13.setText("Text: (frivilligt)");

        txtText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTextActionPerformed(evt);
            }
        });

        jLabel14.setText("Dimensioner:");

        txtHojd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHojdActionPerformed(evt);
            }
        });

        jLabel15.setText("X");

        txtBredd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBreddActionPerformed(evt);
            }
        });

        jLabel1.setText("Skapa specialbeställning");

        jLabel17.setText("Höjd");

        jLabel2.setText("Ordernummer:");

        jLabel8.setText("Tillverkningstid:");

        jLabel18.setText("Bredd");

        jLabel19.setText("X");

        jLabel9.setText("Dagar");

        jLabel20.setText("Djup");

        jLabel11.setText("Huvudmått:");

        jLabel21.setText("cm");

        jLabel16.setText("Material:");

        jLabel22.setText("cm");

        comboMaterial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Välj material" }));
        comboMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMaterialActionPerformed(evt);
            }
        });

        jLabel23.setText("kr");

        btnLaggTillMaterialOrder.setText("Lägg till material i order");
        btnLaggTillMaterialOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaggTillMaterialOrderActionPerformed(evt);
            }
        });

        jLabel24.setText("Mängd");

        btnLaggTillNyttMaterial.setText("Lägg till nytt material");
        btnLaggTillNyttMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaggTillNyttMaterialActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Namn", "Typ", "Färg", "Pris", "Mängd", "Enhet", "Funktion"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(371, Short.MAX_VALUE)
                .addComponent(btnSpara)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGap(3, 3, 3)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel11)
                                        .addComponent(jLabel14)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel24)
                                            .addComponent(jLabel16)
                                            .addComponent(jLabel25)))
                                    .addGap(67, 67, 67))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel13))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(comboMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnLaggTillNyttMaterial))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(16, 16, 16)
                                            .addComponent(jLabel17)
                                            .addGap(41, 41, 41)
                                            .addComponent(jLabel18)
                                            .addGap(43, 43, 43)
                                            .addComponent(jLabel20))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(txtHuvudMatt, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(txtHojd, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                            .addGap(34, 34, 34)
                                                            .addComponent(jLabel9)
                                                            .addGap(78, 78, 78))
                                                        .addGroup(layout.createSequentialGroup()
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(jLabel15)
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(txtBredd, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(jLabel19)
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                            .addComponent(txtDjup, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabel12)
                                                    .addGap(72, 72, 72)))
                                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                            .addGap(1, 1, 1)
                                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(txtOrderID)
                                                                .addGroup(layout.createSequentialGroup()
                                                                    .addComponent(txtPris, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                    .addComponent(jLabel23))))
                                                        .addComponent(jComboKund, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                    .addComponent(txtDatum, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(30, 30, 30)))
                                            .addComponent(jLabel6)
                                            .addGap(18, 18, 18)
                                            .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(txtTillverkningsTid, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtText, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtBeskrivning, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(1, 1, 1)
                                            .addComponent(txtMangd, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(lblEnhet))
                                        .addComponent(comboFunktion, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnLaggTillMaterialOrder))))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(130, 130, 130)
                            .addComponent(jLabel1)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSpara)
                .addContainerGap(741, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(txtOrderID))
                            .addGap(20, 20, 20)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(jComboKund, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(11, 11, 11)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel4)
                                .addComponent(txtDatum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jCheckBox1)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtPris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel23))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txtBeskrivning, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel8)
                        .addComponent(txtTillverkningsTid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel9))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(txtText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(8, 8, 8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(4, 4, 4)
                            .addComponent(jLabel11))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHuvudMatt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel22)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel17)
                        .addComponent(jLabel18)
                        .addComponent(jLabel20))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(txtHojd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15)
                        .addComponent(txtBredd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtDjup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21)
                        .addComponent(jLabel19))
                    .addGap(15, 15, 15)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnLaggTillNyttMaterial)
                        .addComponent(jLabel16)
                        .addComponent(comboMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnLaggTillMaterialOrder)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(comboFunktion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(txtMangd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblEnhet))
                    .addGap(33, 33, 33)
                    .addComponent(jLabel12)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtDatumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDatumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDatumActionPerformed

    private void txtPrisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrisActionPerformed

    private void btnSparaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSparaActionPerformed
        try {

            //Kontrollera att kund är vald
            String valdKund = (String) jComboKund.getSelectedItem();
            if (valdKund.equals("Välj kund")) {
                JOptionPane.showMessageDialog(null, "Vänligen välj en kund.");
                return;
            }

            //Kontrollera att minst ett material är valt
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
            int rowCount = model.getRowCount();
            if (rowCount == 0) {
                JOptionPane.showMessageDialog(null, "Vänligen lägg till minst ett material till ordern.");
                return;
            }
            String datum = txtDatum.getText();
            if (!Validering.valideringDatum(datum)) {
                JOptionPane.showMessageDialog(null, "Ogiltigt datumformat (rätt format: yyyy-MM-dd).\n");
                return;
            }

            String prisText = txtPris.getText().replace(",", ".");
            if (!Validering.faltInteTomt(prisText) || !Validering.arGiltigtDouble(prisText)) {
                JOptionPane.showMessageDialog(null, "Pris måste vara ett giltigt tal, t.ex. 123.45.");
                return;

            }
            String beskrivning1 = txtBeskrivning.getText();
            if (!Validering.faltInteTomt(beskrivning1)) {
                JOptionPane.showMessageDialog(null, "Fyll i beskrivning.");
                return;
            }

            String tillverkningsTidText = txtTillverkningsTid.getText().trim();

            if (!Validering.faltInteTomt(tillverkningsTidText)) {
                JOptionPane.showMessageDialog(null, "Fyll i tillverkningstid.");
                return;
            }

            if (!Validering.arEndastSiffror(tillverkningsTidText)) {
                JOptionPane.showMessageDialog(null, "Tillverkningstid måste bestå av siffror.");
                return;
            }

            //Hämta data från alla boxar
            int kundID = Integer.parseInt(valdKund.split(" - ")[0]); //Gör om String till INT

            double totaltPris = Double.parseDouble(txtPris.getText().replace(",", ".")); //Gör om string till double
            String beskrivning = txtBeskrivning.getText();
            String tid = txtTillverkningsTid.getText().trim() + " dagar";
            String text = txtText.getText();
            boolean express = jCheckBox1.isSelected(); //Kollar det är en expressbeställning eller inte
            String huvudmatt = txtHuvudMatt.getText().trim();
            String hojd = txtHojd.getText().trim();
            String bredd = txtBredd.getText().trim();
            String djup = txtDjup.getText().trim();

            // Validering
            if (!Validering.arEndastSiffror(huvudmatt) || !Validering.arEndastSiffror(hojd)
                    || !Validering.arEndastSiffror(bredd) || !Validering.arEndastSiffror(djup)) {
                JOptionPane.showMessageDialog(null, "Alla mått måste vara siffror.");
                return;
            }
            if (valdKund.equals("Välj kund")
                    || !Validering.faltInteTomt(datum)
                    || !Validering.faltInteTomt(beskrivning)
                    || !Validering.faltInteTomt(tid)) {
                JOptionPane.showMessageDialog(null, "Fyll i alla fält(Utom text).");
                return;
            }

            //Lägger till specialprodukt, statstik ID är 1 för nu, kanske behöver ändras senare
            String insertProdukt = "INSERT INTO SpecialProdukt "
                    + "(Text, Pris, Beskrivning, Tillverkningstid, StatistikID, Matt, Hojd, Bredd, Djup) "
                    + "VALUES ('" + text + "', " + totaltPris + ", '" + beskrivning + "', '" + tid + "', 1, "
                    + huvudmatt + ", " + hojd + ", " + bredd + ", " + djup + ")";

            idb.insert(insertProdukt);

            //Hämtar nytt SpecialProduktID som skapades för denna beställning för att kunna koppla material till detta ID
            String produktID = idb.fetchSingle("SELECT MAX(SpecialProduktID) FROM SpecialProdukt");

            //Lägg till materialrader från jTable till SpecialProdukt_Material-tabellen kopplat till denna beställning
            for (int i = 0; i < rowCount; i++) {
                String materialNamn = (String) model.getValueAt(i, 0);
                String materialID = idb.fetchSingle("SELECT MaterialID FROM Material WHERE Namn = '" + materialNamn + "'");

                String mangdStr = model.getValueAt(i, 4).toString().replace(",", ".");
                double mangd = Double.parseDouble(mangdStr);  // kolumn 5 i tabellen (index 4)
                String funktion = model.getValueAt(i, 6).toString();

                if (funktion.equals("Välj funktion")) {
                    JOptionPane.showMessageDialog(null, "Vänligen välj en giltig funktion för materialet: " + materialNamn);
                    return;
                }

                String insertMaterial = "INSERT INTO SpecialProdukt_Material (SpecialProduktID, MaterialID, Mängd, Funktion) "
                        + "VALUES (" + produktID + ", " + materialID + ", " + mangd + ", '" + funktion + "')";

                idb.insert(insertMaterial);
            }

            //Skapar beställning. Fast status till "Under behandling", kan komma att ändra den senare.
            String insertBestallning = "INSERT INTO Bestallning (Status, Datum, Expressbestallning, KundID, TotalPris, Typ) "
                    + "VALUES ('Under behandling', '" + datum + "', " + express + ", " + kundID + ", " + totaltPris + ", 'Specialbeställning')";
            idb.insert(insertBestallning);

            //Hämta BestallningID
            String bestallningID = idb.fetchSingle("SELECT MAX(BestallningID) FROM Bestallning");

            //Lägg till i OrderItem. ProdSchema och AntstalldID är fasta, kan komma att ändras senare
            String insertOrderItem = "INSERT INTO OrderItem (AntalProdukter, BestallningID, SpecialProduktID, ProduktionsSchemaID, AnstalldID) "
                    + "VALUES (1, " + bestallningID + ", " + produktID + ", 1, NULL)";
            idb.insert(insertOrderItem);

            JOptionPane.showMessageDialog(null, "Specialbeställning sparad!");
        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Fel vid tilläggning av material eller pris: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSparaActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        try {
            String prisText = txtPris.getText().replace(",", ".").trim();

            // Kontrollera att fältet inte är tomt eller ogiltigt
            if (!Validering.faltInteTomt(prisText) || !Validering.arGiltigtDouble(prisText)) {
                JOptionPane.showMessageDialog(null, "Fyll i ett giltigt pris innan du väljer express.");
                jCheckBox1.setSelected(false);
                return;
            }

            double ursprungligtPris = Double.parseDouble(prisText);

            double nyttPris;
            if (jCheckBox1.isSelected()) {
                nyttPris = ursprungligtPris * 1.20;
            } else {
                // Tar bort 20% från nuvarande pris
                nyttPris = ursprungligtPris / 1.20;
            }

            // Visa priset med två decimaler och kommatecken
            txtPris.setText(String.format("%.2f", nyttPris).replace(".", ","));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fel vid uppdatering av pris: " + e.getMessage());
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void txtMangdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMangdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMangdActionPerformed

    private void txtTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTextActionPerformed

    private void txtHojdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHojdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHojdActionPerformed

    private void txtBreddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBreddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBreddActionPerformed

    private void comboMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboMaterialActionPerformed

    }//GEN-LAST:event_comboMaterialActionPerformed

    private void btnLaggTillMaterialOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaggTillMaterialOrderActionPerformed
        laggTillMaterialIRuta();
    }//GEN-LAST:event_btnLaggTillMaterialOrderActionPerformed

    private void btnLaggTillNyttMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaggTillNyttMaterialActionPerformed
        new LaggTillMaterial(idb, inloggadAnvandare).setVisible(true);
    }//GEN-LAST:event_btnLaggTillNyttMaterialActionPerformed

    private void comboFunktionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboFunktionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboFunktionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLaggTillMaterialOrder;
    private javax.swing.JButton btnLaggTillNyttMaterial;
    private javax.swing.JButton btnSpara;
    private javax.swing.JComboBox<String> comboFunktion;
    private javax.swing.JComboBox<String> comboMaterial;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboKund;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblEnhet;
    private javax.swing.JTextField txtBeskrivning;
    private javax.swing.JTextField txtBredd;
    private javax.swing.JTextField txtDatum;
    private javax.swing.JTextField txtDjup;
    private javax.swing.JTextField txtHojd;
    private javax.swing.JTextField txtHuvudMatt;
    private javax.swing.JTextField txtMangd;
    private javax.swing.JLabel txtOrderID;
    private javax.swing.JTextField txtPris;
    private javax.swing.JTextField txtText;
    private javax.swing.JTextField txtTillverkningsTid;
    // End of variables declaration//GEN-END:variables
}
