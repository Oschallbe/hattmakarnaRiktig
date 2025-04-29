/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hattmakarna;

import oru.inf.InfDB;
import oru.inf.InfException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Elin
 */
public class SeSpecialOrder extends javax.swing.JPanel {

    private InfDB idb;
    private String inloggadAnvandare;
    private String klickatOrderNr;
    private String express;
    private int kundID;

    /**
     * Creates new form SeSpecialOrder
     */
    public SeSpecialOrder(InfDB idb, String ePost, String klickatOrderNr) {
        initComponents();
        this.idb = idb;
        this.inloggadAnvandare = ePost;
        this.klickatOrderNr = klickatOrderNr;
        fyllTabell();
        fyllLabels();
        fyllAnstalldTabell();
    }

    //Metod för att fylla tabellerna med info från databasen. 
    public void fyllTabell() {
        try {
            // Skapar en array som lagrar kolumnnamnen (inklusive OrderItemID som vi gömmer sen).
            String kolumnNamn[] = {"OrderItemID", "Huvudmått", "Pris", "AntalProdukter", "AnstalldID"};

            // Skapar en DefaultTableModel som håller kolumnnamnen samt sätter antalet rader till noll.
            DefaultTableModel allaProdukter = new DefaultTableModel(kolumnNamn, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;  // Gör så att ingen cell är redigerbar
                }
            };

            // Hämtar alla OrderItemID för den valda beställningen.
            String selectOid = "select OrderItemID from orderitem where BestallningID = "
                    + klickatOrderNr + " order by(OrderItemID);";
            ArrayList<String> oid = idb.fetchColumn(selectOid);

            if (oid != null) {
                for (String ettOid : oid) {
                    String selectInfo = "select OrderItemID, AntalProdukter, AnstalldID, SpecialProduktID "
                            + "from orderitem where OrderItemID = " + ettOid + ";";
                    HashMap<String, String> info = idb.fetchRow(selectInfo);

                    // Hämtar AnstalldID och slår ihop till namn om det finns.
                    String aID = info.get("AnstalldID");
                    String hopslagetNamn = "";
                    if (aID != null && !aID.isEmpty()) {
                        String selectNamnAnstalld = "select Fornamn, Efternamn from anstalld "
                                + "where AnstalldID = " + aID + ";";
                        HashMap<String, String> anstalldFornamnEfternamn = idb.fetchRow(selectNamnAnstalld);
                        if (anstalldFornamnEfternamn != null) {
                            String fornamn = anstalldFornamnEfternamn.get("Fornamn");
                            String efternamn = anstalldFornamnEfternamn.get("Efternamn");
                            if (fornamn != null && efternamn != null) {
                                hopslagetNamn = fornamn + " " + efternamn;
                            }
                        }
                    }

                    // Hämtar "Matt" (huvudmått) och pris från specialprodukt.
                    String ettProduktID = info.get("SpecialProduktID");
                    String selectProdukt = "select Matt, Pris from specialprodukt "
                            + "where SpecialProduktID = " + ettProduktID + ";";
                    HashMap<String, String> infoNamnPris = idb.fetchRow(selectProdukt);

                    // Skapar en array som håller data för en rad i tabellen.
                    Object[] enRad = new Object[kolumnNamn.length];
                    enRad[0] = ettOid;                           // OrderItemID (dold)
                    enRad[1] = infoNamnPris.get("Matt");        // Huvudmått
                    enRad[2] = infoNamnPris.get("Pris");        // Pris
                    enRad[3] = info.get("AntalProdukter");      // Antal
                    enRad[4] = hopslagetNamn;                   // Tilldelad (namn eller tomt)

                    allaProdukter.addRow(enRad);
                }

                // Sätter modellen i JTable.
                tblAllaProdukter.setModel(allaProdukter);

                // Gör så att OrderItemID-kolumnen (index 0) blir osynlig:
                tblAllaProdukter.getColumnModel().getColumn(0).setMinWidth(0);
                tblAllaProdukter.getColumnModel().getColumn(0).setMaxWidth(0);
                tblAllaProdukter.getColumnModel().getColumn(0).setWidth(0);
            }

            tblAllaProdukter.setAutoResizeMode(tblAllaProdukter.AUTO_RESIZE_OFF);

            // Sätter storleken på de synliga kolumnerna.
            TableColumn col = tblAllaProdukter.getColumnModel().getColumn(1); // huvudmått
            col.setPreferredWidth(75);
            col = tblAllaProdukter.getColumnModel().getColumn(2);             // pris
            col.setPreferredWidth(75);
            col = tblAllaProdukter.getColumnModel().getColumn(3);             // antal
            col.setPreferredWidth(75);
            col = tblAllaProdukter.getColumnModel().getColumn(4);             // tilldelad
            col.setPreferredWidth(154);

            // Ändrar rubrikerna i tabellen.
            tblAllaProdukter.getColumnModel().getColumn(1).setHeaderValue("Huvudmått");
            tblAllaProdukter.getColumnModel().getColumn(2).setHeaderValue("Pris");
            tblAllaProdukter.getColumnModel().getColumn(3).setHeaderValue("Antal");
            tblAllaProdukter.getColumnModel().getColumn(4).setHeaderValue("Tilldelad:");

        } catch (InfException ex) {
            System.out.println(ex);
        }
    }

    public void fyllAnstalldTabell() {
        try {
            //Skapar en array som lagrar kolumnnamnen.
            String kolumnNamn[] = {"Anstallningsnr.", "Namn"};

            //Skapar en DefaultTableModel som håller kolumnnamnen samt sätter antalet rader till noll.
            DefaultTableModel allaAnstallda = new DefaultTableModel(kolumnNamn, 0);

            //Hämtar alla anställdas id och lägger det i Arraylistan "aid".
            String selectAid = "select AnstalldID from anstalld order by(AnstalldID);";
            ArrayList<String> aid = idb.fetchColumn(selectAid);

            //Om AnstalldId inte är null körs en for-each loop som för varje id hämtar id, förnamn och efternamn om det anställda som placeras i Hashmapen "Info".
            if (aid != null) {
                for (String ettAid : aid) {
                    String selectInfo = "select AnstalldID, Fornamn, Efternamn from anstalld where AnstalldID = " + ettAid + ";";
                    HashMap<String, String> info = idb.fetchRow(selectInfo);

                    //Om Hashmapen info inte är null så skapar vi lokalvariabeln hopslaget namn vilket håller både för- och efternamnet på den anställde.
                    if (info != null) {

                        String hopslagetNamn = info.get("Fornamn") + " " + info.get("Efternamn");

                        //Fyller tabellen med anställningsID och det hopslagna för- och efternamnet.
                        Object[] enRad = new Object[kolumnNamn.length];
                        enRad[0] = info.get("AnstalldID");
                        enRad[1] = hopslagetNamn;

                        allaAnstallda.addRow(enRad);
                    }

                }

                //Jtable sätts med data från DefaultTableModel.
                tblAllaAnstallda.setModel(allaAnstallda);

            }
        } catch (InfException ex) {
            System.out.println(ex);
        }
    }

    //Skapar en metod för att endast kunna redigera "Tilldelad:"-kolumnen i jtablen.
    private DefaultTableModel genereraRedigerbarModell() {
        String[] kolumnNamn = {"Ordernummer", "Huvudmått", "Pris", "Antal", "Tilldelad:"};

        //Skapar en ny defaulttablemodel.
        DefaultTableModel redigerbarModell = new DefaultTableModel(kolumnNamn, 0) {

            //Gör enbart kolumn 4 redigerbar genom att overridea isCellEditable.
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        //Hämtar tabellen allaProdukter och lägger den i aktuellTabell.
        DefaultTableModel aktuellTabell = (DefaultTableModel) tblAllaProdukter.getModel();

        //Går igenom varje rad i aktuellTabell.
        for (int r = 0; r < aktuellTabell.getRowCount(); r++) {

            //En array skapas som lagrar radens data.
            Object[] dataForEnRad = new Object[aktuellTabell.getColumnCount()];

            //Går igenom varje kolumn för raden och hämtar all data.
            for (int k = 0; k < aktuellTabell.getColumnCount(); k++) {
                dataForEnRad[k] = aktuellTabell.getValueAt(r, k);

            }
            //Varje rad adderas till den nya tabellen som går att redigera.
            redigerbarModell.addRow(dataForEnRad);
        }

        //Returnerar den nya tabellen. 
        return redigerbarModell;
    }

    public void sparaTilldelad() {
        try {
            // Hämtar modellen från JTable.
            DefaultTableModel tabell = (DefaultTableModel) tblAllaProdukter.getModel();

            // Går igenom varje rad i tabellen.
            for (int i = 0; i < tabell.getRowCount(); i++) {
                try {
                    // Hämtar OrderItemID (dolt, kolumn 0) och Tilldelad (kolumn 4).
                    String artNR = tabell.getValueAt(i, 0).toString();
                    //String artNR = lblOrderNr.getText();
                    String anstID = tabell.getValueAt(i, 4).toString();

                    String uppdateraDatabas;
                    // Om anstID är tomt så sätts det till NULL i databasen.
                    if (anstID == null || anstID.isEmpty()) {
                        uppdateraDatabas = "update orderitem set AnstalldID = null "
                                + "where OrderItemID = " + artNR + ";";
                    } else {
                        // Annars uppdateras med det angivna anställningsID:t.
                        uppdateraDatabas = "update orderitem set AnstalldID = "
                                + anstID + " where OrderItemID = " + artNR + ";";
                    }

                    // Kör uppdateringen.
                    idb.update(uppdateraDatabas);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Fel på rad: " + i + " – " + ex.getMessage());
                }
            }

            // Fyll tabellen igen så att ändringen syns.
            fyllTabell();

        } catch (InfException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    public void sparaStatus() {
        try {
            // Hämtar status från comboboxen och uppdaterar databasen.
            String status = (String) cbStatus.getSelectedItem();
            String updateStatus = "update bestallning set status = '"
                    + status + "' where BestallningID = '"
                    + klickatOrderNr + "';";
            idb.update(updateStatus);

        } catch (InfException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    public void fyllLabels() {
        try {
            // Hämta BestallningID baserat på det klickade ordernumret (klickatOrderNr)
            String selectOrderNr = "SELECT BestallningID FROM bestallning WHERE BestallningID = '" + klickatOrderNr + "'";
            String orderNr = idb.fetchSingle(selectOrderNr);  // Hämtar BestallningID från databasen
            lblOrderNr.setText(orderNr);  // Sätter BestallningID i labeln lblOrderNr

            // Hämta Tillverkningstid för specialprodukten baserat på SpecialProduktID
            String selectTillverkningstid = "SELECT Tillverkningstid FROM specialprodukt WHERE SpecialProduktID = '" + klickatOrderNr + "'";
            String tillverkningstid = idb.fetchSingle(selectTillverkningstid);  // Hämtar tillverkningstiden
            lblTillverkningstid2.setText(tillverkningstid);  // Sätter tillverkningstiden i labeln lblTillverkningstid2

            // Hämta kundID baserat på BestallningID
            String selectKundID = "SELECT KundID FROM bestallning WHERE BestallningID = '" + klickatOrderNr + "'";
            String kundIDStr = idb.fetchSingle(selectKundID);  // Hämtar KundID från databasen
            kundID = Integer.parseInt(kundIDStr);  // Omvandlar kundID till ett heltal (Integer)

            // Sätt endast kundID i labeln lblKundNr utan "KundID:" texten
            lblKundNr.setText(String.valueOf(kundID));  // Uppdaterar labeln med endast KundID (utan text)

            // Hämta Status baserat på BestallningID
            String selectStatus = "SELECT Status FROM bestallning WHERE BestallningID = '" + klickatOrderNr + "'";
            String status = idb.fetchSingle(selectStatus);  // Hämtar status för beställningen
            cbStatus.setSelectedItem(status);  // Sätter status i dropdown-menyn cbStatus
            cbStatus.setEnabled(false);  // Inaktiverar dropdownen så användaren inte kan ändra statusen

        } catch (InfException ex) {
            // Hantera eventuella fel som kan uppstå under databasfrågor eller uppdatering av labels
            JOptionPane.showMessageDialog(this, "Fel i fyllLabels: " + ex.getMessage());
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSeSpecifikProdukt = new javax.swing.JButton();
        lblSpecialorder = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox<>();
        lblOrderNr = new javax.swing.JLabel();
        btnRedigeraStatus = new javax.swing.JButton();
        lblTillverkningstid = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAllaAnstallda = new javax.swing.JTable();
        lblTillverkningstid2 = new javax.swing.JLabel();
        lblKund = new javax.swing.JLabel();
        btnAtaProdukt = new javax.swing.JButton();
        lblKundNr = new javax.swing.JLabel();
        btnSpara = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAllaProdukter = new javax.swing.JTable();
        btnSeKundinfo = new javax.swing.JButton();
        btnTillbaka = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();

        setLayout(null);

        btnSeSpecifikProdukt.setText("Se information om produkt");
        btnSeSpecifikProdukt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeSpecifikProduktActionPerformed(evt);
            }
        });
        add(btnSeSpecifikProdukt);
        btnSeSpecifikProdukt.setBounds(160, 460, 210, 27);

        lblSpecialorder.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblSpecialorder.setText("Specialorder:");
        add(lblSpecialorder);
        lblSpecialorder.setBounds(170, 20, 170, 25);

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Under behandling", "Produktion pågår", "Packas", "Skickad", "Levererad", "Returnerad" }));
        add(cbStatus);
        cbStatus.setBounds(260, 160, 133, 26);

        lblOrderNr.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblOrderNr.setText("jLabel1");
        add(lblOrderNr);
        lblOrderNr.setBounds(340, 20, 81, 25);

        btnRedigeraStatus.setText("Redigera status");
        btnRedigeraStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRedigeraStatusActionPerformed(evt);
            }
        });
        add(btnRedigeraStatus);
        btnRedigeraStatus.setBounds(380, 460, 150, 27);

        lblTillverkningstid.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTillverkningstid.setText("Tillverkningstid:");
        add(lblTillverkningstid);
        lblTillverkningstid.setBounds(170, 120, 110, 20);

        tblAllaAnstallda.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tblAllaAnstallda);

        add(jScrollPane2);
        jScrollPane2.setBounds(590, 220, 196, 227);

        lblTillverkningstid2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTillverkningstid2.setText("jLabel1");
        add(lblTillverkningstid2);
        lblTillverkningstid2.setBounds(300, 120, 140, 20);

        lblKund.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKund.setText("Kund: ");
        add(lblKund);
        lblKund.setBounds(170, 80, 120, 20);

        btnAtaProdukt.setText("Åta produkt");
        btnAtaProdukt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtaProduktActionPerformed(evt);
            }
        });
        add(btnAtaProdukt);
        btnAtaProdukt.setBounds(560, 460, 150, 27);

        lblKundNr.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKundNr.setText("jLabel1");
        add(lblKundNr);
        lblKundNr.setBounds(220, 80, 300, 20);

        btnSpara.setText("Spara");
        btnSpara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSparaActionPerformed(evt);
            }
        });
        add(btnSpara);
        btnSpara.setBounds(720, 460, 76, 27);

        tblAllaProdukter.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ));
        tblAllaProdukter.setDragEnabled(true);
        jScrollPane1.setViewportView(tblAllaProdukter);

        add(jScrollPane1);
        jScrollPane1.setBounds(160, 220, 410, 227);

        btnSeKundinfo.setText("Se kundinformation");
        btnSeKundinfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeKundinfoActionPerformed(evt);
            }
        });
        add(btnSeKundinfo);
        btnSeKundinfo.setBounds(260, 80, 180, 27);

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
            }
        });
        add(btnTillbaka);
        btnTillbaka.setBounds(640, 30, 90, 27);

        lblStatus.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblStatus.setText("Status: ");
        add(lblStatus);
        lblStatus.setBounds(170, 160, 45, 20);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSeSpecifikProduktActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeSpecifikProduktActionPerformed
        try {
            int valdRad = tblAllaProdukter.getSelectedRow();

            if (valdRad == -1) {
                JOptionPane.showMessageDialog(this, "Markera en rad för att se produkten.");
                return;
            }

            // Hämta OrderItemID från tabellen
            String orderItemID = tblAllaProdukter.getValueAt(valdRad, 0).toString();

            // Hämta antal från tabellen
            String antal = tblAllaProdukter.getValueAt(valdRad, 3).toString();

            //Hämta det faktiska SpecialProduktID:t från OrderItem-tabellen
            String specialProduktID = idb.fetchSingle("SELECT SpecialProduktID FROM OrderItem WHERE OrderItemID = " + orderItemID + ";");

            if (specialProduktID != null && !specialProduktID.equals("null")) {
                SeInfoSpecialprodukt nyVy = new SeInfoSpecialprodukt(idb, inloggadAnvandare, specialProduktID, Integer.parseInt(antal));
                nyVy.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Ingen specialprodukt kopplad till denna orderrad.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Kunde inte öppna produkten: " + ex.getMessage());
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnSeSpecifikProduktActionPerformed

    private void btnRedigeraStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRedigeraStatusActionPerformed
        cbStatus.setEnabled(true);
    }//GEN-LAST:event_btnRedigeraStatusActionPerformed

    private void btnAtaProduktActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtaProduktActionPerformed

        //Visar en popup-ruta när man klickar på "Åta produkt" som förklarar hur man ska gå tillväga.
        javax.swing.JOptionPane.showMessageDialog(this, "Tilldela en produkt genom att fylla i rutan Tilldelad med anställningdID. "
                + "OBS klicka ENTER innan du klickar på spara knappen!");

        //Tabellen som visas uppdateras till den tabell där det går att redigera anställningsID i.
        DefaultTableModel redigerbarModell = genereraRedigerbarModell();
        tblAllaProdukter.setModel(redigerbarModell);

        tblAllaProdukter.setEnabled(true);
    }//GEN-LAST:event_btnAtaProduktActionPerformed

    private void btnSparaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSparaActionPerformed
        try {
            sparaTilldelad();
            sparaStatus();
            cbStatus.setEnabled(false);
            JOptionPane.showMessageDialog(null, "Ändring sparad!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }//GEN-LAST:event_btnSparaActionPerformed
    private void btnSeKundinfoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String kundNrText = lblKundNr.getText();
            if (kundNrText != null && !kundNrText.isEmpty()) {
                int kundID = Integer.parseInt(kundNrText);

                MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);

                //Lägg till denna panel i CardLayout med ett namn
                main.addPanelToCardLayout(this, "SeSpecialOrder");

                // Skapa en panel för kund och skicka namnet på denna panel
                SpecifikKund kundPanel = new SpecifikKund(idb, inloggadAnvandare, kundID, "SeSpecialOrder");

                //Lägg till kundpanelen
                main.addPanelToCardLayout(kundPanel, "specifikKundPanel");

                //Visa kundpanelen
                main.showPanel("specifikKundPanel");

            } else {
                JOptionPane.showMessageDialog(this, "Kundnummer saknas.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Kundnummer är ogiltigt.");
        }

    }

    /*
    private void btnSeKundinfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeKundinfoActionPerformed
  
 
    }//GEN-LAST:event_btnSeKundinfoActionPerformed
*/
    private void btnTillbakaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTillbakaActionPerformed
        new SeAllaOrdrar(idb, inloggadAnvandare).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnTillbakaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtaProdukt;
    private javax.swing.JButton btnRedigeraStatus;
    private javax.swing.JButton btnSeKundinfo;
    private javax.swing.JButton btnSeSpecifikProdukt;
    private javax.swing.JButton btnSpara;
    private javax.swing.JButton btnTillbaka;
    private javax.swing.JComboBox<String> cbStatus;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblKund;
    private javax.swing.JLabel lblKundNr;
    private javax.swing.JLabel lblOrderNr;
    private javax.swing.JLabel lblSpecialorder;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JLabel lblTillverkningstid;
    private javax.swing.JLabel lblTillverkningstid2;
    private javax.swing.JTable tblAllaAnstallda;
    private javax.swing.JTable tblAllaProdukter;
    // End of variables declaration//GEN-END:variables
}
