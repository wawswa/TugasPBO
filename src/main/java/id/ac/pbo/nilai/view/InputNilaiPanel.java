package id.ac.pbo.nilai.view;

import id.ac.pbo.nilai.model.KomponenPenilaian;
import id.ac.pbo.nilai.model.Mahasiswa;
import id.ac.pbo.nilai.model.MataKuliah;
import id.ac.pbo.nilai.model.Nilai;
import id.ac.pbo.nilai.view.table.NilaiTableModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.util.List;

/**
 * Halaman input dan pengelolaan nilai mentah mahasiswa.
 */
public class InputNilaiPanel extends JPanel {

    private final NilaiTableModel model = new NilaiTableModel();
    private final JTable tabel = new JTable(model);
    private final JComboBox<MataKuliah> cbMataKuliah = new JComboBox<>();
    private final JComboBox<KomponenPenilaian> cbKomponen = new JComboBox<>();
    private final JComboBox<Mahasiswa> cbMahasiswa = new JComboBox<>();
    private final JSpinner spNilai = new JSpinner(new SpinnerNumberModel(75.0, 0.0, 100.0, 0.5));
    private final JLabel lblAturan = new JLabel("-");
    private final JButton btnTambah = new JButton("Tambah");
    private final JButton btnUbah = new JButton("Ubah");
    private final JButton btnHapus = new JButton("Hapus");
    private final JButton btnBersihkan = new JButton("Bersihkan");

    public InputNilaiPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel form = UIUtil.formPanel("Input Nilai");
        UIUtil.addFormRow(form, 0, "Mata Kuliah", cbMataKuliah);
        UIUtil.addFormRow(form, 1, "Komponen", cbKomponen);
        UIUtil.addFormRow(form, 2, "Mahasiswa", cbMahasiswa);
        UIUtil.addFormRow(form, 3, "Nilai (0-100)", spNilai);
        UIUtil.addFormRow(form, 4, "Aturan Komponen", lblAturan);
        add(form, BorderLayout.NORTH);

        UIUtil.siapkanTabel(tabel);
        add(new JScrollPane(tabel), BorderLayout.CENTER);
        add(UIUtil.buttonBar(btnTambah, btnUbah, btnHapus, btnBersihkan), BorderLayout.SOUTH);
    }

    public void setMataKuliahList(List<MataKuliah> daftar) {
        Object terpilih = cbMataKuliah.getSelectedItem();
        int idTerpilih = terpilih instanceof MataKuliah mataKuliah ? mataKuliah.getId() : -1;
        cbMataKuliah.removeAllItems();
        for (MataKuliah mataKuliah : daftar) {
            cbMataKuliah.addItem(mataKuliah);
        }
        for (int i = 0; i < cbMataKuliah.getItemCount(); i++) {
            if (cbMataKuliah.getItemAt(i).getId() == idTerpilih) {
                cbMataKuliah.setSelectedIndex(i);
                break;
            }
        }
    }

    public void setKomponenList(List<KomponenPenilaian> daftar) {
        Object terpilih = cbKomponen.getSelectedItem();
        int idTerpilih = terpilih instanceof KomponenPenilaian komponen ? komponen.getId() : -1;
        cbKomponen.removeAllItems();
        for (KomponenPenilaian komponen : daftar) {
            cbKomponen.addItem(komponen);
        }
        for (int i = 0; i < cbKomponen.getItemCount(); i++) {
            if (cbKomponen.getItemAt(i).getId() == idTerpilih) {
                cbKomponen.setSelectedIndex(i);
                break;
            }
        }
    }

    public void setMahasiswaList(List<Mahasiswa> daftar) {
        Object terpilih = cbMahasiswa.getSelectedItem();
        int idTerpilih = terpilih instanceof Mahasiswa mahasiswa ? mahasiswa.getId() : -1;
        cbMahasiswa.removeAllItems();
        for (Mahasiswa mahasiswa : daftar) {
            cbMahasiswa.addItem(mahasiswa);
        }
        for (int i = 0; i < cbMahasiswa.getItemCount(); i++) {
            if (cbMahasiswa.getItemAt(i).getId() == idTerpilih) {
                cbMahasiswa.setSelectedIndex(i);
                break;
            }
        }
    }

    public void setData(List<Nilai> data) {
        model.setData(data);
        tabel.clearSelection();
    }

    public MataKuliah getMataKuliahTerpilih() {
        Object terpilih = cbMataKuliah.getSelectedItem();
        return terpilih instanceof MataKuliah mataKuliah ? mataKuliah : null;
    }

    public KomponenPenilaian getKomponenTerpilih() {
        Object terpilih = cbKomponen.getSelectedItem();
        return terpilih instanceof KomponenPenilaian komponen ? komponen : null;
    }

    public Mahasiswa getMahasiswaTerpilih() {
        Object terpilih = cbMahasiswa.getSelectedItem();
        return terpilih instanceof Mahasiswa mahasiswa ? mahasiswa : null;
    }

    public Nilai getTerpilih() {
        int baris = tabel.getSelectedRow();
        if (baris < 0) {
            return null;
        }
        return model.getBaris(tabel.convertRowIndexToModel(baris));
    }

    public double getNilaiInput() {
        return ((Number) spNilai.getValue()).doubleValue();
    }

    public void setNilaiInput(double nilai) {
        spNilai.setValue(nilai);
    }

    public void setAturan(String teks) {
        lblAturan.setText(teks);
    }

    public void bersihkanForm() {
        spNilai.setValue(75.0);
        tabel.clearSelection();
    }

    public JComboBox<MataKuliah> getCbMataKuliah() {
        return cbMataKuliah;
    }

    public JComboBox<KomponenPenilaian> getCbKomponen() {
        return cbKomponen;
    }

    public JTable getTabel() {
        return tabel;
    }

    public JButton getBtnTambah() {
        return btnTambah;
    }

    public JButton getBtnUbah() {
        return btnUbah;
    }

    public JButton getBtnHapus() {
        return btnHapus;
    }

    public JButton getBtnBersihkan() {
        return btnBersihkan;
    }
}
