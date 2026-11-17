package id.ac.pbo.nilai.view;

import id.ac.pbo.nilai.exception.DataAccessException;
import id.ac.pbo.nilai.exception.ValidasiException;
import id.ac.pbo.nilai.model.Dosen;
import id.ac.pbo.nilai.service.AuthService;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * Dialog bagi admin untuk mendaftarkan akun dosen baru.
 */
public class TambahDosenDialog extends JDialog {

    private final JTextField txtNama = new JTextField(20);
    private final JTextField txtUsername = new JTextField(20);
    private final JPasswordField txtPassword = new JPasswordField(20);
    private final JPasswordField txtKonfirmasi = new JPasswordField(20);
    private final JButton btnSimpan = new JButton("Tambah Dosen");
    private final JButton btnBatal = new JButton("Batal");

    private final Dosen admin;
    private final AuthService authService;
    private final Runnable onSuccess;

    public TambahDosenDialog(JFrame parent, Dosen admin, AuthService authService, Runnable onSuccess) {
        super(parent, "Tambah Akun Dosen", true);
        this.admin = admin;
        this.authService = authService != null ? authService : new AuthService();
        this.onSuccess = onSuccess;

        inisialisasiUI();
        inisialisasiListener();
        pack();
        setMinimumSize(new Dimension(380, 240));
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void inisialisasiUI() {
        JPanel form = UIUtil.formPanel("Form Pendaftaran Dosen (Akses Admin)");
        UIUtil.addFormRow(form, 0, "Nama Lengkap:", txtNama);
        UIUtil.addFormRow(form, 1, "Username:", txtUsername);
        UIUtil.addFormRow(form, 2, "Password:", txtPassword);
        UIUtil.addFormRow(form, 3, "Konfirmasi Password:", txtKonfirmasi);

        JPanel panelTombol = UIUtil.buttonBar(btnSimpan, btnBatal);

        JPanel isi = new JPanel(new BorderLayout(8, 8));
        isi.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        isi.add(form, BorderLayout.CENTER);
        isi.add(panelTombol, BorderLayout.SOUTH);

        setContentPane(isi);
        getRootPane().setDefaultButton(btnSimpan);
    }

    private void inisialisasiListener() {
        btnBatal.addActionListener(e -> dispose());
        btnSimpan.addActionListener(e -> simpan());
    }

    private void simpan() {
        String nama = txtNama.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String konfirmasi = new String(txtKonfirmasi.getPassword());

        try {
            Dosen baru = authService.tambahDosen(admin, nama, username, password, konfirmasi);
            UIUtil.showInfo(this, "Akun dosen '" + baru.getUsername() + "' (" + baru.getNama() + ") berhasil ditambahkan!");
            if (onSuccess != null) {
                onSuccess.run();
            }
            dispose();
        } catch (ValidasiException e) {
            UIUtil.showError(this, e.getMessage());
        } catch (DataAccessException e) {
            UIUtil.showError(this, "Gagal mengakses basis data:\n" + e.getMessage());
        }
    }
}
