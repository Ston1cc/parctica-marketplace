package util;

import dao.DatabaseConnection;
import model.Produs;
import model.Vanzator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExportService {

    private Connection conn;
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    public ExportService() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    // ════════════════════════════════════════
    // Export vanzatori CSV
    // ════════════════════════════════════════
    public void exportVanzatoriCSV(List<Vanzator> vanzatori, String cale) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(cale))) {
            pw.println("ID,Nume,Email,Telefon,Adresa");
            for (Vanzator v : vanzatori) {
                pw.println(v.toCsv());
            }
            System.out.println("✔ Export CSV vanzatori: " + cale);
        } catch (IOException e) {
            System.err.println("✘ Eroare export CSV: " + e.getMessage());
        }
    }

    // ════════════════════════════════════════
    // Export produse CSV
    // ════════════════════════════════════════
    public void exportProduseCSV(List<Produs> produse, String cale) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(cale))) {
            pw.println("ID,Nume,Pret,Stoc,VanzatorID,Tip,Extra");
            for (Produs p : produse) {
                pw.println(p.toCsv());
            }
            System.out.println("✔ Export CSV produse: " + cale);
        } catch (IOException e) {
            System.err.println("✘ Eroare export CSV: " + e.getMessage());
        }
    }

    // ════════════════════════════════════════
    // Export comenzi CSV din BD
    // ════════════════════════════════════════
    public void exportComenziCSV(String cale) {
        String sql = "SELECT c.id, c.data_comanda, c.status, c.cantitate, "
                + "c.total_pret, c.nume_cumparator, c.email_cumparator, "
                + "p.nume AS produs, v.nume AS vanzator "
                + "FROM comenzi c "
                + "JOIN produse p ON c.produs_id = p.id "
                + "JOIN vanzatori v ON c.vanzator_id = v.id";

        try (PrintWriter pw = new PrintWriter(new FileWriter(cale));
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            pw.println("ID,Data,Status,Cantitate,Total,Cumparator,Email,Produs,Vanzator");
            while (rs.next()) {
                pw.printf("%d,%s,%s,%d,%.2f,%s,%s,%s,%s%n",
                        rs.getInt("id"),
                        rs.getString("data_comanda"),
                        rs.getString("status"),
                        rs.getInt("cantitate"),
                        rs.getDouble("total_pret"),
                        rs.getString("nume_cumparator"),
                        rs.getString("email_cumparator"),
                        rs.getString("produs"),
                        rs.getString("vanzator"));
            }
            System.out.println("✔ Export CSV comenzi: " + cale);

        } catch (IOException | SQLException e) {
            System.err.println("✘ Eroare export comenzi CSV: " + e.getMessage());
        }
    }

    // ════════════════════════════════════════
    // Export raport TXT
    // ════════════════════════════════════════
    public void exportRaportTXT(List<String> linii, String cale) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(cale))) {
            pw.println("MARKETPLACE APP — Raport generat la: "
                    + LocalDateTime.now().format(FMT));
            pw.println();
            for (String linie : linii) {
                pw.println(linie);
            }
            System.out.println("✔ Export TXT raport: " + cale);
        } catch (IOException e) {
            System.err.println("✘ Eroare export TXT: " + e.getMessage());
        }
    }
}