package id.ac.pbo.nilai.dao;

import id.ac.pbo.nilai.exception.DataAccessException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Pengelola koneksi SQLite. Skema dan data demo dimuat otomatis saat pertama kali.
 * Lokasi basis data dapat diubah dengan properti sistem {@code penilaian.db}.
 */
public final class Database {

    private static final String LOKASI = System.getProperty("penilaian.db", "data/penilaian.db");
    private static final String URL = "jdbc:sqlite:" + LOKASI;
    private static boolean siap;

    private Database() {
    }

    public static synchronized void inisialisasi() {
        if (siap) {
            return;
        }
        try {
            Path path = Path.of(LOKASI).toAbsolutePath();
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Class.forName("org.sqlite.JDBC");
            try (Connection koneksi = DriverManager.getConnection(URL)) {
                aktifkanForeignKeys(koneksi);
                jalankanSkrip(koneksi, "/schema.sql");
                if (hitungBaris(koneksi, "dosen") == 0) {
                    jalankanSkrip(koneksi, "/seed.sql");
                } else {
                    migrasikanAkunAdmin(koneksi);
                }
            }
            siap = true;
        } catch (IOException e) {
            throw new DataAccessException("Gagal menyiapkan folder basis data: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new DataAccessException("Driver SQLite tidak ditemukan di classpath.", e);
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menyiapkan basis data: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() {
        inisialisasi();
        try {
            Connection koneksi = DriverManager.getConnection(URL);
            aktifkanForeignKeys(koneksi);
            return koneksi;
        } catch (SQLException e) {
            throw new DataAccessException("Gagal membuka koneksi basis data: " + e.getMessage(), e);
        }
    }

    private static void aktifkanForeignKeys(Connection koneksi) throws SQLException {
        try (Statement statement = koneksi.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
    }

    private static void migrasikanAkunAdmin(Connection koneksi) {
        try (Statement statement = koneksi.createStatement()) {
            statement.execute("ALTER TABLE dosen ADD COLUMN role TEXT NOT NULL DEFAULT 'DOSEN'");
        } catch (SQLException ignored) {
            // Kolom role sudah ada
        }

        try (Statement statement = koneksi.createStatement();
             ResultSet rs = statement.executeQuery(
                     "SELECT COUNT(*) FROM dosen WHERE UPPER(role) = 'ADMIN' OR LOWER(username) = 'admin'")) {
            if (rs.next() && rs.getInt(1) == 0) {
                try (var ps = koneksi.prepareStatement(
                        "INSERT INTO dosen (nama, username, password_hash, role) VALUES (?, ?, ?, ?)")) {
                    ps.setString(1, "Administrator");
                    ps.setString(2, "admin");
                    ps.setString(3, "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9");
                    ps.setString(4, "ADMIN");
                    ps.executeUpdate();
                }
            }
        } catch (SQLException ignored) {
        }
    }

    private static int hitungBaris(Connection koneksi, String tabel) throws SQLException {
        try (Statement statement = koneksi.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM " + tabel)) {
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    private static void jalankanSkrip(Connection koneksi, String resource) throws IOException, SQLException {
        String skrip = bacaResource(resource);
        StringBuilder tanpaKomentar = new StringBuilder();
        for (String baris : skrip.split("\\R")) {
            if (!baris.trim().startsWith("--")) {
                tanpaKomentar.append(baris).append('\n');
            }
        }
        for (String sql : tanpaKomentar.toString().split(";")) {
            if (sql.isBlank()) {
                continue;
            }
            try (Statement statement = koneksi.createStatement()) {
                statement.execute(sql);
            }
        }
    }

    private static String bacaResource(String nama) throws IOException {
        try (InputStream input = Database.class.getResourceAsStream(nama)) {
            if (input == null) {
                throw new IOException("Resource tidak ditemukan: " + nama);
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
