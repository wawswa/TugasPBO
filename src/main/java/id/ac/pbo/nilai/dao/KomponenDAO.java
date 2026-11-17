package id.ac.pbo.nilai.dao;

import id.ac.pbo.nilai.model.KomponenPenilaian;
import id.ac.pbo.nilai.model.Kuis;
import id.ac.pbo.nilai.model.Tugas;
import id.ac.pbo.nilai.model.Ujian;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class KomponenDAO extends BaseDAO<KomponenPenilaian> {

    private static final String PILIH =
            "SELECT id, mata_kuliah_id, kode, nama, jenis, bobot FROM komponen";

    @Override
    public List<KomponenPenilaian> findAll() {
        return ambilBanyak(PILIH + " ORDER BY mata_kuliah_id, id", this::petakan);
    }

    @Override
    public KomponenPenilaian findById(int id) {
        return ambilSatu(PILIH + " WHERE id = ?", this::petakan, id);
    }

    public List<KomponenPenilaian> findByMataKuliah(int mataKuliahId) {
        return ambilBanyak(PILIH + " WHERE mata_kuliah_id = ? ORDER BY id",
                this::petakan, mataKuliahId);
    }

    public boolean kodeSudahDipakai(int mataKuliahId, String kode, int kecualiId) {
        Integer jumlah = ambilSatu(
                "SELECT COUNT(*) FROM komponen WHERE mata_kuliah_id = ? AND kode = ? AND id <> ?",
                resultSet -> resultSet.getInt(1), mataKuliahId, kode, kecualiId);
        return jumlah != null && jumlah > 0;
    }

    public double totalBobot(int mataKuliahId) {
        return totalBobot(mataKuliahId, 0);
    }

    public double totalBobot(int mataKuliahId, int kecualiId) {
        Double total = ambilSatu(
                "SELECT COALESCE(SUM(bobot), 0) FROM komponen "
                        + "WHERE mata_kuliah_id = ? AND id <> ?",
                resultSet -> resultSet.getDouble(1), mataKuliahId, kecualiId);
        return total == null ? 0.0 : total;
    }

    @Override
    public void insert(KomponenPenilaian komponen) {
        ubah("INSERT INTO komponen (mata_kuliah_id, kode, nama, jenis, bobot) "
                        + "VALUES (?, ?, ?, ?, ?)",
                komponen.getMataKuliahId(), komponen.getKode(), komponen.getNama(),
                komponen.jenis(), komponen.getBobot());
    }

    @Override
    public void update(KomponenPenilaian komponen) {
        int jumlah = ubah("UPDATE komponen SET mata_kuliah_id = ?, kode = ?, nama = ?, "
                        + "jenis = ?, bobot = ? WHERE id = ?",
                komponen.getMataKuliahId(), komponen.getKode(), komponen.getNama(),
                komponen.jenis(), komponen.getBobot(), komponen.getId());
        pastikanTerpengaruh(jumlah,
                "Komponen dengan id " + komponen.getId() + " tidak ditemukan.");
    }

    @Override
    public void delete(int id) {
        pastikanTerpengaruh(ubah("DELETE FROM komponen WHERE id = ?", id),
                "Komponen dengan id " + id + " tidak ditemukan.");
    }

    private KomponenPenilaian petakan(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int mataKuliahId = resultSet.getInt("mata_kuliah_id");
        String kode = resultSet.getString("kode");
        String nama = resultSet.getString("nama");
        String jenis = resultSet.getString("jenis");
        double bobot = resultSet.getDouble("bobot");
        return switch (jenis) {
            case "TUGAS" -> new Tugas(id, mataKuliahId, kode, nama, bobot);
            case "KUIS" -> new Kuis(id, mataKuliahId, kode, nama, bobot);
            case "UJIAN" -> new Ujian(id, mataKuliahId, kode, nama, bobot);
            default -> throw new SQLException("Jenis komponen tidak dikenal: " + jenis);
        };
    }
}
