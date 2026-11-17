package id.ac.pbo.nilai.service;

import id.ac.pbo.nilai.dao.KomponenDAO;
import id.ac.pbo.nilai.dao.MahasiswaDAO;
import id.ac.pbo.nilai.dao.MataKuliahDAO;
import id.ac.pbo.nilai.dao.NilaiDAO;
import id.ac.pbo.nilai.exception.ValidasiException;
import id.ac.pbo.nilai.model.KomponenPenilaian;
import id.ac.pbo.nilai.model.Mahasiswa;
import id.ac.pbo.nilai.model.MataKuliah;
import id.ac.pbo.nilai.model.RekapNilai;
import id.ac.pbo.nilai.model.RincianNilai;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Logika bisnis perhitungan nilai akhir dan konversi nilai huruf.
 *
 * <p>Polimorfisme: setiap {@link KomponenPenilaian} pada daftar dipanggil melalui
 * referensi kelas induk, sehingga aturan Tugas/Kuis/Ujian otomatis berbeda.</p>
 */
public class NilaiService {

    private final NilaiDAO nilaiDAO;
    private final KomponenDAO komponenDAO;
    private final MahasiswaDAO mahasiswaDAO;
    private final MataKuliahDAO mataKuliahDAO;

    public NilaiService() {
        this(new NilaiDAO(), new KomponenDAO(), new MahasiswaDAO(), new MataKuliahDAO());
    }

    public NilaiService(NilaiDAO nilaiDAO, KomponenDAO komponenDAO,
                        MahasiswaDAO mahasiswaDAO, MataKuliahDAO mataKuliahDAO) {
        this.nilaiDAO = nilaiDAO;
        this.komponenDAO = komponenDAO;
        this.mahasiswaDAO = mahasiswaDAO;
        this.mataKuliahDAO = mataKuliahDAO;
    }

    /**
     * Overloading 1: semua komponen wajib dihitung (komponen tanpa nilai = 0).
     */
    public RekapNilai hitungNilaiAkhir(int mahasiswaId, int mataKuliahId) throws ValidasiException {
        return hitungNilaiAkhir(mahasiswaId, mataKuliahId, false);
    }

    /**
     * Overloading 2: komponen tanpa nilai dapat diabaikan; bobot dinormalisasi
     * terhadap komponen yang dihitung.
     */
    public RekapNilai hitungNilaiAkhir(int mahasiswaId, int mataKuliahId,
                                       boolean abaikanKomponenKosong) throws ValidasiException {
        Mahasiswa mahasiswa = mahasiswaDAO.findById(mahasiswaId);
        if (mahasiswa == null) {
            throw new ValidasiException("Mahasiswa tidak ditemukan.");
        }
        MataKuliah mataKuliah = mataKuliahDAO.findById(mataKuliahId);
        if (mataKuliah == null) {
            throw new ValidasiException("Mata kuliah tidak ditemukan.");
        }
        List<KomponenPenilaian> komponenList = komponenDAO.findByMataKuliah(mataKuliahId);
        if (komponenList.isEmpty()) {
            throw new ValidasiException("Mata kuliah " + mataKuliah.getKode()
                    + " belum memiliki komponen penilaian.");
        }

        Map<Integer, List<Double>> nilaiMentah =
                nilaiDAO.nilaiMentah(mahasiswaId, mataKuliahId);
        List<RincianNilai> rincian = new ArrayList<>();
        boolean adaNilai = false;
        double totalKontribusi = 0.0;
        double totalBobot = 0.0;

        for (KomponenPenilaian komponen : komponenList) {
            List<Double> mentah = nilaiMentah.getOrDefault(komponen.getId(), List.of());
            if (mentah.isEmpty() && abaikanKomponenKosong) {
                continue;
            }
            if (!mentah.isEmpty()) {
                adaNilai = true;
            }
            double terhitung;
            double kontribusi;
            try {
                terhitung = komponen.hitungNilai(mentah);
                kontribusi = komponen.kontribusi(mentah);
            } catch (IllegalArgumentException e) {
                throw new ValidasiException("Komponen " + komponen.getNama() + ": " + e.getMessage());
            }
            rincian.add(new RincianNilai(komponen, mentah, terhitung, kontribusi));
            totalKontribusi += kontribusi;
            totalBobot += komponen.getBobot();
        }

        double nilaiAkhir = totalBobot == 0.0 ? 0.0 : totalKontribusi * 100.0 / totalBobot;
        nilaiAkhir = Math.round(nilaiAkhir * 100.0) / 100.0;
        return new RekapNilai(mahasiswa, mataKuliah, rincian, nilaiAkhir,
                konversiHuruf(nilaiAkhir), adaNilai);
    }

    public List<RekapNilai> hitungSemua(int mataKuliahId) throws ValidasiException {
        if (mataKuliahDAO.findById(mataKuliahId) == null) {
            throw new ValidasiException("Mata kuliah tidak ditemukan.");
        }
        List<RekapNilai> hasil = new ArrayList<>();
        for (Mahasiswa mahasiswa : mahasiswaDAO.findAll()) {
            try {
                hasil.add(hitungNilaiAkhir(mahasiswa.getId(), mataKuliahId));
            } catch (ValidasiException e) {
                throw new ValidasiException("Mahasiswa " + mahasiswa.getNim() + ": "
                        + e.getMessage(), e);
            }
        }
        return hasil;
    }

    public static String konversiHuruf(double nilaiAkhir) {
        if (nilaiAkhir >= 85.0) {
            return "A";
        }
        if (nilaiAkhir >= 80.0) {
            return "AB";
        }
        if (nilaiAkhir >= 75.0) {
            return "B";
        }
        if (nilaiAkhir >= 70.0) {
            return "BC";
        }
        if (nilaiAkhir >= 65.0) {
            return "C";
        }
        if (nilaiAkhir >= 60.0) {
            return "CD";
        }
        if (nilaiAkhir >= 50.0) {
            return "D";
        }
        return "E";
    }
}
