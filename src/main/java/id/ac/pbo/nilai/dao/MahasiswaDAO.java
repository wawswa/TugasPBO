package id.ac.pbo.nilai.dao;

import id.ac.pbo.nilai.model.Mahasiswa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MahasiswaDAO extends BaseDAO<Mahasiswa> {

    private static final String PILIH =
            "SELECT id, nim, nama, jenis_kelamin, tanggal_lahir, angkatan FROM mahasiswa";

    @Override
    public List<Mahasiswa> findAll() {
        return ambilBanyak(PILIH + " ORDER BY nim", this::petakan);
    }

    @Override
    public Mahasiswa findById(int id) {
        return ambilSatu(PILIH + " WHERE id = ?", this::petakan, id);
    }

    public Mahasiswa findByNim(String nim) {
        return ambilSatu(PILIH + " WHERE nim = ?", this::petakan, nim != null ? nim.trim() : "");
    }

    public List<Mahasiswa> cari(String kataKunci) {
        if (kataKunci == null || kataKunci.isBlank()) {
            return findAll();
        }
        String pola = "%" + kataKunci.trim() + "%";
        return ambilBanyak(PILIH + " WHERE nim LIKE ? OR nama LIKE ? ORDER BY nim", this::petakan, pola, pola);
    }

    public boolean nimSudahDipakai(String nim, int kecualiId) {
        if (nim == null || nim.isBlank()) {
            return false;
        }
        Boolean ada = ambilSatu(
                "SELECT 1 FROM mahasiswa WHERE nim = ? AND id != ?",
                rs -> true,
                nim.trim(), kecualiId);
        return Boolean.TRUE.equals(ada);
    }

    @Override
    public void insert(Mahasiswa mahasiswa) {
        ubah("INSERT INTO mahasiswa (nim, nama, jenis_kelamin, tanggal_lahir, angkatan) VALUES (?, ?, ?, ?, ?)",
                mahasiswa.getNim(),
                mahasiswa.getNama(),
                mahasiswa.getJenisKelamin(),
                mahasiswa.getTanggalLahir(),
                mahasiswa.getAngkatan());
    }

    @Override
    public void update(Mahasiswa mahasiswa) {
        int jumlah = ubah("UPDATE mahasiswa SET nim = ?, nama = ?, jenis_kelamin = ?, tanggal_lahir = ?, angkatan = ? WHERE id = ?",
                mahasiswa.getNim(),
                mahasiswa.getNama(),
                mahasiswa.getJenisKelamin(),
                mahasiswa.getTanggalLahir(),
                mahasiswa.getAngkatan(),
                mahasiswa.getId());
        pastikanTerpengaruh(jumlah, "Mahasiswa dengan id " + mahasiswa.getId() + " tidak ditemukan.");
    }

    @Override
    public void delete(int id) {
        pastikanTerpengaruh(ubah("DELETE FROM mahasiswa WHERE id = ?", id),
                "Mahasiswa dengan id " + id + " tidak ditemukan.");
    }

    private Mahasiswa petakan(ResultSet resultSet) throws SQLException {
        String tglStr = resultSet.getString("tanggal_lahir");
        LocalDate tgl = tglStr != null && !tglStr.isBlank() ? LocalDate.parse(tglStr) : null;
        return new Mahasiswa(
                resultSet.getInt("id"),
                resultSet.getString("nim"),
                resultSet.getString("nama"),
                resultSet.getString("jenis_kelamin"),
                tgl,
                resultSet.getInt("angkatan")
        );
    }
}
