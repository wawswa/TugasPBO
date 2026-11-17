package id.ac.pbo.nilai.dao;

import id.ac.pbo.nilai.model.MataKuliah;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MataKuliahDAO extends BaseDAO<MataKuliah> {

    private static final String PILIH =
            "SELECT m.id, m.kode, m.nama, m.sks, m.dosen_id, d.nama AS dosen_nama "
                    + "FROM mata_kuliah m "
                    + "JOIN dosen d ON m.dosen_id = d.id";

    @Override
    public List<MataKuliah> findAll() {
        return ambilBanyak(PILIH + " ORDER BY m.kode", this::petakan);
    }

    @Override
    public MataKuliah findById(int id) {
        return ambilSatu(PILIH + " WHERE m.id = ?", this::petakan, id);
    }

    public List<MataKuliah> cari(String kataKunci) {
        if (kataKunci == null || kataKunci.isBlank()) {
            return findAll();
        }
        String pola = "%" + kataKunci.trim() + "%";
        return ambilBanyak(PILIH + " WHERE m.kode LIKE ? OR m.nama LIKE ? ORDER BY m.kode",
                this::petakan, pola, pola);
    }

    public boolean kodeSudahDipakai(String kode, int kecualiId) {
        if (kode == null || kode.isBlank()) {
            return false;
        }
        Boolean ada = ambilSatu(
                "SELECT 1 FROM mata_kuliah WHERE UPPER(kode) = ? AND id != ?",
                rs -> true,
                kode.trim().toUpperCase(), kecualiId);
        return Boolean.TRUE.equals(ada);
    }

    @Override
    public void insert(MataKuliah mataKuliah) {
        ubah("INSERT INTO mata_kuliah (kode, nama, sks, dosen_id) VALUES (?, ?, ?, ?)",
                mataKuliah.getKode(),
                mataKuliah.getNama(),
                mataKuliah.getSks(),
                mataKuliah.getDosenId());
    }

    @Override
    public void update(MataKuliah mataKuliah) {
        int jumlah = ubah("UPDATE mata_kuliah SET kode = ?, nama = ?, sks = ?, dosen_id = ? WHERE id = ?",
                mataKuliah.getKode(),
                mataKuliah.getNama(),
                mataKuliah.getSks(),
                mataKuliah.getDosenId(),
                mataKuliah.getId());
        pastikanTerpengaruh(jumlah, "Mata kuliah dengan id " + mataKuliah.getId() + " tidak ditemukan.");
    }

    @Override
    public void delete(int id) {
        pastikanTerpengaruh(ubah("DELETE FROM mata_kuliah WHERE id = ?", id),
                "Mata kuliah dengan id " + id + " tidak ditemukan.");
    }

    private MataKuliah petakan(ResultSet resultSet) throws SQLException {
        return new MataKuliah(
                resultSet.getInt("id"),
                resultSet.getString("kode"),
                resultSet.getString("nama"),
                resultSet.getInt("sks"),
                resultSet.getInt("dosen_id"),
                resultSet.getString("dosen_nama")
        );
    }
}
