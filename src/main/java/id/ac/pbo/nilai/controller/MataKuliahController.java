package id.ac.pbo.nilai.controller;

import id.ac.pbo.nilai.dao.DosenDAO;
import id.ac.pbo.nilai.dao.MataKuliahDAO;
import id.ac.pbo.nilai.exception.ValidasiException;
import id.ac.pbo.nilai.model.MataKuliah;
import id.ac.pbo.nilai.view.MataKuliahPanel;
import id.ac.pbo.nilai.view.UIUtil;

import java.util.List;

public class MataKuliahController implements Penyegar {

    private final MataKuliahPanel view;
    private final MataKuliahDAO dao;
    private final DosenDAO dosenDao;
    private MataKuliah mataKuliahTerpilih;

    public MataKuliahController(MataKuliahPanel view) {
        this(view, new MataKuliahDAO(), new DosenDAO());
    }

    public MataKuliahController(MataKuliahPanel view, MataKuliahDAO dao, DosenDAO dosenDao) {
        this.view = view;
        this.dao = dao;
        this.dosenDao = dosenDao;
        inisialisasiListener();
    }

    private void inisialisasiListener() {
        view.getBtnSimpan().addActionListener(e -> simpan());
        view.getBtnUbah().addActionListener(e -> ubah());
        view.getBtnHapus().addActionListener(e -> hapus());
        view.getBtnBatal().addActionListener(e -> batal());

        view.getBtnCari().addActionListener(e -> cari());
        view.getTxtCari().addActionListener(e -> cari());
        view.getBtnSegarkan().addActionListener(e -> segarkan());

        view.getTabel().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int baris = view.getTabel().getSelectedRow();
                if (baris != -1) {
                    int modelRow = view.getTabel().convertRowIndexToModel(baris);
                    mataKuliahTerpilih = view.getTableModel().getBaris(modelRow);
                    view.isiForm(mataKuliahTerpilih);
                }
            }
        });
    }

    private void simpan() {
        try {
            MataKuliah mk = view.bacaForm();
            mk.validasi();
            if (dao.kodeSudahDipakai(mk.getKode(), 0)) {
                UIUtil.showError(view, "Kode mata kuliah " + mk.getKode() + " sudah digunakan.");
                return;
            }
            dao.insert(mk);
            UIUtil.showInfo(view, "Data mata kuliah berhasil disimpan.");
            muatUlang();
        } catch (ValidasiException | IllegalArgumentException e) {
            UIUtil.showError(view, e.getMessage());
        } catch (Exception e) {
            UIUtil.showError(view, "Gagal menyimpan mata kuliah: " + e.getMessage());
        }
    }

    private void ubah() {
        if (mataKuliahTerpilih == null) {
            UIUtil.showError(view, "Pilih mata kuliah yang ingin diubah terlebih dahulu.");
            return;
        }
        try {
            MataKuliah mk = view.bacaForm();
            mk.setId(mataKuliahTerpilih.getId());
            mk.validasi();
            if (dao.kodeSudahDipakai(mk.getKode(), mk.getId())) {
                UIUtil.showError(view, "Kode mata kuliah " + mk.getKode() + " sudah digunakan.");
                return;
            }
            dao.update(mk);
            UIUtil.showInfo(view, "Data mata kuliah berhasil diperbarui.");
            muatUlang();
        } catch (ValidasiException | IllegalArgumentException e) {
            UIUtil.showError(view, e.getMessage());
        } catch (Exception e) {
            UIUtil.showError(view, "Gagal mengubah mata kuliah: " + e.getMessage());
        }
    }

    private void hapus() {
        if (mataKuliahTerpilih == null) {
            UIUtil.showError(view, "Pilih mata kuliah yang ingin dihapus terlebih dahulu.");
            return;
        }
        boolean yakin = UIUtil.confirmDelete(view, "mata kuliah " + mataKuliahTerpilih.getNama()
                + " (" + mataKuliahTerpilih.getKode() + ") beserta seluruh komponen penilaian dan nilai yang terkait");
        if (!yakin) {
            return;
        }
        try {
            dao.delete(mataKuliahTerpilih.getId());
            UIUtil.showInfo(view, "Data mata kuliah berhasil dihapus.");
            muatUlang();
        } catch (Exception e) {
            UIUtil.showError(view, "Gagal menghapus mata kuliah: " + e.getMessage());
        }
    }

    private void cari() {
        String kataKunci = view.getTxtCari().getText().trim();
        List<MataKuliah> hasil = dao.cari(kataKunci);
        view.getTableModel().setData(hasil);
    }

    private void segarkan() {
        view.getTxtCari().setText("");
        muatUlang();
    }

    private void batal() {
        mataKuliahTerpilih = null;
        view.kosongkanForm();
    }

    @Override
    public void muatUlang() {
        view.setDaftarDosen(dosenDao.findAll());
        mataKuliahTerpilih = null;
        view.kosongkanForm();
        view.getTableModel().setData(dao.findAll());
    }
}
