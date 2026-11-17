package id.ac.pbo.nilai.controller;

import id.ac.pbo.nilai.dao.MahasiswaDAO;
import id.ac.pbo.nilai.exception.ValidasiException;
import id.ac.pbo.nilai.model.Mahasiswa;
import id.ac.pbo.nilai.view.MahasiswaPanel;
import id.ac.pbo.nilai.view.UIUtil;

import java.util.List;

public class MahasiswaController implements Penyegar {

    private final MahasiswaPanel view;
    private final MahasiswaDAO dao;
    private Mahasiswa mahasiswaTerpilih;

    public MahasiswaController(MahasiswaPanel view) {
        this(view, new MahasiswaDAO());
    }

    public MahasiswaController(MahasiswaPanel view, MahasiswaDAO dao) {
        this.view = view;
        this.dao = dao;
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
                    mahasiswaTerpilih = view.getTableModel().getBaris(modelRow);
                    view.isiForm(mahasiswaTerpilih);
                }
            }
        });
    }

    private void simpan() {
        try {
            Mahasiswa m = view.bacaForm();
            m.validasi();
            if (dao.nimSudahDipakai(m.getNim(), 0)) {
                UIUtil.showError(view, "NIM " + m.getNim() + " sudah digunakan oleh mahasiswa lain.");
                return;
            }
            dao.insert(m);
            UIUtil.showInfo(view, "Data mahasiswa berhasil disimpan.");
            muatUlang();
        } catch (ValidasiException | IllegalArgumentException e) {
            UIUtil.showError(view, e.getMessage());
        } catch (Exception e) {
            UIUtil.showError(view, "Gagal menyimpan mahasiswa: " + e.getMessage());
        }
    }

    private void ubah() {
        if (mahasiswaTerpilih == null) {
            UIUtil.showError(view, "Pilih mahasiswa yang ingin diubah terlebih dahulu.");
            return;
        }
        try {
            Mahasiswa m = view.bacaForm();
            m.setId(mahasiswaTerpilih.getId());
            m.validasi();
            if (dao.nimSudahDipakai(m.getNim(), m.getId())) {
                UIUtil.showError(view, "NIM " + m.getNim() + " sudah digunakan oleh mahasiswa lain.");
                return;
            }
            dao.update(m);
            UIUtil.showInfo(view, "Data mahasiswa berhasil diperbarui.");
            muatUlang();
        } catch (ValidasiException | IllegalArgumentException e) {
            UIUtil.showError(view, e.getMessage());
        } catch (Exception e) {
            UIUtil.showError(view, "Gagal mengubah mahasiswa: " + e.getMessage());
        }
    }

    private void hapus() {
        if (mahasiswaTerpilih == null) {
            UIUtil.showError(view, "Pilih mahasiswa yang ingin dihapus terlebih dahulu.");
            return;
        }
        boolean yakin = UIUtil.confirmDelete(view, "mahasiswa " + mahasiswaTerpilih.getNama()
                + " (NIM: " + mahasiswaTerpilih.getNim() + ") beserta seluruh data nilai yang bersangkutan");
        if (!yakin) {
            return;
        }
        try {
            dao.delete(mahasiswaTerpilih.getId());
            UIUtil.showInfo(view, "Data mahasiswa berhasil dihapus.");
            muatUlang();
        } catch (Exception e) {
            UIUtil.showError(view, "Gagal menghapus mahasiswa: " + e.getMessage());
        }
    }

    private void cari() {
        String kataKunci = view.getTxtCari().getText().trim();
        List<Mahasiswa> hasil = dao.cari(kataKunci);
        view.getTableModel().setData(hasil);
    }

    private void segarkan() {
        view.getTxtCari().setText("");
        muatUlang();
    }

    private void batal() {
        mahasiswaTerpilih = null;
        view.kosongkanForm();
    }

    @Override
    public void muatUlang() {
        mahasiswaTerpilih = null;
        view.kosongkanForm();
        view.getTableModel().setData(dao.findAll());
    }
}
