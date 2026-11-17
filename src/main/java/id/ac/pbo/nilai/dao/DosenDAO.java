package id.ac.pbo.nilai.dao;

import id.ac.pbo.nilai.model.Dosen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DosenDAO extends BaseDAO<Dosen> {

    private static final String PILIH =
            "SELECT id, nama, username, password_hash FROM dosen";

    @Override
    public List<Dosen> findAll() {
        return ambilBanyak(PILIH + " ORDER BY nama", this::petakan);
    }

    @Override
    public Dosen findById(int id) {
        return ambilSatu(PILIH + " WHERE id = ?", this::petakan, id);
    }

    public Dosen findByUsername(String username) {
        return ambilSatu(PILIH + " WHERE username = ?", this::petakan, username);
    }

    @Override
    public void insert(Dosen dosen) {
        ubah("INSERT INTO dosen (nama, username, password_hash) VALUES (?, ?, ?)",
                dosen.getNama(), dosen.getUsername(), dosen.getPasswordHash());
    }

    @Override
    public void update(Dosen dosen) {
        int jumlah = ubah("UPDATE dosen SET nama = ?, username = ?, password_hash = ? WHERE id = ?",
                dosen.getNama(), dosen.getUsername(), dosen.getPasswordHash(), dosen.getId());
        pastikanTerpengaruh(jumlah, "Dosen dengan id " + dosen.getId() + " tidak ditemukan.");
    }

    @Override
    public void delete(int id) {
        pastikanTerpengaruh(ubah("DELETE FROM dosen WHERE id = ?", id),
                "Dosen dengan id " + id + " tidak ditemukan.");
    }

    private Dosen petakan(ResultSet resultSet) throws SQLException {
        return new Dosen(
                resultSet.getInt("id"),
                resultSet.getString("nama"),
                resultSet.getString("username"),
                resultSet.getString("password_hash"));
    }
}
