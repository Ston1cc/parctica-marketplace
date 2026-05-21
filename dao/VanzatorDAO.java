package dao;

import model.Vanzator;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VanzatorDAO {

    private Connection conn;

    public VanzatorDAO() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    // ════════════════════════════════════════
    // CREATE — adauga un vanzator nou
    // ════════════════════════════════════════
    public boolean adauga(Vanzator v) {
        String sql = "INSERT INTO vanzatori (nume, email, telefon, adresa) "
                + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, v.getNume());
            ps.setString(2, v.getEmail());
            ps.setString(3, v.getTelefon());
            ps.setString(4, v.getAdresa());

            int rowsAffected = ps.executeUpdate();

            // Luam ID-ul generat automat de MySQL
            if (rowsAffected > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    v.setId(keys.getInt(1));
                }
                System.out.println("✔ Vanzator adaugat cu ID: " + v.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✘ Eroare la adaugare vanzator: " + e.getMessage());
        }
        return false;
    }

    // ════════════════════════════════════════
    // READ — returneaza toti vanzatorii
    // ════════════════════════════════════════
    public List<Vanzator> getToti() {
        List<Vanzator> lista = new ArrayList<>();
        String sql = "SELECT * FROM vanzatori WHERE activ = true";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Vanzator v = new Vanzator(
                        rs.getInt("id"),
                        rs.getString("nume"),
                        rs.getString("email"),
                        rs.getString("telefon")
                );
                v.setAdresa(rs.getString("adresa"));
                lista.add(v);
            }

        } catch (SQLException e) {
            System.err.println("✘ Eroare la citire vanzatori: " + e.getMessage());
        }
        return lista;
    }

    // ════════════════════════════════════════
    // READ — cauta dupa ID
    // ════════════════════════════════════════
    public Vanzator gasesteDupaId(int id) {
        String sql = "SELECT * FROM vanzatori WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Vanzator v = new Vanzator(
                        rs.getInt("id"),
                        rs.getString("nume"),
                        rs.getString("email"),
                        rs.getString("telefon")
                );
                v.setAdresa(rs.getString("adresa"));
                return v;
            }

        } catch (SQLException e) {
            System.err.println("✘ Eroare la cautare vanzator: " + e.getMessage());
        }
        return null;
    }

    // ════════════════════════════════════════
    // READ — cauta dupa nume
    // ════════════════════════════════════════
    public List<Vanzator> cautaDupaNume(String nume) {
        List<Vanzator> lista = new ArrayList<>();
        String sql = "SELECT * FROM vanzatori WHERE nume LIKE ? AND activ = true";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nume + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vanzator v = new Vanzator(
                        rs.getInt("id"),
                        rs.getString("nume"),
                        rs.getString("email"),
                        rs.getString("telefon")
                );
                v.setAdresa(rs.getString("adresa"));
                lista.add(v);
            }

        } catch (SQLException e) {
            System.err.println("✘ Eroare la cautare: " + e.getMessage());
        }
        return lista;
    }

    // ════════════════════════════════════════
    // UPDATE — modifica un vanzator existent
    // ════════════════════════════════════════
    public boolean actualizeaza(Vanzator v) {
        String sql = "UPDATE vanzatori SET nume=?, email=?, telefon=?, adresa=? "
                + "WHERE id=?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getNume());
            ps.setString(2, v.getEmail());
            ps.setString(3, v.getTelefon());
            ps.setString(4, v.getAdresa());
            ps.setInt(5, v.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("✔ Vanzator actualizat: " + v.getNume());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✘ Eroare la actualizare vanzator: " + e.getMessage());
        }
        return false;
    }

    // ════════════════════════════════════════
    // DELETE — sterge un vanzator (soft delete)
    // ════════════════════════════════════════
    public boolean sterge(int id) {
        // Soft delete — nu stergem din BD, doar marcam ca inactiv
        String sql = "UPDATE vanzatori SET activ = false WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✔ Vanzator cu ID " + id + " dezactivat.");
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✘ Eroare la stergere vanzator: " + e.getMessage());
        }
        return false;
    }
}
