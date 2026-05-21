package model;

public class ProduDigital extends Produs {

    private String linkDescarcare;
    private String format;

    public ProduDigital(int id, String nume, double pret, int stoc,
                        int vanzatorId, String linkDescarcare, String format) {
        super(id, nume, pret, stoc, vanzatorId);
        this.linkDescarcare = linkDescarcare;
        this.format = format;
    }

    // ── Getteri & Setteri ────────────────────────────────────
    public String getLinkDescarcare() { return linkDescarcare; }
    public void setLinkDescarcare(String linkDescarcare) { this.linkDescarcare = linkDescarcare; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    // ── Polimorfism ──────────────────────────────────────────
    @Override
    public String getTipLivrare() {
        return "Descarcare digitala (" + format + ")";
    }

    // ── Exportabil ───────────────────────────────────────────
    @Override
    public String toCsv() {
        return getId() + "," + getNume() + "," + getPret() + ","
                + getStoc() + "," + getVanzatorId() + ","
                + linkDescarcare + "," + format + ",DIGITAL";
    }

    @Override
    public String toText() {
        return "Produs Digital #" + getId()
                + " | Nume: " + getNume()
                + " | Pret: " + getPret() + " lei"
                + " | Format: " + format
                + " | Link: " + linkDescarcare;
    }
}