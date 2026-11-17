package id.ac.pbo.nilai.model;

import java.util.List;

/**
 * Komponen tugas: nilai akhir komponen adalah rata-rata seluruh nilai tugas.
 */
public class Tugas extends KomponenPenilaian {

    public Tugas() {
        super();
    }

    public Tugas(int id, int mataKuliahId, String kode, String nama, double bobot) {
        super(id, mataKuliahId, kode, nama, bobot);
    }

    @Override
    public String jenis() {
        return "TUGAS";
    }

    @Override
    public String aturan() {
        return "Rata-rata seluruh nilai tugas";
    }

    @Override
    public double hitungNilai(List<Double> nilaiMentah) {
        if (nilaiMentah == null || nilaiMentah.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (Double nilai : nilaiMentah) {
            if (nilai == null) {
                throw new IllegalArgumentException("Nilai tugas tidak boleh null.");
            }
            pastikanRentangNilai(nilai);
            total += nilai;
        }
        return total / nilaiMentah.size();
    }
}
