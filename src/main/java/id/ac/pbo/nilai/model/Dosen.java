package id.ac.pbo.nilai.model;

import id.ac.pbo.nilai.exception.ValidasiException;

/**
 * Data dosen sekaligus akun pengguna aplikasi.
 */
public class Dosen implements Validatable {

    private int id;
    private String nama;
    private String username;
    private String passwordHash;

    public Dosen() {
    }

    public Dosen(int id, String nama, String username, String passwordHash) {
        this.id = id;
        setNama(nama);
        setUsername(username);
        setPasswordHash(passwordHash);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("Id dosen tidak boleh negatif.");
        }
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if (nama == null || nama.isBlank()) {
            throw new IllegalArgumentException("Nama dosen wajib diisi.");
        }
        String bersih = nama.trim();
        if (bersih.length() < 2 || bersih.length() > 100) {
            throw new IllegalArgumentException("Nama dosen harus 2-100 karakter.");
        }
        this.nama = bersih;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username wajib diisi.");
        }
        String bersih = username.trim().toLowerCase();
        if (!bersih.matches("[a-z0-9._]{3,30}")) {
            throw new IllegalArgumentException("Username harus 3-30 karakter (huruf kecil, angka, titik, garis bawah).");
        }
        this.username = bersih;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("Password hash wajib diisi.");
        }
        this.passwordHash = passwordHash;
    }

    @Override
    public void validasi() throws ValidasiException {
        try {
            setNama(nama);
            setUsername(username);
            setPasswordHash(passwordHash);
        } catch (IllegalArgumentException e) {
            throw new ValidasiException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return nama == null ? "" : nama;
    }
}
