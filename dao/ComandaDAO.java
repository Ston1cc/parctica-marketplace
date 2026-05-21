package dao;

import model.Comanda;
import model.enums.StatusComanda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComandaDAO {

    private Connection conn;

    public ComandaDAO() {
        this.conn = DatabaseConnection.getInstance().getConnection();
    }

    // ════════════════════════════════════════
    // CREATE — plaseaza o comanda noua
    // ════════════════════════════════════════
    public boolean adauga(Comanda c) {
        String sql = "INSERT INTO comenzi (produs_id, vanzator_id, cantitate, "
                + "pret_unitar, total_pret, status, nume_cumparator, "
                + "email_cumparator, adresa_livrare) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, c.getProdusId());
            ps.setInt(2, c.getVanzatorId());
            ps.setInt(3, c.getCantitate());
            ps.setDouble(4, c.getPretUnitar());
            ps.setDouble(5, c.getTotalPret());
            ps.setString(6, c.getStatus().name());
            ps.setString(7, c.getNumeCumparator());
            ps.setString(8, c.getEmailCumparator());
            ps.setString(9, c.getAdresaLivrare());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) c.setId(keys.getInt(1));
                System.out.println("✔ Comanda plasata cu ID: " + c.getId());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✘ Eroare la plasare comanda: " + e.getMessage());
        }
        return false;
    }

    // ════════════════════════════════════════
    // READ — toate comenzile
    // ════════════════════════════════════════
    public List<Comanda> getTote() {
        List<Comanda> lista = new ArrayList<>();
        String sql = "SELECT * FROM comenzi ORDER BY data_comanda DESC";

        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) lista.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("✘ Eroare la citire comenzi: " + e.getMessage());
        }
        return lista;
    }

    // ════════════════════════════════════════
    // READ — cauta dupa ID
    // ════════════════════════════════════════
    public Comanda gasesteDupaId(int id) {
        String sql = "SELECT * FROM comenzi WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("✘ Eroare la cautare comanda: " + e.getMessage());
        }
        return null;
    }

    // ════════════════════════════════════════
    // READ — filtrare dupa status
    // ════════════════════════════════════════
    public List<Comanda> filtreazaDupaStatus(StatusComanda status) {
        List<Comanda> lista = new ArrayList<>();
        String sql = "SELECT * FROM comenzi WHERE status = ? ORDER BY data_comanda DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("✘ Eroare la filtrare comenzi: " + e.getMessage());
        }
        return lista;
    }

    // ════════════════════════════════════════
    // READ — comenzile unui vanzator
    // ════════════════════════════════════════
    public List<Comanda> getComenziVanzator(int vanzatorId) {
        List<Comanda> lista = new ArrayList<>();
        String sql = "SELECT * FROM comenzi WHERE vanzator_id = ? ORDER BY data_comanda DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vanzatorId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) lista.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("✘ Eroare la cautare comenzi vanzator: " + e.getMessage());
        }
        return lista;
    }

    // ════════════════════════════════════════
    // UPDATE — schimba statusul comenzii
    // ════════════════════════════════════════
    public boolean schimbaStatus(int id, StatusComanda statusNou) {
        String sql = "UPDATE comenzi SET status = ? WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statusNou.name());
            ps.setInt(2, id);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✔ Status comanda " + id
                        + " schimbat la: " + statusNou.getDescriere());
                return true;
            }

        } catch (SQLException e) {
            System.err.println("✘ Eroare la schimbare status: " + e.getMessage());
        }
        return false;
    }

    // ════════════════════════════════════════
    // DELETE — anuleaza o comanda
    // ════════════════════════════════════════
    public boolean anuleaza(int id) {
        return schimbaStatus(id, StatusComanda.ANULATA);
    }

    // ════════════════════════════════════════
    // HELPER — transforma un rand in obiect
    // ════════════════════════════════════════
    private Comanda mapRow(ResultSet rs) throws SQLException {
        Comanda c = new Comanda(
                rs.getInt("id"),
                rs.getInt("produs_id"),
                rs.getInt("vanzator_id"),
                rs.getInt("cantitate"),
                rs.getDouble("pret_unitar"),
                rs.getDouble("total_pret")
        );
        c.setStatus(StatusComanda.valueOf(rs.getString("status")));
        c.setNumeCumparator(rs.getString("nume_cumparator"));
        c.setEmailCumparator(rs.getString("email_cumparator"));
        c.setAdresaLivrare(rs.getString("adresa_livrare"));
        return c;
    }
}