package id.ac.pbo.nilai.model;

import java.util.List;

public class RekapNilai {

    private final Mahasiswa mahasiswa;
    private final MataKuliah mataKuliah;
    private final List<RincianNilai> rincian;
    private final double nilaiAkhir;
    private final String nilaiHuruf;
    private final boolean adaNilai;

    public RekapNilai(Mahasiswa mahasiswa, MataKuliah mataKuliah, List<RincianNilai> rincian,
                      double nilaiAkhir, String nilaiHuruf, boolean adaNilai) {
        this.mahasiswa = mahasiswa;
        this.mataKuliah = mataKuliah;
        this.rincian = List.copyOf(rincian);
        this.nilaiAkhir = nilaiAkhir;
        this.nilaiHuruf = nilaiHuruf;
        this.adaNilai = adaNilai;
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa;
    }

    public MataKuliah getMataKuliah() {
        return mataKuliah;
    }

    public List<RincianNilai> getRincian() {
        return rincian;
    }

    public double getNilaiAkhir() {
        return nilaiAkhir;
    }

    public String getNilaiHuruf() {
        return nilaiHuruf;
    }

    public boolean isAdaNilai() {
        return adaNilai;
    }

    public double getTotalBobot() {
        double total = 0.0;
        for (RincianNilai item : rincian) {
            total += item.getKomponen().getBobot();
        }
        return total;
    }

    public String cetak() {
        return cetak(false);
    }

    public String cetak(boolean detail) {
        throw new UnsupportedOperationException();
    }
}
