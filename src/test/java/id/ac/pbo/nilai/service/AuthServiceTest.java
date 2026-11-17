package id.ac.pbo.nilai.service;

import id.ac.pbo.nilai.dao.DosenDAO;
import id.ac.pbo.nilai.exception.ValidasiException;
import id.ac.pbo.nilai.model.Dosen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthServiceTest {

    private MemoryDosenDAO dao;
    private AuthService service;
    private Dosen admin;
    private Dosen regularDosen;

    @BeforeEach
    void setUp() {
        dao = new MemoryDosenDAO();
        service = new AuthService(dao);

        admin = new Dosen(1, "Administrator", "admin", AuthService.sha256("admin123"), Dosen.ROLE_ADMIN);
        dao.insert(admin);

        regularDosen = new Dosen(2, "Dr. Budi Santoso", "budi", AuthService.sha256("admin123"), Dosen.ROLE_DOSEN);
        dao.insert(regularDosen);
    }

    @Test
    void adminBisaMenambahAkunDosen() throws Exception {
        Dosen baru = service.tambahDosen(admin, "Prof. Joko Widodo", "joko", "rahasia123", "rahasia123");

        assertNotNull(baru);
        assertEquals("joko", baru.getUsername());
        assertEquals("Prof. Joko Widodo", baru.getNama());
        assertEquals(Dosen.ROLE_DOSEN, baru.getRole());
        assertFalse(baru.isAdmin());
    }

    @Test
    void bukanAdminDitolakSaatMenambahDosen() {
        ValidasiException ex = assertThrows(ValidasiException.class, () ->
                service.tambahDosen(regularDosen, "Dr. Baru", "dosenbaru", "password123"));

        assertTrue(ex.getMessage().contains("Hanya admin"));
    }

    @Test
    void pemanggilNullDitolakSaatMenambahDosen() {
        assertThrows(ValidasiException.class, () ->
                service.tambahDosen(null, "Dr. Baru", "dosenbaru", "password123"));
    }

    @Test
    void hanyaBolehAdaSatuAkunAdmin() {
        // Karena di setUp sudah ada 1 admin, inisialisasiAdmin harus ditolak
        ValidasiException ex = assertThrows(ValidasiException.class, () ->
                service.inisialisasiAdmin("Admin Kedua", "admin2", "password123"));

        assertTrue(ex.getMessage().contains("Hanya boleh ada 1 akun admin"));
    }

    @Test
    void usernameAdminDicadangkanUntukAdmin() {
        ValidasiException ex = assertThrows(ValidasiException.class, () ->
                service.tambahDosen(admin, "Dosen Admin", "admin", "password123"));

        assertTrue(ex.getMessage().contains("dicadangkan"));
    }

    @Test
    void usernameDuplikatDitolak() {
        ValidasiException ex = assertThrows(ValidasiException.class, () ->
                service.tambahDosen(admin, "Budi Duplikat", "budi", "password123"));

        assertTrue(ex.getMessage().contains("sudah digunakan"));
    }

    @Test
    void konfirmasiPasswordSalahDitolak() {
        ValidasiException ex = assertThrows(ValidasiException.class, () ->
                service.tambahDosen(admin, "Dosen Baru", "dosen1", "pass1234", "passBeda"));

        assertTrue(ex.getMessage().contains("Konfirmasi password"));
    }

    @Test
    void passwordKurangDariEnamKarakterDitolak() {
        ValidasiException ex = assertThrows(ValidasiException.class, () ->
                service.tambahDosen(admin, "Dosen Baru", "dosen2", "123"));

        assertTrue(ex.getMessage().contains("minimal 6"));
    }

    @Test
    void loginBerhasilUntukAdminDanDosen() throws Exception {
        Dosen loginAdmin = service.login("admin", "admin123");
        assertNotNull(loginAdmin);
        assertTrue(loginAdmin.isAdmin());

        Dosen loginDosen = service.login("budi", "admin123");
        assertNotNull(loginDosen);
        assertFalse(loginDosen.isAdmin());
    }

    @Test
    void loginGagalJikaPasswordSalah() {
        assertThrows(ValidasiException.class, () -> service.login("admin", "passwordsalah"));
    }

    /**
     * Implementasi in-memory untuk pengujian cepat tanpa database fisik.
     */
    private static class MemoryDosenDAO extends DosenDAO {
        private final List<Dosen> list = new ArrayList<>();
        private int autoId = 10;

        @Override
        public List<Dosen> findAll() {
            return new ArrayList<>(list);
        }

        @Override
        public Dosen findById(int id) {
            return list.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
        }

        @Override
        public Dosen findByUsername(String username) {
            if (username == null) return null;
            return list.stream()
                    .filter(d -> d.getUsername().equalsIgnoreCase(username.trim()))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public Dosen findAdmin() {
            return list.stream().filter(Dosen::isAdmin).findFirst().orElse(null);
        }

        @Override
        public int countAdmin() {
            return (int) list.stream().filter(Dosen::isAdmin).count();
        }

        @Override
        public boolean usernameSudahDipakai(String username, int kecualiId) {
            if (username == null) return false;
            return list.stream().anyMatch(d -> d.getId() != kecualiId && d.getUsername().equalsIgnoreCase(username.trim()));
        }

        @Override
        public void insert(Dosen dosen) {
            if (dosen.isAdmin() && countAdmin() >= 1) {
                throw new IllegalArgumentException("Hanya boleh ada 1 akun admin dalam sistem.");
            }
            if (dosen.getId() == 0) {
                dosen.setId(autoId++);
            }
            list.add(dosen);
        }
    }
}
