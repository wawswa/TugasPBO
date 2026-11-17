package id.ac.pbo.nilai.controller;

import id.ac.pbo.nilai.dao.KomponenDAO;
import id.ac.pbo.nilai.dao.MahasiswaDAO;
import id.ac.pbo.nilai.dao.MataKuliahDAO;
import id.ac.pbo.nilai.dao.NilaiDAO;
import id.ac.pbo.nilai.exception.DataAccessException;
import id.ac.pbo.nilai.exception.ValidasiException;
import id.ac.pbo.nilai.model.KomponenPenilaian;
import id.ac.pbo.nilai.model.Mahasiswa;
import id.ac.pbo.nilai.model.MataKuliah;
import id.ac.pbo.nilai.model.Nilai;
import id.ac.pbo.nilai.view.InputNilaiPanel;
import id.ac.pbo.nilai.view.UIUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Controller halaman input nilai.
 */
public class NilaiController implements Penyegar {

    private final InputNilaiPanel view;
    private final NilaiDAO nilaiDAO = new NilaiDAO();
    private final KomponenDAO komponenDAO = new KomponenDAO();
    private final MahasiswaDAO mahasiswaDAO = new MahasiswaDAO();
    private final MataKuliahDAO mataKuliahDAO = new MataKuliahDAO();

    public NilaiController(InputNilaiPanel view) {
        this.view = view;
        view.getCbMataKuliah().addActionListener(e -> muatKomponenDanMahasiswa());
        view.getCbKomponen().addActionListener(e -> {
            perbaruiAturan();
            muatNilai();
        });
        view.getBtnTambah().addActionListener(e -> tambah());
        view.getBtnUbah().addActionListener(e -> ubah());
        view.getBtnHapus().addActionListener(e -> hapus());
        view.getBtnBersihkan().addActionListener(e -> view.bersihkanForm());
        view.getTabel().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Nilai nilai = view.getTerpilih();
                if (nilai != null) {
                    view.setNilaiInput(nilai.getNilai());
                }
            }
        });
    }

    @Override
    public void muatUlang() {
        try {
            view.setMataKuliahList(mataKuliahDAO.findAll());
            muatKomponenDanMahasiswa();
        } catch (DataAccessException e) {
            UIUtil.showError(view, "Gagal memuat data nilai.\n" + e.getMessage());
        }
    }

    private void muatKomponenDanMahasiswa() {
        MataKuliah mataKuliah = view.getMataKuliahTerpilih();
        if (mataKuliah == null) {
            view.setKomponenList(List.of());
            view.setMahasiswaList(List.of());
            view.setData(List.of());
            view.setAturan("-");
            return;
        }
        try {
            view.setKomponenList(komponenDAO.findByMataKuliah(mataKuliah.getId()));
            view.setMahasiswaList(mahasiswaDAO.findAll());
            perbaruiAturan();
            muatNilai();
        } catch (DataAccessException e) {
            UIUtil.showError(view, "Gagal memuat daftar komponen/mahasiswa.\n" + e.getMessage());
        }
    }

    private void perbaruiAturan() {
        KomponenPenilaian komponen = view.getKomponenTerpilih();
        if (komponen == null) {
            view.setAturan("-");
            return;
        }
        String aturan = komponen.aturan() + "  |  bobot "
                + String.format(Locale.US, "%.1f%%", komponen.getBobot());
        if (!komponen.izinkanBanyakNilai()) {
            aturan += "  |  hanya satu nilai per mahasiswa";
        }
        view.setAturan(aturan);
    }

    private void muatNilai() {
        KomponenPenilaian komponen = view.getKomponenTerpilih();
        if (komponen == null) {
            view.setData(List.of());
            return;
        }
        try {
            view.setData(nilaiDAO.findByKomponen(komponen.getId()));
        } catch (DataAccessException e) {
            UIUtil.showError(view, "Gagal memuat nilai.\n" + e.getMessage());
        }
    }

    private void tambah() {
        KomponenPenilaian komponen = view.getKomponenTerpilih();
        Mahasiswa mahasiswa = view.getMahasiswaTerpilih();
        if (komponen == null) {
            UIUtil.showError(view, "Pilih komponen penilaian terlebih dahulu.");
            return;
        }
        if (mahasiswa == null) {
            UIUtil.showError(view, "Pilih mahasiswa terlebih dahulu.");
            return;
        }
        try {
            Nilai nilai = new Nilai();
            nilai.setMahasiswaId(mahasiswa.getId());
            nilai.setKomponenId(komponen.getId());
            nilai.setNilai(view.getNilaiInput());
            nilai.setTanggalInput(LocalDate.now());
            nilai.validasi();
            if (!komponen.izinkanBanyakNilai()
                    && nilaiDAO.jumlahNilai(mahasiswa.getId(), komponen.getId()) > 0) {
                throw new ValidasiException("Komponen ujian hanya boleh memiliki satu nilai "
                        + "per mahasiswa. Gunakan tombol Ubah untuk memperbaiki nilai.");
            }
            nilaiDAO.insert(nilai);
            muatNilai();
            view.bersihkanForm();
            UIUtil.showInfo(view, "Nilai berhasil disimpan.");
        } catch (IllegalArgumentException | ValidasiException e) {
            UIUtil.showError(view, e.getMessage());
        } catch (DataAccessException e) {
            UIUtil.showError(view, "Gagal menyimpan nilai.\n" + e.getMessage());
        }
    }

    private void ubah() {
        Nilai terpilih = view.getTerpilih();
        if (terpilih == null) {
            UIUtil.showError(view, "Pilih nilai pada tabel terlebih dahulu.");
            return;
        }
        try {
            Nilai nilai = new Nilai();
            nilai.setId(terpilih.getId());
            nilai.setMahasiswaId(terpilih.getMahasiswaId());
            nilai.setKomponenId(terpilih.getKomponenId());
            nilai.setNilai(view.getNilaiInput());
            nilai.setTanggalInput(LocalDate.now());
            nilai.validasi();
            nilaiDAO.update(nilai);
            muatNilai();
            view.bersihkanForm();
            UIUtil.showInfo(view, "Nilai berhasil diubah.");
        } catch (IllegalArgumentException | ValidasiException e) {
            UIUtil.showError(view, e.getMessage());
        } catch (DataAccessException e) {
            UIUtil.showError(view, "Gagal mengubah nilai.\n" + e.getMessage());
        }
    }

    private void hapus() {
        Nilai terpilih = view.getTerpilih();
        if (terpilih == null) {
            UIUtil.showError(view, "Pilih nilai pada tabel terlebih dahulu.");
            return;
        }
        if (!UIUtil.confirmDelete(view, "nilai " + String.format(Locale.US, "%.2f", terpilih.getNilai())
                + " milik " + terpilih.getMahasiswaNama())) {
            return;
        }
        try {
            nilaiDAO.delete(terpilih.getId());
            muatNilai();
            view.bersihkanForm();
            UIUtil.showInfo(view, "Nilai berhasil dihapus.");
        } catch (DataAccessException e) {
            UIUtil.showError(view, "Gagal menghapus nilai.\n" + e.getMessage());
        }
    }
}
