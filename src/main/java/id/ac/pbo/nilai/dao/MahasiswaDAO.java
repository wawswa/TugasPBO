package id.ac.pbo.nilai.dao;

import id.ac.pbo.nilai.model.Mahasiswa;

import java.util.List;

public class MahasiswaDAO extends BaseDAO<Mahasiswa> {

    @Override
    public List<Mahasiswa> findAll() {
        throw unsupported();
    }

    @Override
    public Mahasiswa findById(int id) {
        throw unsupported();
    }

    public Mahasiswa findByNim(String nim) {
        throw unsupported();
    }

    public List<Mahasiswa> cari(String kataKunci) {
        throw unsupported();
    }

    public boolean nimSudahDipakai(String nim, int kecualiId) {
        throw unsupported();
    }

    @Override
    public void insert(Mahasiswa mahasiswa) {
        throw unsupported();
    }

    @Override
    public void update(Mahasiswa mahasiswa) {
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
