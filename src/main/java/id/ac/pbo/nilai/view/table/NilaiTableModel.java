package id.ac.pbo.nilai.view.table;

import id.ac.pbo.nilai.model.Nilai;
import id.ac.pbo.nilai.view.UIUtil;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NilaiTableModel extends AbstractTableModel {

    private static final String[] KOLOM = {"NIM", "Nama Mahasiswa", "Nilai", "Tanggal Input"};
    private List<Nilai> data = new ArrayList<>();

    public void setData(List<Nilai> data) {
        this.data = new ArrayList<>(data);
        fireTableDataChanged();
    }

    public Nilai getBaris(int baris) {
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
        Nilai nilai = data.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> nilai.getNim();
            case 1 -> nilai.getMahasiswaNama();
            case 2 -> String.format(Locale.US, "%.2f", nilai.getNilai());
            case 3 -> UIUtil.formatTanggal(nilai.getTanggalInput());
            default -> "";
        };
    }
}
