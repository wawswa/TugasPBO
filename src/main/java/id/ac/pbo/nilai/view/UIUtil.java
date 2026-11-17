package id.ac.pbo.nilai.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerDateModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 * Kumpulan helper tampilan agar layout, dialog, dan komponen konsisten
 * di seluruh halaman.
 */
public final class UIUtil {

    private static final DateTimeFormatter FORMAT_TANGGAL = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private UIUtil() {
    }

    public static void pasangLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
            // Look and feel bawaan tetap dipakai bila Nimbus tidak tersedia.
        }
    }

    public static void showError(Component parent, String pesan) {
        JOptionPane.showMessageDialog(parent, pesan, "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(Component parent, String pesan) {
        JOptionPane.showMessageDialog(parent, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean confirmDelete(Component parent, String namaData) {
        int jawaban = JOptionPane.showConfirmDialog(parent,
                "Hapus " + namaData + "?\nTindakan ini tidak dapat dibatalkan.",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return jawaban == JOptionPane.YES_OPTION;
    }

    public static JPanel formPanel(String judul) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(judul));
        return panel;
    }

    public static void addFormRow(JPanel form, int baris, String label, JComponent field) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = baris;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(4, 6, 4, 6);
        form.add(new JLabel(label), constraints);

        constraints.gridx = 1;
        constraints.weightx = 1.0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        form.add(field, constraints);
    }

    public static JPanel buttonBar(JButton... tombol) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        for (JButton item : tombol) {
            panel.add(item);
        }
        return panel;
    }

    public static JPanel searchBar(String label, JTextField field, JButton... tombol) {
        JPanel panel = new JPanel(new BorderLayout(6, 0));
        JPanel isi = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        isi.add(new JLabel(label));
        field.setColumns(18);
        isi.add(field);
        for (JButton item : tombol) {
            isi.add(item);
        }
        panel.add(isi, BorderLayout.WEST);
        return panel;
    }

    public static void siapkanTabel(JTable tabel) {
        tabel.setRowHeight(24);
        tabel.setFillsViewportHeight(true);
        tabel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabel.setAutoCreateRowSorter(true);
        tabel.getTableHeader().setReorderingAllowed(false);
    }

    public static JSpinner spinnerTanggal(LocalDate awal) {
        SpinnerDateModel model = new SpinnerDateModel(keDate(awal), null, null, Calendar.DAY_OF_MONTH);
        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd-MM-yyyy"));
        return spinner;
    }

    public static LocalDate tanggalDari(JSpinner spinner) {
        Date tanggal = (Date) spinner.getValue();
        return tanggal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date keDate(LocalDate tanggal) {
        return Date.from(tanggal.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static String formatTanggal(LocalDate tanggal) {
        return tanggal == null ? "-" : FORMAT_TANGGAL.format(tanggal);
    }
}
