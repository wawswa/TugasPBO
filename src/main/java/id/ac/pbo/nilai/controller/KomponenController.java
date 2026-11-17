package id.ac.pbo.nilai.controller;

import id.ac.pbo.nilai.dao.KomponenDAO;
import id.ac.pbo.nilai.dao.MataKuliahDAO;
import id.ac.pbo.nilai.model.KomponenPenilaian;
import id.ac.pbo.nilai.model.MataKuliah;
import id.ac.pbo.nilai.view.KomponenPanel;
import id.ac.pbo.nilai.view.UIUtil;

import java.util.Collections;
import java.util.List;

public class KomponenController implements Penyegar {

    private final KomponenPanel view;
    private final KomponenDAO komponenDao;
    private final MataKuliahDAO mataKuliahDao;
    private KomponenPenilaian komponenTerpilih;

    public KomponenController(KomponenPanel view) {
        this(view, new KomponenDAO(), new MataKuliahDAO());
    }

    public KomponenController(KomponenPanel view, KomponenDAO komponenDao, MataKuliahDAO mataKuliahDao) {
        this.view = view;
        this.komponenDao = komponenDao;
        this.mataKuliahDao = mataKuliahDao;
        inisialisasiListener();
    }

    private void inisialisasiListener() {
        view.getCmbMataKuliah().addActionListener(e -> {
            MataKuliah mk = view.getMataKuliahTerpilih();
            if (mk != null) {
                muatKomponen(mk);
            }
        });

        view.getBtnSimpan().addActionListener(e -> simpan());
        view.getBtnUbah().addActionListener(e -> ubah());
        view.getBtnHapus().addActionListener(e -> hapus());
        view.getBtnBatal().addActionListener(e -> batal());

        view.getTabel().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int baris = view.getTabel().getSelectedRow();
                if (baris != -1) {
                    int modelRow = view.getTabel().convertRowIndexToModel(baris);
                    komponenTerpilih = view.getTableModel().getBaris(modelRow);
                    view.isiForm(komponenTerpilih);
                }
            }
        });
    }

    private void muatKomponen(MataKuliah mk) {
        komponenTerpilih = null;
        view.kosongkanForm();
        if (mk == null) {
            view.getTableModel().setData(Collections.emptyList());
            view.setTotalBobot(0.0);
            return;
        }

        try {
            List<KomponenPenilaian> daftar = komponenDao.findByMataKuliah(mk.getId());
            view.getTableModel().setData(daftar);
            double total = komponenDao.totalBobot(mk.getId());
            view.setTotalBobot(total);
        } catch (UnsupportedOperationException e) {
            view.getTableModel().setData(Collections.emptyList());
            view.setTotalBobot(0.0);
        } catch (Exception e) {
            UIUtil.showError(view, "Gagal memuat komponen: " + e.getMessage());
        }
    }

    private void simpan() {
        MataKuliah mk = view.getMataKuliahTerpilih();
        if (mk == null) {
            UIUtil.showError(view, "Pilih mata kuliah terlebih dahulu.");
            return;
        }

        try {
            KomponenPenilaian k = view.buatDariForm();
            validasiInput(k);

            double totalSaatIni = komponenDao.totalBobot(mk.getId());
            double totalBaru = totalSaatIni + k.getBobot();
            if (totalBaru > 100.0001) {
                UIUtil.showError(view, String.format(
                        "Total bobot komponen tidak boleh melebihi 100%%.\nTotal saat ini: %.1f%%, jika ditambah %.1f%% menjadi %.1f%%.",
                        totalSaatIni, k.getBobot(), totalBaru));
                return;
            }

            if (komponenDao.kodeSudahDipakai(mk.getId(), k.getKode(), 0)) {
                UIUtil.showError(view, "Kode komponen '" + k.getKode() + "' sudah digunakan pada mata kuliah ini.");
                return;
            }

            komponenDao.insert(k);
            UIUtil.showInfo(view, "Komponen penilaian berhasil disimpan.");
            muatKomponen(mk);
        } catch (IllegalArgumentException e) {
            UIUtil.showError(view, e.getMessage());
        } catch (UnsupportedOperationException e) {
            UIUtil.showError(view, "Fitur penyimpanan komponen menunggu implementasi KomponenDAO (porsi Anggota 3).");
        } catch (Exception e) {
            UIUtil.showError(view, "Gagal menyimpan komponen: " + e.getMessage());
        }
    }

    private void ubah() {
        if (komponenTerpilih == null) {
            UIUtil.showError(view, "Pilih komponen yang ingin diubah terlebih dahulu.");
            return;
        }
        MataKuliah mk = view.getMataKuliahTerpilih();
        if (mk == null) {
            UIUtil.showError(view, "Pilih mata kuliah terlebih dahulu.");
            return;
        }

        try {
            KomponenPenilaian k = view.buatDariForm();
            k.setId(komponenTerpilih.getId());
            validasiInput(k);

            double totalKecuali = komponenDao.totalBobot(mk.getId(), k.getId());
            double totalBaru = totalKecuali + k.getBobot();
            if (totalBaru > 100.0001) {
                UIUtil.showError(view, String.format(
                        "Total bobot komponen tidak boleh melebihi 100%%.\nTotal tanpa komponen ini: %.1f%%, jika diubah menjadi %.1f%% total menjadi %.1f%%.",
                        totalKecuali, k.getBobot(), totalBaru));
                return;
            }

            if (komponenDao.kodeSudahDipakai(mk.getId(), k.getKode(), k.getId())) {
                UIUtil.showError(view, "Kode komponen '" + k.getKode() + "' sudah digunakan pada mata kuliah ini.");
                return;
            }

            komponenDao.update(k);
            UIUtil.showInfo(view, "Komponen penilaian berhasil diperbarui.");
            muatKomponen(mk);
        } catch (IllegalArgumentException e) {
            UIUtil.showError(view, e.getMessage());
        } catch (UnsupportedOperationException e) {
            UIUtil.showError(view, "Fitur pengubahan komponen menunggu implementasi KomponenDAO (porsi Anggota 3).");
        } catch (Exception e) {
            UIUtil.showError(view, "Gagal mengubah komponen: " + e.getMessage());
        }
    }

    private void hapus() {
        if (komponenTerpilih == null) {
            UIUtil.showError(view, "Pilih komponen yang ingin dihapus terlebih dahulu.");
            return;
        }
        MataKuliah mk = view.getMataKuliahTerpilih();
        if (mk == null) {
            return;
        }

        boolean yakin = UIUtil.confirmDelete(view, "komponen " + komponenTerpilih.getNama()
                + " (" + komponenTerpilih.getKode() + ") beserta seluruh data nilai yang bersangkutan");
        if (!yakin) {
            return;
        }

        try {
            komponenDao.delete(komponenTerpilih.getId());
            UIUtil.showInfo(view, "Komponen penilaian berhasil dihapus.");
            muatKomponen(mk);
        } catch (UnsupportedOperationException e) {
            UIUtil.showError(view, "Fitur penghapusan komponen menunggu implementasi KomponenDAO (porsi Anggota 3).");
        } catch (Exception e) {
            UIUtil.showError(view, "Gagal menghapus komponen: " + e.getMessage());
        }
    }

    private void batal() {
        komponenTerpilih = null;
        view.kosongkanForm();
    }

    private void validasiInput(KomponenPenilaian k) {
        if (k.getKode() == null || k.getKode().isBlank()) {
            throw new IllegalArgumentException("Kode komponen wajib diisi.");
        }
        if (k.getNama() == null || k.getNama().isBlank()) {
            throw new IllegalArgumentException("Nama komponen wajib diisi.");
        }
        if (k.getBobot() <= 0 || k.getBobot() > 100) {
            throw new IllegalArgumentException("Bobot komponen harus lebih dari 0% dan maksimal 100%.");
        }
    }

    @Override
    public void muatUlang() {
        MataKuliah mkSebelumnya = view.getMataKuliahTerpilih();
        List<MataKuliah> daftarMk = mataKuliahDao.findAll();
        view.setDaftarMataKuliah(daftarMk);

        if (mkSebelumnya != null) {
            for (int i = 0; i < view.getCmbMataKuliah().getItemCount(); i++) {
                MataKuliah item = view.getCmbMataKuliah().getItemAt(i);
                if (item != null && item.getId() == mkSebelumnya.getId()) {
                    view.getCmbMataKuliah().setSelectedIndex(i);
                    muatKomponen(item);
                    return;
                }
            }
        }

        if (!daftarMk.isEmpty()) {
            view.getCmbMataKuliah().setSelectedIndex(0);
            muatKomponen(daftarMk.get(0));
        } else {
            muatKomponen(null);
        }
    }
}
