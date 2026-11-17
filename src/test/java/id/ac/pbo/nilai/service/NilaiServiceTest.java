package id.ac.pbo.nilai.service;

import id.ac.pbo.nilai.dao.KomponenDAO;
import id.ac.pbo.nilai.dao.MahasiswaDAO;
import id.ac.pbo.nilai.dao.MataKuliahDAO;
import id.ac.pbo.nilai.dao.NilaiDAO;
import id.ac.pbo.nilai.model.KomponenPenilaian;
import id.ac.pbo.nilai.model.Kuis;
import id.ac.pbo.nilai.model.Mahasiswa;
import id.ac.pbo.nilai.model.MataKuliah;
import id.ac.pbo.nilai.model.RekapNilai;
import id.ac.pbo.nilai.model.Tugas;
import id.ac.pbo.nilai.model.Ujian;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NilaiServiceTest {

    @Test
    void menghitungNilaiAkhirDariAturanBerbedaPerKomponen() throws Exception {
        List<KomponenPenilaian> komponen = List.of(
                new Tugas(1, 1, "T01", "Tugas 1", 20),
                new Kuis(2, 1, "K01", "Kuis 1", 20),
                new Ujian(3, 1, "U01", "Ujian Akhir", 60));
        Map<Integer, List<Double>> nilai = Map.of(
                1, List.of(80.0),
                2, List.of(90.0, 100.0),
                3, List.of(50.0));

        RekapNilai rekap = layanan(komponen, nilai).hitungNilaiAkhir(1, 1);

        // Tugas 16 + Kuis 20 (nilai tertinggi) + Ujian 30 = 66 dari bobot 100
        assertEquals(66.0, rekap.getNilaiAkhir(), 1e-9);
        assertEquals("C", rekap.getNilaiHuruf());
        assertTrue(rekap.isAdaNilai());
        assertEquals(3, rekap.getRincian().size());
    }

    @Test
    void komponenKosongDapatDiabaikanDanBobotDinormalisasi() throws Exception {
        List<KomponenPenilaian> komponen = List.of(
                new Tugas(1, 1, "T01", "Tugas 1", 20),
                new Kuis(2, 1, "K01", "Kuis 1", 20),
                new Ujian(3, 1, "U01", "Ujian Akhir", 40));
        Map<Integer, List<Double>> nilai = Map.of(
                1, List.of(80.0),
                3, List.of(60.0));

        NilaiService service = layanan(komponen, nilai);
        // (16 + 0 + 24) dari total bobot 80 = 50
        assertEquals(50.0, service.hitungNilaiAkhir(1, 1).getNilaiAkhir(), 1e-9);
        RekapNilai diabaikan = service.hitungNilaiAkhir(1, 1, true);
        assertEquals(66.67, diabaikan.getNilaiAkhir(), 0.0001);
        assertEquals("C", diabaikan.getNilaiHuruf());
    }

    @Test
    void seluruhKomponenKosongDitandaiBelumDinilai() throws Exception {
        List<KomponenPenilaian> komponen = List.of(
                new Tugas(1, 1, "T01", "Tugas 1", 100));

        RekapNilai rekap = layanan(komponen, Map.of()).hitungNilaiAkhir(1, 1, true);

        assertFalse(rekap.isAdaNilai());
        assertEquals(0.0, rekap.getNilaiAkhir(), 1e-9);
        assertEquals(0.0, rekap.getTotalBobot(), 1e-9);
    }

    @Test
    void konversiHurufMengikutiBatasNilai() {
        assertEquals("A", NilaiService.konversiHuruf(85.0));
        assertEquals("AB", NilaiService.konversiHuruf(84.99));
        assertEquals("AB", NilaiService.konversiHuruf(80.0));
        assertEquals("B", NilaiService.konversiHuruf(75.0));
        assertEquals("BC", NilaiService.konversiHuruf(70.0));
        assertEquals("C", NilaiService.konversiHuruf(65.0));
        assertEquals("CD", NilaiService.konversiHuruf(60.0));
        assertEquals("D", NilaiService.konversiHuruf(50.0));
        assertEquals("E", NilaiService.konversiHuruf(49.99));
    }

    private static NilaiService layanan(List<KomponenPenilaian> komponen,
                                        Map<Integer, List<Double>> nilai) {
        return new NilaiService(
                new NilaiDAO() {
                    @Override
                    public Map<Integer, List<Double>> nilaiMentah(int mahasiswaId, int mataKuliahId) {
                        return nilai;
                    }
                },
                new KomponenDAO() {
                    @Override
                    public List<KomponenPenilaian> findByMataKuliah(int mataKuliahId) {
                        return komponen;
                    }
                },
                new MahasiswaDAO() {
                    @Override
                    public Mahasiswa findById(int id) {
                        return new Mahasiswa(1, "221001", "Andi Pratama", "L",
                                LocalDate.of(2003, 5, 14), 2022);
                    }
                },
                new MataKuliahDAO() {
                    @Override
                    public MataKuliah findById(int id) {
                        return new MataKuliah(1, "IF101", "Pemrograman Berorientasi Objek",
                                3, 1, "Dr. Budi Santoso");
                    }
                });
    }
}
