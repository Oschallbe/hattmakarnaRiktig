/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package hattmakarna;

import oru.inf.InfDB;
import oru.inf.InfException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mejaa
 */
public class KalenderSchemaRatt extends javax.swing.JPanel {

    private static InfDB idb;
    private static String inloggadAnvandare;
    private JPanel kalenderRuta;
    private JLabel manadLabel;
    private LocalDate visadManad;
    private JTable produktTabell;
    private int anstalldID;

    /**
     * Creates new form KalenderSchemaRatt
     */
    public KalenderSchemaRatt(InfDB idb, String ePost) {
        initComponents();
        this.idb = idb;
        this.inloggadAnvandare = ePost;

        this.setLayout(new BorderLayout());
        visadManad = LocalDate.now().withDayOfMonth(1);

        JPanel toppPanel = new JPanel(new BorderLayout());
        JButton forraKnapp = new JButton("<");
        JButton nastaKnapp = new JButton(">");

        manadLabel = new JLabel("", SwingConstants.CENTER);
        toppPanel.add(forraKnapp, BorderLayout.WEST);
        toppPanel.add(manadLabel, BorderLayout.CENTER);
        toppPanel.add(nastaKnapp, BorderLayout.EAST);

        this.add(toppPanel, BorderLayout.NORTH);

        kalenderRuta = new JPanel();
        kalenderRuta.setLayout(new GridLayout(0, 7));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
        wrapper.setBackground(Color.LIGHT_GRAY);

        JLabel instruktionLabel = new JLabel("Fyll i ID:t på produktrad du vill tillverka", SwingConstants.CENTER);
        instruktionLabel.setFont(new Font("Century Gothic", Font.ITALIC, 14));
        instruktionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        wrapper.add(instruktionLabel, BorderLayout.NORTH);
        JPanel instruktionOchTopp = new JPanel(new BorderLayout());
        instruktionOchTopp.add(instruktionLabel, BorderLayout.NORTH);
        instruktionOchTopp.add(toppPanel, BorderLayout.SOUTH);

        wrapper.add(instruktionOchTopp, BorderLayout.NORTH);
        wrapper.add(kalenderRuta, BorderLayout.CENTER);

        JPanel huvudPanel = new JPanel(new BorderLayout());

        JLabel kalenderRubrik = new JLabel("Mitt Kalenderschema", SwingConstants.LEFT);
        kalenderRubrik.setFont(new Font("Century Gothic", Font.BOLD, 26));
        kalenderRubrik.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        huvudPanel.add(kalenderRubrik, BorderLayout.NORTH);

        huvudPanel.add(wrapper, BorderLayout.WEST);

        String[] kolumner = {"ID", "Ordernr.", "Produktnamn", "Beställningstyp", "Datum"};
        Object[][] data;

        try {
            String anstalldIDText = idb.fetchSingle("select AnstalldID from anstalld where epost = '" + inloggadAnvandare + "'");
            anstalldID = Integer.parseInt(anstalldIDText);
            List<HashMap<String, String>> resultat = idb.fetchRows(
                    "select b.BestallningID as BestallningID, "
                    + "case when b.Typ = 'Specialbeställning' then 'Specialprodukt' "
                    + "     else sp.Namn end as Produktnamn, "
                    + "case when b.Typ = 'Specialbeställning' then 'Special' "
                    + "     when b.Typ = 'Standardbeställning' then 'Standard' "
                    + "     else b.Typ end as Beställningstyp, "
                    + "b.Datum "
                    + "from orderitem oi "
                    + "join bestallning b on oi.BestallningID = b.BestallningID "
                    + "left join standardprodukt sp on oi.StandardProduktID = sp.StandardProduktID "
                    + "left join specialprodukt spp on oi.SpecialProduktID = spp.SpecialProduktID "
                    + "where oi.AnstalldID = " + anstalldID
            );

            data = new Object[resultat.size()][kolumner.length];

            for (int i = 0; i < resultat.size(); i++) {
                HashMap<String, String> rad = resultat.get(i);

                data[i][0] = i + 1;
                data[i][1] = rad.get("BestallningID");
                data[i][2] = rad.get("Produktnamn");
                data[i][3] = rad.get("Beställningstyp");
                data[i][4] = rad.get("Datum");
            }
        } catch (InfException ex) {
            System.out.println(ex);
            data = new Object[0][kolumner.length];
        }

        produktTabell = new JTable(new DefaultTableModel(data, kolumner));
        DefaultTableModel modell = new DefaultTableModel(data, kolumner) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) {
                    return Boolean.class; // kolumn 5 = checkbox
                }
                return String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // endast checkbox är redigerbar
            }
        };
        produktTabell = new JTable(modell);
        JScrollPane tabellScroll = new JScrollPane(produktTabell);
        tabellScroll.setPreferredSize(new Dimension(450, 600));

        // Inget val överhuvudtaget
        produktTabell.setRowSelectionAllowed(false);
        produktTabell.setColumnSelectionAllowed(false);
        produktTabell.setCellSelectionEnabled(false);
        
        produktTabell.setRowHeight(30);

        produktTabell.getColumnModel().getColumn(0).setPreferredWidth(60);
        produktTabell.getColumnModel().getColumn(1).setPreferredWidth(60);
        produktTabell.getColumnModel().getColumn(2).setPreferredWidth(175);
        produktTabell.getColumnModel().getColumn(3).setPreferredWidth(100);
        produktTabell.getColumnModel().getColumn(4).setPreferredWidth(80);
        
        produktTabell.setFont(new Font("Century Gothic", Font.PLAIN, 12));

        JPanel tabellWrapper = new JPanel(new BorderLayout());
        JLabel tabellRubrik = new JLabel("Åtagna produkter", SwingConstants.LEFT);
        tabellRubrik.setFont(new Font("Century Gothic", Font.BOLD, 26));
        tabellRubrik.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabellWrapper.add(tabellRubrik, BorderLayout.NORTH);
        tabellWrapper.add(tabellScroll, BorderLayout.CENTER);

        huvudPanel.add(tabellWrapper, BorderLayout.EAST);

        this.add(huvudPanel, BorderLayout.CENTER);

        forraKnapp.addActionListener(e -> {
            visadManad = visadManad.minusMonths(1);
            fyllKalender();
        });

        nastaKnapp.addActionListener(e -> {
            visadManad = visadManad.plusMonths(1);
            fyllKalender();
        });

        fyllKalender();
    }

    private void fyllKalender() {
        List<JList<String>> allaListor = new java.util.ArrayList<>();

        kalenderRuta.removeAll();

        String manadText = visadManad.getMonth().getDisplayName(TextStyle.FULL, new Locale("sv")) + " " + visadManad.getYear();
        manadLabel.setText(manadText.substring(0, 1).toUpperCase() + manadText.substring(1));
        manadLabel.setFont(new Font("Century Gothic", Font.BOLD, 18));

        String[] dagar = {"Mån", "Tis", "Ons", "Tor", "Fre", "Lör", "Sön"};
        for (String dag : dagar) {
            JLabel lbl = new JLabel(dag, SwingConstants.CENTER);
            lbl.setFont(new Font("Century Gothic", Font.BOLD, 16));
            kalenderRuta.add(lbl);
        }

        LocalDate start = visadManad;
        int forstaDag = start.getDayOfWeek().getValue();
        int dagarIManad = visadManad.lengthOfMonth();

        for (int i = 1; i < forstaDag; i++) {
            kalenderRuta.add(new JLabel(""));
        }

        String anstalldID;
        try {
            anstalldID = idb.fetchSingle("select AnstalldID from anstalld where epost = '" + inloggadAnvandare + "'");
        } catch (InfException ex) {
            System.out.println("Kunde inte hämta anställningsid" + ex.getMessage());
            return;
        }

        if (anstalldID == null) {
            JOptionPane.showMessageDialog(this, "hittades ej");
            return;
        }

        final String aktuellAnstalldID = anstalldID;

        for (int dag = 1; dag <= dagarIManad; dag++) {
            final int aktuellDag = dag;
            JPanel dagPanel = new JPanel(new BorderLayout());
            dagPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            dagPanel.setPreferredSize(new Dimension(110, 80));

            JLabel dagLabel = new JLabel(String.valueOf(dag), SwingConstants.CENTER);
            dagLabel.setFont(new Font("Century Gothic", Font.BOLD, 18));

            DefaultListModel<String> modell = new DefaultListModel<>();
            JList<String> produktLista = new JList<>(modell);
            allaListor.add(produktLista);
            produktLista.setFont(new Font("Century Gothic", Font.PLAIN, 12));
            produktLista.setVisibleRowCount(3);

            JScrollPane scroll = new JScrollPane(produktLista);

            JTextField inputFalt = new JTextField();
            inputFalt.setText("Fyll i ett ID här");
            inputFalt.setFont(new Font("Century Gothic", Font.ITALIC, 12));
            inputFalt.setForeground(Color.GRAY);
            

            inputFalt.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (inputFalt.getText().equals("Fyll i ett ID här")) {
                        inputFalt.setText("");
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (inputFalt.getText().trim().isEmpty()) {
                        inputFalt.setText("Fyll i ett ID här");
                    }
                }
            });

            inputFalt.addActionListener(e -> {
                String text = inputFalt.getText().trim();

                if (!text.isEmpty()) {
                    try {
                        int tilldelningsID = Integer.parseInt(text);

                        //Kontrollera om ID finns i JTable
                        if (!idFinnsITabellen(tilldelningsID, produktTabell)) {
                            JOptionPane.showMessageDialog(null, "TilldelningsID " + tilldelningsID + " finns inte i tabellen.");
                            return;
                        }

                        //Hämta datum
                        String datum = visadManad.withDayOfMonth(aktuellDag).toString();

                        //Hämta info från JTable (kolumn 1 = BeställningID)
                        String bestallningID = null;
                        for (int i = 0; i < produktTabell.getRowCount(); i++) {
                            int id = Integer.parseInt(produktTabell.getValueAt(i, 0).toString());
                            if (id == tilldelningsID) {
                                bestallningID = produktTabell.getValueAt(i, 1).toString();
                                break;
                            }
                        }

                        if (bestallningID == null) {
                            JOptionPane.showMessageDialog(null, "Kunde inte hämta BeställningID från tabellen.");
                            return;
                        }

                        //Spara i databasen
                        String insert = "insert into kalenderschema (TilldelningsID, AnstalldID, BestallningID, Datum) values ("
                                + tilldelningsID + ", " + aktuellAnstalldID + ", " + bestallningID + ", '" + datum + "')";

                        idb.insert(insert);

                        //Visa i kalendern
                        modell.addElement("" + tilldelningsID);
                        inputFalt.setText("");

                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(null, "Endast siffror tillåtna i TilldelningsID.");
                    } catch (InfException ex) {
                        System.out.println("Fel vid sparning: " + ex.getMessage());
                    }
                }
            });

            try {

                LocalDate datum = visadManad.withDayOfMonth(dag);
                String selectText = "select TilldelningsID from kalenderschema where Datum = '" + datum + "' and AnstalldID = " + aktuellAnstalldID;
                List<String> resultat = idb.fetchColumn(selectText);
                if (resultat != null) {
                    for (String text : resultat) {
                        modell.addElement("" + text);
                    }
                }
            } catch (InfException ex) {
                System.out.println("Fel vid insert:" + ex.getMessage());
            }

            dagPanel.add(dagLabel, BorderLayout.NORTH);
            dagPanel.add(scroll, BorderLayout.CENTER);
            dagPanel.add(inputFalt, BorderLayout.SOUTH);
            kalenderRuta.add(dagPanel);

        }
        JButton taBortKnapp = new JButton("Ta bort produkt");
        taBortKnapp.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        taBortKnapp.addActionListener(e -> {
            for (JList<String> lista : allaListor) {
                String vald = lista.getSelectedValue();
                if (vald != null) {
                    String idText = vald.replace("Tilldelning: ", "").trim();

                    try {
                        int tilldelningsID = Integer.parseInt(idText);

                        //Hämta dagen från den markerade listan
                        Component parent = lista.getParent();
                        while (!(parent instanceof JPanel) && parent != null) {
                            parent = parent.getParent();
                        }
                        JPanel dagPanel = (JPanel) parent;
                        JLabel dagLabel = (JLabel) dagPanel.getComponent(0); // överst i panelen
                        int dagNummer = Integer.parseInt(dagLabel.getText());
                        LocalDate datum = visadManad.withDayOfMonth(dagNummer);

                        //Rensa i databasen för just den dagen
                        String delete = "delete from kalenderschema where TilldelningsID = " + tilldelningsID
                                + " and AnstalldID = " + anstalldID
                                + " and Datum = '" + datum + "'";

                        idb.delete(delete);

                        //Ta bort från JList
                        lista.clearSelection();
                        DefaultListModel<String> modell = (DefaultListModel<String>) lista.getModel();
                        modell.removeElement(vald);

                        JOptionPane.showMessageDialog(null, "Tilldelning " + tilldelningsID + " borttagen från " + datum + ".");
                        return;

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Kunde inte ta bort: " + ex.getMessage());
                        return;
                    }
                }
            }

            JOptionPane.showMessageDialog(null, "Inget markerat.");
        });

        JPanel knappPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        knappPanel.add(taBortKnapp);
        this.add(knappPanel, BorderLayout.SOUTH);

        kalenderRuta.revalidate();
        kalenderRuta.repaint();

    }

    private boolean idFinnsITabellen(int id, JTable tabell) {
        for (int i = 0; i < tabell.getRowCount(); i++) {
            Object cellValue = tabell.getValueAt(i, 0); // kolumn 0 = TilldelningsID
            if (cellValue != null && Integer.parseInt(cellValue.toString()) == id) {
                return true;
            }
        }
        return false;
    }

    //Filtrera bort rader med Avklarad=true och uppdatera JTable
    private void uppdateraTabellMedOfiltreradData() {
        DefaultTableModel modell = (DefaultTableModel) produktTabell.getModel();
        for (int i = modell.getRowCount() - 1; i >= 0; i--) {
            Boolean avklarad = (Boolean) modell.getValueAt(i, 5);
            if (avklarad != null && avklarad) {
                modell.removeRow(i);
            }
        }
    }

//Markera en rad som avklarad (checkbox true) utan att visa den i tabellen
    private void doljaRadITabellen(int tilldelningsID) {
        DefaultTableModel modell = (DefaultTableModel) produktTabell.getModel();
        for (int i = 0; i < modell.getRowCount(); i++) {
            int id = Integer.parseInt(modell.getValueAt(i, 0).toString());
            if (id == tilldelningsID) {
                modell.setValueAt(true, i, 5); //checkbox för Avklarad
                break;
            }
        }
        uppdateraTabellMedOfiltreradData(); //filtrera bort avklarade
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame fonster = new JFrame("Min Kalender");
            fonster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fonster.setSize(1000, 800);
            fonster.setContentPane(new KalenderSchemaRatt(idb, inloggadAnvandare));
            fonster.setVisible(true);
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 909, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 567, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
