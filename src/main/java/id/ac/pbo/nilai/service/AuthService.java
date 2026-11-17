package id.ac.pbo.nilai.service;

import id.ac.pbo.nilai.dao.DosenDAO;
import id.ac.pbo.nilai.exception.ValidasiException;
import id.ac.pbo.nilai.model.Dosen;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Autentikasi dosen. Password disimpan sebagai hash SHA-256.
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

    public static String sha256(String teks) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(teks.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritma SHA-256 tidak tersedia.", e);
        }
    }
}
