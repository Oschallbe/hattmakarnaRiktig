/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hattmakarna;
import oru.inf.InfDB; 
import oru.inf.InfException; 
import javax.swing.JOptionPane; 
import java.util.ArrayList; 
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import javax.swing.*;
/**
 *
 * @author iftinserar
 */
public class SkapaVanligOrder extends javax.swing.JFrame {
 
        private static InfDB idb; 
        private String inloggadAnvandare; 
        private ArrayList<Orderrad> orderrader = new ArrayList<>();
        private String inloggadKundID = "Välj KundID";
        private static boolean express = false;

    /**
     * Creates new form SkapaVanligOrder
     */
    public SkapaVanligOrder(InfDB idb,  String inloggadAnvandare) {
        initComponents();
        this.idb = idb;
        this.inloggadAnvandare = inloggadAnvandare;
        cbKundnummer.removeAllItems();
        fyllKundIDComboBox();
        fyllProduktComboBox();
        cbNamn.addActionListener(e -> fyllProduktInfo());
        txtfDatum.setText(java.time.LocalDate.now().toString()); // <--- Dagens datum
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
    
    private void fyllKundIDComboBox(){
        try {
            cbKundnummer.removeAllItems();  // Rensar alla tidigare objekt
            cbKundnummer.addItem("Välj KundID");  // Lägg till "Välj KundID" som första alternativ
            ArrayList<String> kundIDLista = idb.fetchColumn("SELECT KundID FROM kund");
            for (String id : kundIDLista) {
                cbKundnummer.addItem(id);  // Lägg till de faktiska kundID:n
            }

            cbKundnummer.addActionListener(e -> {
                String valdKund = (String) cbKundnummer.getSelectedItem();

                if (valdKund != null && !valdKund.equals("Välj KundID")) {
                    if (!orderrader.isEmpty()) {
                        int svar = JOptionPane.showConfirmDialog(
                            null,
                            "Du har redan lagt till produkter. Vill du byta kund och rensa din varukorg?",
                            "Byt kund",
                            JOptionPane.YES_NO_OPTION
                        );

                        if (svar == JOptionPane.YES_OPTION) {
                            resetOrder();  // <--- HÄR är ändringen
                        } else {
                            cbKundnummer.setSelectedItem(inloggadKundID); // Återställ till tidigare kund
                        }
                    }
                }
            });

        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Fel vid hämtning av KundID: " + e.getMessage());
        }
    }
    
    private void resetOrder() {
    orderrader.clear();
    express = false;
    cbJa.setSelected(false);  // Checkboxen avmarkeras i GUI:t
    JOptionPane.showMessageDialog(null, "Din varukorg har rensats.");
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
    
    public class Orderrad {
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
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblOrdernummer = new javax.swing.JLabel();
        lblKundnummer = new javax.swing.JLabel();
        lblDatum = new javax.swing.JLabel();
        txtfOrdernummer = new javax.swing.JTextField();
        txtfDatum = new javax.swing.JTextField();
        btnTillbaka = new javax.swing.JButton();
        lblExpressleverans = new javax.swing.JLabel();
        cbJa = new javax.swing.JCheckBox();
        lblProduktlista = new javax.swing.JLabel();
        lblArtikelnummer = new javax.swing.JLabel();
        lblNamn = new javax.swing.JLabel();
        lblPris2 = new javax.swing.JLabel();
        lblAntal = new javax.swing.JLabel();
        tfArtikelNummer = new javax.swing.JTextField();
        tfPris = new javax.swing.JTextField();
        tfAntal = new javax.swing.JTextField();
        cbKundnummer = new javax.swing.JComboBox<>();
        cbNamn = new javax.swing.JComboBox<>();
        btnLaggTill = new javax.swing.JButton();
        btnGaVidare = new javax.swing.JButton();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        jLabel1.setText("Skapa order");

        lblOrdernummer.setText("Ordernummer:");

        lblKundnummer.setText("Kundnummer:");

        lblDatum.setText("Datum:");

        txtfOrdernummer.setEnabled(false);

        txtfDatum.setEnabled(false);

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
            }
        });

        lblExpressleverans.setText("Expressleverans?");

        cbJa.setText("Ja");
        cbJa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbJaActionPerformed(evt);
            }
        });

        lblProduktlista.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblProduktlista.setText("Produktlista");

        lblArtikelnummer.setText("Artikelnummer");

        lblNamn.setText("Namn");

        lblPris2.setText("Pris");

        lblAntal.setText("Antal");

        tfArtikelNummer.setEnabled(false);

        tfPris.setEnabled(false);

        cbKundnummer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbKundnummer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbKundnummerActionPerformed(evt);
            }
        });

        cbNamn.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnLaggTill.setText("Lägg till");
        btnLaggTill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaggTillActionPerformed(evt);
            }
        });

        btnGaVidare.setText("Gå vidare");
        btnGaVidare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGaVidareActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnTillbaka)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(89, 89, 89)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfPris, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblPris2))
                                .addGap(62, 62, 62)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblAntal)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(tfAntal, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(btnLaggTill)
                                        .addGap(25, 25, 25))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(lblExpressleverans)
                                        .addGap(6, 6, 6)))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGaVidare)
                            .addComponent(cbJa)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblArtikelnummer)
                            .addComponent(tfArtikelNummer, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
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
                                            .addComponent(txtfDatum, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtfOrdernummer, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addComponent(cbNamn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(170, 170, 170)
                        .addComponent(jLabel1)))
                .addGap(0, 0, Short.MAX_VALUE))
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
                    .addComponent(txtfDatum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(btnGaVidare)
                    .addComponent(btnTillbaka))
                .addGap(20, 20, 20))
        );

        txtfDatum.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbJaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbJaActionPerformed
    if (cbJa.isSelected()) {
        express = true;
        JOptionPane.showMessageDialog(null, "Expressbeställning aktiverad! 20% tillägg kommer att appliceras.");
    } else {
        express = false;
        JOptionPane.showMessageDialog(null, "Expressbeställning är avaktiverad.");
    }
    }//GEN-LAST:event_cbJaActionPerformed

    private void btnTillbakaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTillbakaActionPerformed
        //Kod för tillbaka kappen som skickar tillbaka användaren till huvudmenyn. 
        new HuvudMeny(idb, inloggadAnvandare).setVisible(true); 
        this.dispose(); 
    }//GEN-LAST:event_btnTillbakaActionPerformed

    private void txtfDatumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtfDatumActionPerformed
//
    }//GEN-LAST:event_txtfDatumActionPerformed

    private void btnGaVidareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGaVidareActionPerformed
    // Kontrollera om det finns några orderrader
    if (orderrader.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Du måste lägga till minst en produkt.");
        return; // Avbryt om inga produkter har lagts till
    }

    // Kontrollera om ett giltigt kundnummer har valts
    String kundID = (String) cbKundnummer.getSelectedItem();
    if (kundID == null || kundID.equals("Välj KundID")) {
        JOptionPane.showMessageDialog(null, "Välj ett giltigt kundnummer.");
        return; // Avbryt om ingen kund har valts
    }

        double totalpris = 0;
    for (Orderrad rad : orderrader) {
        totalpris += rad.totalPris();
    }

    if (express) {
        double expressAvgift = totalpris * 0.2;
        totalpris += expressAvgift;
    }
    // Hämta ordernummer från textfältet
    String ordernummer = txtfOrdernummer.getText();
    if (ordernummer.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Ordernummer kan inte vara tomt.");
        return; // Avbryt om inget ordernummer har angetts
    }

    // Skicka till OrderSammanfattning fönstret med nödvändig data
   OrderSammanfattning os = new OrderSammanfattning(idb, inloggadAnvandare, orderrader, totalpris, kundID, ordernummer, express, "Standardbeställning"
    );
    os.setVisible(true);
    this.dispose(); // Stäng nuvarande fönster
    }//GEN-LAST:event_btnGaVidareActionPerformed

    private void btnLaggTillActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaggTillActionPerformed
    // Kontrollera om ett giltigt kund-ID har valts
        String kundID = (String) cbKundnummer.getSelectedItem();
        if (kundID == null || kundID.equals("Välj KundID")) {
            JOptionPane.showMessageDialog(null, "Du måste välja ett giltigt kundnummer innan du kan lägga till produkter.");
            return;
        }

        try {
            // Försök att lägga till produkten
            int antal = Integer.parseInt(tfAntal.getText());
            double pris = Double.parseDouble(tfPris.getText());
            String artikelnummer = tfArtikelNummer.getText();
            Object selectedItem = cbNamn.getSelectedItem();

            if (selectedItem == null || selectedItem.equals("Välj vara")) {
                JOptionPane.showMessageDialog(null, "Välj en produkt först.");
                return;
            }

            String namn = selectedItem.toString();

            orderrader.add(new Orderrad(artikelnummer, namn, antal, pris));

            JOptionPane.showMessageDialog(null, "Produkt tillagd!");

            // Nollställ textfälten och comboboxen efter att produkten har lagts till.
            tfAntal.setText("");
            tfArtikelNummer.setText("");
            tfPris.setText("");
            cbNamn.setSelectedIndex(0);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Ange ett giltigt värde för antal.");
        }
    }//GEN-LAST:event_btnLaggTillActionPerformed

    private void cbKundnummerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbKundnummerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbKundnummerActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            //    new SkapaVanligOrder().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGaVidare;
    private javax.swing.JButton btnLaggTill;
    private javax.swing.JButton btnTillbaka;
    private javax.swing.JCheckBox cbJa;
    private javax.swing.JComboBox<String> cbKundnummer;
    private javax.swing.JComboBox<String> cbNamn;
    private javax.swing.JButton jButton1;
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
    private javax.swing.JTextField txtfDatum;
    private javax.swing.JTextField txtfOrdernummer;
    // End of variables declaration//GEN-END:variables
}
