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
            //Skapar en array som lagrar kolumnnamnen.
            String kolumnNamn[] = {"OrderItemID", "Matt", "Pris", "AntalProdukter", "AnstalldID"};

            //Skapar en DefaultTableModel som håller kolumnnamnen samt sätter antalet rader till noll.
            DefaultTableModel allaProdukter = new DefaultTableModel(kolumnNamn, 0);

            //Hämtar alla anställdas id och lägger det i Arraylistan "id".
            String selectOid = "select OrderItemID from orderitem where BestallningID =" + klickatOrderNr + " order by(OrderItemID);";
            ArrayList<String> oid = idb.fetchColumn(selectOid);

            //Om AnstalldId inte är tom körs en for-each loop som för varje id hämtar id, förnamn och efternamn om det anställda som placeras i Hashmapen "Info".
            if (oid != null) {
                for (String ettOid : oid) {
                    String selectInfo = "select OrderItemID, AntalProdukter, AnstalldID, SpecialProduktID from orderitem where OrderItemID = " + ettOid + ";";
                    HashMap<String, String> info = idb.fetchRow(selectInfo);

                    //Hämtar pris och namn för produkten från tabellen "standardprodukt".
                    String ettProduktID = info.get("SpecialProduktID");
                    String selectProdukt = "select Matt, Pris from specialprodukt where SpecialProduktID = " + ettProduktID + ";";
                    HashMap<String, String> infoNamnPris = idb.fetchRow(selectProdukt);

                    //AnstalldID byts ut mot för- och efternamn på den anställde. 
                    String aid = info.get("AnstalldID");
                    String selectNamnAnstalld = "select Fornamn, Efternamn from anstalld where AnstalldID = " + aid + ";";
                    HashMap<String, String> anstalldFornamnEfternamn = idb.fetchRow(selectNamnAnstalld);
                    String hopslagetNamn = anstalldFornamnEfternamn.get("Fornamn") + " " + anstalldFornamnEfternamn.get("Efternamn");

                    //Skapar en array som håller data för en rad i tabellen.
                    Object[] enRad = new Object[kolumnNamn.length];
                    enRad[0] = info.get("OrderItemID");
                    enRad[1] = infoNamnPris.get("Matt");
                    enRad[2] = infoNamnPris.get("Pris");
                    enRad[3] = info.get("AntalProdukter");
                    enRad[4] = hopslagetNamn;

                    allaProdukter.addRow(enRad);

                }

                //Jtable sätts med data från DefaultTableModel.
                tblAllaProdukter.setModel(allaProdukter);

            }

            tblAllaProdukter.setAutoResizeMode(tblAllaProdukter.AUTO_RESIZE_OFF);

            //Sätter storleken på tabellen.
            TableColumn col = tblAllaProdukter.getColumnModel().getColumn(0); //id
            col.setPreferredWidth(100);

            col = tblAllaProdukter.getColumnModel().getColumn(1); //namn.
            col.setPreferredWidth(75);

            col = tblAllaProdukter.getColumnModel().getColumn(2); //pris.
            col.setPreferredWidth(75);

            col = tblAllaProdukter.getColumnModel().getColumn(3); //antal.
            col.setPreferredWidth(75);

            col = tblAllaProdukter.getColumnModel().getColumn(4); //anstalldID.
            col.setPreferredWidth(154);

            //Ändrar rubrikerna i tabellen.
            tblAllaProdukter.getColumnModel().getColumn(0).setHeaderValue("Artikelnummer");
            tblAllaProdukter.getColumnModel().getColumn(1).setHeaderValue("Matt");
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
        String selectOrderNr = "SELECT BestallningID FROM bestallning WHERE BestallningID = '" + klickatOrderNr + "'";
        String orderNr = idb.fetchSingle(selectOrderNr);
        lblOrderNr.setText(orderNr);

        String selectTillverkningstid = "SELECT Tillverkningstid FROM specialprodukt WHERE SpecialProduktID = '" + klickatOrderNr + "'";
        String tillverkningstid = idb.fetchSingle(selectTillverkningstid);
        lblTillverkningstid2.setText(tillverkningstid);

        String selectKundID = "SELECT KundID FROM bestallning WHERE BestallningID = '" + klickatOrderNr + "'";
        String kundIDStr = idb.fetchSingle(selectKundID);
        kundID = Integer.parseInt(kundIDStr);

        String selectKund = "SELECT Fornamn, Efternamn FROM kund WHERE KundID = '" + kundID + "'";
        HashMap<String, String> kund = idb.fetchRow(selectKund);

        if (kund != null) {
            String kundIDochNamn = kundID + " – " + kund.get("Fornamn") + " " + kund.get("Efternamn");
            lblKundNr.setText(kundIDochNamn);
        } else {
            lblKundNr.setText("Ingen kund hittades.");
        }


        String selectStatus = "SELECT Status FROM bestallning WHERE BestallningID = '" + klickatOrderNr + "'";
        String status = idb.fetchSingle(selectStatus);
        cbStatus.setSelectedItem(status);
        cbStatus.setEnabled(false);
    } catch (InfException ex) {
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

        btnSeSpecifikProdukt.setText("Se produkt");
        btnSeSpecifikProdukt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeSpecifikProduktActionPerformed(evt);
            }
        });

        lblSpecialorder.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblSpecialorder.setText("Specialorder:");

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Under behandling", "Produktion pågår", "Packas", "Skickad", "Levererad" }));

        lblOrderNr.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        lblOrderNr.setText("jLabel1");

        btnRedigeraStatus.setText("Redigera status");
        btnRedigeraStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRedigeraStatusActionPerformed(evt);
            }
        });

        lblTillverkningstid.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTillverkningstid.setText("Tillverkningstid:");

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

        lblTillverkningstid2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTillverkningstid2.setText("jLabel1");

        lblKund.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKund.setText("Kund: ");

        btnAtaProdukt.setText("Åta produkt");
        btnAtaProdukt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtaProduktActionPerformed(evt);
            }
        });

        lblKundNr.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblKundNr.setText("jLabel1");

        btnSpara.setText("Spara");
        btnSpara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSparaActionPerformed(evt);
            }
        });

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
        jScrollPane1.setViewportView(tblAllaProdukter);

        btnSeKundinfo.setText("Se kundinformation");
        btnSeKundinfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeKundinfoActionPerformed(evt);
            }
        });

        btnTillbaka.setText("Tillbaka");
        btnTillbaka.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTillbakaActionPerformed(evt);
            }
        });

        lblStatus.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblStatus.setText("Status: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(169, 169, 169)
                        .addComponent(btnRedigeraStatus)
                        .addGap(18, 18, 18)
                        .addComponent(btnAtaProdukt)
                        .addGap(18, 18, 18)
                        .addComponent(btnSpara))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(129, 129, 129)
                                                .addComponent(lblTillverkningstid2))
                                            .addComponent(lblKund))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnSeSpecifikProdukt))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblTillverkningstid)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblStatus)
                                                .addGap(40, 40, 40)
                                                .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblSpecialorder)
                                                .addGap(18, 18, 18)
                                                .addComponent(lblOrderNr)
                                                .addGap(114, 114, 114)
                                                .addComponent(btnTillbaka))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(115, 115, 115)
                                                .addComponent(lblKundNr)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnSeKundinfo)))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(42, 42, 42)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 227, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblSpecialorder)
                                    .addComponent(lblOrderNr)
                                    .addComponent(btnTillbaka))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblKund)
                                    .addComponent(lblKundNr)
                                    .addComponent(btnSeKundinfo))
                                .addGap(18, 18, 18)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTillverkningstid)
                            .addComponent(lblTillverkningstid2))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addComponent(btnSeSpecifikProdukt))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblStatus)
                                    .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRedigeraStatus)
                    .addComponent(btnAtaProdukt)
                    .addComponent(btnSpara))
                .addContainerGap(11, Short.MAX_VALUE))
        );
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

        //Visar en popup-ruta när man klickar på "Åta produkt" som förklarar hur man åtar en produkt.
        javax.swing.JOptionPane.showMessageDialog(this, "Du kan nu tilldela en person en produkt genom att dubbelklicka i rutan för Tilldelad för den specifika produkten. "
            + "OBS efter du har skrivit in ett nytt ID måste du klicka ENTER innan du klickar på spara knappen!");

        //Tabellen som visas uppdateras till den tabell där det går att redigera anställningsID i.
        DefaultTableModel redigerbarModell = genereraRedigerbarModell();
        tblAllaProdukter.setModel(redigerbarModell);

        //Ordnar storleken på varje kolumn.
        //sattStorlekTabell();
        //Gör tilldelad-kolumnen redigerbar.
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
