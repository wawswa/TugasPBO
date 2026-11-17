package id.ac.pbo.nilai.view.table;

import id.ac.pbo.nilai.model.RekapNilai;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RekapTableModel extends AbstractTableModel {

    private static final String[] KOLOM = {"NIM", "Nama Mahasiswa", "Nilai Akhir", "Huruf"};
    private List<RekapNilai> data = new ArrayList<>();

    public void setData(List<RekapNilai> data) {
        this.data = new ArrayList<>(data);
        fireTableDataChanged();
    }

    public RekapNilai getBaris(int baris) {
        return data.get(baris);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return KOLOM.length;
    }

    @Override
    public String getColumnName(int column) {
        return KOLOM[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        RekapNilai rekap = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> rekap.getMahasiswa().getNim();
            case 1 -> rekap.getMahasiswa().getNama();
            case 2 -> rekap.isAdaNilai()
                    ? String.format(Locale.US, "%.2f", rekap.getNilaiAkhir()) : "-";
            case 3 -> rekap.isAdaNilai() ? rekap.getNilaiHuruf() : "-";
            default -> "";
        };
    }
}
