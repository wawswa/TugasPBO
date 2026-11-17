package id.ac.pbo.nilai.model;

import java.util.List;

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
        throw new UnsupportedOperationException();
    }

    @Override
    public double hitungNilai(List<Double> nilaiMentah) {
        throw new UnsupportedOperationException();
    }
}
