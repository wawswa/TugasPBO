package id.ac.pbo.nilai.model;

import java.util.List;
import java.util.Locale;

/**
 * Hasil perhitungan nilai akhir seorang mahasiswa pada satu mata kuliah.
 */
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

    /**
     * Overloading: cetak ringkas atau cetak detail per komponen.
     */
    public String cetak(boolean detail) {
        StringBuilder hasil = new StringBuilder();
        hasil.append("REKAP NILAI AKADEMIK\n");
        hasil.append("Mahasiswa   : ").append(mahasiswa.getNim()).append(" - ")
                .append(mahasiswa.getNama()).append('\n');
        hasil.append("Mata Kuliah : ").append(mataKuliah.getKode()).append(" - ")
                .append(mataKuliah.getNama()).append(" (").append(mataKuliah.getSks()).append(" SKS)\n");
        hasil.append("--------------------------------------------------------------------\n");
        if (detail) {
            for (RincianNilai item : rincian) {
                KomponenPenilaian komponen = item.getKomponen();
                hasil.append(String.format(Locale.US,
                        "%-24s %-6s bobot %5.1f%%  nilai %6.2f  kontribusi %6.2f%n",
                        komponen.getNama(), komponen.jenis(), komponen.getBobot(),
                        item.getNilaiTerhitung(), item.getKontribusi()));
                hasil.append("    aturan      : ").append(komponen.aturan()).append('\n');
                hasil.append("    nilai mentah: ").append(formatDaftar(item.getNilaiMentah())).append('\n');
            }
            hasil.append("--------------------------------------------------------------------\n");
        }
        if (adaNilai) {
            hasil.append(String.format(Locale.US, "Nilai Akhir : %.2f  |  Huruf: %s%n",
                    nilaiAkhir, nilaiHuruf));
        } else {
            hasil.append("Nilai Akhir : - (belum ada nilai yang diinput)\n");
        }
        hasil.append("Total bobot komponen yang dihitung: ")
                .append(String.format(Locale.US, "%.1f%%", getTotalBobot()));
        return hasil.toString();
    }

    private static String formatDaftar(List<Double> nilai) {
        if (nilai.isEmpty()) {
            return "-";
        }
        StringBuilder teks = new StringBuilder("[");
        for (int i = 0; i < nilai.size(); i++) {
            if (i > 0) {
                teks.append(", ");
            }
            teks.append(String.format(Locale.US, "%.2f", nilai.get(i)));
        }
        return teks.append(']').toString();
    }
}
