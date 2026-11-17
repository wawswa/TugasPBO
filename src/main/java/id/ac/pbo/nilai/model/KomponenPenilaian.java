package id.ac.pbo.nilai.model;

import id.ac.pbo.nilai.exception.ValidasiException;

import java.util.List;
import java.util.Locale;

public abstract class KomponenPenilaian implements Validatable {

    private int id;
    private int mataKuliahId;
    private String kode;
    private String nama;
    private double bobot;

    protected KomponenPenilaian() {
    }

    protected KomponenPenilaian(int id, int mataKuliahId, String kode, String nama, double bobot) {
        this.id = id;
        this.mataKuliahId = mataKuliahId;
        this.kode = kode;
        this.nama = nama;
        this.bobot = bobot;
    }

    public static KomponenPenilaian buat(String jenis) {
        if (jenis == null || jenis.isBlank()) {
            throw new IllegalArgumentException("Jenis komponen wajib dipilih.");
        }
        return switch (jenis.trim().toUpperCase(Locale.ROOT)) {
            case "TUGAS" -> new Tugas();
            case "KUIS" -> new Kuis();
            case "UJIAN" -> new Ujian();
            default -> throw new IllegalArgumentException("Jenis komponen tidak dikenal: " + jenis);
        };
    }

    public abstract String jenis();

    public abstract String aturan();

    public abstract double hitungNilai(List<Double> nilaiMentah);

    public boolean izinkanBanyakNilai() {
        return true;
    }

    public final double kontribusi(List<Double> nilaiMentah) {
        throw new UnsupportedOperationException();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMataKuliahId() {
        return mataKuliahId;
    }

    public void setMataKuliahId(int mataKuliahId) {
        this.mataKuliahId = mataKuliahId;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public double getBobot() {
        return bobot;
    }

    public void setBobot(double bobot) {
        this.bobot = bobot;
    }

    @Override
    public void validasi() throws ValidasiException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return kode + " - " + nama + " (" + jenis() + ", bobot " + bobot + "%)";
    }
}
