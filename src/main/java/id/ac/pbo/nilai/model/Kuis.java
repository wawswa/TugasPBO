package id.ac.pbo.nilai.model;

import java.util.List;

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
        throw new UnsupportedOperationException();
    }

    @Override
    public double hitungNilai(List<Double> nilaiMentah) {
        throw new UnsupportedOperationException();
    }
}
