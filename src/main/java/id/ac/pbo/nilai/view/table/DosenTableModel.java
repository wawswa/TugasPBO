package id.ac.pbo.nilai.view.table;

import id.ac.pbo.nilai.model.Dosen;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class DosenTableModel extends AbstractTableModel {

    private final String[] kolom = {"No", "Nama Dosen", "Username", "Peran (Role)"};
    private final List<Dosen> data = new ArrayList<>();

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
        Dosen d = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> rowIndex + 1;
            case 1 -> d.getNama();
            case 2 -> d.getUsername();
            case 3 -> d.isAdmin() ? "ADMIN" : "DOSEN";
            default -> null;
        };
    }

    public void setData(List<Dosen> data) {
        this.data.clear();
        if (data != null) {
            this.data.addAll(data);
        }
        fireTableDataChanged();
    }

    public Dosen getBaris(int row) {
        if (row >= 0 && row < data.size()) {
            return data.get(row);
        }
        return null;
    }
}
