package id.ac.pbo.nilai.view.table;

import id.ac.pbo.nilai.model.KomponenPenilaian;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KomponenTableModel extends AbstractTableModel {

    private final String[] kolom = {"No", "Kode", "Nama Komponen", "Jenis", "Bobot (%)"};
    private final List<KomponenPenilaian> data = new ArrayList<>();

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return kolom.length;
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        KomponenPenilaian k = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> rowIndex + 1;
            case 1 -> k.getKode();
            case 2 -> k.getNama();
            case 3 -> k.jenis();
            case 4 -> String.format(Locale.ROOT, "%.1f%%", k.getBobot());
            default -> null;
        };
    }

    public void setData(List<KomponenPenilaian> data) {
        this.data.clear();
        if (data != null) {
            this.data.addAll(data);
        }
        fireTableDataChanged();
    }

    public KomponenPenilaian getBaris(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < data.size()) {
            return data.get(rowIndex);
        }
        return null;
    }
}
