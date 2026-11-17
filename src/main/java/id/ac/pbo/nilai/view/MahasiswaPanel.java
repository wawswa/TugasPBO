package id.ac.pbo.nilai.view;

import id.ac.pbo.nilai.model.Mahasiswa;
import id.ac.pbo.nilai.view.table.MahasiswaTableModel;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.time.LocalDate;

public class MahasiswaPanel extends JPanel {

    private final MahasiswaTableModel tableModel = new MahasiswaTableModel();
    private final JTable tabel = new JTable(tableModel);

    private final JTextField txtNim = new JTextField(15);
    private final JTextField txtNama = new JTextField(20);
    private final JRadioButton rbLaki = new JRadioButton("L", true);
    private final JRadioButton rbPerempuan = new JRadioButton("P");
    private final ButtonGroup bgGender = new ButtonGroup();
    private final JSpinner spTanggalLahir = UIUtil.spinnerTanggal(LocalDate.of(2003, 1, 1));
    private final JComboBox<Integer> cmbAngkatan = new JComboBox<>();

    private final JButton btnSimpan = new JButton("Simpan");
    private final JButton btnUbah = new JButton("Ubah");
    private final JButton btnHapus = new JButton("Hapus");
    private final JButton btnBatal = new JButton("Batal");

    private final JTextField txtCari = new JTextField(18);
    private final JButton btnCari = new JButton("Cari");
    private final JButton btnSegarkan = new JButton("Segarkan");

    public MahasiswaPanel() {
        setLayout(new BorderLayout(10, 10));

        bgGender.add(rbLaki);
        bgGender.add(rbPerempuan);

        int tahunSekarang = LocalDate.now().getYear();
        for (int th = tahunSekarang; th >= 2000; th--) {
            cmbAngkatan.addItem(th);
        }

        JPanel panelFormWrapper = new JPanel(new BorderLayout(6, 6));
        JPanel form = UIUtil.formPanel("Data Mahasiswa");
        panelFormWrapper.setPreferredSize(new Dimension(380, 0));

        JPanel panelGender = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelGender.add(rbLaki);
        panelGender.add(rbPerempuan);

        UIUtil.addFormRow(form, 0, "NIM:", txtNim);
        UIUtil.addFormRow(form, 1, "Nama:", txtNama);
        UIUtil.addFormRow(form, 2, "Jenis Kelamin:", panelGender);
        UIUtil.addFormRow(form, 3, "Tanggal Lahir:", spTanggalLahir);
        UIUtil.addFormRow(form, 4, "Angkatan:", cmbAngkatan);

        JPanel panelTombol = UIUtil.buttonBar(btnSimpan, btnUbah, btnHapus, btnBatal);

        panelFormWrapper.add(form, BorderLayout.CENTER);
        panelFormWrapper.add(panelTombol, BorderLayout.SOUTH);

        JPanel panelTabelWrapper = new JPanel(new BorderLayout(6, 6));
        JPanel panelCari = UIUtil.searchBar("Cari (NIM/Nama):", txtCari, btnCari, btnSegarkan);
        UIUtil.siapkanTabel(tabel);

        panelTabelWrapper.add(panelCari, BorderLayout.NORTH);
        panelTabelWrapper.add(new JScrollPane(tabel), BorderLayout.CENTER);

        add(panelFormWrapper, BorderLayout.WEST);
        add(panelTabelWrapper, BorderLayout.CENTER);

        kosongkanForm();
    }

    public void isiForm(Mahasiswa m) {
        if (m == null) {
            kosongkanForm();
            return;
        }
        txtNim.setText(m.getNim());
        txtNama.setText(m.getNama());
        if ("P".equalsIgnoreCase(m.getJenisKelamin())) {
            rbPerempuan.setSelected(true);
        } else {
            rbLaki.setSelected(true);
        }
        if (m.getTanggalLahir() != null) {
            spTanggalLahir.setValue(UIUtil.keDate(m.getTanggalLahir()));
        }
        cmbAngkatan.setSelectedItem(m.getAngkatan());

        btnSimpan.setEnabled(false);
        btnUbah.setEnabled(true);
        btnHapus.setEnabled(true);
    }

    public void kosongkanForm() {
        txtNim.setText("");
        txtNama.setText("");
        rbLaki.setSelected(true);
        spTanggalLahir.setValue(UIUtil.keDate(LocalDate.of(2003, 1, 1)));
        if (cmbAngkatan.getItemCount() > 0) {
            cmbAngkatan.setSelectedIndex(0);
        }
        tabel.clearSelection();

        btnSimpan.setEnabled(true);
        btnUbah.setEnabled(false);
        btnHapus.setEnabled(false);
    }

    public Mahasiswa bacaForm() {
        Mahasiswa m = new Mahasiswa();
        m.setNim(txtNim.getText().trim());
        m.setNama(txtNama.getText().trim());
        m.setJenisKelamin(rbLaki.isSelected() ? "L" : "P");
        m.setTanggalLahir(UIUtil.tanggalDari(spTanggalLahir));
        Object angkatan = cmbAngkatan.getSelectedItem();
        if (angkatan instanceof Integer angkatanInt) {
            m.setAngkatan(angkatanInt);
        }
        return m;
    }

    public MahasiswaTableModel getTableModel() {
        return tableModel;
    }

    public JTable getTabel() {
        return tabel;
    }

    public JTextField getTxtNim() {
        return txtNim;
    }

    public JTextField getTxtNama() {
        return txtNama;
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
