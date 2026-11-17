package id.ac.pbo.nilai.model;

import id.ac.pbo.nilai.exception.ValidasiException;

import java.time.LocalDate;

public class Mahasiswa implements Validatable {

    private int id;
    private String nim;
    private String nama;
    private String jenisKelamin;
    private LocalDate tanggalLahir;
    private int angkatan;

    public Mahasiswa() {
    }

    public Mahasiswa(int id, String nim, String nama, String jenisKelamin,
                     LocalDate tanggalLahir, int angkatan) {
        this.id = id;
        this.nim = nim;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.tanggalLahir = tanggalLahir;
        this.angkatan = angkatan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        if (nim == null || !nim.trim().matches("^\\d{5,15}$")) {
            throw new IllegalArgumentException("NIM harus berupa 5-15 digit angka.");
        }
        this.nim = nim.trim();
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().length() < 2 || nama.trim().length() > 100) {
            throw new IllegalArgumentException("Nama harus terdiri dari 2-100 karakter.");
        }
        this.nama = nama.trim();
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        if (jenisKelamin == null || (!jenisKelamin.trim().equalsIgnoreCase("L") && !jenisKelamin.trim().equalsIgnoreCase("P"))) {
            throw new IllegalArgumentException("Jenis kelamin harus 'L' atau 'P'.");
        }
        this.jenisKelamin = jenisKelamin.trim().toUpperCase();
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        if (tanggalLahir == null) {
            throw new IllegalArgumentException("Tanggal lahir tidak boleh kosong.");
        }
        if (tanggalLahir.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Tanggal lahir tidak boleh di masa depan.");
        }
        this.tanggalLahir = tanggalLahir;
    }

    public int getAngkatan() {
        return angkatan;
    }

    public void setAngkatan(int angkatan) {
        int tahunSekarang = LocalDate.now().getYear();
        if (angkatan < 2000 || angkatan > tahunSekarang) {
            throw new IllegalArgumentException("Angkatan harus antara 2000 dan " + tahunSekarang + ".");
        }
        this.angkatan = angkatan;
    }

    @Override
    public void validasi() throws ValidasiException {
        try {
            setNim(this.nim);
            setNama(this.nama);
            setJenisKelamin(this.jenisKelamin);
            setTanggalLahir(this.tanggalLahir);
            setAngkatan(this.angkatan);
        } catch (IllegalArgumentException e) {
            throw new ValidasiException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        if (nim == null || nim.isBlank()) {
            return nama == null ? "" : nama;
        }
        return nim + " - " + nama;
    }
}
