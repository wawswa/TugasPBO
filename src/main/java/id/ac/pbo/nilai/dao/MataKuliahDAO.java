package id.ac.pbo.nilai.dao;

import id.ac.pbo.nilai.model.MataKuliah;

import java.util.List;

public class MataKuliahDAO extends BaseDAO<MataKuliah> {

    @Override
    public List<MataKuliah> findAll() {
        throw unsupported();
    }

    @Override
    public MataKuliah findById(int id) {
        throw unsupported();
    }

    public List<MataKuliah> cari(String kataKunci) {
        throw unsupported();
    }

    public boolean kodeSudahDipakai(String kode, int kecualiId) {
        throw unsupported();
    }

    @Override
    public void insert(MataKuliah mataKuliah) {
        throw unsupported();
    }

    @Override
    public void update(MataKuliah mataKuliah) {
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
