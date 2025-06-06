/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package hattmakarna;

import oru.inf.InfDB;
import oru.inf.InfException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

//package com.itextpdf.highlevel.chapter01;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.FileAlreadyExistsException;

/**
 *
 * @author iftinserar
 */
public class OrderSammanfattning extends javax.swing.JPanel {

    private InfDB idb;
    private String inloggadAnvandare;
    private ArrayList<SkapaNyOrder.Orderrad> orderrader;
    private double totalpris;
    private String kundID;
    private String ordernummer;
    private boolean express = false;
    private boolean redigeringsLage = false;
    private String typ;

    /**
     * Creates new form OrderSammanfattning
     */
    public OrderSammanfattning(InfDB idb, String inloggadAnvandare, ArrayList<SkapaNyOrder.Orderrad> orderrader, double totalpris, String kundID, String ordernummer, boolean express, String typ) {
        initComponents();
        this.idb = idb;
        this.inloggadAnvandare = inloggadAnvandare;
        this.orderrader = orderrader;
        this.totalpris = totalpris;
        this.kundID = kundID;
        this.ordernummer = ordernummer;
        this.express = express;
        this.typ = typ;

        // Anropa metod för att sätta textfält och knappar
        setupTextFieldsAndButtons();
    }

    private void setupTextFieldsAndButtons() {
        tfKundID.setText(kundID);
        tfOrdernummer.setText(ordernummer);

        String[] kolumner = {"Produkt", "Antal", "Pris per st", "Totalt"};

        DefaultTableModel modell = new DefaultTableModel(kolumner, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return redigeringsLage && column == 1; //Endast kolumn 1 ("Antal") är redigerbar i redigeringsläge
            }
        };

        // Fyll tabellen med data från orderrader
        for (SkapaNyOrder.Orderrad rad : orderrader) {
            String produktNamn = rad.getNamn();
            int antal = rad.getAntal();
            double prisPerSt = rad.getPris();
            double totalRadPris = rad.totalPris();

            Object[] radData = {
                produktNamn,
                antal,
                String.format("%.2f kr", prisPerSt).replace(",", "."),
                String.format("%.2f kr", totalRadPris).replace(",", ".")
            };
            modell.addRow(radData);
        }

        // Beräkna expressavgift och totalpris
        double expressAvgift = express ? totalpris * 0.2 : 0;
        double slutpris = totalpris + expressAvgift;

        tfExpress.setText(express ? String.format("%.2f kr", expressAvgift).replace(",", ".") : "0 kr");
        tfTotalpris.setText(String.format("%.2f kr", slutpris).replace(",", "."));

        tblOrdersammanfattning.setModel(modell);
        if (tblOrdersammanfattning.getRowCount() > 0) {
            tblOrdersammanfattning.setRowSelectionInterval(0, 0);
        }

        btnSpara.setEnabled(false);
        btnTaBort.setEnabled(false);
        uppdateraTotalpris();
    }

    private void andraAntal(int andring) {
        if (!redigeringsLage) {
            JOptionPane.showMessageDialog(this, "Klicka på 'Redigera' först innan du kan ändra antal!");
            return;
        }

        int valdRad = tblOrdersammanfattning.getSelectedRow();

        if (valdRad == -1) {
            JOptionPane.showMessageDialog(this, "Välj en produkt först genom att klicka på en rad i tabellen.");
            return;
        }

        SkapaNyOrder.Orderrad rad = orderrader.get(valdRad);
        int nuvarandeAntal = rad.getAntal(); // Använd getter
        int nyttAntal = nuvarandeAntal + andring;

        if (nyttAntal <= 0) {
            JOptionPane.showMessageDialog(this, "Antal måste vara minst 1.");
            return;
        }

        rad.setAntal(nyttAntal); // Använd setter

        DefaultTableModel modell = (DefaultTableModel) tblOrdersammanfattning.getModel();
        modell.setValueAt(nyttAntal, valdRad, 1);
        modell.setValueAt(String.format("%.2f kr", nyttAntal * rad.getPris()), valdRad, 3);

        uppdateraTotalpris();
    }

    private void uppdateraTotalpris() {
    double basPris = 0;
       for (SkapaNyOrder.Orderrad rad : orderrader) {
           basPris += rad.totalPris();
       }

       double expressAvgift = express ? basPris * 0.2 : 0.0;
       double slutpris = basPris + expressAvgift;

       tfExpress.setText(String.format("%.2f kr", expressAvgift));
       tfTotalpris.setText(String.format("%.2f kr", slutpris));
   }

    private class BestallningsRad {

        public String produktNamn = "";
        public int antal = -1;
        public String styckPris = "";
        public String totaltRadPris = "";
    };

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        tfKundID = new javax.swing.JTextField();
        lblTotalpris = new javax.swing.JLabel();
        tfTotalpris = new javax.swing.JTextField();
        tfExpress = new javax.swing.JTextField();
        lblExpressavgift = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblOrdersammanfattning = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnBekrafta = new javax.swing.JButton();
        btnRedigera = new javax.swing.JButton();
        btnSpara = new javax.swing.JButton();
        btnTaBort = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        tfOrdernummer = new javax.swing.JTextField();

        setLayout(null);

        jLabel3.setText("KundID:");
        add(jLabel3);
        jLabel3.setBounds(540, 460, 90, 16);

        tfKundID.setEnabled(false);
        add(tfKundID);
        tfKundID.setBounds(630, 450, 50, 22);

        lblTotalpris.setText("Totalpris:");
        add(lblTotalpris);
        lblTotalpris.setBounds(800, 500, 90, 16);

        tfTotalpris.setEnabled(false);
        add(tfTotalpris);
        tfTotalpris.setBounds(880, 500, 120, 22);

        tfExpress.setEnabled(false);
        add(tfExpress);
        tfExpress.setBounds(880, 460, 119, 22);

        lblExpressavgift.setText("Expressavgift:");
        add(lblExpressavgift);
        lblExpressavgift.setBounds(800, 460, 120, 16);

        tblOrdersammanfattning.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblOrdersammanfattning);

        add(jScrollPane1);
        jScrollPane1.setBounds(530, 70, 654, 365);

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel1.setText("Orderspecifikation");
        add(jLabel1);
        jLabel1.setBounds(761, 10, 170, 24);

        btnBekrafta.setText("Slutför order");
        btnBekrafta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBekraftaActionPerformed(evt);
            }
        });
        add(btnBekrafta);
        btnBekrafta.setBounds(1070, 560, 110, 23);

        btnRedigera.setText("Redigera");
        btnRedigera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRedigeraActionPerformed(evt);
            }
        });
        add(btnRedigera);
        btnRedigera.setBounds(530, 560, 80, 23);

        btnSpara.setText("Spara");
        btnSpara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSparaActionPerformed(evt);
            }
        });
        add(btnSpara);
        btnSpara.setBounds(740, 560, 72, 23);

        btnTaBort.setText("Ta bort");
        btnTaBort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaBortActionPerformed(evt);
            }
        });
        add(btnTaBort);
        btnTaBort.setBounds(640, 560, 72, 23);

        jLabel2.setText("Ordernummer:");
        add(jLabel2);
        jLabel2.setBounds(540, 500, 110, 16);

        tfOrdernummer.setEnabled(false);
        add(tfOrdernummer);
        tfOrdernummer.setBounds(630, 490, 50, 22);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBekraftaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBekraftaActionPerformed
    if (tblOrdersammanfattning.isEditing()) {
        tblOrdersammanfattning.getCellEditor().stopCellEditing();
    }

    DefaultTableModel modell = (DefaultTableModel) tblOrdersammanfattning.getModel();
    boolean valid = true;
    ArrayList<BestallningsRad> pdforderlista = new ArrayList<>();

    for (int i = 0; i < modell.getRowCount(); i++) {
        BestallningsRad rad = new BestallningsRad();
        Object cellValue = modell.getValueAt(i, 1);
        try {
            int antal = Integer.parseInt(cellValue.toString());
            if (antal <= 0) {
                JOptionPane.showMessageDialog(this, "Antalet på rad " + (i + 1) + " måste vara ett positivt heltal.");
                valid = false;
                break;
            } else {
                rad.produktNamn = modell.getValueAt(i, 0).toString();
                rad.styckPris = modell.getValueAt(i, 2).toString();
                orderrader.get(i).setAntal(antal);
                rad.antal = antal;
                rad.totaltRadPris = String.format("%.2f kr", antal * orderrader.get(i).getPris());
                modell.setValueAt(rad.totaltRadPris, i, 3);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Antalet på rad " + (i + 1) + " är inte ett giltigt heltal.");
            valid = false;
            break;
        }
        pdforderlista.add(rad);
    }

    if (!valid) return;

    uppdateraTotalpris(); // Uppdaterar tfTotalpris med eller utan express beroende på flagga

    String status = "Under behandling";
    String datum = java.time.LocalDate.now().toString();
    String expressBool = express ? "1" : "0";

    // Läs ut totalpriset från textfältet
    String totalprisStr = tfTotalpris.getText().replace("kr", "").trim().replace(",", ".");
    double totalpris = Double.parseDouble(totalprisStr);

    // Räkna ut expressavgift om den är tillagd (för PDF-visning)
    double expressAvgift = express ? totalpris * (1.0 / 6.0) : 0.0; // Om total = 120%, express = 20%
    double basPris = express ? totalpris - expressAvgift : totalpris;

    String formattedTotalpris = String.format("%.2f", totalpris).replace(",", ".");

    // SQL: Lägg in ordern
    String sql = String.format(
        "INSERT INTO Bestallning (Status, Datum, Expressbestallning, KundID, FraktsedelID, Totalpris, Typ) "
        + "VALUES ('%s', '%s', %s, %s, NULL, %s, '%s')",
        status, datum, expressBool, kundID, formattedTotalpris, typ
    );

    String ordernummer = "";

    try {
        idb.insert(sql);
        ordernummer = idb.fetchSingle("SELECT MAX(BestallningID) FROM Bestallning");

        for (SkapaNyOrder.Orderrad rad : orderrader) {
            String artikelnummer = rad.getArtikelnummer();
            int antal = rad.getAntal();

            if (antal <= 0) continue;

            String produktID = idb.fetchSingle("SELECT StandardProduktID FROM StandardProdukt WHERE Artikelnummer = '" + artikelnummer + "'");
            String orderItemSQL = String.format(
                "INSERT INTO OrderItem (AntalProdukter, BestallningID, StandardProduktID, ProduktionsSchemaID, AnstalldID) "
                + "VALUES (%d, %s, %s, 1, NULL)",
                antal, ordernummer, produktID
            );
            idb.insert(orderItemSQL);
        }
    } catch (InfException ex) {
        JOptionPane.showMessageDialog(this, "Fel vid sparande av orderrader: " + ex.getMessage());
        return;
    }

    // Skapa admin-mapp
    Path admin = null;
    try {
        admin = Paths.get(System.getProperty("user.home"), "hattadmin");
        Files.createDirectory(admin);
    } catch (FileAlreadyExistsException ex) {
        // Mappen finns redan – ignoreras
    } catch (IOException ex) {
        JOptionPane.showMessageDialog(null, "Fel vid skapande av admin-mapp: " + ex.getMessage());
    }

    // Hämta kundinfo
    HashMap<String, String> kundInfo = new HashMap<>();
    try {
        String kundQuery = "SELECT Fornamn, Efternamn, Epost, Telefonnummer, "
                + "LeveransOrt, LeveransAdress, LeveransPostnummer, LeveransLand, "
                + "FakturaAdress, FakturaPostnummer, FakturaOrt, FakturaLand "
                + "FROM Kund WHERE KundID = " + kundID + ";";
        HashMap<String, String> result = idb.fetchRow(kundQuery);
        if (result != null) kundInfo.putAll(result);
        else JOptionPane.showMessageDialog(this, "Kundinformation kunde inte hämtas.");
    } catch (InfException e) {
        JOptionPane.showMessageDialog(this, "Fel vid hämtning av kunddata: " + e.getMessage());
    }

    // Skapa PDF
    try {
        Path path = Paths.get(admin.toString(), "Orderbekr_" + String.format("%08d", Integer.parseInt(ordernummer)) + ".pdf");
        PdfDocument pdf = new PdfDocument(new PdfWriter(path.toString()));
        Document document = new Document(pdf);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);

        document.add(new Paragraph("Orderbekräftelse " + datum).setFont(bold));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Hej! " + kundInfo.get("Fornamn") + " " + kundInfo.get("Efternamn")));
        document.add(new Paragraph("Tack för din beställning hos oss på Hattmakarna"));
        document.add(new Paragraph("Vi har nu mottagit din order och kommer påbörja tillverkningen"));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Kundinformation:").setFont(bold));
        document.add(new Paragraph("Namn: " + kundInfo.get("Fornamn") + " " + kundInfo.get("Efternamn")));
        document.add(new Paragraph("E-post: " + kundInfo.get("Epost")));
        document.add(new Paragraph("Telefonnummer: " + kundInfo.get("Telefonnummer")));
        document.add(new Paragraph("Leveransadress: " + kundInfo.get("LeveransAdress")));
        document.add(new Paragraph("Postnummer: " + kundInfo.get("LeveransPostnummer")));
        document.add(new Paragraph("Ort: " + kundInfo.get("LeveransOrt")));
        document.add(new Paragraph("Land: " + kundInfo.get("LeveransLand")));
        document.add(new Paragraph("Fakturaadress: " + kundInfo.get("FakturaAdress")));
        document.add(new Paragraph("Postnummer: " + kundInfo.get("FakturaPostnummer")));
        document.add(new Paragraph("Ort: " + kundInfo.get("FakturaOrt")));
        document.add(new Paragraph("Land: " + kundInfo.get("FakturaLand")));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Beställning:").setFont(bold));
        for (BestallningsRad rad : pdforderlista) {
            document.add(new Paragraph(rad.antal + " st '" + rad.produktNamn + "', styckpris: " + rad.styckPris + ", totalt: " + rad.totaltRadPris));
        }

        if (express) {
            document.add(new Paragraph("Expressavgift: " + String.format("%.2f kr", expressAvgift)));
        }
        document.add(new Paragraph("Totalt pris: " + String.format("%.2f kr", totalpris)));
        document.add(new Paragraph("Vid frågor, kontakta oss via mail och ange ordernummer " + ordernummer));
        document.close();

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(path.toFile());
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Fel vid skapande av PDF: " + e.getMessage());
    }

    MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
    mainFrame.showPanel("Skapa ny order");
    }//GEN-LAST:event_btnBekraftaActionPerformed

    private void btnRedigeraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRedigeraActionPerformed
        redigeringsLage = true;
        btnSpara.setEnabled(true);
        btnTaBort.setEnabled(true);
        btnRedigera.setEnabled(false);
        btnBekrafta.setEnabled(true);

        tblOrdersammanfattning.repaint(); // Säkerställ att tabellen uppdateras visuellt
    }//GEN-LAST:event_btnRedigeraActionPerformed

    private void btnSparaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSparaActionPerformed

        if (tblOrdersammanfattning.isEditing()) {
            tblOrdersammanfattning.getCellEditor().stopCellEditing();
        }

        DefaultTableModel modell = (DefaultTableModel) tblOrdersammanfattning.getModel();
        boolean valid = true;

        //Gå igenom alla rader och validera antal
        for (int i = 0; i < modell.getRowCount(); i++) {
            Object cellValue = modell.getValueAt(i, 1); // kolumn "Antal"
            try {
                String cellStr = cellValue.toString().trim();

                //Kontrollera om cellen är tom
                if (cellStr.isEmpty()) {
                    throw new NumberFormatException("Tomt antal");
                }

                //Försök konvertera till ett heltal
                int antal = Integer.parseInt(cellStr);

                //Kontrollera om antalet är positivt
                if (antal <= 0) {
                    JOptionPane.showMessageDialog(this, "Antalet måste vara ett positivt heltal.");
                    valid = false;  //Valideringen misslyckades
                    break;  //Avbryt loopen om valideringen misslyckas
                }

                //Om antalet är giltigt, uppdatera orderraden och totalpris
                orderrader.get(i).setAntal(antal);
                modell.setValueAt(String.format("%.2f kr", antal * orderrader.get(i).getPris()), i, 3); //Uppdatera totalsumma

            } catch (NumberFormatException e) {
                //Visa felmeddelande för ogiltigt antal
                JOptionPane.showMessageDialog(this, "Antalet är inte ett giltigt heltal.");
                valid = false;  //Valideringen misslyckades
                break;  //Avbryt loopen om det finns ogiltigt värde
            }
        }

        //Om alla värden är giltiga, uppdatera totalpris och visa meddelande om sparade ändringar
        if (valid) {
            uppdateraTotalpris();  // Uppdatera totalpris efter validering
            JOptionPane.showMessageDialog(this, "Ändringar har sparats i varukorgen! Klicka på 'Bekräfta' för att skicka beställningen.");
            redigeringsLage = false;
            btnSpara.setEnabled(false);
            btnTaBort.setEnabled(false);
            btnRedigera.setEnabled(true);
        }
    }//GEN-LAST:event_btnSparaActionPerformed

    private void btnTaBortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaBortActionPerformed
        int valdRad = tblOrdersammanfattning.getSelectedRow();

        if (valdRad == -1) {
            JOptionPane.showMessageDialog(this, "Välj en produkt att ta bort.");
            return;
        }

        int bekrafta = JOptionPane.showConfirmDialog(this,
                "Vill du verkligen ta bort den här produkten?",
                "Bekräfta borttagning",
                JOptionPane.YES_NO_OPTION
        );

        if (bekrafta == JOptionPane.YES_OPTION) {
            DefaultTableModel modell = (DefaultTableModel) tblOrdersammanfattning.getModel();
            modell.removeRow(valdRad);
            orderrader.remove(valdRad);

            uppdateraTotalpris();

            if (orderrader.isEmpty()) {
                express = false;
                tfExpress.setText("0 kr");
                tfTotalpris.setText("0 kr");

                JOptionPane.showMessageDialog(this, "Varukorgen är tom och expresskostnad har nollställts.");
            }

            JOptionPane.showMessageDialog(this, "Produkten togs bort.");
        }
    }//GEN-LAST:event_btnTaBortActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBekrafta;
    private javax.swing.JButton btnRedigera;
    private javax.swing.JButton btnSpara;
    private javax.swing.JButton btnTaBort;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblExpressavgift;
    private javax.swing.JLabel lblTotalpris;
    private javax.swing.JTable tblOrdersammanfattning;
    private javax.swing.JTextField tfExpress;
    private javax.swing.JTextField tfKundID;
    private javax.swing.JTextField tfOrdernummer;
    private javax.swing.JTextField tfTotalpris;
    // End of variables declaration//GEN-END:variables
}
