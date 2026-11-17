package id.ac.pbo.nilai.dao;

import id.ac.pbo.nilai.model.Dosen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DosenDAO extends BaseDAO<Dosen> {

    private static final String PILIH =
            "SELECT id, nama, username, password_hash, role FROM dosen";

    @Override
    public List<Dosen> findAll() {
        return ambilBanyak(PILIH + " ORDER BY nama", this::petakan);
    }

    @Override
    public Dosen findById(int id) {
        return ambilSatu(PILIH + " WHERE id = ?", this::petakan, id);
    }

    public Dosen findByUsername(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }
        return ambilSatu(PILIH + " WHERE LOWER(username) = ?", this::petakan, username.trim().toLowerCase());
    }

    public Dosen findAdmin() {
        return ambilSatu(PILIH + " WHERE UPPER(role) = 'ADMIN' OR LOWER(username) = 'admin' LIMIT 1", this::petakan);
    }

    public int countAdmin() {
        Integer jumlah = ambilSatu(
                "SELECT COUNT(*) FROM dosen WHERE UPPER(role) = 'ADMIN' OR LOWER(username) = 'admin'",
                rs -> rs.getInt(1));
        return jumlah == null ? 0 : jumlah;
    }

    public boolean usernameSudahDipakai(String username) {
        return usernameSudahDipakai(username, 0);
    }

    public boolean usernameSudahDipakai(String username, int kecualiId) {
        if (username == null || username.isBlank()) {
            return false;
        }
        Boolean ada = ambilSatu(
                "SELECT 1 FROM dosen WHERE LOWER(username) = ? AND id != ?",
                rs -> true,
                username.trim().toLowerCase(), kecualiId);
        return Boolean.TRUE.equals(ada);
    }

    @Override
    public void insert(Dosen dosen) {
        if (dosen.isAdmin() && countAdmin() >= 1) {
            throw new IllegalArgumentException("Hanya boleh ada 1 akun admin dalam sistem.");
        }
        ubah("INSERT INTO dosen (nama, username, password_hash, role) VALUES (?, ?, ?, ?)",
                dosen.getNama(),
                dosen.getUsername(),
                dosen.getPasswordHash(),
                dosen.getRole() != null ? dosen.getRole() : Dosen.ROLE_DOSEN);
    }

    @Override
    public void update(Dosen dosen) {
        if (dosen.isAdmin()) {
            Dosen adminExisting = findAdmin();
            if (adminExisting != null && adminExisting.getId() != dosen.getId()) {
                throw new IllegalArgumentException("Hanya boleh ada 1 akun admin dalam sistem.");
            }
        }
        int jumlah = ubah("UPDATE dosen SET nama = ?, username = ?, password_hash = ?, role = ? WHERE id = ?",
                dosen.getNama(),
                dosen.getUsername(),
                dosen.getPasswordHash(),
                dosen.getRole() != null ? dosen.getRole() : Dosen.ROLE_DOSEN,
                dosen.getId());
        pastikanTerpengaruh(jumlah, "Dosen dengan id " + dosen.getId() + " tidak ditemukan.");
    }

    @Override
    public void delete(int id) {
        Dosen target = findById(id);
        if (target != null && target.isAdmin()) {
            throw new IllegalArgumentException("Akun admin tunggal tidak dapat dihapus.");
        }
        pastikanTerpengaruh(ubah("DELETE FROM dosen WHERE id = ?", id),
                "Dosen dengan id " + id + " tidak ditemukan.");
    }

    private Dosen petakan(ResultSet resultSet) throws SQLException {
        String role = Dosen.ROLE_DOSEN;
        try {
            role = resultSet.getString("role");
            if (role == null || role.isBlank()) {
                role = Dosen.ROLE_DOSEN;
            }
        } catch (SQLException ignored) {
            // Kolom role belum ada
        }
        return new Dosen(
                resultSet.getInt("id"),
                resultSet.getString("nama"),
                resultSet.getString("username"),
                resultSet.getString("password_hash"),
                role);
    }
}
