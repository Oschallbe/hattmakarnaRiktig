/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hattmakarna;

import oru.inf.InfDB;
import oru.inf.InfException;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import javax.swing.*;
import java.util.HashMap;

/**
 *
 * @author iftinserar
 */
public class SkapaNyOrder extends javax.swing.JPanel {

    private static InfDB idb;
    private String inloggadAnvandare;
    private ArrayList<Orderrad> orderrader = new ArrayList<>();
    private String inloggadKundID = "Välj KundID";
    private static boolean express = false;

    /**
     * Creates new form SkapaVanligOrder
     */
    public SkapaNyOrder(InfDB idb, String inloggadAnvandare) {
        initComponents();
        this.idb = idb;
        this.inloggadAnvandare = inloggadAnvandare;
        cbKundnummer.removeAllItems();
        fyllKundIDComboBox();
        fyllProduktComboBox();
        cbNamn.addActionListener(e -> fyllProduktInfo());
        txtDatum.setText(java.time.LocalDate.now().toString()); // <--- Dagens datum
        uppdateraOrdernummer();
    }

    public static boolean isExpress() {
        return express;
    }

    private void uppdateraOrdernummer() {
        try {
            // Hämta det senaste BestallningID från databasen
            String senasteBestallningID = idb.fetchSingle("SELECT MAX(BestallningID) FROM Bestallning");

            // Om det finns ett BestallningID i databasen, sätt nästa BestallningID till det + 1
            if (senasteBestallningID != null) {
                int nyttBestallningID = Integer.parseInt(senasteBestallningID) + 1;
                txtfOrdernummer.setText(String.valueOf(nyttBestallningID));
            } else {
                // Om tabellen är tom (det finns ingen order än), sätt BestallningID till 1
                txtfOrdernummer.setText("1");
            }
        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Fel vid hämtning av senaste BestallningID: " + e.getMessage());
        }
    }

    private void fyllKundIDComboBox() {
        try {
            cbKundnummer.removeAllItems();
            cbKundnummer.addItem("Välj KundID");

            ArrayList<HashMap<String, String>> kunder = idb.fetchRows("SELECT KundID, Fornamn, Efternamn FROM kund WHERE Fornamn IS NOT NULL");

            for (HashMap<String, String> kund : kunder) {
                String kundID = kund.get("KundID");
                String fornamn = kund.get("Fornamn");
                String efternamn = kund.get("Efternamn");
                cbKundnummer.addItem(kundID + " - " + fornamn + " " + efternamn);
            }

            cbKundnummer.addActionListener(e -> {
                String valdKund = (String) cbKundnummer.getSelectedItem();

                if (valdKund != null && !valdKund.equals("Välj KundID")) {
                    String valdKundID = valdKund.split(" - ")[0];  // Hämta bara själva ID:t

                    if (!orderrader.isEmpty()) {
                        int svar = JOptionPane.showConfirmDialog(
                                null,
                                "Du har redan lagt till produkter. Vill du byta kund och rensa din varukorg?",
                                "Byt kund",
                                JOptionPane.YES_NO_OPTION
                        );

                        if (svar == JOptionPane.YES_OPTION) {
                            resetOrder();
                            inloggadKundID = valdKundID;
                        } else {
                            // Återställ till tidigare kund
                            for (int i = 0; i < cbKundnummer.getItemCount(); i++) {
                                String item = cbKundnummer.getItemAt(i);
                                if (item.startsWith(inloggadKundID + " - ")) {
                                    cbKundnummer.setSelectedIndex(i);
                                    break;
                                }
                            }
                        }
                    } else {
                        inloggadKundID = valdKundID;
                    }
                }
            });

        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Fel vid hämtning av kundinformation: " + e.getMessage());
        }
    }

    private void resetOrder() {
        orderrader.clear();
        express = false;
        cbJa.setSelected(false);  // Checkboxen avmarkeras i GUI:t
        JOptionPane.showMessageDialog(null, "Din varukorg har rensats.");
    }

    public void resetForm() {
        orderrader.clear();
        express = false;
        cbJa.setSelected(false);
        cbKundnummer.removeAllItems();
        fyllKundIDComboBox();
        uppdateraOrdernummer();
    }

    private void fyllProduktComboBox() {
        try {
            cbNamn.removeAllItems();
            cbNamn.addItem("Välj vara");
            ArrayList<String> produktNamn = idb.fetchColumn("SELECT Namn FROM StandardProdukt");
            for (String namn : produktNamn) {
                cbNamn.addItem(namn);
            }
        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Fel vid hämtning av produkter: " + e.getMessage());
        }
    }

    private void fyllProduktInfo() {
        String valtNamn = (String) cbNamn.getSelectedItem();

        if (valtNamn != null && !valtNamn.equals("Välj vara")) {
            try {
                String artikelnummer = idb.fetchSingle("SELECT Artikelnummer FROM StandardProdukt WHERE Namn = '" + valtNamn + "'");
                String pris = idb.fetchSingle("SELECT Pris FROM StandardProdukt WHERE Namn = '" + valtNamn + "'");

                tfArtikelNummer.setText(artikelnummer);
                tfPris.setText(pris);
            } catch (InfException e) {
                JOptionPane.showMessageDialog(null, "Fel vid hämtning av produktinfo: " + e.getMessage());
            }
        } else {
            tfArtikelNummer.setText("");
            tfPris.setText("");
        }
    }

    public static class Orderrad {

        String artikelnummer;
        String namn;
        int antal;
        double pris;

        public Orderrad(String artikelnummer, String namn, int antal, double pris) {
            this.artikelnummer = artikelnummer;
            this.namn = namn;
            this.antal = antal;
            this.pris = pris;
        }

        public Orderrad(Orderrad annan) {
            this.artikelnummer = annan.artikelnummer;
            this.namn = annan.namn;
            this.antal = annan.antal;
            this.pris = annan.pris;
        }

        public double totalPris() {
            return antal * pris;
        }

        public String getNamn() {
            return namn;
        }

        public int getAntal() {
            return antal;
        }

        public double getPris() {
            return pris;
        }

        public String getArtikelnummer() {
            return artikelnummer;
        }

        public void setAntal(int nyttAntal) {
            this.antal = nyttAntal;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tfAntal = new javax.swing.JTextField();
        cbKundnummer = new javax.swing.JComboBox<>();
        lblExpressleverans = new javax.swing.JLabel();
        cbNamn = new javax.swing.JComboBox<>();
        cbJa = new javax.swing.JCheckBox();
        btnLaggTill = new javax.swing.JButton();
        lblProduktlista = new javax.swing.JLabel();
        btnGaVidare = new javax.swing.JButton();
        lblArtikelnummer = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lblNamn = new javax.swing.JLabel();
        lblOrdernummer = new javax.swing.JLabel();
        lblPris2 = new javax.swing.JLabel();
        lblKundnummer = new javax.swing.JLabel();
        lblAntal = new javax.swing.JLabel();
        lblDatum = new javax.swing.JLabel();
        tfArtikelNummer = new javax.swing.JTextField();
        txtfOrdernummer = new javax.swing.JTextField();
        tfPris = new javax.swing.JTextField();
        txtDatum = new javax.swing.JTextField();

        cbKundnummer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbKundnummer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbKundnummerActionPerformed(evt);
            }
        });

        lblExpressleverans.setText("Expressleverans?");

        cbNamn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cbJa.setText("Ja");
        cbJa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbJaActionPerformed(evt);
            }
        });

        btnLaggTill.setText("Lägg till");
        btnLaggTill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaggTillActionPerformed(evt);
            }
        });

        lblProduktlista.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblProduktlista.setText("Produktlista");

        btnGaVidare.setText("Ordersammanfattning");
        btnGaVidare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGaVidareActionPerformed(evt);
            }
        });

        lblArtikelnummer.setText("Artikelnummer");

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel1.setText("Skapa order");

        lblNamn.setText("Namn");

        lblOrdernummer.setText("Ordernummer:");

        lblPris2.setText("Pris");

        lblKundnummer.setText("Kundnummer:");

        lblAntal.setText("Antal");

        lblDatum.setText("Datum:");

        tfArtikelNummer.setEnabled(false);

        txtfOrdernummer.setEnabled(false);

        tfPris.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblArtikelnummer)
                    .addComponent(tfArtikelNummer, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblProduktlista)
                                .addComponent(lblNamn)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblOrdernummer)
                                        .addComponent(lblKundnummer)
                                        .addComponent(lblDatum))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(cbKundnummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtDatum, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtfOrdernummer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(cbNamn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(170, 170, 170)
                        .addComponent(jLabel1)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(171, 171, 171)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfPris, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPris2))
                        .addGap(62, 62, 62)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAntal)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(tfAntal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblExpressleverans)
                        .addGap(6, 6, 6)))
                .addComponent(cbJa)
                .addGap(56, 56, 56))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLaggTill)
                .addGap(18, 18, 18)
                .addComponent(btnGaVidare)
                .addGap(36, 36, 36))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOrdernummer)
                    .addComponent(txtfOrdernummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(lblKundnummer))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbKundnummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDatum)
                    .addComponent(txtDatum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblExpressleverans)
                    .addComponent(cbJa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblProduktlista)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblNamn)
                .addGap(27, 27, 27)
                .addComponent(cbNamn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPris2)
                    .addComponent(lblAntal)
                    .addComponent(lblArtikelnummer))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfArtikelNummer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfPris, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfAntal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLaggTill)
                    .addComponent(btnGaVidare))
                .addGap(20, 20, 20))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbKundnummerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbKundnummerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbKundnummerActionPerformed

    private void cbJaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbJaActionPerformed
        if (cbJa.isSelected()) {
            express = true;
            JOptionPane.showMessageDialog(null, "Expressbeställning tillagd! 20% tillägg kommer att appliceras.");
        } else {
            express = false;
            JOptionPane.showMessageDialog(null, "Expressavgiften är borttagen.");
        }
    }//GEN-LAST:event_cbJaActionPerformed

    private void btnLaggTillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaggTillActionPerformed
        if (inloggadKundID == null || inloggadKundID.equals("Välj KundID")) {
            JOptionPane.showMessageDialog(null, "Du måste välja ett giltigt kundnummer innan du kan lägga till produkter.");
            return;
        }

        try {
            int antal = Integer.parseInt(tfAntal.getText());
            double pris = Double.parseDouble(tfPris.getText());
            String artikelnummer = tfArtikelNummer.getText();
            Object selectedItem = cbNamn.getSelectedItem();
            String datum = txtDatum.getText();
            if (!Validering.valideringDatum(datum)) {
                JOptionPane.showMessageDialog(null, "Ogiltigt datumformat (rätt format: yyyy-MM-dd).\n");
                return;
            }

            if (selectedItem == null || selectedItem.equals("Välj vara")) {
                JOptionPane.showMessageDialog(null, "Välj en produkt först.");
                return;
            }

            String namn = selectedItem.toString();
            orderrader.add(new Orderrad(artikelnummer, namn, antal, pris));

            JOptionPane.showMessageDialog(null, "Produkt tillagd!");

            tfAntal.setText("");
            tfArtikelNummer.setText("");
            tfPris.setText("");
            cbNamn.setSelectedIndex(0);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ange ett giltigt värde för antal.");
        }
    }//GEN-LAST:event_btnLaggTillActionPerformed

    private void btnGaVidareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGaVidareActionPerformed
        if (orderrader.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Du måste lägga till minst en produkt. Använd knappen 'Lägg Till'");
            return;
        }

        if (inloggadKundID == null || inloggadKundID.equals("Välj KundID")) {
            JOptionPane.showMessageDialog(null, "Välj ett giltigt kundnummer.");
            return;
        }

        double totalpris = 0;
        for (Orderrad rad : orderrader) {
            totalpris += rad.totalPris();
        }

        if (express) {
            totalpris += totalpris * 0.2;
        }

        String ordernummer = txtfOrdernummer.getText();
        if (ordernummer.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ordernummer kan inte vara tomt.");
            return;
        }

        ArrayList<Orderrad> orderraderKopia = new ArrayList<>();
        for (Orderrad rad : orderrader) {
            orderraderKopia.add(new Orderrad(rad));  // Använder copy constructor
        }

        OrderSammanfattning osPanel = new OrderSammanfattning(idb, inloggadAnvandare, orderraderKopia, totalpris,
                inloggadKundID, ordernummer, express, "Standardbeställning");

        MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
        mainFrame.addPanelToCardLayout(osPanel, "OrderSammanfattning");
        mainFrame.showPanel("OrderSammanfattning");
        resetForm();
        uppdateraOrdernummer();
    }//GEN-LAST:event_btnGaVidareActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGaVidare;
    private javax.swing.JButton btnLaggTill;
    private javax.swing.JCheckBox cbJa;
    private javax.swing.JComboBox<String> cbKundnummer;
    private javax.swing.JComboBox<String> cbNamn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblAntal;
    private javax.swing.JLabel lblArtikelnummer;
    private javax.swing.JLabel lblDatum;
    private javax.swing.JLabel lblExpressleverans;
    private javax.swing.JLabel lblKundnummer;
    private javax.swing.JLabel lblNamn;
    private javax.swing.JLabel lblOrdernummer;
    private javax.swing.JLabel lblPris2;
    private javax.swing.JLabel lblProduktlista;
    private javax.swing.JTextField tfAntal;
    private javax.swing.JTextField tfArtikelNummer;
    private javax.swing.JTextField tfPris;
    private javax.swing.JTextField txtDatum;
    private javax.swing.JTextField txtfOrdernummer;
    // End of variables declaration//GEN-END:variables
}
