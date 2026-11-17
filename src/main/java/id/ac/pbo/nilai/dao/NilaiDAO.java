package id.ac.pbo.nilai.dao;

import id.ac.pbo.nilai.model.Nilai;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class NilaiDAO extends BaseDAO<Nilai> {

    private static final String PILIH =
            "SELECT n.id, n.mahasiswa_id, n.komponen_id, n.nilai, n.tanggal_input, "
                    + "m.nim, m.nama AS mahasiswa_nama "
                    + "FROM nilai n JOIN mahasiswa m ON m.id = n.mahasiswa_id";

    @Override
    public List<Nilai> findAll() {
        return ambilBanyak(PILIH + " ORDER BY n.id", this::petakan);
    }

    @Override
    public Nilai findById(int id) {
        return ambilSatu(PILIH + " WHERE n.id = ?", this::petakan, id);
    }

    public List<Nilai> findByKomponen(int komponenId) {
        return ambilBanyak(PILIH + " WHERE n.komponen_id = ? ORDER BY m.nim",
                this::petakan, komponenId);
    }

    public List<Nilai> findByMahasiswa(int mahasiswaId) {
        return ambilBanyak(PILIH + " WHERE n.mahasiswa_id = ? ORDER BY n.id",
                this::petakan, mahasiswaId);
    }

    public int jumlahNilai(int mahasiswaId, int komponenId) {
        Integer jumlah = ambilSatu(
                "SELECT COUNT(*) FROM nilai WHERE mahasiswa_id = ? AND komponen_id = ?",
                resultSet -> resultSet.getInt(1), mahasiswaId, komponenId);
        return jumlah == null ? 0 : jumlah;
    }

    /**
     * Mengambil seluruh nilai mentah mahasiswa pada satu mata kuliah,
     * dikelompokkan per id komponen. Penggunaan Map generik.
     */
    public Map<Integer, List<Double>> nilaiMentah(int mahasiswaId, int mataKuliahId) {
        Map<Integer, List<Double>> peta = new LinkedHashMap<>();
        String sql = "SELECT n.komponen_id, n.nilai FROM nilai n "
                + "JOIN komponen k ON k.id = n.komponen_id "
                + "WHERE n.mahasiswa_id = ? AND k.mata_kuliah_id = ? ORDER BY n.id";
        bacaBaris(sql, resultSet -> {
            int komponenId = resultSet.getInt("komponen_id");
            peta.computeIfAbsent(komponenId, kunci -> new ArrayList<>())
                    .add(resultSet.getDouble("nilai"));
        }, mahasiswaId, mataKuliahId);
        return peta;
    }

    @Override
    public void insert(Nilai nilai) {
        ubah("INSERT INTO nilai (mahasiswa_id, komponen_id, nilai, tanggal_input) "
                        + "VALUES (?, ?, ?, ?)",
                nilai.getMahasiswaId(), nilai.getKomponenId(), nilai.getNilai(),
                nilai.getTanggalInput());
    }

    @Override
    public void update(Nilai nilai) {
        int jumlah = ubah("UPDATE nilai SET mahasiswa_id = ?, komponen_id = ?, nilai = ?, "
                        + "tanggal_input = ? WHERE id = ?",
                nilai.getMahasiswaId(), nilai.getKomponenId(), nilai.getNilai(),
                nilai.getTanggalInput(), nilai.getId());
        pastikanTerpengaruh(jumlah, "Nilai dengan id " + nilai.getId() + " tidak ditemukan.");
    }

    @Override
    public void delete(int id) {
        pastikanTerpengaruh(ubah("DELETE FROM nilai WHERE id = ?", id),
                "Nilai dengan id " + id + " tidak ditemukan.");
    }

    private Nilai petakan(ResultSet resultSet) throws SQLException {
        return new Nilai(
                resultSet.getInt("id"),
                resultSet.getInt("mahasiswa_id"),
                resultSet.getInt("komponen_id"),
                resultSet.getDouble("nilai"),
                LocalDate.parse(resultSet.getString("tanggal_input")),
                resultSet.getString("nim"),
                resultSet.getString("mahasiswa_nama"));
    }
}
