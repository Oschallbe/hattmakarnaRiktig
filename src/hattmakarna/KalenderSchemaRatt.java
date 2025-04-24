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
import java.util.Locale;

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
        kalenderRuta.setPreferredSize(new Dimension(850, 600)); //NY
        
        //NYTT
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 10));
        wrapper.add(kalenderRuta);
        
        this.add(wrapper, BorderLayout.CENTER);
        
        // ORIGINAL this.add(kalenderRuta, BorderLayout.CENTER);
        
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
    
    private void fyllKalender(){
        kalenderRuta.removeAll();
        
        String manadText = visadManad.getMonth().getDisplayName(TextStyle.FULL, new Locale("sv")) + " " + visadManad.getYear();
        manadLabel.setText(manadText.substring(0, 1).toUpperCase() + manadText.substring(1));
        
        String[] dagar = {"Mån", "Tis", "Ons", "Tor", "Fre", "Lör", "Sön"};
        for(String dag:dagar){
            JLabel lbl = new JLabel(dag, SwingConstants.CENTER);
            lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            kalenderRuta.add(lbl);
        }
        
        LocalDate start = visadManad;
        int forstaDag = start.getDayOfWeek().getValue();
        int dagarIManad = visadManad.lengthOfMonth();
        
        for(int i = 1; i < forstaDag; i++){
            kalenderRuta.add(new JLabel(""));
        }
        
        String anstalldID = null;
            try{
                anstalldID = idb.fetchSingle("select AnstalldID from anstalld where epost = '" + inloggadAnvandare + "'");
            }
            catch(InfException ex){
                System.out.println("Kunde inte hämta anställningsid" + ex.getMessage());
            }
            
        if(anstalldID == null){
            JOptionPane.showMessageDialog(this, "hittades ej");
            return;
        }
        
        final String aktuellAnstalldID = anstalldID;
            
        for(int dag = 1; dag <= dagarIManad; dag++){
            final int aktuellDag = dag;
            JPanel dagPanel = new JPanel(new BorderLayout());
            dagPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            dagPanel.setPreferredSize(new Dimension(120, 100));
            
            JLabel dagLabel = new JLabel(String.valueOf(dag), SwingConstants.CENTER);
            dagLabel.setFont(new Font("Arial", Font.BOLD, 18));
            
            DefaultListModel<String> modell = new DefaultListModel<>();
            JList<String> produktLista = new JList<>(modell);
            produktLista.setFont(new Font("Arial", Font.PLAIN, 12));
            produktLista.setVisibleRowCount(3);
            
            JScrollPane scroll = new JScrollPane(produktLista);
            
            JTextField inputFalt = new JTextField();
            Font normalFont = inputFalt.getFont();
            Font italicFont = normalFont.deriveFont(Font.ITALIC);
            inputFalt.setText("Fyll i här");
            inputFalt.setForeground(Color.GRAY);
            inputFalt.setFont(italicFont);
                              
                                
                          
                    inputFalt.addFocusListener(new FocusAdapter(){
                    @Override
                    public void focusGained(FocusEvent e){
                        if(inputFalt.getText().equals("Fyll i här")){
                            inputFalt.setText("");
                        }
                    }            
                    
                    @Override
                    public void focusLost(FocusEvent e){
                        if(inputFalt.getText().trim().isEmpty()) {
                            inputFalt.setText("Fyll i här");
                        }
                    }
                });

                inputFalt.addActionListener(e ->{
                String text = inputFalt.getText().trim();
                if(!text.isEmpty()){
                    modell.addElement(text);
                    inputFalt.setText("");
                   try{
                        LocalDate datum = visadManad.withDayOfMonth(aktuellDag);
                        String insertProdukt = "insert into kalenderschema (AnstalldID, OrderItemID, Datum) values (" +
                                aktuellAnstalldID + ", " + text + ", '" + datum + "')"; 
                        idb.insert(insertProdukt);
                    }
                    catch(InfException ex){
                        System.out.println("Fel vid insert:" + ex.getMessage());
                    }
                    
                }
            });

            try{
                LocalDate datum = visadManad.withDayOfMonth(dag);
                String selectText = "select OrderItemID from kalenderschema where Datum = '" + datum + "' and AnstalldID = " + aktuellAnstalldID;
                java.util.List<String> resultat = idb.fetchColumn(selectText);
                if(resultat != null){
                    for(String text:resultat){
                        modell.addElement(text);
                    }
                }
            }
            catch(InfException ex){
                System.out.println(ex.getMessage());
            }
                 
                           
            dagPanel.add(dagLabel, BorderLayout.NORTH);
            dagPanel.add(scroll, BorderLayout.CENTER);
            dagPanel.add(inputFalt, BorderLayout.SOUTH);
            kalenderRuta.add(dagPanel);
       
        }
         
        kalenderRuta.revalidate();
        kalenderRuta.repaint();
    }
    
    public static void main(String[] args){
            
            SwingUtilities.invokeLater(() -> {
            JFrame fonster = new JFrame("Min Kalender");
            fonster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fonster.setSize(1000, 800);
            fonster.setContentPane(new KalenderSchemaRatt(idb,inloggadAnvandare));
            fonster.setVisible(true);
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
