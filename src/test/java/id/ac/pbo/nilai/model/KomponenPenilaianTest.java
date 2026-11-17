package id.ac.pbo.nilai.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KomponenPenilaianTest {

    @Test
    void tugasMenghitungRataRataSeluruhNilai() {
        KomponenPenilaian tugas = new Tugas(1, 1, "T01", "Tugas 1", 20);

        assertEquals(85.0, tugas.hitungNilai(List.of(80.0, 90.0)), 1e-9);
        assertEquals(17.0, tugas.kontribusi(List.of(80.0, 90.0)), 1e-9);
    }

    @Test
    void kuisMengambilNilaiTertinggi() {
        KomponenPenilaian kuis = new Kuis(2, 1, "K01", "Kuis 1", 20);

        assertEquals(95.0, kuis.hitungNilai(List.of(80.0, 95.0, 60.0)), 1e-9);
        assertEquals(19.0, kuis.kontribusi(List.of(80.0, 95.0, 60.0)), 1e-9);
    }

    @Test
    void ujianMenerapkanAmbangMinimumEmpatPuluh() {
        KomponenPenilaian ujian = new Ujian(3, 1, "U01", "Ujian Akhir", 40);

        assertEquals(0.0, ujian.hitungNilai(List.of(39.99)), 1e-9);
        assertEquals(40.0, ujian.hitungNilai(List.of(40.0)), 1e-9);
        assertEquals(16.0, ujian.kontribusi(List.of(40.0)), 1e-9);
    }

    @Test
    void ujianMenolakLebihDariSatuNilai() {
        KomponenPenilaian ujian = new Ujian(3, 1, "U01", "Ujian Akhir", 40);

        assertThrows(IllegalArgumentException.class,
                () -> ujian.hitungNilai(List.of(70.0, 80.0)));
    }

    @Test
    void komponenTanpaNilaiMenghasilkanNol() {
        assertEquals(0.0, new Tugas(1, 1, "T01", "Tugas 1", 20).hitungNilai(List.of()), 1e-9);
        assertEquals(0.0, new Ujian(3, 1, "U01", "Ujian Akhir", 40).hitungNilai(List.of()), 1e-9);
    }
}
