package id.ac.pbo.nilai.view;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;

public class KomponenPanel extends JPanel {

    public KomponenPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        JLabel label = new JLabel("Komponen Penilaian", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 18f));
        add(label, BorderLayout.CENTER);
    }
}
