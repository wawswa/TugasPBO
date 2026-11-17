package id.ac.pbo.nilai.service;

import id.ac.pbo.nilai.dao.KomponenDAO;
import id.ac.pbo.nilai.dao.MahasiswaDAO;
import id.ac.pbo.nilai.dao.MataKuliahDAO;
import id.ac.pbo.nilai.dao.NilaiDAO;
import id.ac.pbo.nilai.exception.ValidasiException;
import id.ac.pbo.nilai.model.RekapNilai;

import java.util.List;

public class NilaiService {

    public NilaiService() {
    }

    public NilaiService(NilaiDAO nilaiDAO, KomponenDAO komponenDAO,
                        MahasiswaDAO mahasiswaDAO, MataKuliahDAO mataKuliahDAO) {
    }

    public RekapNilai hitungNilaiAkhir(int mahasiswaId, int mataKuliahId)
            throws ValidasiException {
        throw unsupported();
    }

    public RekapNilai hitungNilaiAkhir(int mahasiswaId, int mataKuliahId,
                                       boolean abaikanKomponenKosong)
            throws ValidasiException {
        throw unsupported();
    }

    public List<RekapNilai> hitungSemua(int mataKuliahId) throws ValidasiException {
        throw unsupported();
    }

    public static String konversiHuruf(double nilaiAkhir) {
        throw new UnsupportedOperationException();
    }

    private UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException();
    }
}
