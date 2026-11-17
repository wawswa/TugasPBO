package id.ac.pbo.nilai.model;

import id.ac.pbo.nilai.exception.ValidasiException;

public class MataKuliah implements Validatable {

    private int id;
    private String kode;
    private String nama;
    private int sks;
    private int dosenId;
    private String dosenNama;

    public MataKuliah() {
    }

    public MataKuliah(int id, String kode, String nama, int sks, int dosenId, String dosenNama) {
        this.id = id;
        this.kode = kode;
        this.nama = nama;
        this.sks = sks;
        this.dosenId = dosenId;
        this.dosenNama = dosenNama;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        if (kode == null || !kode.trim().matches("^[A-Za-z]{2,4}[0-9]{3,4}$")) {
            throw new IllegalArgumentException("Kode mata kuliah harus terdiri dari 2-4 huruf diikuti 3-4 angka (contoh: IF101).");
        }
        this.kode = kode.trim().toUpperCase();
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.trim().length() < 2 || nama.trim().length() > 100) {
            throw new IllegalArgumentException("Nama mata kuliah harus terdiri dari 2-100 karakter.");
        }
        this.nama = nama.trim();
    }

    public int getSks() {
        return sks;
    }

    public void setSks(int sks) {
        if (sks < 1 || sks > 6) {
            throw new IllegalArgumentException("SKS harus bernilai antara 1 dan 6.");
        }
        this.sks = sks;
    }

    public int getDosenId() {
        return dosenId;
    }

    public void setDosenId(int dosenId) {
        if (dosenId <= 0) {
            throw new IllegalArgumentException("Dosen pengampu wajib dipilih.");
        }
        this.dosenId = dosenId;
    }

    public String getDosenNama() {
        return dosenNama;
    }

    public void setDosenNama(String dosenNama) {
        this.dosenNama = dosenNama;
    }

    @Override
    public void validasi() throws ValidasiException {
        try {
            setKode(this.kode);
            setNama(this.nama);
            setSks(this.sks);
            setDosenId(this.dosenId);
        } catch (IllegalArgumentException e) {
            throw new ValidasiException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        if (kode == null || kode.isBlank()) {
            return nama == null ? "" : nama;
        }
        return kode + " - " + nama;
    }
}
