package id.ac.pbo.nilai.view.table;

import id.ac.pbo.nilai.model.Mahasiswa;
import id.ac.pbo.nilai.view.UIUtil;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaTableModel extends AbstractTableModel {

    private final String[] kolom = {"No", "NIM", "Nama", "Jenis Kelamin", "Tanggal Lahir", "Angkatan"};
    private final List<Mahasiswa> data = new ArrayList<>();

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
        Mahasiswa m = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> rowIndex + 1;
            case 1 -> m.getNim();
            case 2 -> m.getNama();
            case 3 -> "L".equalsIgnoreCase(m.getJenisKelamin()) ? "Laki-laki" : "Perempuan";
            case 4 -> UIUtil.formatTanggal(m.getTanggalLahir());
            case 5 -> m.getAngkatan();
            default -> null;
        };
    }

    public void setData(List<Mahasiswa> data) {
        this.data.clear();
        if (data != null) {
            this.data.addAll(data);
        }
        fireTableDataChanged();
    }

    public Mahasiswa getBaris(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < data.size()) {
            return data.get(rowIndex);
        }
        return null;
    }
}
