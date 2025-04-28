/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hattmakarna;

import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import oru.inf.InfDB;
import oru.inf.InfException;

/**
 *
 *
 * @author linodeluca
 */
public class Validering {

    //Email
    private static final String EMAIL_REGEX = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    //Telefon
    private static final String PHONE_REGEX = "^\\+[0-9]{1,15}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    //Email
    public static boolean valideringEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    //Telefon
    public static boolean valideringTelefon(String telefon) {
        if (telefon == null || telefon.isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(telefon).matches();
    }

    //Datum
    public static boolean valideringDatum(String date) {
        if (date == null || date.isEmpty()) {
            return false;
        }

        // Ange det förväntade datumformatet
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            // Försök att parsa datumet
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            // Ogiltigt datumformat eller ogiltigt datum
            return false;
        }
    }

    // Kontrollerar att ett fält inte är tomt
    public static boolean faltInteTomt(String input) {
        return input != null && !input.trim().isEmpty();
    }

    //Kontrollerar endast siffror
    public static boolean arEndastSiffror(String input) {
        return input.trim().matches("\\d+");
    }

    public static boolean arGiltigtDouble(String input) {
        return input.matches("^\\d+(\\.\\d+)?$");
    }

    // Kontrollerar att input endast innehåller bokstäver (inkl. svenska tecken)
    public static boolean arEndastBokstaver(String input) {
        return input.trim().matches("^[a-zA-ZåäöÅÄÖ]+$");
    }
    
    public static boolean finnsEpost(String epost, InfDB idb) {
    try {
        String fraga = "SELECT Epost FROM anstalld WHERE Epost = '" + epost + "';";
        String resultat = idb.fetchSingle(fraga);
        return resultat != null;
    } catch (InfException e) {
        JOptionPane.showMessageDialog(null, "Fel");
        return false;
    }}
    
    public static boolean arLosenordKorrekt(String epost, String losenord, InfDB idb) {
    try {
        String fraga = "SELECT Losenord FROM anstalld WHERE Epost = '" + epost + "';";
        String dbLosen = idb.fetchSingle(fraga);
        return losenord.equals(dbLosen);
    } catch (InfException e) {
        JOptionPane.showMessageDialog(null, "Fel");
        return false;
    }
}

}
