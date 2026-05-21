package model;

import java.time.LocalDate;

import model.interfaces.Exportabil;
import model.enums.StatusComanda;
public class Comanda implements Exportabil {

    private int id;
    private int produsId;
    private int vanzatorId;
    private int cantitate;
    private double pretUnitar;
    private double totalPret;
    private StatusComanda status;
    private String numeCumparator;
    private String emailCumparator;
    private String adresaLivrare;
    private LocalDate dataComenzii;

    public Comanda(int id, int produsId, int vanzatorId,
                   int cantitate, double pretUnitar, double totalPret) {
        this.id = id;
        this.produsId = produsId;
        this.vanzatorId = vanzatorId;
        this.cantitate = cantitate;
        this.pretUnitar = pretUnitar;
        this.totalPret = totalPret;
        this.dataComenzii = LocalDate.now();
        this.status = StatusComanda.IN_PROCESARE;
    }

    // ── Getteri & Setteri ────────────────────────────────────
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProdusId() { return produsId; }
    public void setProdusId(int produsId) { this.produsId = produsId; }

    public int getVanzatorId() { return vanzatorId; }
    public void setVanzatorId(int vanzatorId) { this.vanzatorId = vanzatorId; }

    public int getCantitate() { return cantitate; }
    public void setCantitate(int cantitate) { this.cantitate = cantitate; }

    public double getPretUnitar() { return pretUnitar; }
    public void setPretUnitar(double pretUnitar) { this.pretUnitar = pretUnitar; }

    public double getTotalPret() { return totalPret; }
    public void setTotalPret(double totalPret) { this.totalPret = totalPret; }

    public StatusComanda getStatus() { return status; }
    public void setStatus(StatusComanda status) { this.status = status; }

    public String getNumeCumparator() { return numeCumparator; }
    public void setNumeCumparator(String numeCumparator) { this.numeCumparator = numeCumparator; }

    public String getEmailCumparator() { return emailCumparator; }
    public void setEmailCumparator(String emailCumparator) { this.emailCumparator = emailCumparator; }

    public String getAdresaLivrare() { return adresaLivrare; }
    public void setAdresaLivrare(String adresaLivrare) { this.adresaLivrare = adresaLivrare; }

    public LocalDate getDataComenzii() { return dataComenzii; }
    public void setDataComenzii(LocalDate dataComenzii) { this.dataComenzii = dataComenzii; }

    // ── Exportabil ───────────────────────────────────────────
    @Override
    public String toCsv() {
        return id + "," + produsId + "," + vanzatorId + ","
                + cantitate + "," + pretUnitar + "," + totalPret + ","
                + status.name() + "," + numeCumparator + ","
                + emailCumparator + "," + (adresaLivrare != null ? adresaLivrare : "")
                + "," + dataComenzii;
    }

    @Override
    public String toText() {
        return "Comanda #" + id
                + " | Produs ID: " + produsId
                + " | Cantitate: " + cantitate
                + " | Total: " + totalPret + " lei"
                + " | Status: " + status.getDescriere()
                + " | Cumparator: " + numeCumparator
                + " | Data: " + dataComenzii;
    }

    @Override
    public String toString() {
        return "Comanda{id=" + id + ", status=" + status.getDescriere()
                + ", total=" + totalPret + "}";
    }
}