/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package hattmakarna;

import com.itextpdf.barcodes.Barcode128;
import oru.inf.InfDB;
import oru.inf.InfException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.stream.Collectors;

//package com.itextpdf.highlevel.chapter01;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.awt.Desktop;
import com.itextpdf.layout.element.Image;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.FileAlreadyExistsException;
import java.util.List;
import java.util.Map;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.barcodes.Barcode128;
import java.awt.Desktop;
import java.nio.file.*;
import javax.swing.*;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.properties.HorizontalAlignment;
import java.net.URL;

/**
 *
 * @author iftinserar
 */
public class SkapaNyFraktsedel extends javax.swing.JPanel {

    private static InfDB idb;
    private static String inloggadAnvandare;
    private static String klickatOrderNr;

    /**
     * Creates new form SkapaNyFraktsedel
     */
    public SkapaNyFraktsedel(InfDB idb, String inloggadAnvandare, String klickatOrderNr) {
        this.inloggadAnvandare = inloggadAnvandare;
        this.idb = idb;
        this.klickatOrderNr = klickatOrderNr;
        initComponents();
        txtBestallningID.setText(klickatOrderNr);
    }

    private void skapaFraktsedel(Map<String, String> kundInfo, List<OrderRad> orderLista, double totalpris, String bestallningID) {
        try {
            // Kontrollera om beställningen redan har en fraktsedel
            String checkSQL = "SELECT FraktsedelID FROM Bestallning WHERE BestallningID = " + bestallningID;
            Map<String, String> fraktCheck = idb.fetchRow(checkSQL);

            boolean fraktsedelRedanFinns = fraktCheck != null && fraktCheck.get("FraktsedelID") != null;
            String fraktsedelID = "";

            // Om beställningen inte har ett fraktsedelID
            if (!fraktsedelRedanFinns) {
                // Data för fraktsedel
                String adress = kundInfo.get("LeveransAdress").replace("'", "''");  // Escape special characters
                String mottagare = (kundInfo.get("Fornamn") + " " + kundInfo.get("Efternamn")).replace("'", "''");
                String innehall = orderLista.stream()
                        .map(rad -> (rad.antal + "x " + rad.produktNamn).replace("'", "''"))
                        .collect(Collectors.joining(", "));
                String exportkod = "SE";
                double vikt = 1.0; // Placeholder, kan justeras beroende på produktvikt
                double moms = totalpris * 0.25;  // 25% moms
                double inklMoms = totalpris + moms;
                String datum = java.time.LocalDate.now().toString();

                // Spara fraktsedel i databasen
                String sql = String.format(
                        "INSERT INTO Fraktsedel (Adress, Avsandare, Mottagare, Innehåll, Exportkod, Pris, Datum, Vikt, Moms, PrisInklMoms) "
                        + "VALUES ('%s', '%s', '%s', '%s', '%s', %.2f, '%s', %.2f, %.2f, %.2f)",
                        adress, "Hattmakarna AB", mottagare, innehall, exportkod, totalpris, datum, vikt, moms, inklMoms
                );
                idb.insert(sql);

                // Hämta det genererade FraktsedelID
                fraktsedelID = idb.getAutoIncrement("Fraktsedel", "FraktsedelID");

                // Uppdatera Bestallning med det nya FraktsedelID
                String updateSQL = "UPDATE Bestallning SET FraktsedelID = " + fraktsedelID + " WHERE BestallningID = " + bestallningID;
                idb.update(updateSQL);

            } else {
                // Om fraktsedelID redan finns för denna beställning, använd det befintliga fraktsedelID
                fraktsedelID = fraktCheck.get("FraktsedelID");
            }

            // Skapa fraktsedel etikett PDF om det behövs
            skapaFraktsedelEtikettPDF(fraktsedelID, kundInfo, 1.0, bestallningID);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fel vid skapande av fraktsedel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void skapaFraktsedelEtikettPDF(String fraktsedelID, Map<String, String> kundInfo, double viktKg, String bestallningID) {
        try {
            Path path = Paths.get(System.getProperty("user.home"), "hattadmin", "Fraktsedel_" + fraktsedelID + ".pdf");
            Files.createDirectories(path.getParent());

            PdfWriter writer = new PdfWriter(path.toString());
            PdfDocument pdfDoc = new PdfDocument(writer);
            PageSize etikettSize = new PageSize(400, 800);
            pdfDoc.setDefaultPageSize(etikettSize);
            Document document = new Document(pdfDoc);
            document.setMargins(20, 20, 20, 20);

            PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normal = PdfFontFactory.createFont(StandardFonts.HELVETICA);

            URL imageURL = getClass().getResource("/hattmakarna/bilder/DHL-logga.png");
            if (imageURL != null) {
                Image dhlLogo = new Image(ImageDataFactory.create(imageURL));

                // Hämta bildens ursprungliga dimensioner
                float originalWidth = dhlLogo.getImageWidth();
                float originalHeight = dhlLogo.getImageHeight();

                // Maximal bredd (kan justeras för att passa layout)
                float maxWidth = 150;

                // Beräkna den nya höjden baserat på maximal bredd för att bevara proportionerna
                float newWidth = maxWidth;
                float newHeight = (newWidth / originalWidth) * originalHeight;

                // Sätt den nya bredden och höjden på bilden
                dhlLogo.setWidth(newWidth);
                dhlLogo.setHeight(newHeight);

                // Justera bildens horisontella position
                dhlLogo.setHorizontalAlignment(HorizontalAlignment.RIGHT);  // Placera logotypen till höger

                // Lägg till logotypen i dokumentet
                document.add(dhlLogo);
            } else {
                JOptionPane.showMessageDialog(this, "Kunde inte hitta DHL-logotypen!", "Fel", JOptionPane.ERROR_MESSAGE);
            }

            // Lägg till datumet som en text i dokumentet
            document.add(new Paragraph("Datum: " + java.time.LocalDate.now())
                    .setFontSize(8)
                    .setMarginBottom(10));

            // MOTTAGARE
            document.add(new Paragraph(kundInfo.get("Fornamn") + " " + kundInfo.get("Efternamn")).setFont(bold).setFontSize(10));
            document.add(new Paragraph(kundInfo.get("LeveransAdress") + " " + kundInfo.get("LeveransPostnummer")).setFont(normal).setFontSize(9));
            document.add(new Paragraph(kundInfo.get("LeveransOrt") + ", " + kundInfo.get("LeveransLand")).setFont(normal).setFontSize(9));
            document.add(new Paragraph("SE - " + java.time.LocalDate.now().getYear()).setFont(normal).setFontSize(9).setMarginBottom(15));

            // FRAKTTYP
            Paragraph frakttyp = new Paragraph("Varubrev Sverige").setFont(normal).setFontSize(9);
            Paragraph kod = new Paragraph("19").setFont(bold).setFontSize(14).setTextAlignment(TextAlignment.RIGHT);
            Table frakttypTable = new Table(new float[]{1, 1}).useAllAvailableWidth();
            frakttypTable.addCell(new Cell().add(frakttyp).setBorder(Border.NO_BORDER));
            frakttypTable.addCell(new Cell().add(kod).setBorder(Border.NO_BORDER));
            document.add(frakttypTable);

            document.add(new Paragraph("LF").setFont(bold).setFontSize(18));
            document.add(new Paragraph("Shipment ID: 00000000000000" + bestallningID).setFont(normal).setFontSize(8));
            document.add(new Paragraph("Package Code: PK    Parcel: 1/1    Weight: " + viktKg + " Kg")
                    .setFont(normal).setFontSize(8).setMarginBottom(20));

            // STRECKKOD 1
            Barcode128 barcode1 = new Barcode128(pdfDoc);
            barcode1.setCode("SE" + fraktsedelID);
            barcode1.setCodeType(Barcode128.CODE128);
            barcode1.setFont(null);

            // Minska X-faktorn för att öka streckdensiteten
            barcode1.setX(0.8f); // Fler streck
            barcode1.setBarHeight(80); // Sätt höjd på strecken

            Image code128Image = new Image(barcode1.createFormXObject(ColorConstants.BLACK, ColorConstants.BLACK, pdfDoc));
            code128Image.setWidth(etikettSize.getWidth() - 60); // Håll bredden på streckkoden under kontroll
            code128Image.setHeight(80); // Sätt höjd på streckkoden för att inte ta upp för mycket vertikalt utrymme
            code128Image.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // Lägg till streckkoden utan för mycket vertikalt utrymme
            document.add(code128Image);

            // Lägg till texten under streckkoden med mindre marginal
            Paragraph barcodeText1 = new Paragraph("Fraktsedel ID: " + "SE" + fraktsedelID)
                    .setFont(normal).setFontSize(8).setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(5); // Mindre marginal för att spara plats
            document.add(barcodeText1);

            // Lägg till mellanrum så den andra streckkoden inte hamnar för nära
            document.add(new Paragraph(" ").setMarginTop(10));  // Lämna lite mer utrymme för den andra streckkoden

            // STRECKKOD 2
            Barcode128 barcode2 = new Barcode128(pdfDoc);
            barcode2.setCode(fraktsedelID);
            barcode2.setCodeType(Barcode128.CODE128);
            barcode2.setFont(null);

            // Ändra X-faktorn för fler streck
            barcode2.setX(0.8f); // Fler streck i streckkoden
            barcode2.setBarHeight(80); // Höjd på strecken

            Image extraBarcode = new Image(barcode2.createFormXObject(ColorConstants.BLACK, ColorConstants.BLACK, pdfDoc));
            extraBarcode.setWidth(etikettSize.getWidth() - 60); // Håll bredden på streckkoden stabil
            extraBarcode.setHeight(80); // Sätt höjd på streckkoden för att bibehålla proportioner
            extraBarcode.setHorizontalAlignment(HorizontalAlignment.CENTER);

            // Lägg till streckkoden
            document.add(extraBarcode);

            // Lägg till text under streckkoden med mindre marginal
            Paragraph barcodeText2 = new Paragraph("Fraktsedel ID: " + fraktsedelID)
                    .setFont(normal).setFontSize(8).setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(5); // Mindre marginal för att minska vertikalt utrymme
            document.add(barcodeText2);

            document.close();

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(path.toFile());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fel vid skapande av fraktsedel PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public class OrderRad {

        int antal;
        String produktNamn;

        public OrderRad(int antal, String produktNamn) {
            this.antal = antal;
            this.produktNamn = produktNamn;
        }

        // Getters och setters om du behöver
        public int getAntal() {
            return antal;
        }

        public String getProduktNamn() {
            return produktNamn;
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

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtBestallningID = new javax.swing.JTextField();
        btnSkapaFraktsedel = new javax.swing.JButton();
        btnHamta = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBestallningInfo = new javax.swing.JTable();
        btnTillbaka = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        jLabel1.setText("Skapa ny fraktsedel");

        jLabel2.setText("Ordernummer");

        txtBestallningID.setEditable(false);

        btnSkapaFraktsedel.setText("Skapa");
        btnSkapaFraktsedel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSkapaFraktsedelActionPerformed(evt);
            }
        });

        btnHamta.setText("Hämta");
        btnHamta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHamtaActionPerformed(evt);
            }
        });

        tblBestallningInfo.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblBestallningInfo);

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(103, 103, 103)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtBestallningID, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnHamta))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnTillbaka)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSkapaFraktsedel)
                                .addGap(15, 15, 15)))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtBestallningID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHamta))
                .addGap(37, 37, 37)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSkapaFraktsedel)
                    .addComponent(btnTillbaka))
                .addGap(22, 22, 22))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnHamtaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHamtaActionPerformed
        String bestallningID = txtBestallningID.getText().trim();

        if (bestallningID.isEmpty() || !bestallningID.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Ange ett giltigt numeriskt beställnings-ID.");
            return;
        }

        try {
            String sql = "SELECT oi.AntalProdukter, "
                    + "COALESCE(stdp.Namn, specp.Text) AS ProduktNamn, "
                    + "COALESCE(stdp.Pris, specp.Pris) AS ProduktPris "
                    + "FROM OrderItem oi "
                    + "LEFT JOIN StandardProdukt stdp ON oi.StandardProduktID = stdp.StandardProduktID "
                    + "LEFT JOIN SpecialProdukt specp ON oi.SpecialProduktID = specp.SpecialProduktID "
                    + "WHERE oi.BestallningID = " + bestallningID + ";";

            ArrayList<HashMap<String, String>> produkter = idb.fetchRows(sql);

            if (produkter.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingen beställning hittades med ID: " + bestallningID);
                return;
            }

            String[] kolumner = {"Produkt", "Antal", "Pris (kr)"};
            DefaultTableModel modell = new DefaultTableModel(kolumner, 0);

            for (HashMap<String, String> rad : produkter) {
                String produktNamn = rad.get("ProduktNamn");
                int antal = Integer.parseInt(rad.get("AntalProdukter"));
                double prisPerSt = Double.parseDouble(rad.get("ProduktPris"));
                double totalPris = prisPerSt * antal;

                modell.addRow(new Object[]{
                    produktNamn,
                    antal,
                    String.format("%.2f", totalPris) + " kr"
                });
            }

            tblBestallningInfo.setModel(modell);

        } catch (InfException ex) {
            JOptionPane.showMessageDialog(this, "Fel vid hämtning: " + ex.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Fel vid beräkning av pris: " + e.getMessage());
        }
    }//GEN-LAST:event_btnHamtaActionPerformed

    private void btnSkapaFraktsedelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSkapaFraktsedelActionPerformed
        try {
            String bestallningID = txtBestallningID.getText().trim();

            if (bestallningID.isEmpty() || !bestallningID.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "Ange ett giltigt numeriskt beställnings-ID.");
                return;
            }

            // Kolla om beställningen redan har en fraktsedel kopplad
            String checkSQL = "SELECT FraktsedelID FROM Bestallning WHERE BestallningID = " + bestallningID;
            Map<String, String> fraktCheck = idb.fetchRow(checkSQL);
            boolean fraktsedelRedanFinns = fraktCheck != null && fraktCheck.get("FraktsedelID") != null;

            // Hämta kundinfo
            String sqlKund
                    = "SELECT k.Fornamn, k.Efternamn, k.Epost, k.Telefonnummer, "
                    + "k.LeveransAdress, k.LeveransOrt, k.LeveransPostnummer, k.LeveransLand, "
                    + "k.FakturaAdress, k.FakturaPostnummer, k.FakturaOrt, k.FakturaLand "
                    + "FROM Kund k "
                    + "JOIN Bestallning b ON k.KundID = b.KundID "
                    + "WHERE b.BestallningID = " + bestallningID;

            Map<String, String> kundInfo = idb.fetchRow(sqlKund);

            if (kundInfo == null || kundInfo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Kundinformation kunde inte hämtas. Kontrollera beställnings-ID.");
                return;
            }

            // Hämta produkter
            String sqlProdukter
                    = "SELECT oi.AntalProdukter, "
                    + "COALESCE(stdp.Namn, specp.Text) AS ProduktNamn, "
                    + "COALESCE(stdp.Pris, specp.Pris) AS ProduktPris "
                    + "FROM OrderItem oi "
                    + "LEFT JOIN StandardProdukt stdp ON oi.StandardProduktID = stdp.StandardProduktID "
                    + "LEFT JOIN SpecialProdukt specp ON oi.SpecialProduktID = specp.SpecialProduktID "
                    + "WHERE oi.BestallningID = " + bestallningID;

            ArrayList<HashMap<String, String>> produkter = idb.fetchRows(sqlProdukter);

            if (produkter == null || produkter.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Inga produkter hittades för denna beställning.");
                return;
            }

            // Skapa orderlista + totalpris
            List<OrderRad> pdforderlista = new ArrayList<>();
            double totalpris = 0.0;

            for (HashMap<String, String> rad : produkter) {
                int antal = Integer.parseInt(rad.get("AntalProdukter"));
                double pris = Double.parseDouble(rad.get("ProduktPris"));
                String namn = rad.get("ProduktNamn");

                pdforderlista.add(new OrderRad(antal, namn));
                totalpris += antal * pris;
            }

            if (totalpris <= 0) {
                JOptionPane.showMessageDialog(this, "Totalpriset verkar vara 0 kr. Kontrollera orderraderna.");
                return;
            }

            // Anropa metod som hanterar fraktsedelskapande och PDF
            skapaFraktsedel(kundInfo, pdforderlista, totalpris, bestallningID);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ett fel uppstod: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSkapaFraktsedelActionPerformed

    private void btnTillbakaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTillbakaActionPerformed
        MainFrame mainFrame = (MainFrame) SwingUtilities.getWindowAncestor(this);
        mainFrame.showPanel("Alla ordrar");
    }//GEN-LAST:event_btnTillbakaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHamta;
    private javax.swing.JButton btnSkapaFraktsedel;
    private javax.swing.JButton btnTillbaka;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblBestallningInfo;
    private javax.swing.JTextField txtBestallningID;
    // End of variables declaration//GEN-END:variables
}
