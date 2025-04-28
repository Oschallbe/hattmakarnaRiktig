/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hattmakarna;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import oru.inf.InfDB;
import oru.inf.InfException;
import java.util.HashMap;
import java.util.List;


/**
 *
 * @author linodeluca
 */
public class SeAllaOrdrar extends javax.swing.JPanel {
private String inloggadAnvandare;
private InfDB idb;
private String klickatOrderNr;
private String fraga;
private String filtreraPaDatum;
private JDatePickerImpl datePickerFran;
private JDatePickerImpl datePickerTill;
private String aktivStatusFilter = null;
private String aktivKundFilter = null;
private LocalDate aktivDatumFilter = null; // eller String, beroende på din implementation


    /**
     * Creates new form SeBestallning
     */
    public SeAllaOrdrar(InfDB idb, String ePost) {
        initComponents();  
        Properties p = new Properties();
        p.put("text.today", "Idag");
        p.put("text.month", "Månad");
        p.put("text.year", "År");

        SqlDateModel modelFran = new SqlDateModel();
        JDatePanelImpl datePanelFran = new JDatePanelImpl(modelFran, p);
        datePickerFran = new JDatePickerImpl(datePanelFran, new org.jdatepicker.impl.DateComponentFormatter());

        SqlDateModel modelTill = new SqlDateModel();
        JDatePanelImpl datePanelTill = new JDatePanelImpl(modelTill, p);
        datePickerTill = new JDatePickerImpl(datePanelTill, new org.jdatepicker.impl.DateComponentFormatter());

        // Lägg till date pickers i respektive JPanel
        panDatumFran.setLayout(new java.awt.BorderLayout());
        panDatumFran.add(datePickerFran, java.awt.BorderLayout.CENTER);

        panDatumTill.setLayout(new java.awt.BorderLayout());
        panDatumTill.add(datePickerTill, java.awt.BorderLayout.CENTER);
 
        this.idb = idb;
        this.inloggadAnvandare = ePost;
        fraga = "select BestallningID, Status, Datum, Expressbestallning, TotalPris, Typ, KundID from Bestallning;";
        fyllTable(fraga);
    }
    
    public void fyllTable(String fraga){
        try {
            List<HashMap<String, String>> result = idb.fetchRows(fraga);

            DefaultTableModel model = (DefaultTableModel) BestallningsLista.getModel();
            model.setRowCount(0); // Rensa gamla data
            if (result != null) {
                for (HashMap<String, String> row : result) {
                    String expressHamtning = row.get("Expressbestallning");
                    String expressOmvandling;
                    String typ = row.get("Typ");
                    String selectPris = "SELECT\n"
                            + "    SUM(\n"
                            + "        CASE\n"
                            + "            WHEN oi.StandardProduktID IS NOT NULL THEN IFNULL(sp1.Pris, 0) * IFNULL(oi.AntalProdukter, 0)\n"
                            + "            WHEN oi.SpecialProduktID IS NOT NULL THEN IFNULL(sp2.Pris, 0) * IFNULL(oi.AntalProdukter, 0)\n"
                            + "            ELSE 0\n"
                            + "        END\n"
                            + "    ) AS TotalPris\n"
                            + "FROM OrderItem oi\n"
                            + "LEFT JOIN StandardProdukt sp1 ON oi.StandardProduktID = sp1.StandardProduktID\n"
                            + "LEFT JOIN SpecialProdukt sp2 ON oi.SpecialProduktID = sp2.SpecialProduktID\n"
                            + "WHERE oi.BestallningID = " + row.get("BestallningID") + ";";

                    String totalPrisText = idb.fetchSingle(selectPris);
                    double totalPris = 0.0;

                    if (totalPrisText != null && !totalPrisText.trim().isEmpty()) {
                        totalPris = Double.parseDouble(totalPrisText);
                    } else {
                        System.out.println("Inget pris hittades för order " + row.get("BestallningID"));
                    }

                    if (expressHamtning.equals("1")) {
                        expressOmvandling = "Ja";
                        if (typ != null && typ.contains("Standard")) {
                            totalPris *= 1.2;
                        }
                    }
                    
                    else{
                        expressOmvandling = "Nej";
                    }
                    // Lägg till en rad i JTable
                    model.addRow(new Object[]{
                        row.get("Typ"),
                        row.get("BestallningID"),
                        row.get("KundID"),
                        row.get("Status"),
                        String.format("%.1f", totalPris),
                        row.get("Datum"),
                        expressOmvandling
                    });
                }
            } 
            else {
                System.out.println("Ingen data hittades i tabellen.");
            }

        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Fel vid hämtning av data: " + e.getMessage());
        }   
    }
   
    
    private List<String> hamtaMarkeradeOrdernummer() {
    List<String> markeradeOrdernummer = new ArrayList<>();
    DefaultTableModel model = (DefaultTableModel) BestallningsLista.getModel();

    for (int i = 0; i < model.getRowCount(); i++) {
        Boolean ibockad = (Boolean) model.getValueAt(i, 7); // Kolumn 7 = "Material för order"
        if (ibockad != null && ibockad) {
            String ordernummer = model.getValueAt(i, 1).toString(); // Kolumn 1 = BestallningID
            markeradeOrdernummer.add(ordernummer);
        }
    }

    return markeradeOrdernummer;
}           

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSok = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        BestallningsLista = new javax.swing.JTable();
        btnRensa = new javax.swing.JButton();
        btnSeOrder = new javax.swing.JButton();
        cbStatus = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        txtKund = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnVisaMarkerade = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        panDatumFran = new javax.swing.JPanel();
        panDatumTill = new javax.swing.JPanel();
        btnFraktsedel = new javax.swing.JButton();

        btnSok.setText("Sök");
        btnSok.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSokActionPerformed(evt);
            }
        });

        jLabel3.setText("Datum:");

        BestallningsLista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Ordertyp", "Ordernummer", "Kundnummer", "Status", "Pris", "Datum", "Expressbeställning", "Material för order"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(BestallningsLista);

        btnRensa.setText("Rensa filtrering");
        btnRensa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRensaActionPerformed(evt);
            }
        });

        btnSeOrder.setText("Se order");
        btnSeOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeOrderActionPerformed(evt);
            }
        });

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Status:", "Under behandling", "Produktion pågår", "Packas", "Skickad", "Levererad", "Returnerad" }));
        cbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStatusActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Filtrera efter: ");

        txtKund.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKundActionPerformed(evt);
            }
        });

        jLabel2.setText("KundID eller för- och efternamn: ");

        btnVisaMarkerade.setText("Visa material");
        btnVisaMarkerade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVisaMarkeradeActionPerformed(evt);
            }
        });

        jLabel4.setText("Från:");

        jLabel5.setText("Från:");

        jLabel6.setText("Till:");

        javax.swing.GroupLayout panDatumFranLayout = new javax.swing.GroupLayout(panDatumFran);
        panDatumFran.setLayout(panDatumFranLayout);
        panDatumFranLayout.setHorizontalGroup(
            panDatumFranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        panDatumFranLayout.setVerticalGroup(
            panDatumFranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panDatumTillLayout = new javax.swing.GroupLayout(panDatumTill);
        panDatumTill.setLayout(panDatumTillLayout);
        panDatumTillLayout.setHorizontalGroup(
            panDatumTillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 140, Short.MAX_VALUE)
        );
        panDatumTillLayout.setVerticalGroup(
            panDatumTillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        btnFraktsedel.setText("Skapa fraktsedel");
        btnFraktsedel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFraktsedelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFraktsedel)
                .addGap(48, 48, 48)
                .addComponent(btnSeOrder)
                .addGap(49, 49, 49)
                .addComponent(btnVisaMarkerade)
                .addGap(22, 22, 22))
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtKund, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(74, 74, 74)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(panDatumFran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panDatumTill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(btnSok, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnRensa)
                .addGap(72, 72, 72))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(579, 579, 579)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(580, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panDatumTill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtKund, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)
                                .addComponent(jLabel5))
                            .addComponent(panDatumFran, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnSok)
                                .addComponent(btnRensa)))
                        .addGap(35, 35, 35)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnVisaMarkerade)
                            .addComponent(btnSeOrder)
                            .addComponent(btnFraktsedel)))
                    .addComponent(jLabel6))
                .addGap(52, 52, 52))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(261, 261, 261)
                    .addComponent(jLabel4)
                    .addContainerGap(264, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSokActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSokActionPerformed
        String kund = txtKund.getText().trim();
        String valdStatus = cbStatus.getSelectedItem().toString();
        java.sql.Date fran = (java.sql.Date) datePickerFran.getModel().getValue();
        java.sql.Date till = (java.sql.Date) datePickerTill.getModel().getValue();

        
        try {
        String fraga = "SELECT b.Typ, b.BestallningID, b.KundID, b.Status, b.Datum, b.Expressbestallning " +
                       "FROM Bestallning b " +
                       "JOIN Kund k ON k.KundID = b.KundID WHERE 1=1 ";

        // Filtrera på kund
        if (!kund.isEmpty()) {
            if (kund.contains(" ")) {
                // Namn: Förnamn Efternamn
                String[] delar = kund.split("\\s+", 2);
                String fornamn = delar[0].toLowerCase().replace("'", "''");
                String efternamn = delar[1].toLowerCase().replace("'", "''");
                fraga += "AND LOWER(k.Fornamn) = '" +fornamn+ "' " +
                         "AND LOWER(k.Efternamn) = '" +efternamn+ "' ";
            } else {
                // KundID
                try {
                    int kundID = Integer.parseInt(kund);
                    fraga += "AND k.KundID = " +kundID+ " ";
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Ogiltigt KundID. Ange ett heltal eller fullständigt namn.");
                    return;
                }
            }
        }

        // Filtrera på status
        if (!valdStatus.equals("Status:")) {
            fraga += "AND b.Status = '"+ valdStatus+ "' ";
        }

            // Filtrera på datum
            if (fran != null && till != null) {
                java.sql.Date sqlFran = new java.sql.Date(fran.getTime());
                java.sql.Date sqlTill = new java.sql.Date(till.getTime());
                fraga += "AND b.Datum >= '" + sqlFran + "' AND b.Datum <= '" + sqlTill + "' ";
            } else if ((fran != null && till == null) || (fran == null && till != null)) {
                JOptionPane.showMessageDialog(this, "Fyll i både Från- och Till-datum.");
                return;
            }

        List<HashMap<String, String>> resultat = idb.fetchRows(fraga.toString());

        DefaultTableModel model = (DefaultTableModel) BestallningsLista.getModel();
        model.setRowCount(0);

        if (resultat != null && !resultat.isEmpty()) {
            for (HashMap<String, String> rad : resultat) {
                String expressHamtning = rad.get("Expressbestallning");
                String expressOmvandling;
                String typ = rad.get("Typ");

                String selectPris = "SELECT SUM(CASE "
                                  + "WHEN oi.StandardProduktID IS NOT NULL THEN IFNULL(sp1.Pris, 0) * IFNULL(oi.AntalProdukter, 0) "
                                  + "WHEN oi.SpecialProduktID IS NOT NULL THEN IFNULL(sp2.Pris, 0) * IFNULL(oi.AntalProdukter, 0) "
                                  + "ELSE 0 END) AS TotalPris "
                                  + "FROM OrderItem oi "
                                  + "LEFT JOIN StandardProdukt sp1 ON oi.StandardProduktID = sp1.StandardProduktID "
                                  + "LEFT JOIN SpecialProdukt sp2 ON oi.SpecialProduktID = sp2.SpecialProduktID "
                                  + "WHERE oi.BestallningID = " + rad.get("BestallningID") + ";";

                String totalPrisText = idb.fetchSingle(selectPris);
                Double totalPris = Double.parseDouble(totalPrisText);

                if ("1".equals(expressHamtning)) {
                    expressOmvandling = "Ja";
                    if (typ != null && typ.contains("Standard")) {
                        totalPris *= 1.2;
                    }
                } else {
                    expressOmvandling = "Nej";
                }

                model.addRow(new Object[]{
                    rad.get("Typ"),
                    rad.get("BestallningID"),
                    rad.get("KundID"),
                    rad.get("Status"),
                    totalPris,
                    rad.get("Datum"),
                    expressOmvandling
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Inga beställningar hittades.");
        }

    } catch (InfException e) {
        JOptionPane.showMessageDialog(this, "Fel vid sökning: " + e.getMessage());
    }
//        if (kund.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Du måste ange KundID eller namn.");
//            return;
//        }
//
//        try {
//            String query = "";
//
//            if (kund.contains(" ")) {
//                // Förnamn + Efternamn
//                String[] delar = kund.trim().split("\\s+", 2);
//                String fornamn = delar[0].toLowerCase().replace("'", "''");
//                String efternamn = delar[1].toLowerCase().replace("'", "''");
//
//                query = "SELECT k.KundID, k.Fornamn, k.Efternamn, b.BestallningID, b.Typ, b.Datum, b.Status, b.TotalPris, b.Expressbestallning " +
//                "FROM Kund k " +
//                "JOIN Bestallning b ON k.KundID = b.KundID " +
//                "WHERE LOWER(k.Fornamn) = '" + fornamn + "' " +
//                "AND LOWER(k.Efternamn) = '" + efternamn + "'";
//            } else {
//                // KundID
//                try {
//                    int kundID = Integer.parseInt(kund);
//                    query = "SELECT k.KundID, k.Fornamn, k.Efternamn, b.BestallningID, b.Typ , b.Datum, b.Status, b.TotalPris, b.Expressbestallning " +
//                    "FROM Kund k " +
//                    "JOIN Bestallning b ON k.KundID = b.KundID " +
//                    "WHERE k.KundID = " + kundID;
//                } catch (NumberFormatException ex) {
//                    JOptionPane.showMessageDialog(this, "Ogiltigt KundID. Ange ett numeriskt värde eller namn.");
//                    return;
//                }
//            }
//
//            List<HashMap<String, String>> kunder = idb.fetchRows(query);
//
//            if (kunder == null || kunder.isEmpty()) {
//                JOptionPane.showMessageDialog(this, "Ingen kund hittades.");
//                return;
//            }
//
//            DefaultTableModel model = (DefaultTableModel) BestallningsLista.getModel();
//            model.setRowCount(0); // Rensa gammal data
//
//            for (HashMap<String, String> kundData : kunder) {
//                String expressHamtning = kundData.get("Expressbestallning");
//                    String expressOmvandling;
//                    String typ = kundData.get("Typ");
//                    String selectPris = "SELECT\n" +
//                                        "    SUM(\n" +
//                                        "        CASE\n" +
//                                        "            WHEN oi.StandardProduktID IS NOT NULL THEN IFNULL(sp1.Pris, 0) * IFNULL(oi.AntalProdukter, 0)\n" +
//                                        "            WHEN oi.SpecialProduktID IS NOT NULL THEN IFNULL(sp2.Pris, 0) * IFNULL(oi.AntalProdukter, 0)\n" +
//                                        "            ELSE 0\n" +
//                                        "        END\n" +
//                                        "    ) AS TotalPris\n" +
//                                        "FROM OrderItem oi\n" +
//                                        "LEFT JOIN StandardProdukt sp1 ON oi.StandardProduktID = sp1.StandardProduktID\n" +
//                                        "LEFT JOIN SpecialProdukt sp2 ON oi.SpecialProduktID = sp2.SpecialProduktID\n" +
//                                        "WHERE oi.BestallningID = " + kundData.get("BestallningID") + ";";
//                    String totalPrisText = idb.fetchSingle(selectPris);
//                    Double totalPris = Double.parseDouble(totalPrisText);
//                    if(expressHamtning.equals("1")){
//                        expressOmvandling = "Ja";
//                        if (typ != null && typ.contains("Standard")) {
//                        totalPris = totalPris * 1.2;
//    }
//                    }
//                    else{
//                        expressOmvandling = "Nej";
//                    }
//                model.addRow(new Object[]{
//                    kundData.get("Typ"),
//                        kundData.get("BestallningID"),
//                        kundData.get("KundID"),
//                        kundData.get("Status"),
//                        totalPris,
//                        kundData.get("Datum"),
//                        expressOmvandling
//                });
//            }
//
//        } catch (InfException e) {
//            JOptionPane.showMessageDialog(this, "Fel vid sökning: " + e.getMessage());
//        }
    }//GEN-LAST:event_btnSokActionPerformed

    private void btnRensaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRensaActionPerformed
    fyllTable(fraga);
    
        datePickerFran.getModel().setValue(null);
        datePickerTill.getModel().setValue(null);
        
        txtKund.setText("");
        
        cbStatus.setSelectedIndex(0);

//    java.sql.Date fran = (java.sql.Date) datePickerFran.getModel().getValue();
//    java.sql.Date till = (java.sql.Date) datePickerTill.getModel().getValue();
//    try {
//                if (fran != null && till != null) {
//            java.sql.Date sqlFran = new java.sql.Date(fran.getTime());
//            java.sql.Date sqlTill = new java.sql.Date(till.getTime());
//            filtreraPaDatum = "select BestallningID, Status, Datum, Expressbestallning, TotalPris, Typ, KundID from Bestallning where Datum >= '" + sqlFran + "' AND Datum <= '" + sqlTill + "';";
//        }
//                      else if (fran != null || till != null) {
//                // Ett av datumen är ifyllt men inte båda
//                JOptionPane.showMessageDialog(this, "Fyll i både Från- och Till-datum om du vill söka på datum.");
//                return;
//            }
//            List<HashMap<String, String>> resultat = idb.fetchRows(filtreraPaDatum);
//            System.out.println(resultat);
//
//            DefaultTableModel model = (DefaultTableModel) BestallningsLista.getModel();
//            model.setRowCount(0); // Töm tabellen
//            if (resultat != null && !resultat.isEmpty()) {
//                for (HashMap<String, String> rad : resultat) {
//                    String expressHamtning = rad.get("Expressbestallning");
//                    String expressOmvandling;
//                    String typ = rad.get("Typ");
//                    String selectPris = "SELECT\n" +
//                                        "    SUM(\n" +
//                                        "        CASE\n" +
//                                        "            WHEN oi.StandardProduktID IS NOT NULL THEN IFNULL(sp1.Pris, 0) * IFNULL(oi.AntalProdukter, 0)\n" +
//                                        "            WHEN oi.SpecialProduktID IS NOT NULL THEN IFNULL(sp2.Pris, 0) * IFNULL(oi.AntalProdukter, 0)\n" +
//                                        "            ELSE 0\n" +
//                                        "        END\n" +
//                                        "    ) AS TotalPris\n" +
//                                        "FROM OrderItem oi\n" +
//                                        "LEFT JOIN StandardProdukt sp1 ON oi.StandardProduktID = sp1.StandardProduktID\n" +
//                                        "LEFT JOIN SpecialProdukt sp2 ON oi.SpecialProduktID = sp2.SpecialProduktID\n" +
//                                        "WHERE oi.BestallningID = " + rad.get("BestallningID") + ";";
//                    String totalPrisText = idb.fetchSingle(selectPris);
//                    Double totalPris = Double.parseDouble(totalPrisText);
//                    
//                    if(expressHamtning.equals("1")){
//                        expressOmvandling = "Ja";
//                        if (typ != null && typ.contains("Standard")) {
//                        totalPris = totalPris * 1.2;
//    }
//                    }
//                    else{
//                        expressOmvandling = "Nej";
//                    }
//                    model.addRow(new Object[]{
//                        rad.get("Typ"),
//                        rad.get("BestallningID"),
//                        rad.get("KundID"),
//                        rad.get("Status"),
//                        totalPris,
//                        rad.get("Datum"),
//                        expressOmvandling
//                    });
//                }
//            } else {
//                JOptionPane.showMessageDialog(this, "Inga resultat hittades.");
//            }
//            
//    } catch 
//            (InfException e) {
//            JOptionPane.showMessageDialog(this, "Fel vid sökning: " + e.getMessage());
//        }
        /*

        // Validera att användaren har skrivit ett korrekt datum
        if (!Validering.valideringDatum(datum)) {
            JOptionPane.showMessageDialog(this, "Datumet måste vara i formatet YYYY-MM-DD.");
            return;
        }

        try {
            String query = "SELECT k.KundID, b.BestallningID, b.Typ, b.Datum, b.Status, b.TotalPris, b.Expressbestallning " +
            "FROM Kund k " +
            "JOIN Bestallning b ON k.KundID = b.KundID " +
            "WHERE b.Datum = '" + datum.replace("'", "''") + "'";

            List<HashMap<String, String>> resultat = idb.fetchRows(query);

            if (resultat == null || resultat.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inga beställningar hittades för det angivna datumet.");
                return;
            }

            // Uppdatera tabellen
            DefaultTableModel model = (DefaultTableModel) BestallningsLista.getModel();
            model.setRowCount(0); // Rensa tidigare data

            for (HashMap<String, String> rad : resultat) {
                model.addRow(new Object[]{
                    rad.get("Typ"),
                    rad.get("BestallningID"),
                    rad.get("KundID"),
                    rad.get("Status"),
                    rad.get("TotalPris"),
                    rad.get("Datum"),
                    rad.get("Expressbestallning")
                });
            }

        } catch (InfException e) {
            JOptionPane.showMessageDialog(this, "Fel vid filtrering: " + e.getMessage());
        }*/
    }//GEN-LAST:event_btnRensaActionPerformed

    private void btnSeOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeOrderActionPerformed

    try {
        // Hämta den valda raden från tabellen
        int valdRad = BestallningsLista.getSelectedRow();


        // Om ingen rad är vald, visa felmeddelande
        if (valdRad == -1) {
            JOptionPane.showMessageDialog(this, "Markera en beställningsrad för att se ordern.");
            return;
        }

        // Hämta typ och ordernummer för den valda raden
        String typ = BestallningsLista.getValueAt(valdRad, 0).toString();
        klickatOrderNr = BestallningsLista.getValueAt(valdRad, 1).toString();

        // Skapa en panel baserat på vilken typ av order som är vald
        JPanel orderPanel = null; // Initiera till null

        if (typ.contains("Standard")) {
            orderPanel = new SeVanligOrder(idb, inloggadAnvandare, klickatOrderNr);
        } else {
            orderPanel = new SeSpecialOrder(idb, inloggadAnvandare, klickatOrderNr);
        }

        // Kontrollera att panelen har skapats
        if (orderPanel != null) {
            // Hämta MainFrame som är förälder för den aktuella panelen
            MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);

            // Lägg till den nya panelen i CardLayout (byt ut befintlig panel)
            main.addPanelToCardLayout(orderPanel, "orderVy");

            // Visa den nya panelen
            main.showPanel("orderVy");
        }

    } catch (Exception ex) {
        ex.printStackTrace();
    }
    }//GEN-LAST:event_btnSeOrderActionPerformed

    private void cbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatusActionPerformed
        txtKund.setText("");
        String valdStatus = cbStatus.getSelectedItem().toString();

        try {
            String query = "SELECT b.Typ, b.BestallningID, b.KundID, b.Datum, b.Status, b.TotalPris, b.Expressbestallning "
                    + "FROM Bestallning b "
                    + "JOIN Kund k ON k.KundID = b.KundID";

            if (!valdStatus.equalsIgnoreCase("Status:")) {
                query += " WHERE b.Status = '" + valdStatus.replace("'", "''") + "'";
            }
            
            List<HashMap<String, String>> bestallningar = idb.fetchRows(query);

            DefaultTableModel model = (DefaultTableModel) BestallningsLista.getModel();
            model.setRowCount(0); // Rensa tidigare data

            for (HashMap<String, String> rad : bestallningar) {
                String expressHamtning = rad.get("Expressbestallning");
                    String expressOmvandling;
                    String typ = rad.get("Typ");
                    String selectPris = "SELECT\n" +
                                        "    SUM(\n" +
                                        "        CASE\n" +
                                        "            WHEN oi.StandardProduktID IS NOT NULL THEN IFNULL(sp1.Pris, 0) * IFNULL(oi.AntalProdukter, 0)\n" +
                                        "            WHEN oi.SpecialProduktID IS NOT NULL THEN IFNULL(sp2.Pris, 0) * IFNULL(oi.AntalProdukter, 0)\n" +
                                        "            ELSE 0\n" +
                                        "        END\n" +
                                        "    ) AS TotalPris\n" +
                                        "FROM OrderItem oi\n" +
                                        "LEFT JOIN StandardProdukt sp1 ON oi.StandardProduktID = sp1.StandardProduktID\n" +
                                        "LEFT JOIN SpecialProdukt sp2 ON oi.SpecialProduktID = sp2.SpecialProduktID\n" +
                                        "WHERE oi.BestallningID = " + rad.get("BestallningID") + ";";
                    String totalPrisText = idb.fetchSingle(selectPris);
                    Double totalPris = Double.parseDouble(totalPrisText);
                if (expressHamtning.equals("1")) {
                    expressOmvandling = "Ja";
                    if (typ != null && typ.contains("Standard")) {
                        totalPris = totalPris * 1.2;
                    }
                } else {
                    expressOmvandling = "Nej";
                }
                model.addRow(new Object[]{
                    rad.get("Typ"),
                        rad.get("BestallningID"),
                        rad.get("KundID"),
                        rad.get("Status"),
                        totalPris,
                        rad.get("Datum"),
                        expressOmvandling
                });
            }

        } catch (InfException e) {
            JOptionPane.showMessageDialog(this, "Fel vid filtrering: " + e.getMessage());
        }
    }//GEN-LAST:event_cbStatusActionPerformed

    private void btnVisaMarkeradeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVisaMarkeradeActionPerformed
        // TODO add your handling code here:
    List<String> standardOrder = new ArrayList<>();
    List<String> specialOrder = new ArrayList<>();
    DefaultTableModel model = (DefaultTableModel) BestallningsLista.getModel();

    // Dela upp markerade ordrar efter typ
    for (int i = 0; i < model.getRowCount(); i++) {
        Boolean ibockad = (Boolean) model.getValueAt(i, 7);  // checkbox-kolumnen
        if (ibockad != null && ibockad) {
            String ordernr = model.getValueAt(i, 1).toString();
            String typ = model.getValueAt(i, 0).toString();
            if (typ.contains("Standard")) {
                standardOrder.add(ordernr);
            } else if (typ.contains("Special")) {
                specialOrder.add(ordernr);
            }
        }
    }

    if (standardOrder.isEmpty() && specialOrder.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Inga ordrar är markerade.");
        return;
    }

    try {
        StringBuilder query = new StringBuilder();
        query.append("SELECT m.MaterialID, m.Namn AS MaterialNamn, m.Typ, m.Farg, SUM(materialdata.Mängd) AS TotalMangd ")
             .append("FROM (");

        boolean needsUnion = false;

        if (!standardOrder.isEmpty()) {
            String inClause = String.join(",", standardOrder);
            query.append(
                "SELECT m.MaterialID, m.Namn, m.Typ, m.Farg, spm.Mängd " +
                "FROM Bestallning b " +
                "JOIN OrderItem oi ON b.BestallningID = oi.BestallningID " +
                "JOIN StandardProdukt sp ON oi.StandardProduktID = sp.StandardProduktID " +
                "JOIN StandardProdukt_Material spm ON sp.StandardProduktID = spm.StandardProduktID " +
                "JOIN Material m ON spm.MaterialID = m.MaterialID " +
                "WHERE b.BestallningID IN (" + inClause + ")"
            );
            needsUnion = true;
        }

        if (!specialOrder.isEmpty()) {
            if (needsUnion) query.append(" UNION ALL ");
            String inClause = String.join(",", specialOrder);
            query.append(
                "SELECT m.MaterialID, m.Namn, m.Typ, m.Farg, spm.Mängd " +
                "FROM Bestallning b " +
                "JOIN OrderItem oi ON b.BestallningID = oi.BestallningID " +
                "JOIN SpecialProdukt sp ON oi.SpecialProduktID = sp.SpecialProduktID " +
                "JOIN SpecialProdukt_Material spm ON sp.SpecialProduktID = spm.SpecialProduktID " +
                "JOIN Material m ON spm.MaterialID = m.MaterialID " +
                "WHERE b.BestallningID IN (" + inClause + ")"
            );
        }

        query.append(") AS materialdata ")
             .append("JOIN Material m ON m.MaterialID = materialdata.MaterialID ")
             .append("GROUP BY m.MaterialID, m.Namn, m.Typ, m.Farg");

        List<HashMap<String, String>> resultat = idb.fetchRows(query.toString());

        if (resultat != null && !resultat.isEmpty()) {
            try {
            // Skapa en instans av LäggTillNyKund som en JPanel
            SeMaterialLista materialPanel = new SeMaterialLista(resultat, idb, inloggadAnvandare);

            // Lägg till panelen i MainFrame
            MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
            mainFrame.addPanelToCardLayout(materialPanel, "Se materiallista");
            mainFrame.showPanel("Se materiallista");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Inga material hittades.");
        }
        
        int checkboxKolumnIndex = 7; // eller det index du använder

        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(false, i, checkboxKolumnIndex);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Fel vid hämtning av material: " + e.getMessage());
    }
    }//GEN-LAST:event_btnVisaMarkeradeActionPerformed

    private void txtKundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKundActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKundActionPerformed

    private void btnFraktsedelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFraktsedelActionPerformed
        // TODO add your handling code here:
        // Hämta den valda raden från tabellen
        
        int valdRad = BestallningsLista.getSelectedRow();


        // Om ingen rad är vald, visa felmeddelande
        if (valdRad == -1) {
            JOptionPane.showMessageDialog(this, "Markera en beställningsrad för att se ordern.");
            return;
        }

        // Hämta typ och ordernummer för den valda raden
        klickatOrderNr = BestallningsLista.getValueAt(valdRad, 1).toString();
        JPanel orderPanel = null; // Initiera till null
        
        orderPanel = new SkapaNyFraktsedel(idb, inloggadAnvandare, klickatOrderNr);
        
        if (orderPanel != null) {
            // Hämta MainFrame som är förälder för den aktuella panelen
            MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);

            // Lägg till den nya panelen i CardLayout (byt ut befintlig panel)
            main.addPanelToCardLayout(orderPanel, "orderVy");

            // Visa den nya panelen
            main.showPanel("orderVy");
        }
    }//GEN-LAST:event_btnFraktsedelActionPerformed


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
            java.util.logging.Logger.getLogger(SeVanligOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SeVanligOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SeVanligOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SeVanligOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               // new SeVanligOrder().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable BestallningsLista;
    private javax.swing.JButton btnFraktsedel;
    private javax.swing.JButton btnRensa;
    private javax.swing.JButton btnSeOrder;
    private javax.swing.JButton btnSok;
    private javax.swing.JButton btnVisaMarkerade;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panDatumFran;
    private javax.swing.JPanel panDatumTill;
    private javax.swing.JTextField txtKund;
    // End of variables declaration//GEN-END:variables
}
