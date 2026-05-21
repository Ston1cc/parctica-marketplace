package model;

import model.interfaces.Exportabil;
public class Vanzator implements Exportabil {

    private int id;
    private String nume;
    private String email;
    private String telefon;
    private String adresa;

    public Vanzator(int id, String nume, String email, String telefon) {
        this.id = id;
        this.nume = nume;
        this.email = email;
        this.telefon = telefon;
    }

    // ── Getteri & Setteri ────────────────────────────────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }

    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }

    // ── Exportabil ───────────────────────────────────────────
    @Override
    public String toCsv() {
        return id + "," + nume + "," + email + ","
                + telefon + "," + (adresa != null ? adresa : "");
    }

    @Override
    public String toText() {
        return "Vanzator #" + id
                + " | Nume: " + nume
                + " | Email: " + email
                + " | Tel: " + telefon
                + (adresa != null ? " | Adresa: " + adresa : "");
    }

    @Override
    public String toString() {
        return "Vanzator{id=" + id + ", nume='" + nume + "'}";
    }
}