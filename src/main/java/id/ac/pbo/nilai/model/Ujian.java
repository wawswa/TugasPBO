package id.ac.pbo.nilai.model;

import java.util.List;

/**
 * Komponen ujian: tepat satu nilai per mahasiswa; nilai di bawah ambang minimum
 * dihitung nol.
 */
public class Ujian extends KomponenPenilaian {

    public static final double NILAI_MINIMUM = 40.0;

    public Ujian() {
        super();
    }

    public Ujian(int id, int mataKuliahId, String kode, String nama, double bobot) {
        super(id, mataKuliahId, kode, nama, bobot);
    }

    @Override
    public String jenis() {
        return "UJIAN";
    }

    @Override
    public boolean izinkanBanyakNilai() {
        return false;
    }

    @Override
    public String aturan() {
        return "Tepat satu nilai; nilai di bawah 40 dihitung 0";
    }

    @Override
    public double hitungNilai(List<Double> nilaiMentah) {
        if (nilaiMentah == null || nilaiMentah.isEmpty()) {
            return 0.0;
        }
        if (nilaiMentah.size() != 1) {
            throw new IllegalArgumentException(
                    "Ujian hanya boleh memiliki tepat satu nilai (ditemukan " + nilaiMentah.size() + ").");
        }
        Double nilai = nilaiMentah.get(0);
        if (nilai == null) {
            throw new IllegalArgumentException("Nilai ujian tidak boleh null.");
        }
        pastikanRentangNilai(nilai);
        return nilai < NILAI_MINIMUM ? 0.0 : nilai;
    }
}
