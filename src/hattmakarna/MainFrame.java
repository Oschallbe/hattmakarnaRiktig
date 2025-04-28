/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package hattmakarna;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import oru.inf.InfDB;
import oru.inf.InfException;
/**
 *
 * @author linodeluca
 */
public class MainFrame extends javax.swing.JFrame {
    private static InfDB idb;
    ImageIcon icon;
    
    //Skapar fält för varje panel som ska visas i menyn
    private KalenderSchemaRatt kalenderPanel;
    private SeAllaOrdrar allaOrdrarPanel;
    private SeAllaKunder allaKunderPanel;
    private SeAllaLagerfordaProdukter allaLagerfordaProdukterPanel;
    private SkapaNyOrder nyOrderPanel;
    private SkapaNySpecialOrder skapaSpecialOrderPanel;
    private SeForsaljningsstatistik seForsaljningsstatistikPanel;
    private HanteraAllaAnstallda hanteraAllaAnstalldaPanel;
    private SkapaNyFraktsedel fraktsedelPanel;
    private LäggTillNyKund laggTillKundPanel;
    private LaggTillLagerfordHatt laggTillLagerfordHattPanel;
    private SeForsaljningsstatistikSpecialprodukt statistikSpecialPanel;
    private LaggTillAnstalld laggTillAnstalldPanel;
    //Fält för att anropa validerings klassen
    private Validering validera;
    //private MainFrame parentFrame;  // referens till MainFrame


    
    
    /**
     * Creates new form MainFrame
     */
    public MainFrame(InfDB idb) {
    initComponents();
     // centrerar fönstret på skärmen (snyggt)
    // Se till att layouten på MainFrame är BorderLayout
    lblValkommen.setVisible(false);
    this.setLayout(new BorderLayout());
    
    // Hämta skärmstorlek
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    // Sätt fönstrets storlek till hela skärmen
    setSize(screenSize);
    setLocation(0, 0);

    // Alternativ: gör det till riktig helskärm (utan fönsterkant)
    // setUndecorated(true); // OBS: detta måste göras före setVisible(true)

    setVisible(true);

    // Lägg till jPanel1 i mitten
    this.add(jPanel1, BorderLayout.CENTER);

    // Lägg padding runt om
    jPanel1.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    jPanel1.setLayout(new CardLayout()); // säkerställ att den verkligen har rätt layout
    this.idb = idb;
    lblFelMeddelande.setVisible(false);
    txtfEmail.setText("karin@hattmakarna.se");
    pswfLosenord.setText("hatt123");
    lblFelMeddelande.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    // Vi vill inte att menyn ska visas förens vi loggat in
    jMenuBar1.setVisible(false);

    // Anpassa menyn (eventuella justeringar för menyer)
    for (int i = 0; i < jMenuBar1.getMenuCount(); i++) {
        JMenu menu = jMenuBar1.getMenu(i);
        menu.setOpaque(true);
        menu.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 10, 5, 10), // spacing
            BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)
        ));
    }
    
    // Ladda bilden från src/hattmakarna/hattmakarna_logotyp.png
    URL imageURL = getClass().getResource("/hattmakarna/hattmakarna_logotyp.png");

    // Kontrollera om bilden finns
    if (imageURL != null) {
        // Skapa en ImageIcon med den laddade bilden
        ImageIcon icon = new ImageIcon(imageURL);

        // Skala om bilden till t.ex. 200x200 pixlar
        Image scaledImage = icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);

        // Lägg in den skalade bilden i JLabel
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        logotyp.setIcon(scaledIcon);
    } else {
        // Om bilden inte hittades, skriv ut ett felmeddelande
        System.out.println("Bilden kunde inte hittas. Kontrollera sökvägen.");
    }
}

        private void loggaUt() {
        //När man trycker på logga ut får man upp ett fönster där mna behöver bekräfta utloggning
        int svar = JOptionPane.showConfirmDialog(this, "Vill du logga ut?", "Bekräfta", JOptionPane.YES_NO_OPTION);
        //Om man väljer jag loggas man ut
        if (svar == JOptionPane.YES_OPTION) {
            this.dispose();
            MainFrame nyMainFrame = new MainFrame(idb);
            nyMainFrame.setVisible(true);
        }
    }
        
        


    public void hanteraAnstallda(){
        try{
            String hamtaAnstallda = "select Behorighet from Anstalld where Epost = '" + txtfEmail.getText() + "';";
            String behorighet = idb.fetchSingle(hamtaAnstallda); 
                if(behorighet.equals("1")){
                    hanteraAnstallda.setVisible(false);
                }
                else if(behorighet.equals("2")){                           
                    hanteraAnstallda.setVisible(true);
                }
        }
        catch(InfException ex){
            System.out.println(ex);
        }
    }
    
    public void addPanelToCardLayout(JPanel panel, String name) {
        jPanel1.add(panel, name);
    }

    public void showPanel(String name) {
        CardLayout cl = (CardLayout) jPanel1.getLayout();
        cl.show(jPanel1, name);
        
        // Uppdatera layouten för att undvika att logotypen blir beskuren
        revalidate();
        repaint();
    }
    
    public String getForNamn() {
    String anvandarnamn = txtfEmail.getText();
    String[] delar = anvandarnamn.split("@"); // Dela på "@" för att få användarnamnet
    String baraNamn = delar[0]; // Få användarnamnet före "@"
    String namn = "";

    // Kolla om namnet innehåller en punkt, vilket indikerar för- och efternamn
    if (baraNamn.contains(".")) {
        // Dela på punkten och ta första delen (förnamnet)
        baraNamn = baraNamn.split("\\.")[0];
    }

    // Sätt förnamnet så att första bokstaven blir versal och resten gemener
    namn = baraNamn.substring(0, 1).toUpperCase() + baraNamn.substring(1).toLowerCase();

    return namn; // Returnera det formaterade förnamnet
}
    
    public void taBortValkommen(){
        lblValkommen.setVisible(false);
    }
   
   


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pswfLosenord = new javax.swing.JPasswordField();
        btnLoggaIn = new javax.swing.JButton();
        lblFelMeddelande = new javax.swing.JLabel();
        lblInloggning = new javax.swing.JLabel();
        txtfEmail = new javax.swing.JTextField();
        lblEpost = new javax.swing.JLabel();
        lblLosenord = new javax.swing.JLabel();
        logotyp = new javax.swing.JLabel();
        lblValkommen = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuOrdrar = new javax.swing.JMenu();
        seOrdrarItem = new javax.swing.JMenuItem();
        skapaNyOrderItem = new javax.swing.JMenuItem();
        skapaNySpecialOrderItem = new javax.swing.JMenuItem();
        seAllaLagerfordaProdukter = new javax.swing.JMenu();
        menuItemAllaLagerfordaProdukter = new javax.swing.JMenuItem();
        menuItemLaggTillLagerfordHatt = new javax.swing.JMenuItem();
        forsalningsstatistik = new javax.swing.JMenu();
        menuItemStandard = new javax.swing.JMenuItem();
        menuItemSpecial = new javax.swing.JMenuItem();
        visaKalender = new javax.swing.JMenu();
        hanteraAnstallda = new javax.swing.JMenu();
        menuItemHanteraAnstallda = new javax.swing.JMenuItem();
        menuItemLaggTillAnstalld = new javax.swing.JMenuItem();
        seAllaKunder = new javax.swing.JMenu();
        menuItemLaggTillKund = new javax.swing.JMenuItem();
        menuItemSeAllaKunder = new javax.swing.JMenuItem();
        installningar = new javax.swing.JMenu();
        loggaUt = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel1.setLayout(new java.awt.CardLayout());
        getContentPane().add(jPanel1);
        jPanel1.setBounds(109, 1541, 0, 0);

        pswfLosenord.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        pswfLosenord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pswfLosenordActionPerformed(evt);
            }
        });
        getContentPane().add(pswfLosenord);
        pswfLosenord.setBounds(483, 548, 228, 41);

        btnLoggaIn.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnLoggaIn.setText("Logga in");
        btnLoggaIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoggaInActionPerformed(evt);
            }
        });
        getContentPane().add(btnLoggaIn);
        btnLoggaIn.setBounds(515, 595, 137, 36);

        lblFelMeddelande.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        lblFelMeddelande.setText("Felaktig inloggning! ");
        lblFelMeddelande.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblFelMeddelande.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                lblFelMeddelandeComponentHidden(evt);
            }
        });
        getContentPane().add(lblFelMeddelande);
        lblFelMeddelande.setBounds(506, 750, 154, 21);

        lblInloggning.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        lblInloggning.setText("Inloggning");
        getContentPane().add(lblInloggning);
        lblInloggning.setBounds(480, 330, 230, 72);

        txtfEmail.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        getContentPane().add(txtfEmail);
        txtfEmail.setBounds(483, 459, 228, 41);

        lblEpost.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblEpost.setText("E-post");
        getContentPane().add(lblEpost);
        lblEpost.setBounds(385, 464, 80, 31);

        lblLosenord.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblLosenord.setText("Lösenord");
        getContentPane().add(lblLosenord);
        lblLosenord.setBounds(385, 557, 75, 25);

        logotyp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hattmakarna/hattmakarna_logotyp.png"))); // NOI18N
        getContentPane().add(logotyp);
        logotyp.setBounds(500, 120, 200, 200);

        lblValkommen.setFont(new java.awt.Font("Helvetica Neue", 1, 60)); // NOI18N
        lblValkommen.setText("efrgtyh");
        getContentPane().add(lblValkommen);
        lblValkommen.setBounds(530, 380, 660, 50);

        jMenuBar1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenuBar1.setFont(new java.awt.Font("Helvetica Neue", 0, 16)); // NOI18N

        menuOrdrar.setText("Ordrar");

        seOrdrarItem.setText("Se alla ordrar");
        seOrdrarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seOrdrarItemActionPerformed(evt);
            }
        });
        menuOrdrar.add(seOrdrarItem);

        skapaNyOrderItem.setText("Skapa ny order");
        skapaNyOrderItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skapaNyOrderItemActionPerformed(evt);
            }
        });
        menuOrdrar.add(skapaNyOrderItem);

        skapaNySpecialOrderItem.setText("Skapa ny specialorder");
        skapaNySpecialOrderItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skapaNySpecialOrderItemActionPerformed(evt);
            }
        });
        menuOrdrar.add(skapaNySpecialOrderItem);

        jMenuBar1.add(menuOrdrar);

        seAllaLagerfordaProdukter.setText("Lagerförda produkter");
        seAllaLagerfordaProdukter.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                seAllaLagerfordaProdukterMenuSelected(evt);
            }
        });

        menuItemAllaLagerfordaProdukter.setText("Se alla lagerförda produkter");
        menuItemAllaLagerfordaProdukter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAllaLagerfordaProdukterActionPerformed(evt);
            }
        });
        seAllaLagerfordaProdukter.add(menuItemAllaLagerfordaProdukter);

        menuItemLaggTillLagerfordHatt.setText("Lägg till ny lagerförd hatt");
        menuItemLaggTillLagerfordHatt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemLaggTillLagerfordHattActionPerformed(evt);
            }
        });
        seAllaLagerfordaProdukter.add(menuItemLaggTillLagerfordHatt);

        jMenuBar1.add(seAllaLagerfordaProdukter);

        forsalningsstatistik.setText("Försäljningsstatistik");
        forsalningsstatistik.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                forsalningsstatistikMenuSelected(evt);
            }
        });

        menuItemStandard.setText("Standard");
        menuItemStandard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemStandardActionPerformed(evt);
            }
        });
        forsalningsstatistik.add(menuItemStandard);

        menuItemSpecial.setText("Special");
        menuItemSpecial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSpecialActionPerformed(evt);
            }
        });
        forsalningsstatistik.add(menuItemSpecial);

        jMenuBar1.add(forsalningsstatistik);

        visaKalender.setText("Visa Kalenderschema");
        visaKalender.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                visaKalenderMenuSelected(evt);
            }
        });
        jMenuBar1.add(visaKalender);

        hanteraAnstallda.setText("Anställda");
        hanteraAnstallda.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                hanteraAnstalldaMenuSelected(evt);
            }
        });

        menuItemHanteraAnstallda.setText("Hantera alla anställda");
        menuItemHanteraAnstallda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemHanteraAnstalldaActionPerformed(evt);
            }
        });
        hanteraAnstallda.add(menuItemHanteraAnstallda);

        menuItemLaggTillAnstalld.setText("Lägg till ny anställd");
        menuItemLaggTillAnstalld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemLaggTillAnstalldActionPerformed(evt);
            }
        });
        hanteraAnstallda.add(menuItemLaggTillAnstalld);

        jMenuBar1.add(hanteraAnstallda);

        seAllaKunder.setText("Kunder");
        seAllaKunder.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                seAllaKunderMenuSelected(evt);
            }
        });

        menuItemLaggTillKund.setText("Lägg till ny kund");
        menuItemLaggTillKund.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemLaggTillKundActionPerformed(evt);
            }
        });
        seAllaKunder.add(menuItemLaggTillKund);

        menuItemSeAllaKunder.setText("Se alla kunder");
        menuItemSeAllaKunder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSeAllaKunderActionPerformed(evt);
            }
        });
        seAllaKunder.add(menuItemSeAllaKunder);

        jMenuBar1.add(seAllaKunder);

        installningar.setText("Inställningar");
        installningar.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                installningarMenuSelected(evt);
            }
        });

        loggaUt.setText("Logga ut");
        loggaUt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loggaUtActionPerformed(evt);
            }
        });
        installningar.add(loggaUt);

        jMenuBar1.add(installningar);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void visaKalenderMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_visaKalenderMenuSelected
        // TODO add your handling code here:
        // Kontrollera om "inlogg" redan finns
        // Skapa SeAllaOrdrar-panelen som vanligt
        taBortValkommen();
        kalenderPanel = new KalenderSchemaRatt(idb, txtfEmail.getText());

        // Skapa en wrapper-panel med centrerad layout
        JPanel wrapper = new JPanel(new GridBagLayout()); // centrerar automatiskt sitt innehåll
        wrapper.add(kalenderPanel); // lägg SeAllaOrdrar i mitten

        // Lägg till wrappern i card layout-systemet istället för SeAllaOrdrar direkt
        addPanelToCardLayout(wrapper, "Kalenderschema");

        // Visa
        showPanel("Kalenderschema");
        
        
        
    }//GEN-LAST:event_visaKalenderMenuSelected

    private void seAllaKunderMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_seAllaKunderMenuSelected
        // TODO add your handling code here:
    }//GEN-LAST:event_seAllaKunderMenuSelected

    private void seAllaLagerfordaProdukterMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_seAllaLagerfordaProdukterMenuSelected
        // TODO add your handling code here: 
    }//GEN-LAST:event_seAllaLagerfordaProdukterMenuSelected

    private void forsalningsstatistikMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_forsalningsstatistikMenuSelected
        // TODO add your handling code here:
    }//GEN-LAST:event_forsalningsstatistikMenuSelected

    private void hanteraAnstalldaMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_hanteraAnstalldaMenuSelected
        // TODO add your handling code here:
    }//GEN-LAST:event_hanteraAnstalldaMenuSelected

    private void installningarMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_installningarMenuSelected
        // TODO add your handling code here:
        taBortValkommen();
    }//GEN-LAST:event_installningarMenuSelected

    private void loggaUtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loggaUtActionPerformed
        // TODO add your handling code here:
        taBortValkommen();
        loggaUt();
        
    }//GEN-LAST:event_loggaUtActionPerformed

    private void pswfLosenordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pswfLosenordActionPerformed

    }//GEN-LAST:event_pswfLosenordActionPerformed

    private void btnLoggaInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoggaInActionPerformed
        //Hämtar användarinmatning
        String ePost = txtfEmail.getText();
    char[] losenord = pswfLosenord.getPassword();
    String losen = new String(losenord);

    try {
        lblFelMeddelande.setVisible(false);

        if (!Validering.faltInteTomt(ePost) || !Validering.faltInteTomt(losen)) {
            lblFelMeddelande.setText("Inget av fälten får vara tomma");
            lblFelMeddelande.setVisible(true);
            return;
        }

        if (!Validering.valideringEmail(ePost)) {
            lblFelMeddelande.setText("Ange korrekt format för e-postadress");
            lblFelMeddelande.setVisible(true);
            return;
        }

        if (!Validering.finnsEpost(ePost, idb)) {
            lblFelMeddelande.setText("Finns ingen användare med denna epost");
            lblFelMeddelande.setVisible(true);
            return;
        }

        if (!Validering.arLosenordKorrekt(ePost, losen, idb)) {
            lblFelMeddelande.setText("Fel lösenord för denna användare");
            lblFelMeddelande.setVisible(true);
            pswfLosenord.setText("");
            return;
        }

        // Hämta behörighet från databasen
        String behorighetQuery = "SELECT Behorighet FROM Anstalld WHERE Epost = '" + ePost + "'";
        String behorighet = idb.fetchSingle(behorighetQuery);
        if("1".equals(behorighet)){
            hanteraAnstallda.setVisible(false);
        }else{
            hanteraAnstallda.setVisible(true);
        }

        // Dölj inloggningskomponenter
        lblInloggning.setVisible(false);
        lblEpost.setVisible(false);
        txtfEmail.setVisible(false);
        lblLosenord.setVisible(false);
        pswfLosenord.setVisible(false);
        btnLoggaIn.setVisible(false);
        lblFelMeddelande.setVisible(false);

        // Visa menyrad
        jMenuBar1.setVisible(true);
        logotyp.setVisible(false);
        // Visa inloggade användare välkommen
        
        // Ladda bilden från src/hattmakarna/hattmakarna_logotyp.png
        URL imageURL = getClass().getResource("/hattmakarna/hattmakarna_logotyp.png");

        // Kontrollera om bilden finns
        // Kontrollera om bilden finns
           if (imageURL != null) {
            // Skapa en ImageIcon med den laddade bilden
            ImageIcon icon = new ImageIcon(imageURL);

            // Skala om bilden till t.ex. 100x100 pixlar
            Image scaledImage = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

            // Lägg in den skalade bilden i JLabel
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            logotyp.setIcon(scaledIcon);

            // 🟢 Sätt bounds EFTER setIcon men INNAN du visar logotypen
            logotyp.setBounds(10, 10, 100, 100);

            // 🟢 Visa logotypen (om den var dold tidigare)
            logotyp.setVisible(true);
        } else {
            System.out.println("Bilden kunde inte hittas. Kontrollera sökvägen.");
        }

        lblValkommen.setText("Välkommen" + " " + getForNamn());
        lblValkommen.setVisible(true);
        
       // hanteraAnstallda();
        

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Något gick fel: " + ex.getMessage());
    }
    }//GEN-LAST:event_btnLoggaInActionPerformed

    private void lblFelMeddelandeComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_lblFelMeddelandeComponentHidden

    }//GEN-LAST:event_lblFelMeddelandeComponentHidden

    private void seOrdrarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seOrdrarItemActionPerformed
        // TODO add your handling code here:
        taBortValkommen();
        allaOrdrarPanel = new SeAllaOrdrar(idb, txtfEmail.getText());

        // Skapa en wrapper-panel med centrerad layout
        JPanel wrapper = new JPanel(new GridBagLayout()); // centrerar automatiskt sitt innehåll
        wrapper.add(allaOrdrarPanel); // lägg SeAllaOrdrar i mitten

        // Lägg till wrappern i card layout-systemet istället för SeAllaOrdrar direkt
        addPanelToCardLayout(wrapper, "Alla ordrar");

        // Visa
        showPanel("Alla ordrar");
    }//GEN-LAST:event_seOrdrarItemActionPerformed

    private void skapaNyOrderItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skapaNyOrderItemActionPerformed
        // TODO add your handling code here:
        taBortValkommen();
        nyOrderPanel = new SkapaNyOrder(idb, txtfEmail.getText());

        // Skapa en wrapper-panel med centrerad layout
        JPanel wrapper = new JPanel(new GridBagLayout()); // centrerar automatiskt sitt innehåll
        wrapper.add(nyOrderPanel); // lägg SeAllaOrdrar i mitten

        // Lägg till wrappern i card layout-systemet istället för SeAllaOrdrar direkt
        addPanelToCardLayout(wrapper, "Skapa ny order");

        // Visa
        showPanel("Skapa ny order");
    }//GEN-LAST:event_skapaNyOrderItemActionPerformed

    private void skapaNySpecialOrderItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skapaNySpecialOrderItemActionPerformed
        // TODO add your handling code here:
        taBortValkommen();
        skapaSpecialOrderPanel = new SkapaNySpecialOrder(idb, txtfEmail.getText());

        // Skapa en wrapper-panel med centrerad layout
        JPanel wrapper = new JPanel(new GridBagLayout()); // centrerar automatiskt sitt innehåll
        wrapper.add(skapaSpecialOrderPanel); // lägg SeAllaOrdrar i mitten

        // Lägg till wrappern i card layout-systemet istället för SeAllaOrdrar direkt
        addPanelToCardLayout(wrapper, "Skapa ny specialorder");

        // Visa
        showPanel("Skapa ny specialorder");
    }//GEN-LAST:event_skapaNySpecialOrderItemActionPerformed

    private void menuItemLaggTillKundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemLaggTillKundActionPerformed
        // TODO add your handling code here:
        taBortValkommen();
        laggTillKundPanel = new LäggTillNyKund(idb);

        // Skapa en wrapper-panel med centrerad layout
        JPanel wrapper = new JPanel(new GridBagLayout()); // centrerar automatiskt sitt innehåll
        wrapper.add(laggTillKundPanel); // lägg SeAllaOrdrar i mitten

        // Lägg till wrappern i card layout-systemet istället för SeAllaOrdrar direkt
        addPanelToCardLayout(wrapper, "Lägg till ny kund");

        // Visa
        showPanel("Lägg till ny kund");
    }//GEN-LAST:event_menuItemLaggTillKundActionPerformed

    private void menuItemSeAllaKunderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSeAllaKunderActionPerformed
        // TODO add your handling code here:
        taBortValkommen();
        allaKunderPanel = new SeAllaKunder(idb, txtfEmail.getText());

        // Skapa en wrapper-panel med centrerad layout
        JPanel wrapper = new JPanel(new GridBagLayout()); // centrerar automatiskt sitt innehåll
        wrapper.add(allaKunderPanel); // lägg SeAllaOrdrar i mitten

        // Lägg till wrappern i card layout-systemet istället för SeAllaOrdrar direkt
        addPanelToCardLayout(wrapper, "Alla kunder");

        // Visa
        showPanel("Alla kunder");
    }//GEN-LAST:event_menuItemSeAllaKunderActionPerformed

    private void menuItemAllaLagerfordaProdukterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAllaLagerfordaProdukterActionPerformed
        // TODO add your handling code here:
        taBortValkommen();
        allaLagerfordaProdukterPanel = new SeAllaLagerfordaProdukter(idb, txtfEmail.getText());

        // Skapa en wrapper-panel med centrerad layout
        JPanel wrapper = new JPanel(new GridBagLayout()); // centrerar automatiskt sitt innehåll
        wrapper.add(allaLagerfordaProdukterPanel); // lägg SeAllaOrdrar i mitten

        // Lägg till wrappern i card layout-systemet istället för SeAllaOrdrar direkt
        addPanelToCardLayout(wrapper, "Alla lagerförda produkter");

        // Visa
        showPanel("Alla lagerförda produkter");
    }//GEN-LAST:event_menuItemAllaLagerfordaProdukterActionPerformed

    private void menuItemLaggTillLagerfordHattActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemLaggTillLagerfordHattActionPerformed
        // TODO add your handling code here:
        taBortValkommen();
        laggTillLagerfordHattPanel = new LaggTillLagerfordHatt(idb, txtfEmail.getText());

        // Skapa en wrapper-panel med centrerad layout
        JPanel wrapper = new JPanel(new GridBagLayout()); // centrerar automatiskt sitt innehåll
        wrapper.add(laggTillLagerfordHattPanel); // lägg SeAllaOrdrar i mitten

        // Lägg till wrappern i card layout-systemet istället för SeAllaOrdrar direkt
        addPanelToCardLayout(wrapper, "Lägg till lagerförda hattar");

        // Visa
        showPanel("Lägg till lagerförda hattar");
    }//GEN-LAST:event_menuItemLaggTillLagerfordHattActionPerformed

    private void menuItemStandardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemStandardActionPerformed
        // TODO add your handling code here:
        taBortValkommen();
        seForsaljningsstatistikPanel = new SeForsaljningsstatistik(idb, txtfEmail.getText());

        // Skapa en wrapper-panel med centrerad layout
        JPanel wrapper = new JPanel(new GridBagLayout()); // centrerar automatiskt sitt innehåll
        wrapper.add(seForsaljningsstatistikPanel); // lägg SeAllaOrdrar i mitten

        // Lägg till wrappern i card layout-systemet istället för SeAllaOrdrar direkt
        addPanelToCardLayout(wrapper, "Se försäljningsstatistik");

        // Visa
        showPanel("Se försäljningsstatistik");
    }//GEN-LAST:event_menuItemStandardActionPerformed

    private void menuItemSpecialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSpecialActionPerformed
        // TODO add your handling code here:
        taBortValkommen();
        statistikSpecialPanel = new SeForsaljningsstatistikSpecialprodukt(idb, txtfEmail.getText());

        // Skapa en wrapper-panel med centrerad layout
        JPanel wrapper = new JPanel(new GridBagLayout()); // centrerar automatiskt sitt innehåll
        wrapper.add(statistikSpecialPanel); // lägg SeAllaOrdrar i mitten

        // Lägg till wrappern i card layout-systemet istället för SeAllaOrdrar direkt
        addPanelToCardLayout(wrapper, "Se försäljningsstatistik för specialhattar");

        // Visa
        showPanel("Se försäljningsstatistik för specialhattar");
    }//GEN-LAST:event_menuItemSpecialActionPerformed

    private void menuItemHanteraAnstalldaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemHanteraAnstalldaActionPerformed
        // TODO add your handling code here:
        taBortValkommen();
        
        hanteraAllaAnstalldaPanel = new HanteraAllaAnstallda(idb, txtfEmail.getText());

        // Skapa en wrapper-panel med centrerad layout
        JPanel wrapper = new JPanel(new GridBagLayout()); // centrerar automatiskt sitt innehåll
        wrapper.add(hanteraAllaAnstalldaPanel); // lägg SeAllaOrdrar i mitten

        // Lägg till wrappern i card layout-systemet istället för SeAllaOrdrar direkt
        addPanelToCardLayout(wrapper, "Hantera anställda");

        // Visa
        showPanel("Hantera anställda");
    }//GEN-LAST:event_menuItemHanteraAnstalldaActionPerformed

    private void menuItemLaggTillAnstalldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemLaggTillAnstalldActionPerformed
        // TODO add your handling code here:
        taBortValkommen();
        
        laggTillAnstalldPanel = new LaggTillAnstalld(idb, txtfEmail.getText());

        // Skapa en wrapper-panel med centrerad layout
        JPanel wrapper = new JPanel(new GridBagLayout()); // centrerar automatiskt sitt innehåll
        wrapper.add(laggTillAnstalldPanel); // lägg SeAllaOrdrar i mitten

        // Lägg till wrappern i card layout-systemet istället för SeAllaOrdrar direkt
        addPanelToCardLayout(wrapper, "Hantera anställda");

        // Visa
        showPanel("Hantera anställda");
    }//GEN-LAST:event_menuItemLaggTillAnstalldActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLoggaIn;
    private javax.swing.JMenu forsalningsstatistik;
    private javax.swing.JMenu hanteraAnstallda;
    private javax.swing.JMenu installningar;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblEpost;
    private javax.swing.JLabel lblFelMeddelande;
    private javax.swing.JLabel lblInloggning;
    private javax.swing.JLabel lblLosenord;
    private javax.swing.JLabel lblValkommen;
    private javax.swing.JMenuItem loggaUt;
    private javax.swing.JLabel logotyp;
    private javax.swing.JMenuItem menuItemAllaLagerfordaProdukter;
    private javax.swing.JMenuItem menuItemHanteraAnstallda;
    private javax.swing.JMenuItem menuItemLaggTillAnstalld;
    private javax.swing.JMenuItem menuItemLaggTillKund;
    private javax.swing.JMenuItem menuItemLaggTillLagerfordHatt;
    private javax.swing.JMenuItem menuItemSeAllaKunder;
    private javax.swing.JMenuItem menuItemSpecial;
    private javax.swing.JMenuItem menuItemStandard;
    private javax.swing.JMenu menuOrdrar;
    private javax.swing.JPasswordField pswfLosenord;
    private javax.swing.JMenu seAllaKunder;
    private javax.swing.JMenu seAllaLagerfordaProdukter;
    private javax.swing.JMenuItem seOrdrarItem;
    private javax.swing.JMenuItem skapaNyOrderItem;
    private javax.swing.JMenuItem skapaNySpecialOrderItem;
    private javax.swing.JTextField txtfEmail;
    private javax.swing.JMenu visaKalender;
    // End of variables declaration//GEN-END:variables
}
