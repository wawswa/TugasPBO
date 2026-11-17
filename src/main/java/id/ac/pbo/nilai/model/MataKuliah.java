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
        this.kode = kode;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getSks() {
        return sks;
    }

    public void setSks(int sks) {
        this.sks = sks;
    }

    public int getDosenId() {
        return dosenId;
    }

    public void setDosenId(int dosenId) {
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
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        if (kode == null || kode.isBlank()) {
            return nama == null ? "" : nama;
        }
        return kode + " - " + nama;
    }
}
