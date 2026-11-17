package id.ac.pbo.nilai.model;

import java.util.List;

/**
 * Komponen kuis: nilai akhir komponen adalah nilai kuis tertinggi (best attempt).
 */
public class Kuis extends KomponenPenilaian {

    public Kuis() {
        super();
    }

    public Kuis(int id, int mataKuliahId, String kode, String nama, double bobot) {
        super(id, mataKuliahId, kode, nama, bobot);
    }

    @Override
    public String jenis() {
        return "KUIS";
    }

    @Override
    public String aturan() {
        return "Nilai tertinggi dari seluruh percobaan kuis";
    }

    @Override
    public double hitungNilai(List<Double> nilaiMentah) {
        if (nilaiMentah == null || nilaiMentah.isEmpty()) {
            return 0.0;
        }
        double tertinggi = 0.0;
        for (Double nilai : nilaiMentah) {
            if (nilai == null) {
                throw new IllegalArgumentException("Nilai kuis tidak boleh null.");
            }
            pastikanRentangNilai(nilai);
            tertinggi = Math.max(tertinggi, nilai);
        }
        return tertinggi;
    }
}
