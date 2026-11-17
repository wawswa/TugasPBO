package id.ac.pbo.nilai.model;

import id.ac.pbo.nilai.exception.ValidasiException;

import java.time.LocalDate;

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
        this.mahasiswaId = mahasiswaId;
        this.komponenId = komponenId;
        this.nilai = nilai;
        this.tanggalInput = tanggalInput;
        this.nim = nim;
        this.mahasiswaNama = mahasiswaNama;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMahasiswaId() {
        return mahasiswaId;
    }

    public void setMahasiswaId(int mahasiswaId) {
        this.mahasiswaId = mahasiswaId;
    }

    public int getKomponenId() {
        return komponenId;
    }

    public void setKomponenId(int komponenId) {
        this.komponenId = komponenId;
    }

    public double getNilai() {
        return nilai;
    }

    public void setNilai(double nilai) {
        this.nilai = nilai;
    }

    public LocalDate getTanggalInput() {
        return tanggalInput;
    }

    public void setTanggalInput(LocalDate tanggalInput) {
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
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return (mahasiswaNama == null ? "" : mahasiswaNama) + " : " + nilai;
    }
}
