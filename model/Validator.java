package util;

public class Validator {

    // ════════════════════════════════════════
    // Validare string — nu e gol
    // ════════════════════════════════════════
    public static boolean esteValid(String valoare) {
        return valoare != null && !valoare.trim().isEmpty();
    }

    // ════════════════════════════════════════
    // Validare email
    // ════════════════════════════════════════
    public static boolean esteEmailValid(String email) {
        if (!esteValid(email)) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    // ════════════════════════════════════════
    // Validare pret — pozitiv
    // ════════════════════════════════════════
    public static boolean estePretValid(double pret) {
        return pret >= 0;
    }

    // ════════════════════════════════════════
    // Validare stoc — pozitiv
    // ════════════════════════════════════════
    public static boolean esteStocValid(int stoc) {
        return stoc >= 0;
    }

    // ════════════════════════════════════════
    // Validare cantitate — mai mare ca 0
    // ════════════════════════════════════════
    public static boolean esteCantitateValida(int cantitate) {
        return cantitate > 0;
    }

    // ════════════════════════════════════════
    // Validare telefon
    // ════════════════════════════════════════
    public static boolean esteTelefonValid(String telefon) {
        if (!esteValid(telefon)) return false;
        return telefon.matches("^[0-9+\\-() ]{7,20}$");
    }

    // ════════════════════════════════════════
    // Validare vanzator complet
    // ════════════════════════════════════════
    public static String valideazaVanzator(String nume, String email, String telefon) {
        if (!esteValid(nume))           return "Numele nu poate fi gol!";
        if (!esteEmailValid(email))     return "Email-ul nu este valid!";
        if (!esteTelefonValid(telefon)) return "Telefonul nu este valid!";
        return null; // null = totul e ok
    }

    // ════════════════════════════════════════
    // Validare produs complet
    // ════════════════════════════════════════
    public static String valideazaProdus(String nume, double pret, int stoc) {
        if (!esteValid(nume))       return "Numele produsului nu poate fi gol!";
        if (!estePretValid(pret))   return "Pretul nu poate fi negativ!";
        if (!esteStocValid(stoc))   return "Stocul nu poate fi negativ!";
        return null; // null = totul e ok
    }

    // ════════════════════════════════════════
    // Validare comanda complet
    // ════════════════════════════════════════
    public static String valideazaComanda(String numeCumparator,
                                          String emailCumparator,
                                          int cantitate) {
        if (!esteValid(numeCumparator))        return "Numele cumparatorului nu poate fi gol!";
        if (!esteEmailValid(emailCumparator))  return "Email-ul cumparatorului nu este valid!";
        if (!esteCantitateValida(cantitate))   return "Cantitatea trebuie sa fie mai mare ca 0!";
        return null; // null = totul e ok
    }
}