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
        //this.kundID = kundID; 
        fyllTabell();
        fyllLabels();
        //hamtaTotalPris();
        fyllAnstalldTabell();
    }

    //Metod för att fylla tabellerna med info från databasen. 
    public void fyllTabell() {
        try {
            // Skapar en array som lagrar kolumnnamnen utan "OrderItemID" (Artikelnummer).
            String kolumnNamn[] = {"Huvudmått", "Pris", "AntalProdukter", "AnstalldID"};

            // Skapar en DefaultTableModel som håller kolumnnamnen samt sätter antalet rader till noll.
            DefaultTableModel allaProdukter = new DefaultTableModel(kolumnNamn, 0) {
                // Gör att tabellen inte går att redigera, men det går fortfarande att markera en rad i tabellen.
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // Hämtar alla anställdas id och lägger det i Arraylistan "id".
            String selectOid = "select OrderItemID from orderitem where BestallningID = " + klickatOrderNr + " order by(OrderItemID);";
            ArrayList<String> oid = idb.fetchColumn(selectOid);

            // Om AnstalldId inte är tom körs en for-each loop som för varje id hämtar id, förnamn och efternamn om det anställda som placeras i Hashmapen "Info".
            if (oid != null) {
                for (String ettOid : oid) {
                    String selectInfo = "select OrderItemID, AntalProdukter, AnstalldID, SpecialProduktID from orderitem where OrderItemID = " + ettOid + ";";
                    HashMap<String, String> info = idb.fetchRow(selectInfo);

                    // Hämtar AnstalldID och lagrar det i lokalvariabeln aID.
                    String aID = info.get("AnstalldID");

                    // Skapar en tom sträng som default för hopslaget namn.
                    String hopslagetNamn = "";

                    // Om aID inte är null och inte tomt hämtas namn på anställd.
                    if (aID != null && !aID.isEmpty()) {
                        String selectNamnAnstalld = "select Fornamn, Efternamn from anstalld where AnstalldID = " + aID + ";";
                        HashMap<String, String> anstalldFornamnEfternamn = idb.fetchRow(selectNamnAnstalld);

                        // Om vi får ett resultat, slå ihop förnamn och efternamn.
                        if (anstalldFornamnEfternamn != null) {
                            String fornamn = anstalldFornamnEfternamn.get("Fornamn");
                            String efternamn = anstalldFornamnEfternamn.get("Efternamn");

                            if (fornamn != null && efternamn != null) {
                                hopslagetNamn = fornamn + " " + efternamn;
                            }
                        }
                    }

                    // Hämtar pris och namn för produkten från tabellen "specialprodukt".
                    String ettProduktID = info.get("SpecialProduktID");
                    String selectProdukt = "select Matt, Pris from specialprodukt where SpecialProduktID = " + ettProduktID + ";";
                    HashMap<String, String> infoNamnPris = idb.fetchRow(selectProdukt);

                    // Skapar en array som håller data för en rad i tabellen, utan "OrderItemID" (Artikelnummer).
                    Object[] enRad = new Object[kolumnNamn.length];
                    enRad[0] = infoNamnPris.get("Matt"); // visas som "Huvudmått" i kolumn
                    enRad[1] = infoNamnPris.get("Pris");
                    enRad[2] = info.get("AntalProdukter");
                    enRad[3] = hopslagetNamn;  // Anställdens namn

                    allaProdukter.addRow(enRad);
                }

                // Jtable sätts med data från DefaultTableModel.
                tblAllaProdukter.setModel(allaProdukter);
            }

            tblAllaProdukter.setAutoResizeMode(tblAllaProdukter.AUTO_RESIZE_OFF);

            // Sätter storleken på tabellen.
            TableColumn col = tblAllaProdukter.getColumnModel().getColumn(0); // huvudmått
            col.setPreferredWidth(103);

            col = tblAllaProdukter.getColumnModel().getColumn(1); // pris
            col.setPreferredWidth(75);

            col = tblAllaProdukter.getColumnModel().getColumn(2); // antal
            col.setPreferredWidth(75);

            col = tblAllaProdukter.getColumnModel().getColumn(3); // tilldelad
            col.setPreferredWidth(154);

            // Ändrar rubrikerna i tabellen.
            tblAllaProdukter.getColumnModel().getColumn(0).setHeaderValue("Huvudmått (cm)");
            tblAllaProdukter.getColumnModel().getColumn(1).setHeaderValue("Pris");
            tblAllaProdukter.getColumnModel().getColumn(2).setHeaderValue("Antal");
            tblAllaProdukter.getColumnModel().getColumn(3).setHeaderValue("Tilldelad:");

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
        String[] kolumnNamn = {"Artikelnummer", "Matt", "Pris", "Antal", "Tilldelad:"};

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
            //Hämtar datan från tblAllaProdukter och lägger den i "tabell".
            DefaultTableModel tabell = (DefaultTableModel) tblAllaProdukter.getModel();

            //Går igenom varje rad i tabellen "tabell".
            for (int i = 0; i < tabell.getRowCount(); i++) {

                //Försöker hämta artikelnummer och anställningsid från kolumn 1(0) och 5(4) för varje rad.
                try {
                    String artNR = tabell.getValueAt(i, 0).toString();
                    String anstID = tabell.getValueAt(i, 4).toString();

                    //Skapar lokalvariabeln uppdateraDatabas.
                    String uppdateraDatabas;

                    //Om anstID är null eller rutan är tom så ändras uppdateraDatabas till att AnstalldID ska vara null för den raden 
                    if (anstID == null || anstID.isEmpty()) {
                        uppdateraDatabas = "update orderitem set AnstalldID = null where OrderItemID = " + artNR + ";";
                    } //Annars sätts AnstalldID till det anställningsid som hämtas och läggs i uppdateraDatabas
                    else {
                        uppdateraDatabas = "update orderitem set AnstalldID = " + anstID + " where OrderItemID = " + artNR + ";";
                    }

                    //Uppdaterar databasen med det värde vi lagrat i uppdateraDatabas
                    idb.update(uppdateraDatabas);

                } catch (NumberFormatException ex) {
                    System.out.println("Fel på rad: " + i + ":" + ex.getMessage());
                }
            }

            fyllTabell();

        } catch (InfException ex) {
            System.out.println(ex);
        }
    }

    public void sparaStatus() {
        try {
            //Hämtar status från comboboxen och uppdaterar databasen till det nya värdet.
            String status = (String) cbStatus.getSelectedItem();
            String updateStatus = "update bestallning set status = '" + status + "' where BestallningID = '" + klickatOrderNr + "';";
            idb.update(updateStatus);

        } catch (InfException ex) {
            System.out.println(ex);
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
            System.out.println("Fel i fyllLabels: " + ex.getMessage());
        }

//    try {
//        String selectOrderNr = "select SpecialProduktID from specialprodukt where SpecialProduktID = '" + klickatOrderNr + "';";
//        String orderNr = idb.fetchSingle(selectOrderNr);
//        lblOrderNr.setText(orderNr);
//
//        String selectTillverkningstid = "select Tillverkningstid from specialprodukt where SpecialProduktID = '" + klickatOrderNr + "';";
//        String tillverkningstid = idb.fetchSingle(selectTillverkningstid);
//        lblTillverkningstid2.setText(tillverkningstid);
//
//        String selectKund = "SELECT KundID, Fornamn, Efternamn FROM kund WHERE KundID = '" + klickatOrderNr + "'";
//        HashMap<String, String> kund = idb.fetchRow(selectKund);
//
//        if (kund != null) {
//            kundID = Integer.parseInt(kund.get("KundID")); // Spara endast siffran här
//            String kundIDochNamn = kund.get("KundID") + " – " + kund.get("Fornamn") + " " + kund.get("Efternamn");
//            lblKundNr.setText(kundIDochNamn);
//        } else {
//            lblKundNr.setText("Ingen kund hittades.");
//        }
//
//        String selectStatus = "select Status from bestallning where BestallningID = '" + klickatOrderNr + "';";
//        String status = idb.fetchSingle(selectStatus);
//        cbStatus.setSelectedItem(status); 
//        cbStatus.setEnabled(false); 
//
//    } catch (InfException ex) {
//        System.out.println(ex);
//    }
    }

    // Metod som fyller i labels med information om en specifik specialprodukt.
    /*
    public void fyllLabels() {
        //Dessa metoder hämtar info från databasen och fyler textfälten. 
        try {
            String selectOrderNr = "select SpecialProduktID from specialprodukt where SpecialProduktID = '" + klickatOrderNr + "';";
            String orderNr = idb.fetchSingle(selectOrderNr);
            lblOrderNr.setText(orderNr);

            String selectTillverkningstid = "select Tillverkningstid from specialprodukt where SpecialProduktID = '" + klickatOrderNr + "';";
            String tillverkningstid = idb.fetchSingle(selectTillverkningstid);
            lblTillverkningstid2.setText(tillverkningstid);

            String selectKund = "SELECT KundID, Fornamn, Efternamn FROM kund WHERE KundID = '" + klickatOrderNr + "'";
            HashMap<String, String> kund = idb.fetchRow(selectKund);

            if (kund != null) {
                String kundIDochNamn = kund.get("KundID") + " – " + kund.get("Fornamn") + " " + kund.get("Efternamn");
                lblKundNr.setText(kundIDochNamn);
            } else {
                lblKundNr.setText("Ingen kund hittades.");
            }

            String selectStatus = "select Status from bestallning where BestallningID = '" + klickatOrderNr + "';";
            String status = idb.fetchSingle(selectStatus);
            cbStatus.setSelectedItem(status); 
            cbStatus.setEnabled(false); 

        } // Om något går fel vid databasanrop skrivs felet ut i konsolen.
        catch (InfException ex) {
            System.out.println(ex);

        }
    }
     */
 /*public void hamtaTotalPris() {
        try {
            double totalPris = 0.0;
            DefaultTableModel prisTabell = (DefaultTableModel) tblTabell.getModel();

            for (int i = 0; i < prisTabell.getRowCount(); i++) {
                Object ettPris = prisTabell.getValueAt(i, 0); //Ändras till fyra när listan är klar!
                Object ettAntal = prisTabell.getValueAt(i, 1);
                System.out.println(ettPris);
                System.out.println(ettAntal);

                try {
                    double pris = Double.parseDouble(ettPris.toString());
                    int antal = Integer.parseInt(ettAntal.toString());
                    totalPris = totalPris + (pris * antal);
                } catch (NumberFormatException ex) {
                    System.out.println(ex);
                }

            }
            lblPris.setText(String.valueOf(totalPris));
        } catch (NumberFormatException ex) {
            System.out.println(ex);
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
        btnSeSpecifikProdukt.setBounds(160, 460, 210, 23);

        lblSpecialorder.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblSpecialorder.setText("Specialorder:");
        add(lblSpecialorder);
        lblSpecialorder.setBounds(170, 20, 150, 32);

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Under behandling", "Produktion pågår", "Packas", "Skickad", "Levererad", "Returnerad" }));
        add(cbStatus);
        cbStatus.setBounds(260, 160, 129, 22);

        lblOrderNr.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblOrderNr.setText("jLabel1");
        add(lblOrderNr);
        lblOrderNr.setBounds(340, 20, 81, 32);

        btnRedigeraStatus.setText("Redigera status");
        btnRedigeraStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRedigeraStatusActionPerformed(evt);
            }
        });
        add(btnRedigeraStatus);
        btnRedigeraStatus.setBounds(380, 460, 150, 23);

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
        lblKund.setBounds(170, 80, 39, 20);

        btnAtaProdukt.setText("Åta produkt");
        btnAtaProdukt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtaProduktActionPerformed(evt);
            }
        });
        add(btnAtaProdukt);
        btnAtaProdukt.setBounds(560, 460, 150, 23);

        lblKundNr.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKundNr.setText("jLabel1");
        add(lblKundNr);
        lblKundNr.setBounds(220, 80, 30, 20);

        btnSpara.setText("Spara");
        btnSpara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSparaActionPerformed(evt);
            }
        });
        add(btnSpara);
        btnSpara.setBounds(720, 460, 72, 23);

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
        btnSeKundinfo.setBounds(260, 80, 180, 23);

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
            }
        });
        add(btnTillbaka);
        btnTillbaka.setBounds(640, 30, 90, 23);

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

            // 🧠 Hämta det faktiska SpecialProduktID:t från OrderItem-tabellen
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
            System.out.println(ex);
        }
    }//GEN-LAST:event_btnSparaActionPerformed
    private void btnSeKundinfoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (kundID > 0) {
                JPanel kundPanel = new SpecifikKund(idb, inloggadAnvandare, kundID, "SeAllaOrdrar");
                MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);
                main.addPanelToCardLayout(kundPanel, "specifikKundPanel");
                main.showPanel("specifikKundPanel");
            } else {
                JOptionPane.showMessageDialog(this, "Kundnummer saknas eller kunde inte laddas.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Kunde inte öppna kundinformationen: " + e.getMessage());
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
