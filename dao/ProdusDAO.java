package dao;

import model.Produs;
import model.ProdusFizic;
import model.ProduDigital;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdusDAO {

    private Connection conn;

    public ProdusDAO() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    // ════════════════════════════════════════
    // CREATE — adauga un produs nou
    // ════════════════════════════════════════
    public boolean adauga(Produs p) {
        String sql = "INSERT INTO produse (nume, descriere, pret, stoc, tip, "
                + "greutate_kg, dimensiuni, link_descarcare, format_fisier, vanzator_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNume());
            ps.setString(2, p.getDescriere());
            ps.setDouble(3, p.getPret());
            ps.setInt(4, p.getStoc());

            if (p instanceof ProdusFizic) {
                ProdusFizic pf = (ProdusFizic) p;
                ps.setString(5, "FIZIC");
                ps.setDouble(6, pf.getGreutateKg());
                ps.setString(7, pf.getDimensiuni());
                ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.VARCHAR);
            } else {
                ProduDigital pd = (ProduDigital) p;
                ps.setString(5, "DIGITAL");
                ps.setNull(6, Types.DECIMAL);
                ps.setNull(7, Types.VARCHAR);
                ps.setString(8, pd.getLinkDescarcare());
                ps.setString(9, pd.getFormat());
            }

            ps.setInt(10, p.getVanzatorId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) p.setId(keys.getInt(1));
                System.out.println("✔ Produs adaugat cu ID: " + p.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✘ Eroare la adaugare produs: " + e.getMessage());
        }
        return false;
    }

    // ════════════════════════════════════════
    // READ — toti produsele active
    // ════════════════════════════════════════
    public List<Produs> getToti() {
        List<Produs> lista = new ArrayList<>();
        String sql = "SELECT * FROM produse WHERE activ = true";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapRow(rs));
            }

        } catch (SQLException e) {
            System.err.println("✘ Eroare la citire produse: " + e.getMessage());
        }
        return lista;
    }

    // ════════════════════════════════════════
    // READ — cauta dupa ID
    // ════════════════════════════════════════
    public Produs gasesteDupaId(int id) {
        String sql = "SELECT * FROM produse WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("✘ Eroare la cautare produs: " + e.getMessage());
        }
        return null;
    }

    // ════════════════════════════════════════
    // READ — filtrare dupa pret maxim
    // ════════════════════════════════════════
    public List<Produs> filtreazaDupaPret(double pretMax) {
        List<Produs> lista = new ArrayList<>();
        String sql = "SELECT * FROM produse WHERE pret <= ? AND activ = true ORDER BY pret ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, pretMax);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("✘ Eroare la filtrare dupa pret: " + e.getMessage());
        }
        return lista;
    }

    // ════════════════════════════════════════
    // READ — filtrare dupa tip
    // ════════════════════════════════════════
    public List<Produs> filtreazaDupaTip(String tip) {
        List<Produs> lista = new ArrayList<>();
        String sql = "SELECT * FROM produse WHERE tip = ? AND activ = true";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tip);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("✘ Eroare la filtrare dupa tip: " + e.getMessage());
        }
        return lista;
    }

    // ════════════════════════════════════════
    // READ — cauta dupa nume
    // ════════════════════════════════════════
    public List<Produs> cautaDupaNume(String nume) {
        List<Produs> lista = new ArrayList<>();
        String sql = "SELECT * FROM produse WHERE nume LIKE ? AND activ = true";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nume + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("✘ Eroare la cautare produs: " + e.getMessage());
        }
        return lista;
    }

    // ════════════════════════════════════════
    // UPDATE — modifica un produs
    // ════════════════════════════════════════
    public boolean actualizeaza(Produs p) {
        String sql = "UPDATE produse SET nume=?, pret=?, stoc=?, descriere=? WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNume());
            ps.setDouble(2, p.getPret());
            ps.setInt(3, p.getStoc());
            ps.setString(4, p.getDescriere());
            ps.setInt(5, p.getId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✔ Produs actualizat: " + p.getNume());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✘ Eroare la actualizare produs: " + e.getMessage());
        }
        return false;
    }

    // ════════════════════════════════════════
    // DELETE — soft delete
    // ════════════════════════════════════════
    public boolean sterge(int id) {
        String sql = "UPDATE produse SET activ = false WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✔ Produs cu ID " + id + " dezactivat.");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✘ Eroare la stergere produs: " + e.getMessage());
        }
        return false;
    }

    // ════════════════════════════════════════
    // HELPER — transforma un rand din BD in obiect
    // ════════════════════════════════════════
    private Produs mapRow(ResultSet rs) throws SQLException {
        String tip = rs.getString("tip");

        if ("FIZIC".equals(tip)) {
            ProdusFizic p = new ProdusFizic(
                    rs.getInt("id"),
                    rs.getString("nume"),
                    rs.getDouble("pret"),
                    rs.getInt("stoc"),
                    rs.getInt("vanzator_id"),
                    rs.getDouble("greutate_kg"),
                    rs.getString("dimensiuni")
            );
            p.setDescriere(rs.getString("descriere"));
            return p;
        } else {
            ProduDigital p = new ProduDigital(
                    rs.getInt("id"),
                    rs.getString("nume"),
                    rs.getDouble("pret"),
                    rs.getInt("stoc"),
                    rs.getInt("vanzator_id"),
                    rs.getString("link_descarcare"),
                    rs.getString("format_fisier")
            );
            p.setDescriere(rs.getString("descriere"));
            return p;
        }
    }
}