package id.ac.pbo.nilai.view;

import id.ac.pbo.nilai.model.Dosen;
import id.ac.pbo.nilai.model.MataKuliah;
import id.ac.pbo.nilai.view.table.MataKuliahTableModel;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

public class MataKuliahPanel extends JPanel {

    private final MataKuliahTableModel tableModel = new MataKuliahTableModel();
    private final JTable tabel = new JTable(tableModel);

    private final JTextField txtKode = new JTextField(10);
    private final JTextField txtNama = new JTextField(20);
    private final JSpinner spSks = new JSpinner(new SpinnerNumberModel(3, 1, 6, 1));
    private final JComboBox<Dosen> cmbDosen = new JComboBox<>();

    private final JButton btnSimpan = new JButton("Simpan");
    private final JButton btnUbah = new JButton("Ubah");
    private final JButton btnHapus = new JButton("Hapus");
    private final JButton btnBatal = new JButton("Batal");

    private final JTextField txtCari = new JTextField(18);
    private final JButton btnCari = new JButton("Cari");
    private final JButton btnSegarkan = new JButton("Segarkan");

    public MataKuliahPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormWrapper = new JPanel(new BorderLayout(6, 6));
        JPanel form = UIUtil.formPanel("Data Mata Kuliah");
        panelFormWrapper.setPreferredSize(new Dimension(380, 0));

        UIUtil.addFormRow(form, 0, "Kode:", txtKode);
        UIUtil.addFormRow(form, 1, "Nama:", txtNama);
        UIUtil.addFormRow(form, 2, "SKS (1-6):", spSks);
        UIUtil.addFormRow(form, 3, "Dosen Pengampu:", cmbDosen);

        JPanel panelTombol = UIUtil.buttonBar(btnSimpan, btnUbah, btnHapus, btnBatal);

        panelFormWrapper.add(form, BorderLayout.CENTER);
        panelFormWrapper.add(panelTombol, BorderLayout.SOUTH);

        JPanel panelTabelWrapper = new JPanel(new BorderLayout(6, 6));
        JPanel panelCari = UIUtil.searchBar("Cari (Kode/Nama):", txtCari, btnCari, btnSegarkan);
        UIUtil.siapkanTabel(tabel);

        panelTabelWrapper.add(panelCari, BorderLayout.NORTH);
        panelTabelWrapper.add(new JScrollPane(tabel), BorderLayout.CENTER);

        add(panelFormWrapper, BorderLayout.WEST);
        add(panelTabelWrapper, BorderLayout.CENTER);

        kosongkanForm();
    }

    public void setDaftarDosen(List<Dosen> daftarDosen) {
        cmbDosen.removeAllItems();
        if (daftarDosen != null) {
            for (Dosen d : daftarDosen) {
                cmbDosen.addItem(d);
            }
        }
    }

    public void isiForm(MataKuliah mk) {
        if (mk == null) {
            kosongkanForm();
            return;
        }
        txtKode.setText(mk.getKode());
        txtNama.setText(mk.getNama());
        spSks.setValue(mk.getSks());

        for (int i = 0; i < cmbDosen.getItemCount(); i++) {
            Dosen d = cmbDosen.getItemAt(i);
            if (d != null && d.getId() == mk.getDosenId()) {
                cmbDosen.setSelectedIndex(i);
                break;
            }
        }

        btnSimpan.setEnabled(false);
        btnUbah.setEnabled(true);
        btnHapus.setEnabled(true);
    }

    public void kosongkanForm() {
        txtKode.setText("");
        txtNama.setText("");
        spSks.setValue(3);
        if (cmbDosen.getItemCount() > 0) {
            cmbDosen.setSelectedIndex(0);
        }
        tabel.clearSelection();

        btnSimpan.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
    }

    public MataKuliah bacaForm() {
        MataKuliah mk = new MataKuliah();
        mk.setKode(txtKode.getText().trim());
        mk.setNama(txtNama.getText().trim());
        Object valSks = spSks.getValue();
        if (valSks instanceof Number num) {
            mk.setSks(num.intValue());
        }
        Dosen dosenTerpilih = (Dosen) cmbDosen.getSelectedItem();
        if (dosenTerpilih != null) {
            mk.setDosenId(dosenTerpilih.getId());
            mk.setDosenNama(dosenTerpilih.getNama());
        }
        return mk;
    }

    public MataKuliahTableModel getTableModel() {
        return tableModel;
    }

    public JTable getTabel() {
        return tabel;
    }

    public JTextField getTxtKode() {
        return txtKode;
    }

    public JTextField getTxtNama() {
        return txtNama;
    }

    public JSpinner getSpSks() {
        return spSks;
    }

    public JComboBox<Dosen> getCmbDosen() {
        return cmbDosen;
    }

    public JTextField getTxtCari() {
        return txtCari;
    }

    public JButton getBtnSimpan() {
        return btnSimpan;
    }

    public JButton getBtnUbah() {
        return btnUbah;
    }

    public JButton getBtnHapus() {
        return btnHapus;
    }

    public JButton getBtnBatal() {
        return btnBatal;
    }

    public JButton getBtnCari() {
        return btnCari;
    }

    public JButton getBtnSegarkan() {
        return btnSegarkan;
    }
}
