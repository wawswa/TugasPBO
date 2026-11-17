package id.ac.pbo.nilai;

import id.ac.pbo.nilai.dao.Database;
import id.ac.pbo.nilai.dao.DosenDAO;
import id.ac.pbo.nilai.exception.DataAccessException;
import id.ac.pbo.nilai.exception.ValidasiException;
import id.ac.pbo.nilai.model.Dosen;
import id.ac.pbo.nilai.service.AuthService;

import java.io.Console;

/**
 * Utilitas command-line untuk menambah akun dosen.
 *
 * <p>Pemakaian:
 * {@code java -cp penilaian-akademik-1.0.0.jar id.ac.pbo.nilai.TambahDosen "Nama" username [password]}</p>
 *
 * <p>Bila password tidak diberikan sebagai argumen, password diminta lewat console
 * agar tidak tersimpan di riwayat shell.</p>
 */
public final class TambahDosen {

    private TambahDosen() {
    }

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.println("Pemakaian: java -cp target/penilaian-akademik-1.0.0.jar "
                    + "id.ac.pbo.nilai.TambahDosen \"Nama Dosen\" username [password]");
            System.exit(1);
        }

        String nama = args[0];
        String username = args[1];
        try {
            Database.inisialisasi();
            String password = args.length == 3 ? args[2] : mintaPassword();
            DosenDAO dosenDAO = new DosenDAO();
            Dosen admin = dosenDAO.findAdmin();
            if (admin == null) {
                admin = new Dosen(0, "Administrator", "admin", "", Dosen.ROLE_ADMIN);
            }

            AuthService authService = new AuthService(dosenDAO);
            Dosen dosen = authService.tambahDosen(admin, nama, username, password);
            System.out.println("Akun dosen '" + dosen.getUsername() + "' (" + dosen.getNama() + ") berhasil ditambahkan.");
        } catch (IllegalArgumentException | ValidasiException e) {
            System.err.println("Gagal: " + e.getMessage());
            System.exit(2);
        } catch (DataAccessException e) {
            System.err.println("Gagal mengakses basis data: " + e.getMessage());
            System.exit(3);
        }
    }

    private static String mintaPassword() {
        Console console = System.console();
        if (console == null) {
            System.err.println("Console tidak tersedia. Jalankan di terminal, "
                    + "atau sertakan password sebagai argumen ketiga.");
            System.exit(1);
        }
        char[] password = console.readPassword("Password akun baru: ");
        if (password == null || password.length == 0) {
            System.err.println("Gagal: password wajib diisi.");
            System.exit(2);
        }
        return new String(password);
    }
}
