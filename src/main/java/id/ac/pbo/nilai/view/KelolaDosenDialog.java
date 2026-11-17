package id.ac.pbo.nilai.view;

import id.ac.pbo.nilai.dao.DosenDAO;
import id.ac.pbo.nilai.exception.DataAccessException;
import id.ac.pbo.nilai.model.Dosen;
import id.ac.pbo.nilai.service.AuthService;
import id.ac.pbo.nilai.view.table.DosenTableModel;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Dialog bagi admin untuk melihat dan mengelola akun dosen.
 */
public class KelolaDosenDialog extends JDialog {

    private final DosenTableModel tableModel = new DosenTableModel();
    private final JTable tabel = new JTable(tableModel);

    private final JButton btnTambah = new JButton("Tambah Akun Dosen");
    private final JButton btnSegarkan = new JButton("Segarkan");
    private final JButton btnTutup = new JButton("Tutup");

    private final JFrame parentFrame;
    private final Dosen admin;
    private final DosenDAO dosenDAO = new DosenDAO();
    private final AuthService authService;

    public KelolaDosenDialog(JFrame parent, Dosen admin, AuthService authService) {
        super(parent, "Daftar & Kelola Akun Dosen", true);
        this.parentFrame = parent;
        this.admin = admin;
        this.authService = authService != null ? authService : new AuthService();

        inisialisasiUI();
        inisialisasiListener();
        muatData();

        setSize(600, 420);
        setMinimumSize(new Dimension(500, 320));
        setLocationRelativeTo(parent);
    }

    private void inisialisasiUI() {
        UIUtil.siapkanTabel(tabel);

        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 8, 6, 8));
        panelAtas.add(new JLabel("Hanya ada 1 akun admin. Admin dapat menambah akun dosen di bawah ini:"), BorderLayout.WEST);

        JPanel panelBawah = UIUtil.buttonBar(btnTambah, btnSegarkan, btnTutup);

        JPanel isi = new JPanel(new BorderLayout(8, 8));
        isi.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        isi.add(panelAtas, BorderLayout.NORTH);
        isi.add(new JScrollPane(tabel), BorderLayout.CENTER);
        isi.add(panelBawah, BorderLayout.SOUTH);

        setContentPane(isi);
    }

    private void inisialisasiListener() {
        btnTutup.addActionListener(e -> dispose());
        btnSegarkan.addActionListener(e -> muatData());
        btnTambah.addActionListener(e -> {
            TambahDosenDialog dialog = new TambahDosenDialog(parentFrame, admin, authService, this::muatData);
            dialog.setVisible(true);
        });
    }

    public void muatData() {
        try {
            tableModel.setData(dosenDAO.findAll());
        } catch (DataAccessException e) {
            UIUtil.showError(this, "Gagal memuat daftar dosen:\n" + e.getMessage());
        }
    }
}
