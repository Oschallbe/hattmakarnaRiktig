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
            DefaultTableModel allaProdukter = new DefaultTableModel(kolumnNamn, 0);

            //Hämtar alla anställdas id och lägger det i Arraylistan "id".
            String selectOid = "SELECT OrderItemID FROM orderitem WHERE BestallningID = " + klickatOrderNr + " ORDER BY OrderItemID;";
            ArrayList<String> oid = idb.fetchColumn(selectOid);

            //Om AnstalldId inte är tom körs en for-each loop som för varje id hämtar id, förnamn och efternamn om det anställda som placeras i Hashmapen "Info".
            if (oid != null) {
                for (String ettOid : oid) {
                    String selectInfo = "SELECT OrderItemID, AntalProdukter, AnstalldID, StandardProduktID FROM orderitem WHERE OrderItemID = " + ettOid + ";";
                    HashMap<String, String> info = idb.fetchRow(selectInfo);

                    //Hämtar AnstalldID och lagrar det i lokalvariabeln aid.
                    String aid = info.get("AnstalldID");

                    //Om aid är null så sätts det som en tom sträng som sedan visas som en tom ruta i jtable.
                    if (aid == null) {
                        aid = "";
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
                    enRad[4] = aid;
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
            lblPris.setText(String.valueOf(totalPris));
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
            String status = (String) comboStatus.getSelectedItem();
            String updateStatus = "update bestallning set status = '" + status + "' where BestallningID = '" + klickatOrderNr + "';";
            idb.update(updateStatus);

        } catch (InfException ex) {
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
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnAtaProdukt = new javax.swing.JButton();
        btnSeKund = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblExpress = new javax.swing.JLabel();

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Totalpris:");

        btnSpara.setText("Spara");
        btnSpara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSparaActionPerformed(evt);
            }
        });

        lblOrderNr.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblOrderNr.setText("jLabel6");

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

        lblKundNr.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblKundNr.setText("jLabel7");

        lblDatum.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblDatum.setText("jLabel8");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        jLabel7.setText("Produktlista");

        lblPris.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblPris.setText("jLabel10");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        jLabel8.setText("Hitta rätt anställningsnummer här!");

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

        comboStatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        comboStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Under behandling", "Produktion pågår", "Packas", "Skickad", "Levererad" }));
        comboStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboStatusActionPerformed(evt);
            }
        });

        btnRedigeraStatus.setText("Redigera status");
        btnRedigeraStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRedigeraStatusActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("Order:");

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
            }
        });

        jButton1.setText("Se information om produkt");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Kund:");

        btnAtaProdukt.setText("Åta produkt");
        btnAtaProdukt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtaProduktActionPerformed(evt);
            }
        });

        btnSeKund.setText("Se kundinformation");
        btnSeKund.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeKundActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Datum:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Expressleverans:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Status:");

        lblExpress.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblExpress.setText("jLabel7");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblPris, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblExpress))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(btnTillbaka)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 607, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnRedigeraStatus)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnAtaProdukt)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnSpara)))
                                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblDatum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblOrderNr, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblKundNr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSeKund)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblOrderNr))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblKundNr)
                    .addComponent(btnSeKund))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblDatum))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(comboStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblExpress)
                    .addComponent(jLabel6))
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblPris))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAtaProdukt)
                            .addComponent(btnSpara)
                            .addComponent(btnRedigeraStatus)
                            .addComponent(jButton1)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTillbaka)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnSparaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSparaActionPerformed
        try {
            sparaTilldelad();
            sparaStatus();
            comboStatus.setEnabled(false);
            JOptionPane.showMessageDialog(null, "Ändring sparad!");

        } catch (NumberFormatException ex) {
            System.out.println(ex);
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            int valdRad = tblAllaProdukter.getSelectedRow();
            int antal = Integer.parseInt(tblAllaProdukter.getValueAt(valdRad, 3).toString()); // Anta

            if (valdRad == -1) {
                JOptionPane.showMessageDialog(this, "Markera en rad för att se produkten.");
                return;
            }

            // Hämta StandardProduktID från kolumn 5 (dold)
            String produktID = tblAllaProdukter.getValueAt(valdRad, 5).toString();

            if (produktID == null || produktID.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingen produktinformation hittades för vald rad.");
                return;
            }

            // Skicka vidare StandardProduktID direkt
new SeInfoStandardprodukt(idb, inloggadAnvandare, produktID, antal).setVisible(true);

        } catch (Exception ex) {
            System.out.println("Fel i jButton1ActionPerformed: " + ex.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnAtaProduktActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtaProduktActionPerformed

        //Visar en popup-ruta när man klickar på "Åta produkt" som förklarar hur man åtar en produkt.
        javax.swing.JOptionPane.showMessageDialog(this, "Du kan nu tilldela en person en produkt genom att dubbelklicka i rutan för Tilldelad för den specifika produkten. "
                + "OBS efter du har skrivit in ett nytt ID måste du klicka ENTER innan du klickar på spara knappen!");

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

                // Skapa en panel för SpecifikKund istället för ett nytt fönster
                JPanel specifikKundPanel = new SpecifikKund(idb, inloggadAnvandare, kundID, "SeAllaKunder");

                // Hämta MainFrame för att byta till den nya panelen
                MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);

                // Lägg till panelen i CardLayout (om den inte redan är tillagd)
                main.addPanelToCardLayout(specifikKundPanel, "specifikKundPanel");

                // Byt till den nya panelen
                main.showPanel("specifikKundPanel");

            } else {
                JOptionPane.showMessageDialog(this, "Kundnummer saknas.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Kundnummer är ogiltigt.");
        }
    }//GEN-LAST:event_btnSeKundActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtaProdukt;
    private javax.swing.JButton btnRedigeraStatus;
    private javax.swing.JButton btnSeKund;
    private javax.swing.JButton btnSpara;
    private javax.swing.JButton btnTillbaka;
    private javax.swing.JComboBox<String> comboStatus;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
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
