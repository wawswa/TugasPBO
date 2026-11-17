package id.ac.pbo.nilai.dao;

import id.ac.pbo.nilai.model.Nilai;

import java.util.List;
import java.util.Map;

public class NilaiDAO extends BaseDAO<Nilai> {

    @Override
    public List<Nilai> findAll() {
        throw unsupported();
    }

    @Override
    public Nilai findById(int id) {
        throw unsupported();
    }

    public List<Nilai> findByKomponen(int komponenId) {
        throw unsupported();
    }

    public List<Nilai> findByMahasiswa(int mahasiswaId) {
        throw unsupported();
    }

    public int jumlahNilai(int mahasiswaId, int komponenId) {
        throw unsupported();
    }

    public Map<Integer, List<Double>> nilaiMentah(int mahasiswaId, int mataKuliahId) {
        throw unsupported();
    }

    @Override
    public void insert(Nilai nilai) {
        throw unsupported();
    }

    @Override
    public void update(Nilai nilai) {
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
