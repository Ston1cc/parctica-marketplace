package model;

import model.interfaces.Exportabil;

public abstract class Produs implements Exportabil {

    private int id;
    private String nume;
    private String descriere;
    private double pret;
    private int stoc;
    private int vanzatorId;

    public Produs(int id, String nume, double pret, int stoc, int vanzatorId) {
        this.id = id;
        this.nume = nume;
        this.pret = pret;
        this.stoc = stoc;
        this.vanzatorId = vanzatorId;
    }

    // ── Metoda abstracta — polimorfism ───────────────────────
    public abstract String getTipLivrare();

    // ── Getteri & Setteri ────────────────────────────────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNume() { return nume; }
    public void setNume(String nume) { this.nume = nume; }

    public String getDescriere() { return descriere; }
    public void setDescriere(String descriere) { this.descriere = descriere; }

    public double getPret() { return pret; }
    public void setPret(double pret) { this.pret = pret; }

    public int getStoc() { return stoc; }
    public void setStoc(int stoc) { this.stoc = stoc; }

    public int getVanzatorId() { return vanzatorId; }
    public void setVanzatorId(int vanzatorId) { this.vanzatorId = vanzatorId; }

    @Override
    public String toString() {
        return "Produs{id=" + id + ", nume='" + nume
                + "', pret=" + pret + ", tip=" + getTipLivrare() + "}";
    }
}