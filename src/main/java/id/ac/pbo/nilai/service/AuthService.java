package id.ac.pbo.nilai.service;

import id.ac.pbo.nilai.dao.DosenDAO;
import id.ac.pbo.nilai.exception.ValidasiException;
import id.ac.pbo.nilai.model.Dosen;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Autentikasi dan manajemen akun dosen. Password disimpan sebagai hash SHA-256.
 * Sistem hanya memiliki 1 akun admin, dan hanya admin yang dapat menambah akun dosen.
 */
public class AuthService {

    private final DosenDAO dosenDAO;

    public AuthService() {
        this(new DosenDAO());
    }

    public AuthService(DosenDAO dosenDAO) {
        this.dosenDAO = dosenDAO;
    }

    public Dosen login(String username, String password) throws ValidasiException {
        if (username == null || username.isBlank()) {
            throw new ValidasiException("Username wajib diisi.");
        }
        if (password == null || password.isEmpty()) {
            throw new ValidasiException("Password wajib diisi.");
        }
        Dosen dosen = dosenDAO.findByUsername(username.trim().toLowerCase());
        if (dosen == null || !dosen.getPasswordHash().equals(sha256(password))) {
            throw new ValidasiException("Username atau password salah.");
        }
        return dosen;
    }

    /**
     * Memeriksa apakah akun admin sudah terdaftar dalam sistem.
     */
    public boolean adaAdmin() {
        return dosenDAO.countAdmin() > 0;
    }

    /**
     * Menambahkan akun dosen baru oleh admin.
     * Hanya pengguna dengan peran ADMIN yang dapat memanggil method ini.
     *
     * @param pemanggil Akun yang sedang login dan melakukan tindakan (harus admin).
     * @param nama      Nama lengkap dosen.
     * @param username  Username untuk dosen.
     * @param password  Password polos untuk dosen.
     * @return Dosen yang berhasil ditambahkan.
     * @throws ValidasiException Jika bukan admin, validasi gagal, atau username duplikat.
     */
    public Dosen tambahDosen(Dosen pemanggil, String nama, String username, String password)
            throws ValidasiException {
        if (pemanggil == null || !pemanggil.isAdmin()) {
            throw new ValidasiException("Hanya admin yang memiliki hak untuk menambah akun dosen.");
        }
        return daftarkanDosenInternal(nama, username, password);
    }

    /**
     * Menambahkan akun dosen baru oleh admin dengan verifikasi konfirmasi password.
     */
    public Dosen tambahDosen(Dosen pemanggil, String nama, String username, String password,
                             String konfirmasiPassword) throws ValidasiException {
        if (pemanggil == null || !pemanggil.isAdmin()) {
            throw new ValidasiException("Hanya admin yang memiliki hak untuk menambah akun dosen.");
        }
        if (password == null || !password.equals(konfirmasiPassword)) {
            throw new ValidasiException("Konfirmasi password tidak cocok.");
        }
        return daftarkanDosenInternal(nama, username, password);
    }

    /**
     * Mendaftarkan akun admin pertama dan satu-satunya.
     * Ditolak jika akun admin sudah ada dalam sistem.
     */
    public Dosen inisialisasiAdmin(String nama, String username, String password) throws ValidasiException {
        if (dosenDAO.countAdmin() >= 1) {
            throw new ValidasiException("Hanya boleh ada 1 akun admin dalam sistem.");
        }
        if (password == null || password.isBlank()) {
            throw new ValidasiException("Password admin wajib diisi.");
        }
        if (password.length() < 6) {
            throw new ValidasiException("Password minimal 6 karakter.");
        }
        String usernameBersih = username != null ? username.trim().toLowerCase() : "admin";
        Dosen admin = buatDosen(0, nama, usernameBersih, sha256(password), Dosen.ROLE_ADMIN);
        dosenDAO.insert(admin);
        return dosenDAO.findByUsername(admin.getUsername());
    }

    private Dosen daftarkanDosenInternal(String nama, String username, String password) throws ValidasiException {
        if (password == null || password.isBlank()) {
            throw new ValidasiException("Password wajib diisi.");
        }
        if (password.length() < 6) {
            throw new ValidasiException("Password minimal 6 karakter.");
        }

        String usernameBersih = username != null ? username.trim().toLowerCase() : "";
        if ("admin".equals(usernameBersih)) {
            throw new ValidasiException("Username 'admin' dicadangkan untuk akun admin tunggal.");
        }

        if (dosenDAO.findByUsername(usernameBersih) != null) {
            throw new ValidasiException("Username '" + usernameBersih + "' sudah digunakan.");
        }

        Dosen dosen = buatDosen(0, nama, usernameBersih, sha256(password), Dosen.ROLE_DOSEN);

        dosenDAO.insert(dosen);
        return dosenDAO.findByUsername(usernameBersih);
    }

    /**
     * Membuat entitas dosen sekaligus menerjemahkan pelanggaran validasi model
     * menjadi {@link ValidasiException} agar dapat ditampilkan oleh lapisan GUI.
     */
    private static Dosen buatDosen(int id, String nama, String username, String passwordHash, String role)
            throws ValidasiException {
        try {
            return new Dosen(id, nama, username, passwordHash, role);
        } catch (IllegalArgumentException e) {
            throw new ValidasiException(e.getMessage(), e);
        }
    }

    public static String sha256(String teks) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(teks.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritma SHA-256 tidak tersedia.", e);
        }
    }
}
