package id.ac.pbo.nilai.controller;

import id.ac.pbo.nilai.dao.MataKuliahDAO;
import id.ac.pbo.nilai.exception.DataAccessException;
import id.ac.pbo.nilai.exception.ValidasiException;
import id.ac.pbo.nilai.model.MataKuliah;
import id.ac.pbo.nilai.model.RekapNilai;
import id.ac.pbo.nilai.service.NilaiService;
import id.ac.pbo.nilai.view.RekapPanel;
import id.ac.pbo.nilai.view.UIUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Controller halaman rekap nilai akhir.
 */
public class RekapController implements Penyegar {

    private final RekapPanel view;
    private final NilaiService nilaiService;
    private final MataKuliahDAO mataKuliahDAO = new MataKuliahDAO();
    private List<RekapNilai> terakhir = new ArrayList<>();

    public RekapController(RekapPanel view, NilaiService nilaiService) {
        this.view = view;
        this.nilaiService = nilaiService;
        view.getBtnHitung().addActionListener(e -> hitung());
        view.getBtnCari().addActionListener(e -> terapkanFilter());
        view.getTabel().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                RekapNilai rekap = view.getTerpilih();
                if (rekap != null) {
                    view.setDetail(rekap.cetak(true));
                }
            }
        });
    }

    @Override
    public void muatUlang() {
        try {
            view.setMataKuliahList(mataKuliahDAO.findAll());
        } catch (DataAccessException e) {
            UIUtil.showError(view, "Gagal memuat daftar mata kuliah.\n" + e.getMessage());
        }
    }

    private void hitung() {
        MataKuliah mataKuliah = view.getMataKuliahTerpilih();
        if (mataKuliah == null) {
            UIUtil.showError(view, "Pilih mata kuliah terlebih dahulu.");
            return;
        }
        try {
            terakhir = nilaiService.hitungSemua(mataKuliah.getId());
            terapkanFilter();
            view.setDetail("");
        } catch (ValidasiException e) {
            UIUtil.showError(view, e.getMessage());
        } catch (DataAccessException e) {
            UIUtil.showError(view, "Gagal menghitung nilai akhir.\n" + e.getMessage());
        }
    }

    private void terapkanFilter() {
        String kunci = view.getKataKunci() == null ? "" : view.getKataKunci().trim().toLowerCase();
        List<RekapNilai> hasil = new ArrayList<>();
        for (RekapNilai rekap : terakhir) {
            if (kunci.isEmpty()
                    || rekap.getMahasiswa().getNim().toLowerCase().contains(kunci)
                    || rekap.getMahasiswa().getNama().toLowerCase().contains(kunci)) {
                hasil.add(rekap);
            }
        }
        view.setData(hasil);
        perbaruiRingkasan(hasil);
    }

    private void perbaruiRingkasan(List<RekapNilai> data) {
        int sudahDinilai = 0;
        double total = 0.0;
        for (RekapNilai rekap : data) {
            if (rekap.isAdaNilai()) {
                sudahDinilai++;
                total += rekap.getNilaiAkhir();
            }
        }
        double rataRata = sudahDinilai == 0 ? 0.0 : total / sudahDinilai;
        view.setRingkasan(String.format(Locale.US,
                "Menampilkan %d mahasiswa | %d sudah dinilai | rata-rata nilai akhir %.2f",
                data.size(), sudahDinilai, rataRata));
    }
}
