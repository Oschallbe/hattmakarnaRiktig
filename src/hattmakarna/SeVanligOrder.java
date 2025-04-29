/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package hattmakarna;

import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import oru.inf.InfDB;
import oru.inf.InfException;

/**
 *
 * @author iftinserar
 */
public class SeVanligOrder extends javax.swing.JPanel {

    private InfDB idb;
    private String inloggadAnvandare;
    private String klickatOrderNr;
    private String express;


    public SeVanligOrder(InfDB idb, String ePost, String klickatOrderNr) {
        initComponents();
        this.idb = idb;
        this.inloggadAnvandare = ePost;
        this.klickatOrderNr = klickatOrderNr;
        fyllTabell();
        fyllLables();
        hamtaTotalPris();
        fyllAnstalldTabell();
        tblAllaProdukter.setEnabled(true);

    }

    public void fyllTabell() {
        try {
            //Skapar en array som lagrar kolumnnamnen.
            String kolumnNamn[] = {"Artikelnummer", "Namn", "Pris", "AntalProdukter", "AnstalldID", "ProduktID"};

            //Skapar en DefaultTableModel som håller kolumnnamnen samt sätter antalet rader till noll.
            DefaultTableModel allaProdukter = new DefaultTableModel(kolumnNamn, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            //Hämtar alla anställdas id och lägger det i Arraylistan "id".
            String selectOid = "SELECT OrderItemID FROM orderitem WHERE BestallningID = " + klickatOrderNr + " ORDER BY OrderItemID;";
            ArrayList<String> oid = idb.fetchColumn(selectOid);

            // Om AnstalldId inte är tom körs en for-each loop som för varje id hämtar id, förnamn och efternamn om det anställda som placeras i Hashmapen "Info".
            if (oid != null) {
                for (String ettOid : oid) {
                    String selectInfo = "select OrderItemID, AntalProdukter, AnstalldID, StandardProduktID from orderitem where OrderItemID = " + ettOid + ";";
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

                    //Hämtar pris och namn för produkten från tabellen "standardprodukt".
                    String ettProduktID = info.get("StandardProduktID");
                    String selectProdukt = "SELECT Artikelnummer, Namn, Pris FROM StandardProdukt WHERE StandardProduktID = " + ettProduktID + ";";
                    HashMap<String, String> produktData = idb.fetchRow(selectProdukt);

                    //Skapar en array som håller data för en rad i tabellen.
                    Object[] enRad = new Object[kolumnNamn.length];
                    enRad[0] = produktData.get("Artikelnummer");
                    enRad[1] = produktData.get("Namn");
                    enRad[2] = produktData.get("Pris");
                    enRad[3] = info.get("AntalProdukter");
                    enRad[4] = hopslagetNamn;
                    enRad[5] = ettProduktID;
                    allaProdukter.addRow(enRad);

                }

                //Jtable sätts med data från DefaultTableModel.
                tblAllaProdukter.setModel(allaProdukter);

            }

            sattStorlekTabell();

            //Ändrar rubrikerna i tabellen.
            tblAllaProdukter.getColumnModel().getColumn(0).setHeaderValue("Artikelnummer");
            tblAllaProdukter.getColumnModel().getColumn(1).setHeaderValue("Namn");
            tblAllaProdukter.getColumnModel().getColumn(2).setHeaderValue("Pris");
            tblAllaProdukter.getColumnModel().getColumn(3).setHeaderValue("Antal");
            tblAllaProdukter.getColumnModel().getColumn(4).setHeaderValue("Tilldelad:");
            tblAllaProdukter.getColumnModel().getColumn(5).setMinWidth(0);
            tblAllaProdukter.getColumnModel().getColumn(5).setMaxWidth(0);
            tblAllaProdukter.getColumnModel().getColumn(5).setWidth(0);

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
        String[] kolumnNamn = {"Artikelnummer", "Namn", "Pris", "Antal", "Tilldelad:"};

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

    public void hamtaTotalPris() {
        try {

            //Sätter totalpris till 0 och hämtar Jtable och lägger i pristabell.
            double totalPris = 0.0;
            DefaultTableModel prisTabell = (DefaultTableModel) tblAllaProdukter.getModel();

            //Loopar igenom pristabellen och hämtar och lagrar pris och antal för varje rad.
            for (int i = 0; i < prisTabell.getRowCount(); i++) {
                Object ettPris = prisTabell.getValueAt(i, 2);
                Object ettAntal = prisTabell.getValueAt(i, 3);

                //Gör om pris och antal till String och beräknar totalpris.
                try {
                    double pris = Double.parseDouble(ettPris.toString());
                    int antal = Integer.parseInt(ettAntal.toString());
                    totalPris = totalPris + (pris * antal);
                } catch (NumberFormatException ex) {
                    System.out.println(ex);
                }

            }
            //Om expressleverans är satt till "Ja" multipliceras totalpriset med 1,2 och det totala priset uppdateras.
            if (express.equals("Ja")) {
                totalPris = totalPris * 1.2;
            }

            //Sätter label till det totalpris som beräknats.
            lblPris.setText(String.format("%.1f", totalPris));
        } catch (NumberFormatException ex) {
            System.out.println(ex);
        }
    }

    public void fyllLables() {

        try {
            //Fyller labeles med information från den specifika ordern man klickat på.
            String selectOrderNr = "select BestallningID from bestallning where BestallningID = '" + klickatOrderNr + "';";
            String orderNr = idb.fetchSingle(selectOrderNr);
            lblOrderNr.setText(orderNr);

            String selectKund = "select KundID from bestallning where BestallningID = '" + klickatOrderNr + "';";
            String kundNr = idb.fetchSingle(selectKund);
            lblKundNr.setText(kundNr);

            String selectDatum = "select Datum from bestallning where BestallningID = '" + klickatOrderNr + "';";
            String datum = idb.fetchSingle(selectDatum);
            lblDatum.setText(datum);

            //Hämtar statusen och sätter comboboxen till det värdet. Gör även så att comboboxen inte går att redigera till en början.
            String selectStatus = "select Status from bestallning where BestallningID = '" + klickatOrderNr + "';";
            String status = idb.fetchSingle(selectStatus);
            comboStatus.setSelectedItem(status);
            comboStatus.setEnabled(false);

            String selectExpress = "select Expressbestallning from bestallning where BestallningID = '" + klickatOrderNr + "';";
            express = idb.fetchSingle(selectExpress);

            //Om värdet som hämtas från databasen för expressleverans är "1" ska det bytas ut mot "ja", annars sätt expressleveransen till "nej".
            if (express.equals("1")) {
                express = "Ja";
            } else {
                express = "Nej";
            }
            lblExpress.setText(express);

        } catch (InfException ex) {
            System.out.println(ex);

        }
    }

    public void sattStorlekTabell() {

        //Storleken på kolumnerna justeras.
        tblAllaProdukter.setAutoResizeMode(tblAllaProdukter.AUTO_RESIZE_OFF);

        //Sätter storleken på tabellen.
        TableColumn col = tblAllaProdukter.getColumnModel().getColumn(0); //id
        col.setPreferredWidth(100);

        col = tblAllaProdukter.getColumnModel().getColumn(1); //namn.
        col.setPreferredWidth(200);

        col = tblAllaProdukter.getColumnModel().getColumn(2); //pris.
        col.setPreferredWidth(75);

        col = tblAllaProdukter.getColumnModel().getColumn(3); //antal.
        col.setPreferredWidth(75);

        col = tblAllaProdukter.getColumnModel().getColumn(4); //anstalldID.
        col.setPreferredWidth(154);
    }

    public void sparaTilldelad() {
        try {
            DefaultTableModel model = (DefaultTableModel) tblAllaProdukter.getModel();

            for (int i = 0; i < model.getRowCount(); i++) {
                String artNR = model.getValueAt(i, 0).toString();    // artikelnummer
                String anstID = model.getValueAt(i, 4).toString();   // nu ID

                // om tomt → NULL, annars siffran
                String idText = anstID.isEmpty() ? "NULL" : anstID;

                String sql
                        = "UPDATE OrderItem oi "
                        + "JOIN StandardProdukt sp ON oi.StandardProduktID = sp.StandardProduktID "
                        + "SET oi.AnstalldID = " + idText + " "
                        + "WHERE sp.Artikelnummer = '" + artNR + "' "
                        + "  AND oi.BestallningID = " + klickatOrderNr + ";";

                System.out.println("Kör SQL: " + sql);
                idb.update(sql);
            }

            fyllTabell();  // ladda om med nya ID:n
        } catch (InfException ex) {
            System.err.println("Databasfel: " + ex.getMessage());
        }
    }


    public void sparaStatus() {
        try {
            //Hämtar status från comboboxen och uppdaterar databasen till det nya värdet.
            String status = (String) comboStatus.getSelectedItem();
            String updateStatus = "update bestallning set status = '" + status + "' where BestallningID = '" + klickatOrderNr + "';";
            idb.update(updateStatus);

        } catch (InfException ex) {
            JOptionPane.showMessageDialog(this, ex);
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

        jLabel5 = new javax.swing.JLabel();
        btnSpara = new javax.swing.JButton();
        lblOrderNr = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblAllaAnstallda = new javax.swing.JTable();
        lblKundNr = new javax.swing.JLabel();
        lblDatum = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblPris = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAllaProdukter = new javax.swing.JTable();
        comboStatus = new javax.swing.JComboBox<>();
        btnRedigeraStatus = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnTillbaka = new javax.swing.JButton();
        btnSeProduktOmProdukt = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnAtaProdukt = new javax.swing.JButton();
        btnSeKund = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblExpress = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        setLayout(null);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Totalpris:");
        add(jLabel5);
        jLabel5.setBounds(120, 250, 140, 25);

        btnSpara.setText("Spara");
        btnSpara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSparaActionPerformed(evt);
            }
        });
        add(btnSpara);
        btnSpara.setBounds(660, 490, 76, 27);

        lblOrderNr.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblOrderNr.setText("jLabel6");
        add(lblOrderNr);
        lblOrderNr.setBounds(210, 30, 110, 25);

        tblAllaAnstallda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        jScrollPane2.setViewportView(tblAllaAnstallda);

        add(jScrollPane2);
        jScrollPane2.setBounds(780, 330, 214, 154);

        lblKundNr.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblKundNr.setText("jLabel7");
        add(lblKundNr);
        lblKundNr.setBounds(200, 80, 200, 25);

        lblDatum.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblDatum.setText("jLabel8");
        add(lblDatum);
        lblDatum.setBounds(200, 130, 139, 25);

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel7.setText("Produktlista");
        add(jLabel7);
        jLabel7.setBounds(120, 310, 101, 20);

        lblPris.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPris.setText("jLabel10");
        add(lblPris);
        lblPris.setBounds(210, 250, 250, 25);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel8.setText("Hitta rätt anställningsnummer här!");
        add(jLabel8);
        jLabel8.setBounds(810, 310, 187, 16);

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
        tblAllaProdukter.setEnabled(false);
        jScrollPane1.setViewportView(tblAllaProdukter);

        add(jScrollPane1);
        jScrollPane1.setBounds(120, 330, 607, 127);

        comboStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        comboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Under behandling", "Produktion pågår", "Packas", "Skickad", "Levererad", "Returnerad" }));
        comboStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboStatusActionPerformed(evt);
            }
        });
        add(comboStatus);
        comboStatus.setBounds(190, 170, 137, 26);

        btnRedigeraStatus.setText("Redigera status");
        btnRedigeraStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRedigeraStatusActionPerformed(evt);
            }
        });
        add(btnRedigeraStatus);
        btnRedigeraStatus.setBounds(390, 490, 130, 27);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Order:");
        add(jLabel1);
        jLabel1.setBounds(120, 30, 54, 25);

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
            }
        });
        add(btnTillbaka);
        btnTillbaka.setBounds(910, 20, 76, 27);

        btnSeProduktOmProdukt.setText("Se information om produkt");
        btnSeProduktOmProdukt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeProduktOmProduktActionPerformed(evt);
            }
        });
        add(btnSeProduktOmProdukt);
        btnSeProduktOmProdukt.setBounds(120, 490, 203, 27);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Kund:");
        add(jLabel2);
        jLabel2.setBounds(120, 80, 210, 25);

        btnAtaProdukt.setText("Åta produkt");
        btnAtaProdukt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtaProduktActionPerformed(evt);
            }
        });
        add(btnAtaProdukt);
        btnAtaProdukt.setBounds(540, 490, 97, 27);

        btnSeKund.setText("Se kundinformation");
        btnSeKund.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeKundActionPerformed(evt);
            }
        });
        add(btnSeKund);
        btnSeKund.setBounds(300, 80, 139, 27);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Datum:");
        add(jLabel3);
        jLabel3.setBounds(120, 130, 110, 25);

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Expressleverans:");
        add(jLabel6);
        jLabel6.setBounds(120, 210, 230, 25);

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Status:");
        add(jLabel4);
        jLabel4.setBounds(120, 170, 180, 25);

        lblExpress.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblExpress.setText("jLabel7");
        add(lblExpress);
        lblExpress.setBounds(280, 210, 170, 25);

        jLabel9.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel9.setText("Skriv anställningsnummer i rutan \"Tilldelad\"!");
        add(jLabel9);
        jLabel9.setBounds(480, 310, 243, 16);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSparaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSparaActionPerformed
        try {
            sparaTilldelad();
            sparaStatus();
            comboStatus.setEnabled(false);
            JOptionPane.showMessageDialog(null, "Ändring sparad!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
    }//GEN-LAST:event_btnSparaActionPerformed

    private void comboStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboStatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboStatusActionPerformed

    private void btnRedigeraStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRedigeraStatusActionPerformed
        comboStatus.setEnabled(true);

    }//GEN-LAST:event_btnRedigeraStatusActionPerformed

    private void btnTillbakaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTillbakaActionPerformed
        new SeAllaOrdrar(idb, inloggadAnvandare).setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnTillbakaActionPerformed

    private void btnSeProduktOmProduktActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeProduktOmProduktActionPerformed
        try {
            int valdRad = tblAllaProdukter.getSelectedRow();

            // Kontrollera först om en rad är vald
            if (valdRad == -1) {
                JOptionPane.showMessageDialog(this, "Markera raden för att se information om produkten.");
                return;
            }

            // Hämta antal från kolumn 3
            int antal = Integer.parseInt(tblAllaProdukter.getValueAt(valdRad, 3).toString());

            // Hämta StandardProduktID från kolumn 5 (dold)
            String produktID = tblAllaProdukter.getValueAt(valdRad, 5).toString();

            if (produktID == null || produktID.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingen produktinformation hittades för vald rad.");
                return;
            }

            // Öppna ny vy med produktinfo
            new SeInfoStandardprodukt(idb, inloggadAnvandare, produktID, antal).setVisible(true);

        } catch (Exception ex) {
            System.out.println("Fel i btnSeProduktOmProduktActionPerformed: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnSeProduktOmProduktActionPerformed

    private void btnAtaProduktActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtaProduktActionPerformed
        //Visar en popup-ruta när man klickar på "Åta produkt" som förklarar hur man åtar en produkt.
        javax.swing.JOptionPane.showMessageDialog(this, "Tilldela en produkt genom att fylla i rutan Tilldelad med anställningdID. "
                + "OBS klicka ENTER innan du klickar på spara knappen!");

        //Tabellen som visas uppdateras till den tabell där det går att redigera anställningsID i.
        DefaultTableModel redigerbarModell = genereraRedigerbarModell();
        tblAllaProdukter.setModel(redigerbarModell);

        //Ordnar storleken på varje kolumn.
        sattStorlekTabell();
        //Gör tilldelad-kolumnen redigerbar.
        tblAllaProdukter.setEnabled(true);
    }//GEN-LAST:event_btnAtaProduktActionPerformed

    private void btnSeKundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeKundActionPerformed
    try {
        String kundNrText = lblKundNr.getText();
        if (kundNrText != null && !kundNrText.isEmpty()) {
            int kundID = Integer.parseInt(kundNrText);

            MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);

            // ✅ Lägg till denna panel i CardLayout med ett namn
            main.addPanelToCardLayout(this, "SeVanligOrder");

            // ✅ Skapa en panel för kund och skicka namnet på denna panel
            SpecifikKund kundPanel = new SpecifikKund(idb, inloggadAnvandare, kundID, "SeVanligOrder");

            // ✅ Lägg till kundpanelen
            main.addPanelToCardLayout(kundPanel, "specifikKundPanel");

            // ✅ Visa kundpanelen
            main.showPanel("specifikKundPanel");

        } else {
            JOptionPane.showMessageDialog(this, "Kundnummer saknas.");
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Kundnummer är ogiltigt.");
    }

//        try {
//            String kundNrText = lblKundNr.getText();
//            if (kundNrText != null && !kundNrText.isEmpty()) {
//                int kundID = Integer.parseInt(kundNrText);
//
//                // Skapa en panel för SpecifikKund istället för ett nytt fönster
//                SpecifikKund kundPanel = new SpecifikKund(idb, inloggadAnvandare, kundID, "SeVanligOrder");
//
//
//                // Hämta MainFrame för att byta till den nya panelen
//                MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);
//
//                // Lägg till panelen i CardLayout (om den inte redan är tillagd)
//                main.addPanelToCardLayout(kundPanel, "SeVanligOrder");
//
//                // Byt till den nya panelen
//                main.showPanel("SeVanligOrder");
//
//            } else {
//                JOptionPane.showMessageDialog(this, "Kundnummer saknas.");
//            }
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "Kundnummer är ogiltigt.");
//        }
    }//GEN-LAST:event_btnSeKundActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtaProdukt;
    private javax.swing.JButton btnRedigeraStatus;
    private javax.swing.JButton btnSeKund;
    private javax.swing.JButton btnSeProduktOmProdukt;
    private javax.swing.JButton btnSpara;
    private javax.swing.JButton btnTillbaka;
    private javax.swing.JComboBox<String> comboStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDatum;
    private javax.swing.JLabel lblExpress;
    private javax.swing.JLabel lblKundNr;
    private javax.swing.JLabel lblOrderNr;
    private javax.swing.JLabel lblPris;
    private javax.swing.JTable tblAllaAnstallda;
    private javax.swing.JTable tblAllaProdukter;
    // End of variables declaration//GEN-END:variables
}
