package util;

import model.Vanzator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VanzatorService {

    // ArrayList — lista tuturor vanzatorilor in ordine
    private List<Vanzator> listaVanzatori = new ArrayList<>();

    // HashMap — gasim rapid un vanzator dupa ID
    // Cheia = ID-ul (Integer), Valoarea = obiectul Vanzator
    private Map<Integer, Vanzator> mapVanzatori = new HashMap<>();

    // ── Adauga un vanzator ───────────────────────────────────
    public void adauga(Vanzator v) {
        listaVanzatori.add(v);           // adaugam in lista
        mapVanzatori.put(v.getId(), v);  // adaugam in map dupa ID
        System.out.println("✔ Adaugat: " + v.getNume());
    }

    // ── Cauta dupa ID (instant, folosind HashMap) ────────────
    public Vanzator gasesteDupaId(int id) {
        return mapVanzatori.get(id);  // O(1) — extrem de rapid
    }

    // ── Returneaza toti vanzatorii ───────────────────────────
    public List<Vanzator> getToti() {
        return listaVanzatori;
    }

    // ── Sterge un vanzator ───────────────────────────────────
    public void sterge(int id) {
        Vanzator v = mapVanzatori.remove(id);
        if (v != null) {
            listaVanzatori.remove(v);
            System.out.println("✔ Sters: " + v.getNume());
        } else {
            System.out.println("✘ Vanzatorul cu ID " + id + " nu exista!");
        }
    }

    // ── Afiseaza toti vanzatorii ─────────────────────────────
    public void afiseazaToti() {
        if (listaVanzatori.isEmpty()) {
            System.out.println("  Lista vanzatorilor e goala!");
            return;
        }
        System.out.println("  Lista vanzatori (" + listaVanzatori.size() + "):");
        for (Vanzator v : listaVanzatori) {
            System.out.println("  → " + v.toText());
        }
    }
}