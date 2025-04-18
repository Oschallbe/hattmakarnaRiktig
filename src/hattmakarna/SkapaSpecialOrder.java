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
public class SkapaSpecialOrder extends javax.swing.JFrame {

    private static InfDB idb;
    private String inloggadAnvandare;

    /**
     * Creates new form SkapaSpecialOrder
     */
    public SkapaSpecialOrder(InfDB idb, String ePost) {
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

            // Hämta info om materialet från databasen
            String sql = "SELECT Namn, Typ, Farg, Pris FROM Material WHERE Namn = '" + valtMaterial + "'";
            HashMap<String, String> rad = idb.fetchRow(sql);

            if (rad != null && !rad.isEmpty()) {
                javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
                model.addRow(new Object[]{
                    rad.get("Namn"),
                    rad.get("Typ"),
                    rad.get("Farg"),
                    rad.get("Pris")
                });
            }

        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Fel vid tilläggning av material " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jBtnTillbaka = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtDatum = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtPris = new javax.swing.JTextField();
        btnSpara = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtBeskrivning = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtTillverkningsTid = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        comboMaterial = new javax.swing.JComboBox<>();
        btnLaggTillMaterialOrder = new javax.swing.JButton();
        btnLaggTillNyttMaterial = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel12 = new javax.swing.JLabel();
        txtOrderID = new javax.swing.JLabel();
        jComboKund = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        txtText = new javax.swing.JTextField();
        txtHuvudMatt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtHojd = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtBredd = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtDjup = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        txtMangd = new javax.swing.JTextField();
        lblEnhet = new javax.swing.JLabel();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jBtnTillbaka.setText("Tillbaka");
        jBtnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnTillbakaActionPerformed(evt);
            }
        });

        jLabel1.setText("Skapa specialbeställning");

        jLabel2.setText("Ordernummer:");

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

        jLabel7.setText("Beskrivning:");

        jLabel8.setText("Tillverkningstid:");

        jLabel9.setText("Dagar");

        jLabel11.setText("Huvudmått:");

        jLabel16.setText("Material:");

        comboMaterial.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Välj material" }));
        comboMaterial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMaterialActionPerformed(evt);
            }
        });

        btnLaggTillMaterialOrder.setText("Lägg till material i order");
        btnLaggTillMaterialOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLaggTillMaterialOrderActionPerformed(evt);
            }
        });

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
                "Namn", "Typ", "Färg", "Pris", "Mängd", "Enhet"
            }
        ));
        jScrollPane2.setViewportView(jTable1);

        jLabel12.setText("Tillagt material");

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

        jLabel17.setText("Höjd");

        jLabel18.setText("Bredd");

        jLabel19.setText("X");

        jLabel20.setText("Djup");

        jLabel21.setText("cm");

        jLabel22.setText("cm");

        jLabel23.setText("kr");

        jLabel24.setText("Mängd");

        txtMangd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMangdActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(185, 185, 185)
                .addComponent(jLabel12)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel11)
                                            .addComponent(jLabel14)
                                            .addComponent(jLabel16)
                                            .addComponent(jLabel24))))
                                .addGap(67, 67, 67))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
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
                                .addComponent(txtMangd, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblEnhet)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnLaggTillMaterialOrder))
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
                                                .addComponent(txtDjup, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel6)
                                                .addGap(18, 18, 18)
                                                .addComponent(jCheckBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(btnSpara, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jBtnTillbaka, javax.swing.GroupLayout.Alignment.TRAILING)))
                                    .addComponent(txtTillverkningsTid, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtText, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtBeskrivning, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(136, 136, 136)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnSpara)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jBtnTillbaka)
                        .addGap(9, 9, 9)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jCheckBox1))))
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
                .addGap(3, 3, 3)
                .addComponent(jLabel10)
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel11))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtHuvudMatt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel22)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
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
                        .addComponent(btnLaggTillMaterialOrder))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel24)
                        .addComponent(txtMangd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblEnhet)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jBtnTillbakaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnTillbakaActionPerformed
        new HuvudMeny(idb, inloggadAnvandare).setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jBtnTillbakaActionPerformed

    private void txtDatumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDatumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDatumActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void btnLaggTillNyttMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaggTillNyttMaterialActionPerformed
        new LaggTillMaterial(idb, inloggadAnvandare, this).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnLaggTillNyttMaterialActionPerformed

    private void comboMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboMaterialActionPerformed


    }//GEN-LAST:event_comboMaterialActionPerformed

    private void btnLaggTillMaterialOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLaggTillMaterialOrderActionPerformed
        laggTillMaterialIRuta();


    }//GEN-LAST:event_btnLaggTillMaterialOrderActionPerformed

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
                String insertMaterial = "INSERT INTO SpecialProdukt_Material (SpecialProduktID, MaterialID, Antal, Beskrivning) "
                        + "VALUES (" + produktID + ", " + materialID + ", 1, '" + materialNamn + "')";
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

            new HuvudMeny(idb, inloggadAnvandare).setVisible(true);
            this.dispose();

        } catch (InfException e) {
            JOptionPane.showMessageDialog(null, "Fel vid tilläggning av material eller pris: " + e.getMessage());
        }

    }//GEN-LAST:event_btnSparaActionPerformed

    private void txtTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTextActionPerformed

    private void txtPrisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrisActionPerformed

    private void txtHojdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHojdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHojdActionPerformed

    private void txtBreddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBreddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBreddActionPerformed

    private void txtMangdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMangdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMangdActionPerformed

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
            java.util.logging.Logger.getLogger(SkapaSpecialOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SkapaSpecialOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SkapaSpecialOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SkapaSpecialOrder.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new SkapaSpecialOrder().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLaggTillMaterialOrder;
    private javax.swing.JButton btnLaggTillNyttMaterial;
    private javax.swing.JButton btnSpara;
    private javax.swing.JComboBox<String> comboMaterial;
    private javax.swing.JButton jBtnTillbaka;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboKund;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea1;
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
