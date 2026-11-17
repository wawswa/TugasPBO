package id.ac.pbo.nilai.model;

import java.util.List;

/**
 * Rincian kontribusi satu komponen pada rekap nilai seorang mahasiswa.
 * Objek ini immutable.
 */
public class RincianNilai {

    private final KomponenPenilaian komponen;
    private final List<Double> nilaiMentah;
    private final double nilaiTerhitung;
    private final double kontribusi;

    public RincianNilai(KomponenPenilaian komponen, List<Double> nilaiMentah,
                        double nilaiTerhitung, double kontribusi) {
        this.komponen = komponen;
        this.nilaiMentah = List.copyOf(nilaiMentah);
        this.nilaiTerhitung = nilaiTerhitung;
        this.kontribusi = kontribusi;
    }

    public KomponenPenilaian getKomponen() {
        return komponen;
    }

    public List<Double> getNilaiMentah() {
        return nilaiMentah;
    }

    public double getNilaiTerhitung() {
        return nilaiTerhitung;
    }

    public double getKontribusi() {
        return kontribusi;
    }
}
