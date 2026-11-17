package id.ac.pbo.nilai;

import id.ac.pbo.nilai.controller.LoginController;
import id.ac.pbo.nilai.dao.Database;
import id.ac.pbo.nilai.exception.DataAccessException;
import id.ac.pbo.nilai.view.LoginFrame;
import id.ac.pbo.nilai.view.UIUtil;

import javax.swing.SwingUtilities;

/**
 * Titik masuk aplikasi.
 */
public final class App {

    private App() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UIUtil.pasangLookAndFeel();
            try {
                Database.inisialisasi();
            } catch (DataAccessException e) {
                UIUtil.showError(null, "Aplikasi gagal menyiapkan basis data.\n" + e.getMessage());
                return;
            }
            LoginFrame login = new LoginFrame();
            new LoginController(login);
            login.setVisible(true);
        });
    }
}
