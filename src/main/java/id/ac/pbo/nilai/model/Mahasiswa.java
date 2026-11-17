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
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public int getAngkatan() {
        return angkatan;
    }

    public void setAngkatan(int angkatan) {
        this.angkatan = angkatan;
    }

    @Override
    public void validasi() throws ValidasiException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        if (nim == null || nim.isBlank()) {
            return nama == null ? "" : nama;
        }
        return nim + " - " + nama;
    }
}
