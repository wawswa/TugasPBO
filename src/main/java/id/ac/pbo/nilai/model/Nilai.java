package id.ac.pbo.nilai.model;

import id.ac.pbo.nilai.exception.ValidasiException;

import java.time.LocalDate;

/**
 * Satu baris nilai mentah yang diinput untuk seorang mahasiswa pada satu komponen.
 * Nama mahasiswa hanya untuk tampilan (hasil join).
 */
public class Nilai implements Validatable {

    private int id;
    private int mahasiswaId;
    private int komponenId;
    private double nilai;
    private LocalDate tanggalInput;
    private String nim;
    private String mahasiswaNama;

    public Nilai() {
    }

    public Nilai(int id, int mahasiswaId, int komponenId, double nilai,
                 LocalDate tanggalInput, String nim, String mahasiswaNama) {
        this.id = id;
        setMahasiswaId(mahasiswaId);
        setKomponenId(komponenId);
        setNilai(nilai);
        setTanggalInput(tanggalInput);
        this.nim = nim;
        this.mahasiswaNama = mahasiswaNama;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Id nilai tidak boleh negatif.");
        }
        this.id = id;
    }

    public int getMahasiswaId() {
        return mahasiswaId;
    }

    public void setMahasiswaId(int mahasiswaId) {
        if (mahasiswaId <= 0) {
            throw new IllegalArgumentException("Mahasiswa wajib dipilih.");
        }
        this.mahasiswaId = mahasiswaId;
    }

    public int getKomponenId() {
        return komponenId;
    }

    public void setKomponenId(int komponenId) {
        if (komponenId <= 0) {
            throw new IllegalArgumentException("Komponen penilaian wajib dipilih.");
        }
        this.komponenId = komponenId;
    }

    public double getNilai() {
        return nilai;
    }

    public void setNilai(double nilai) {
        if (nilai < 0 || nilai > 100) {
            throw new IllegalArgumentException("Nilai harus berada pada rentang 0-100.");
        }
        this.nilai = nilai;
    }

    public LocalDate getTanggalInput() {
        return tanggalInput;
    }

    public void setTanggalInput(LocalDate tanggalInput) {
        if (tanggalInput == null) {
            throw new IllegalArgumentException("Tanggal input wajib diisi.");
        }
        if (tanggalInput.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Tanggal input tidak boleh di masa depan.");
        }
        this.tanggalInput = tanggalInput;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getMahasiswaNama() {
        return mahasiswaNama;
    }

    public void setMahasiswaNama(String mahasiswaNama) {
        this.mahasiswaNama = mahasiswaNama;
    }

    @Override
    public void validasi() throws ValidasiException {
        try {
            setMahasiswaId(mahasiswaId);
            setKomponenId(komponenId);
            setNilai(nilai);
            setTanggalInput(tanggalInput);
        } catch (IllegalArgumentException e) {
            throw new ValidasiException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return (mahasiswaNama == null ? "" : mahasiswaNama) + " : " + nilai;
    }
}
