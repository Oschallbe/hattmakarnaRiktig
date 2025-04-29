/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hattmakarna;

import oru.inf.InfDB;
import oru.inf.InfException;

/**
 *
 * @author limme
 */
public class Hattmakarna {

    private static InfDB idb; //skapar ett fält av klassen InfDB. Fältet som man vill lagra databasuppkopplingen i

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            idb = new InfDB("hattmakaren", "3306", "dbHattAdmin", "dbHattAdminPW"); //Namnet på databasen, vilken port,användarnamnet till databasen, lösenordet för användaren
            new MainFrame(idb).setVisible(true);
        } catch (InfException ex) { //om någotning går fel i uppkopplingen av databasen så kommer variabeln ex fånga upp de specifika felmeddelandena som finns i infdb-klassen
            System.out.println(ex.getMessage());
        }
    }

}
