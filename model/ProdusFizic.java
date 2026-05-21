package model;

public class ProdusFizic extends Produs {

    private double greutateKg;
    private String dimensiuni;

    public ProdusFizic(int id, String nume, double pret, int stoc,
                       int vanzatorId, double greutateKg, String dimensiuni) {
        super(id, nume, pret, stoc, vanzatorId);
        this.greutateKg = greutateKg;
        this.dimensiuni = dimensiuni;
    }

    // ── Getteri & Setteri ────────────────────────────────────
    public double getGreutateKg() { return greutateKg; }
    public void setGreutateKg(double greutateKg) { this.greutateKg = greutateKg; }

    public String getDimensiuni() { return dimensiuni; }
    public void setDimensiuni(String dimensiuni) { this.dimensiuni = dimensiuni; }

    // ── Polimorfism ──────────────────────────────────────────
    @Override
    public String getTipLivrare() {
        return "Livrare fizica (" + greutateKg + " kg, " + dimensiuni + ")";
    }

    // ── Exportabil ───────────────────────────────────────────
    @Override
    public String toCsv() {
        return getId() + "," + getNume() + "," + getPret() + ","
                + getStoc() + "," + getVanzatorId() + ","
                + greutateKg + "," + dimensiuni + ",FIZIC";
    }

    @Override
    public String toText() {
        return "Produs Fizic #" + getId()
                + " | Nume: " + getNume()
                + " | Pret: " + getPret() + " lei"
                + " | Stoc: " + getStoc()
                + " | Greutate: " + greutateKg + " kg"
                + " | Dim: " + dimensiuni;
    }
}