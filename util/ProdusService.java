package util;

import model.Produs;
import model.ProdusFizic;
import model.ProduDigital;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProdusService {

    private List<Produs> listaProduse   = new ArrayList<>();
    private Map<Integer, Produs> mapProduse = new HashMap<>();

    // ── Adauga un produs ─────────────────────────────────────
    public void adauga(Produs p) {
        listaProduse.add(p);
        mapProduse.put(p.getId(), p);
        System.out.println("✔ Adaugat: " + p.getNume()
                + " [" + p.getTipLivrare() + "]");
    }

    // ── Cauta dupa ID ────────────────────────────────────────
    public Produs gasesteDupaId(int id) {
        return mapProduse.get(id);
    }

    // ── Filtrare dupa pret maxim ─────────────────────────────
    public List<Produs> filtreazaDupaPret(double pretMaxim) {
        List<Produs> rezultat = new ArrayList<>();
        for (Produs p : listaProduse) {
            if (p.getPret() <= pretMaxim) {
                rezultat.add(p);
            }
        }
        return rezultat;
    }

    // ── Filtrare doar produse fizice ─────────────────────────
    public List<Produs> doarFizice() {
        List<Produs> rezultat = new ArrayList<>();
        for (Produs p : listaProduse) {
            if (p instanceof ProdusFizic) {  // polimorfism!
                rezultat.add(p);
            }
        }
        return rezultat;
    }

    // ── Filtrare doar produse digitale ───────────────────────
    public List<Produs> doarDigitale() {
        List<Produs> rezultat = new ArrayList<>();
        for (Produs p : listaProduse) {
            if (p instanceof ProduDigital) {  // polimorfism!
                rezultat.add(p);
            }
        }
        return rezultat;
    }

    // ── Afiseaza toate produsele ─────────────────────────────
    public void afiseazaToate() {
        System.out.println("  Produse (" + listaProduse.size() + "):");
        for (Produs p : listaProduse) {
            System.out.println("  → " + p.toText());
        }
    }

    public List<Produs> getToate() {
        return listaProduse;
    }
}