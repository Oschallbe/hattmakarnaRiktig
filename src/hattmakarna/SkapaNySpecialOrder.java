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

public class SkapaNySpecialOrder extends javax.swing.JPanel {

    private static InfDB idb;
    private String inloggadAnvandare;

    public SkapaNySpecialOrder(InfDB idb, String ePost) {
        initComponents();
        this.idb = idb;
        this.inloggadAnvandare = ePost;
        fyllMaterialComboBox();
        fyllKundComboBox();
        seOrderNummer();
        lblEnhet.setText("");
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

            //Sortera listan i bokstavsordning
            java.util.Collections.sort(materialLista, String.CASE_INSENSITIVE_ORDER);

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

    //Fyller comboBox med kundid samt namn
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

    private void sorteraTabellEfterNamn() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        int rowCount = model.getRowCount();

        // Spara raderna i en lista
        ArrayList<Object[]> rader = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            Object[] radData = new Object[model.getColumnCount()];
            for (int j = 0; j < model.getColumnCount(); j++) {
                radData[j] = model.getValueAt(i, j);
            }
            rader.add(radData);
        }

        // Sortera listan efter kolumn 0 (namn)
        rader.sort((r1, r2) -> r1[0].toString().compareToIgnoreCase(r2[0].toString()));

        // Töm modellen och lägg tillbaka raderna i sorterad ordning
        model.setRowCount(0);
        for (Object[] rad : rader) {
            model.addRow(rad);
        }
    }

    //metod för att välja material för att sedan kunna lägga till i order
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

                // Lägg till den nya raden
                model.addRow(new Object[]{
                    rad.get("Namn"),
                    rad.get("Typ"),
                    rad.get("Farg"),
                    rad.get("Pris"),
                    mangdText,
                    rad.get("Enhet"),
                    valjFunktion
                });

                // Sortera om raderna efter namn (kolumn 0)
                sorteraTabellEfterNamn();
            }

        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Fel vid tilläggning av material " + e.getMessage());
        }

    }

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
        btnTaBortMaterialOrder = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(958, 600));
        setLayout(null);

        jLabel3.setText("Kundnummer:");
        add(jLabel3);
        jLabel3.setBounds(20, 80, 90, 16);

        jLabel4.setText("Datum:");
        add(jLabel4);
        jLabel4.setBounds(20, 120, 70, 16);

        txtDatum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDatumActionPerformed(evt);
            }
        });
        add(txtDatum);
        txtDatum.setBounds(120, 120, 115, 26);

        jLabel5.setText("Totalt pris:");
        add(jLabel5);
        jLabel5.setBounds(20, 160, 90, 16);

        txtPris.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrisActionPerformed(evt);
            }
        });
        add(txtPris);
        txtPris.setBounds(120, 160, 75, 26);

        btnSpara.setText("Spara");
        btnSpara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSparaActionPerformed(evt);
            }
        });
        add(btnSpara);
        btnSpara.setBounds(750, 0, 190, 27);

        jCheckBox1.setText("Ja");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        add(jCheckBox1);
        jCheckBox1.setBounds(280, 148, 50, 20);

        jLabel6.setText("Expressleverans?");
        add(jLabel6);
        jLabel6.setBounds(270, 118, 130, 16);

        txtMangd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMangdActionPerformed(evt);
            }
        });
        add(txtMangd);
        txtMangd.setBounds(560, 150, 104, 26);

        jLabel7.setText("Beskrivning:");
        add(jLabel7);
        jLabel7.setBounds(20, 200, 90, 16);

        lblEnhet.setText("Enhet");
        add(lblEnhet);
        lblEnhet.setBounds(680, 160, 120, 16);
        add(txtBeskrivning);
        txtBeskrivning.setBounds(120, 200, 270, 26);

        jLabel25.setText("Funktion:");
        add(jLabel25);
        jLabel25.setBounds(490, 100, 70, 16);

        jLabel12.setText("Tillagt material");
        add(jLabel12);
        jLabel12.setBounds(680, 200, 120, 16);

        comboFunktion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Välj funktion", "Basmaterial", "Dekoration", "Innerfoder", "Innertyg", "Yttertyg", "Stomme" }));
        comboFunktion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboFunktionActionPerformed(evt);
            }
        });
        add(comboFunktion);
        comboFunktion.setBounds(560, 100, 120, 26);

        txtOrderID.setText("txtOrderID");
        add(txtOrderID);
        txtOrderID.setBounds(130, 40, 118, 16);

        jComboKund.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        add(jComboKund);
        jComboKund.setBounds(120, 80, 140, 26);

        jLabel13.setText("Text: (frivilligt)");
        add(jLabel13);
        jLabel13.setBounds(20, 280, 90, 16);

        txtText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTextActionPerformed(evt);
            }
        });
        add(txtText);
        txtText.setBounds(120, 280, 81, 26);
        add(txtHuvudMatt);
        txtHuvudMatt.setBounds(120, 330, 42, 26);

        jLabel14.setText("Dimensioner:");
        add(jLabel14);
        jLabel14.setBounds(20, 400, 100, 16);

        txtHojd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHojdActionPerformed(evt);
            }
        });
        add(txtHojd);
        txtHojd.setBounds(120, 400, 53, 26);

        jLabel15.setText("X");
        add(jLabel15);
        jLabel15.setBounds(180, 410, 20, 16);

        txtBredd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBreddActionPerformed(evt);
            }
        });
        add(txtBredd);
        txtBredd.setBounds(200, 400, 53, 26);

        jLabel1.setText("Skapa specialbeställning");
        add(jLabel1);
        jLabel1.setBounds(410, 0, 180, 16);

        jLabel17.setText("Höjd");
        add(jLabel17);
        jLabel17.setBounds(130, 380, 40, 16);

        jLabel2.setText("Ordernummer:");
        add(jLabel2);
        jLabel2.setBounds(20, 40, 90, 16);

        jLabel8.setText("Tillverkningstid:");
        add(jLabel8);
        jLabel8.setBounds(20, 240, 100, 16);

        jLabel18.setText("Bredd");
        add(jLabel18);
        jLabel18.setBounds(210, 380, 50, 16);
        add(txtTillverkningsTid);
        txtTillverkningsTid.setBounds(120, 240, 81, 26);

        jLabel19.setText("X");
        add(jLabel19);
        jLabel19.setBounds(260, 410, 20, 16);

        jLabel9.setText("Dagar");
        add(jLabel9);
        jLabel9.setBounds(217, 244, 50, 16);
        add(txtDjup);
        txtDjup.setBounds(280, 400, 53, 26);

        jLabel20.setText("Djup");
        add(jLabel20);
        jLabel20.setBounds(290, 380, 40, 16);

        jLabel11.setText("Huvudmått:");
        add(jLabel11);
        jLabel11.setBounds(20, 340, 100, 16);

        jLabel21.setText("cm");
        add(jLabel21);
        jLabel21.setBounds(340, 410, 40, 16);

        jLabel16.setText("Material:");
        add(jLabel16);
        jLabel16.setBounds(490, 50, 60, 16);

        jLabel22.setText("cm");
        add(jLabel22);
        jLabel22.setBounds(172, 333, 28, 16);

        comboMaterial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Välj material" }));
        comboMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMaterialActionPerformed(evt);
            }
        });
        add(comboMaterial);
        comboMaterial.setBounds(560, 40, 120, 26);

        jLabel23.setText("kr");
        add(jLabel23);
        jLabel23.setBounds(200, 170, 60, 16);

        btnLaggTillMaterialOrder.setText("Lägg till material i order");
        btnLaggTillMaterialOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaggTillMaterialOrderActionPerformed(evt);
            }
        });
        add(btnLaggTillMaterialOrder);
        btnLaggTillMaterialOrder.setBounds(750, 80, 190, 27);

        jLabel24.setText("Mängd:");
        add(jLabel24);
        jLabel24.setBounds(490, 160, 60, 16);

        btnLaggTillNyttMaterial.setText("Lägg till nytt material");
        btnLaggTillNyttMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaggTillNyttMaterialActionPerformed(evt);
            }
        });
        add(btnLaggTillNyttMaterial);
        btnLaggTillNyttMaterial.setBounds(750, 40, 190, 27);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Namn", "Typ", "Färg", "Pris", "Mängd", "Enhet", "Funktion"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        add(jScrollPane2);
        jScrollPane2.setBounds(483, 229, 475, 255);

        btnTaBortMaterialOrder.setText("Ta bort material från order");
        btnTaBortMaterialOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaBortMaterialOrderActionPerformed(evt);
            }
        });
        add(btnTaBortMaterialOrder);
        btnTaBortMaterialOrder.setBounds(750, 120, 190, 27);
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
        LaggTillMaterial nyttMaterialFönster = new LaggTillMaterial(idb, inloggadAnvandare);

        nyttMaterialFönster.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                fyllMaterialComboBox(); // Uppdatera material-listan efter stängning
            }
        });

        nyttMaterialFönster.setVisible(true);
    }//GEN-LAST:event_btnLaggTillNyttMaterialActionPerformed

    private void comboFunktionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboFunktionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboFunktionActionPerformed

    private void btnTaBortMaterialOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaBortMaterialOrderActionPerformed
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
    }//GEN-LAST:event_btnTaBortMaterialOrderActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLaggTillMaterialOrder;
    private javax.swing.JButton btnLaggTillNyttMaterial;
    private javax.swing.JButton btnSpara;
    private javax.swing.JButton btnTaBortMaterialOrder;
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
