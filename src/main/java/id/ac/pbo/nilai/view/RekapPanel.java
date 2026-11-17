package id.ac.pbo.nilai.view;

import id.ac.pbo.nilai.model.MataKuliah;
import id.ac.pbo.nilai.model.RekapNilai;
import id.ac.pbo.nilai.view.table.RekapTableModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

/**
 * Halaman rekap nilai akhir mahasiswa per mata kuliah.
 */
public class RekapPanel extends JPanel {

    private final RekapTableModel model = new RekapTableModel();
    private final JTable tabel = new JTable(model);
    private final JComboBox<MataKuliah> cbMataKuliah = new JComboBox<>();
    private final JButton btnHitung = new JButton("Hitung Nilai Akhir");
    private final JTextField txtCari = new JTextField();
    private final JButton btnCari = new JButton("Cari");
    private final JLabel lblRingkasan = new JLabel("Pilih mata kuliah lalu tekan Hitung.");
    private final JTextArea areaDetail = new JTextArea(10, 60);

    public RekapPanel() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel alat = new JPanel(new BorderLayout(8, 4));
        JPanel kiri = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        kiri.add(new JLabel("Mata Kuliah:"));
        cbMataKuliah.setPreferredSize(new java.awt.Dimension(280, cbMataKuliah.getPreferredSize().height));
        kiri.add(cbMataKuliah);
        kiri.add(btnHitung);
        kiri.add(new JLabel("   Cari NIM/Nama:"));
        txtCari.setColumns(16);
        kiri.add(txtCari);
        kiri.add(btnCari);
        alat.add(kiri, BorderLayout.NORTH);
        alat.add(lblRingkasan, BorderLayout.SOUTH);
        add(alat, BorderLayout.NORTH);

        UIUtil.siapkanTabel(tabel);
        areaDetail.setEditable(false);
        areaDetail.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        areaDetail.setLineWrap(false);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tabel), new JScrollPane(areaDetail));
        split.setResizeWeight(0.62);
        split.setBorder(BorderFactory.createEmptyBorder());
        add(split, BorderLayout.CENTER);
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

    public void setData(List<RekapNilai> data) {
        model.setData(data);
        tabel.clearSelection();
    }

    public RekapNilai getTerpilih() {
        int baris = tabel.getSelectedRow();
        if (baris < 0) {
            return null;
        }
        return model.getBaris(tabel.convertRowIndexToModel(baris));
    }

    public MataKuliah getMataKuliahTerpilih() {
        Object terpilih = cbMataKuliah.getSelectedItem();
        return terpilih instanceof MataKuliah mataKuliah ? mataKuliah : null;
    }

    public String getKataKunci() {
        return txtCari.getText();
    }

    public void setRingkasan(String teks) {
        lblRingkasan.setText(teks);
    }

    public void setDetail(String teks) {
        areaDetail.setText(teks);
        areaDetail.setCaretPosition(0);
    }

    public JTable getTabel() {
        return tabel;
    }

    public JButton getBtnHitung() {
        return btnHitung;
    }

    public JButton getBtnCari() {
        return btnCari;
    }
}
