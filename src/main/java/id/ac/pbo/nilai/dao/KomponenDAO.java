package id.ac.pbo.nilai.dao;

import id.ac.pbo.nilai.model.KomponenPenilaian;

import java.util.List;

public class KomponenDAO extends BaseDAO<KomponenPenilaian> {

    @Override
    public List<KomponenPenilaian> findAll() {
        throw unsupported();
    }

    @Override
    public KomponenPenilaian findById(int id) {
        throw unsupported();
    }

    public List<KomponenPenilaian> findByMataKuliah(int mataKuliahId) {
        throw unsupported();
    }

    public boolean kodeSudahDipakai(int mataKuliahId, String kode, int kecualiId) {
        throw unsupported();
    }

    public double totalBobot(int mataKuliahId) {
        throw unsupported();
    }

    public double totalBobot(int mataKuliahId, int kecualiId) {
        throw unsupported();
    }

    @Override
    public void insert(KomponenPenilaian komponen) {
        throw unsupported();
    }

    @Override
    public void update(KomponenPenilaian komponen) {
        throw unsupported();
    }

    @Override
    public void delete(int id) {
        throw unsupported();
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException();
    }
}
