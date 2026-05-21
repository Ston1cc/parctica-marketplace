package util;

import dao.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RaportService {

    private Connection conn;

    public RaportService() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    // ════════════════════════════════════════
    // RAPORT 1: Top vanzatori dupa venituri
    // ════════════════════════════════════════
    public List<String> raportTopVanzatori() {
        List<String> rezultat = new ArrayList<>();
        rezultat.add("══════════════════════════════════════════");
        rezultat.add("  RAPORT 1: TOP VANZATORI DUPA VENITURI");
        rezultat.add("══════════════════════════════════════════");

        String sql = "SELECT v.nume, v.email, "
                + "COUNT(c.id) AS nr_comenzi, "
                + "COALESCE(SUM(c.total_pret), 0) AS venituri_totale "
                + "FROM vanzatori v "
                + "LEFT JOIN comenzi c ON v.id = c.vanzator_id "
                + "AND c.status != 'ANULATA' "
                + "GROUP BY v.id, v.nume, v.email "
                + "ORDER BY venituri_totale DESC";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            int loc = 1;
            while (rs.next()) {
                rezultat.add(String.format("  #%d %-20s | Comenzi: %d | Venituri: %.2f lei",
                        loc++,
                        rs.getString("nume"),
                        rs.getInt("nr_comenzi"),
                        rs.getDouble("venituri_totale")));
            }

        } catch (SQLException e) {
            rezultat.add("  ✘ Eroare raport: " + e.getMessage());
        }

        rezultat.add("══════════════════════════════════════════");
        return rezultat;
    }

    // ════════════════════════════════════════
    // RAPORT 2: Comenzi grupate pe status
    // ════════════════════════════════════════
    public List<String> raportComenziPeStatus() {
        List<String> rezultat = new ArrayList<>();
        rezultat.add("══════════════════════════════════════════");
        rezultat.add("  RAPORT 2: COMENZI GRUPATE PE STATUS");
        rezultat.add("══════════════════════════════════════════");

        String sql = "SELECT status, COUNT(*) AS nr_comenzi, "
                + "SUM(total_pret) AS valoare_totala "
                + "FROM comenzi GROUP BY status";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                rezultat.add(String.format("  %-15s | Comenzi: %d | Valoare: %.2f lei",
                        rs.getString("status"),
                        rs.getInt("nr_comenzi"),
                        rs.getDouble("valoare_totala")));
            }

        } catch (SQLException e) {
            rezultat.add("  ✘ Eroare raport: " + e.getMessage());
        }

        rezultat.add("══════════════════════════════════════════");
        return rezultat;
    }

    // ════════════════════════════════════════
    // RAPORT 3: Produse cu stoc scazut
    // ════════════════════════════════════════
    public List<String> raportStocScazut() {
        List<String> rezultat = new ArrayList<>();
        rezultat.add("══════════════════════════════════════════");
        rezultat.add("  RAPORT 3: PRODUSE CU STOC SCAZUT (< 5)");
        rezultat.add("══════════════════════════════════════════");

        String sql = "SELECT p.nume, p.stoc, p.pret, v.nume AS vanzator "
                + "FROM produse p "
                + "JOIN vanzatori v ON p.vanzator_id = v.id "
                + "WHERE p.tip = 'FIZIC' AND p.stoc < 5 AND p.activ = true "
                + "ORDER BY p.stoc ASC";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            boolean gasit = false;
            while (rs.next()) {
                gasit = true;
                rezultat.add(String.format("  %-25s | Stoc: %d | Pret: %.2f lei | Vanzator: %s",
                        rs.getString("nume"),
                        rs.getInt("stoc"),
                        rs.getDouble("pret"),
                        rs.getString("vanzator")));
            }
            if (!gasit) rezultat.add("  ✔ Toate produsele au stoc suficient!");

        } catch (SQLException e) {
            rezultat.add("  ✘ Eroare raport: " + e.getMessage());
        }

        rezultat.add("══════════════════════════════════════════");
        return rezultat;
    }

    // ════════════════════════════════════════
    // AFISEAZA toate rapoartele
    // ════════════════════════════════════════
    public void afiseazaToateRapoartele() {
        for (String linie : raportTopVanzatori())    System.out.println(linie);
        System.out.println();
        for (String linie : raportComenziPeStatus()) System.out.println(linie);
        System.out.println();
        for (String linie : raportStocScazut())      System.out.println(linie);
    }
}