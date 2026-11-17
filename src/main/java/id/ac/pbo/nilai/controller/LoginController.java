package id.ac.pbo.nilai.controller;

import id.ac.pbo.nilai.exception.DataAccessException;
import id.ac.pbo.nilai.exception.ValidasiException;
import id.ac.pbo.nilai.model.Dosen;
import id.ac.pbo.nilai.service.AuthService;
import id.ac.pbo.nilai.view.LoginFrame;
import id.ac.pbo.nilai.view.MainFrame;
import id.ac.pbo.nilai.view.UIUtil;

/**
 * Controller halaman login.
 */
public class LoginController {

    private final LoginFrame view;
    private final AuthService authService = new AuthService();

    public LoginController(LoginFrame view) {
        this.view = view;
        view.getBtnLogin().addActionListener(e -> login());
        view.getTxtPassword().addActionListener(e -> login());
        view.getRootPane().setDefaultButton(view.getBtnLogin());
    }

    private void login() {
        try {
            Dosen dosen = authService.login(view.getUsername(), view.getPassword());
            MainFrame utama = new MainFrame(dosen);
            utama.setVisible(true);
            view.dispose();
        } catch (ValidasiException e) {
            UIUtil.showError(view, e.getMessage());
        } catch (DataAccessException e) {
            UIUtil.showError(view, "Gagal mengakses basis data.\n" + e.getMessage());
        }
    }
}
