package id.ac.pbo.nilai.view;

import id.ac.pbo.nilai.controller.KomponenController;
import id.ac.pbo.nilai.controller.LoginController;
import id.ac.pbo.nilai.controller.MahasiswaController;
import id.ac.pbo.nilai.controller.MataKuliahController;
import id.ac.pbo.nilai.controller.NilaiController;
import id.ac.pbo.nilai.controller.Penyegar;
import id.ac.pbo.nilai.controller.RekapController;
import id.ac.pbo.nilai.model.Dosen;
import id.ac.pbo.nilai.service.NilaiService;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Frame utama setelah login: menu bar, navigasi antar halaman, dan status bar.
 */
public class MainFrame extends JFrame {

    private final Dosen pengguna;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel konten = new JPanel(cardLayout);
    private final Map<String, Penyegar> penyegar = new LinkedHashMap<>();
    private final Map<String, String> judulHalaman = new LinkedHashMap<>();
    private final JLabel lblStatus = new JLabel();

    public MainFrame(Dosen pengguna) {
        super("Sistem Penilaian Akademik - " + pengguna.getNama());
        this.pengguna = pengguna;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1150, 760);
        setMinimumSize(new Dimension(980, 640));
        setLocationRelativeTo(null);
        setJMenuBar(buatMenuBar());

        konten.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        add(konten, BorderLayout.CENTER);
        add(buatStatusBar(), BorderLayout.SOUTH);

        daftarkanHalaman();
        tampilkan("mahasiswa");
    }

    private void daftarkanHalaman() {
        MahasiswaPanel mahasiswaPanel = new MahasiswaPanel();
        daftarkan("mahasiswa", "Data Mahasiswa", mahasiswaPanel,
                new MahasiswaController(mahasiswaPanel));

        MataKuliahPanel mataKuliahPanel = new MataKuliahPanel();
        daftarkan("mataKuliah", "Mata Kuliah", mataKuliahPanel,
                new MataKuliahController(mataKuliahPanel));

        KomponenPanel komponenPanel = new KomponenPanel();
        daftarkan("komponen", "Komponen Penilaian", komponenPanel,
                new KomponenController(komponenPanel));

        InputNilaiPanel nilaiPanel = new InputNilaiPanel();
        daftarkan("nilai", "Input Nilai", nilaiPanel,
                new NilaiController(nilaiPanel));

        RekapPanel rekapPanel = new RekapPanel();
        daftarkan("rekap", "Rekap Nilai", rekapPanel,
                new RekapController(rekapPanel, new NilaiService()));
    }

    private void daftarkan(String kunci, String judul, JPanel panel, Penyegar controller) {
        konten.add(panel, kunci);
        judulHalaman.put(kunci, judul);
        penyegar.put(kunci, controller);
    }

    /**
     * Menampilkan halaman dan menyegarkan datanya.
     */
    public void tampilkan(String kunci) {
        cardLayout.show(konten, kunci);
        Penyegar halaman = penyegar.get(kunci);
        if (halaman != null) {
            halaman.muatUlang();
        }
        lblStatus.setText("Login sebagai: " + pengguna.getNama()
                + "   |   Halaman: " + judulHalaman.getOrDefault(kunci, kunci));
    }

    private JMenuBar buatMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuData = new JMenu("Data");
        menuData.add(itemHalaman("Data Mahasiswa", "mahasiswa", KeyEvent.VK_1));
        menuData.add(itemHalaman("Mata Kuliah", "mataKuliah", KeyEvent.VK_2));
        menuData.add(itemHalaman("Komponen Penilaian", "komponen", KeyEvent.VK_3));
        menuData.add(itemHalaman("Input Nilai", "nilai", KeyEvent.VK_4));
        menuData.add(itemHalaman("Rekap Nilai", "rekap", KeyEvent.VK_5));
        menuBar.add(menuData);

        JMenu menuAkun = new JMenu("Akun");
        JMenuItem logout = new JMenuItem("Logout");
        logout.addActionListener(e -> logout());
        menuAkun.add(logout);
        JMenuItem keluar = new JMenuItem("Keluar Aplikasi");
        keluar.addActionListener(e -> keluarAplikasi());
        menuAkun.add(keluar);
        menuBar.add(menuAkun);

        JMenu menuBantuan = new JMenu("Bantuan");
        JMenuItem tentang = new JMenuItem("Tentang Aplikasi");
        tentang.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Sistem Penilaian Akademik\n"
                        + "Tugas Besar Pemrograman Berorientasi Objek\n\n"
                        + "Java Swing + SQLite + pola MVC\n"
                        + "Tema: Penilaian Akademik\n\n"
                        + "Kelompok 3 orang",
                "Tentang Aplikasi", JOptionPane.INFORMATION_MESSAGE));
        menuBantuan.add(tentang);
        menuBar.add(menuBantuan);

        return menuBar;
    }

    private JMenuItem itemHalaman(String teks, String kunci, int tombol) {
        JMenuItem item = new JMenuItem(teks);
        item.setAccelerator(KeyStroke.getKeyStroke(tombol, InputEvent.CTRL_DOWN_MASK));
        item.addActionListener(e -> tampilkan(kunci));
        return item;
    }

    private JPanel buatStatusBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        panel.add(lblStatus, BorderLayout.WEST);
        JLabel versi = new JLabel("Penilaian Akademik v1.0");
        panel.add(versi, BorderLayout.EAST);
        return panel;
    }

    private void logout() {
        int jawaban = JOptionPane.showConfirmDialog(this,
                "Keluar dari akun " + pengguna.getNama() + "?",
                "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
        if (jawaban != JOptionPane.YES_OPTION) {
            return;
        }
        dispose();
        LoginFrame login = new LoginFrame();
        new LoginController(login);
        login.setVisible(true);
    }

    private void keluarAplikasi() {
        int jawaban = JOptionPane.showConfirmDialog(this,
                "Tutup aplikasi?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (jawaban == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
