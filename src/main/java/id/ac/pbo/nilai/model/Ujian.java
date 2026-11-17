package id.ac.pbo.nilai.model;

import java.util.List;

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
    public String aturan() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double hitungNilai(List<Double> nilaiMentah) {
        throw new UnsupportedOperationException();
    }
}
