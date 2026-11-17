package id.ac.pbo.nilai.view.table;

import id.ac.pbo.nilai.model.MataKuliah;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MataKuliahTableModel extends AbstractTableModel {

    private final String[] kolom = {"No", "Kode", "Nama Mata Kuliah", "SKS", "Dosen Pengampu"};
    private final List<MataKuliah> data = new ArrayList<>();

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
        MataKuliah mk = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> rowIndex + 1;
            case 1 -> mk.getKode();
            case 2 -> mk.getNama();
            case 3 -> mk.getSks();
            case 4 -> mk.getDosenNama() != null ? mk.getDosenNama() : "-";
            default -> null;
        };
    }

    public void setData(List<MataKuliah> data) {
        this.data.clear();
        if (data != null) {
            this.data.addAll(data);
        }
        fireTableDataChanged();
    }

    public MataKuliah getBaris(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < data.size()) {
            return data.get(rowIndex);
        }
        return null;
    }
}
