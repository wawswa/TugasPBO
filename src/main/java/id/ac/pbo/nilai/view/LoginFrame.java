package id.ac.pbo.nilai.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Halaman login dosen.
 */
public class LoginFrame extends JFrame {

    private final JTextField txtUsername = new JTextField(18);
    private final JPasswordField txtPassword = new JPasswordField(18);
    private final JButton btnLogin = new JButton("Masuk");

    public LoginFrame() {
        super("Login - Sistem Penilaian Akademik");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel kartu = new JPanel(new GridBagLayout());
        kartu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Silakan masuk"),
                BorderFactory.createEmptyBorder(12, 24, 18, 24)));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(2, 2, 2, 2);

        JLabel judul = new JLabel("Sistem Penilaian Akademik");
        judul.setFont(judul.getFont().deriveFont(Font.BOLD, 20f));
        kartu.add(judul, constraints);

        constraints.gridy = 1;
        JLabel subjudul = new JLabel("Tugas Besar PBO - Tema Penilaian Akademik", SwingConstants.CENTER);
        kartu.add(subjudul, constraints);

        constraints.gridwidth = 1;
        constraints.gridy = 2;
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(14, 2, 4, 8);
        kartu.add(new JLabel("Username"), constraints);
        constraints.gridx = 1;
        constraints.insets = new Insets(14, 0, 4, 2);
        kartu.add(txtUsername, constraints);

        constraints.gridy = 3;
        constraints.gridx = 0;
        constraints.insets = new Insets(4, 2, 4, 8);
        kartu.add(new JLabel("Password"), constraints);
        constraints.gridx = 1;
        constraints.insets = new Insets(4, 0, 4, 2);
        kartu.add(txtPassword, constraints);

        constraints.gridy = 4;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(12, 2, 2, 2);
        kartu.add(btnLogin, constraints);

        constraints.gridy = 5;
        constraints.insets = new Insets(8, 2, 2, 2);
        JLabel petunjuk = new JLabel("Akun demo: budi / admin123", SwingConstants.CENTER);
        kartu.add(petunjuk, constraints);

        JPanel isi = new JPanel(new BorderLayout());
        isi.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        isi.add(kartu, BorderLayout.CENTER);
        setContentPane(isi);
        pack();
        setLocationRelativeTo(null);
    }

    public String getUsername() {
        return txtUsername.getText().trim();
    }

    public String getPassword() {
        return new String(txtPassword.getPassword());
    }

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JTextField getTxtPassword() {
        return txtPassword;
    }
}
