package id.ac.pbo.nilai.model;

import id.ac.pbo.nilai.exception.ValidasiException;

import java.util.List;
import java.util.Locale;

/**
 * Abstract class komponen penilaian. Method {@link #hitungNilai(List)} di-override
 * oleh setiap subclasses dan dipanggil secara polimorfik oleh NilaiService.
 */
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
        setMataKuliahId(mataKuliahId);
        setKode(kode);
        setNama(nama);
        setBobot(bobot);
    }

    /**
     * Membuat objek komponen sesuai jenisnya.
     */
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

    /**
     * Penjelasan aturan perhitungan, ditampilkan di GUI.
     */
    public abstract String aturan();

    /**
     * Menghitung nilai komponen dari nilai mentah yang diinput.
     * Setiap subclasses memiliki aturan berbeda.
     */
    public abstract double hitungNilai(List<Double> nilaiMentah);

    /**
     * Apakah komponen boleh memiliki lebih dari satu nilai per mahasiswa.
     */
    public boolean izinkanBanyakNilai() {
        return true;
    }

    /**
     * Kontribusi komponen terhadap nilai akhir: bobot x nilai komponen.
     */
    public final double kontribusi(List<Double> nilaiMentah) {
        return bobot / 100.0 * hitungNilai(nilaiMentah);
    }

    protected static void pastikanRentangNilai(double nilai) {
        if (nilai < 0 || nilai > 100) {
            throw new IllegalArgumentException("Nilai harus berada pada rentang 0-100.");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Id komponen tidak boleh negatif.");
        }
        this.id = id;
    }

    public int getMataKuliahId() {
        return mataKuliahId;
    }

    public void setMataKuliahId(int mataKuliahId) {
        if (mataKuliahId <= 0) {
            throw new IllegalArgumentException("Mata kuliah komponen wajib dipilih.");
        }
        this.mataKuliahId = mataKuliahId;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        if (kode == null || kode.isBlank()) {
            throw new IllegalArgumentException("Kode komponen wajib diisi.");
        }
        String bersih = kode.trim().toUpperCase(Locale.ROOT);
        if (!bersih.matches("[A-Z0-9]{2,10}")) {
            throw new IllegalArgumentException("Kode komponen harus 2-10 karakter huruf/angka.");
        }
        this.kode = bersih;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama komponen wajib diisi.");
        }
        String bersih = nama.trim();
        if (bersih.length() < 2 || bersih.length() > 100) {
            throw new IllegalArgumentException("Nama komponen harus 2-100 karakter.");
        }
        this.nama = bersih;
    }

    public double getBobot() {
        return bobot;
    }

    public void setBobot(double bobot) {
        if (bobot < 0 || bobot > 100) {
            throw new IllegalArgumentException("Bobot komponen harus berada pada rentang 0-100.");
        }
        this.bobot = bobot;
    }

    @Override
    public void validasi() throws ValidasiException {
        try {
            setMataKuliahId(mataKuliahId);
            setKode(kode);
            setNama(nama);
            setBobot(bobot);
        } catch (IllegalArgumentException e) {
            throw new ValidasiException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return kode + " - " + nama + " (" + jenis() + ", bobot "
                + String.format(Locale.US, "%.1f", bobot) + "%)";
    }
}
