package id.ac.pbo.nilai.view;

import id.ac.pbo.nilai.model.KomponenPenilaian;
import id.ac.pbo.nilai.model.MataKuliah;
import id.ac.pbo.nilai.view.table.KomponenTableModel;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import java.util.Locale;

public class KomponenPanel extends JPanel {

    private final KomponenTableModel tableModel = new KomponenTableModel();
    private final JTable tabel = new JTable(tableModel);

    private final JComboBox<MataKuliah> cmbMataKuliah = new JComboBox<>();
    private final JLabel lblTotalBobot = new JLabel("Total Bobot: 0.0%");

    private final JComboBox<String> cmbJenis = new JComboBox<>(new String[]{"TUGAS", "KUIS", "UJIAN"});
    private final JTextField txtKode = new JTextField(10);
    private final JTextField txtNama = new JTextField(20);
    private final JSpinner spBobot = new JSpinner(new SpinnerNumberModel(20.0, 0.0, 100.0, 5.0));

    private final JButton btnSimpan = new JButton("Simpan");
    private final JButton btnUbah = new JButton("Ubah");
    private final JButton btnHapus = new JButton("Hapus");
    private final JButton btnBatal = new JButton("Batal");

    public KomponenPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelAtas = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        panelAtas.add(new JLabel("Mata Kuliah:"));
        cmbMataKuliah.setPreferredSize(new Dimension(280, 26));
        panelAtas.add(cmbMataKuliah);

        lblTotalBobot.setFont(lblTotalBobot.getFont().deriveFont(Font.BOLD, 13f));
        panelAtas.add(lblTotalBobot);

        JPanel panelFormWrapper = new JPanel(new BorderLayout(6, 6));
        JPanel form = UIUtil.formPanel("Data Komponen Penilaian");
        panelFormWrapper.setPreferredSize(new Dimension(380, 0));

        UIUtil.addFormRow(form, 0, "Jenis:", cmbJenis);
        UIUtil.addFormRow(form, 1, "Kode:", txtKode);
        UIUtil.addFormRow(form, 2, "Nama Komponen:", txtNama);
        UIUtil.addFormRow(form, 3, "Bobot (%):", spBobot);

        JPanel panelTombol = UIUtil.buttonBar(btnSimpan, btnUbah, btnHapus, btnBatal);

        panelFormWrapper.add(form, BorderLayout.CENTER);
        panelFormWrapper.add(panelTombol, BorderLayout.SOUTH);

        JPanel panelTabelWrapper = new JPanel(new BorderLayout(6, 6));
        UIUtil.siapkanTabel(tabel);
        panelTabelWrapper.add(new JScrollPane(tabel), BorderLayout.CENTER);

        add(panelAtas, BorderLayout.NORTH);
        add(panelFormWrapper, BorderLayout.WEST);
        add(panelTabelWrapper, BorderLayout.CENTER);

        kosongkanForm();
    }

    public void setDaftarMataKuliah(List<MataKuliah> daftar) {
        cmbMataKuliah.removeAllItems();
        if (daftar != null) {
            for (MataKuliah mk : daftar) {
                cmbMataKuliah.addItem(mk);
            }
        }
    }

    public MataKuliah getMataKuliahTerpilih() {
        return (MataKuliah) cmbMataKuliah.getSelectedItem();
    }

    public void setTotalBobot(double total) {
        lblTotalBobot.setText(String.format(Locale.ROOT, "Total Bobot: %.1f%% / 100.0%%", total));
        if (total > 100.0) {
            lblTotalBobot.setForeground(new Color(180, 0, 0));
        } else if (Math.abs(total - 100.0) < 0.001) {
            lblTotalBobot.setForeground(new Color(0, 130, 0));
        } else {
            lblTotalBobot.setForeground(Color.DARK_GRAY);
        }
    }

    public void isiForm(KomponenPenilaian k) {
        if (k == null) {
            kosongkanForm();
            return;
        }
        cmbJenis.setSelectedItem(k.jenis());
        txtKode.setText(k.getKode());
        txtNama.setText(k.getNama());
        spBobot.setValue(k.getBobot());

        btnSimpan.setEnabled(false);
        btnUbah.setEnabled(true);
        btnHapus.setEnabled(true);
    }

    public void kosongkanForm() {
        cmbJenis.setSelectedIndex(0);
        txtKode.setText("");
        txtNama.setText("");
        spBobot.setValue(20.0);
        tabel.clearSelection();

        btnSimpan.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
    }

    public KomponenPenilaian buatDariForm() {
        String jenis = (String) cmbJenis.getSelectedItem();
        KomponenPenilaian k = KomponenPenilaian.buat(jenis);

        MataKuliah mk = getMataKuliahTerpilih();
        if (mk != null) {
            k.setMataKuliahId(mk.getId());
        }
        k.setKode(txtKode.getText().trim());
        k.setNama(txtNama.getText().trim());
        Object valBobot = spBobot.getValue();
        if (valBobot instanceof Number num) {
            k.setBobot(num.doubleValue());
        }
        return k;
    }

    public KomponenTableModel getTableModel() {
        return tableModel;
    }

    public JTable getTabel() {
        return tabel;
    }

    public JComboBox<MataKuliah> getCmbMataKuliah() {
        return cmbMataKuliah;
    }

    public JComboBox<String> getCmbJenis() {
        return cmbJenis;
    }

    public JTextField getTxtKode() {
        return txtKode;
    }

    public JTextField getTxtNama() {
        return txtNama;
    }

    public JSpinner getSpBobot() {
        return spBobot;
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
}
