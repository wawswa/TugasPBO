package id.ac.pbo.nilai.dao;

import id.ac.pbo.nilai.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Kerangka DAO generik. Seluruh operasi basis data memakai try-with-resources dan
 * membungkus SQLException menjadi DataAccessException.
 *
 * @param <T> tipe entitas yang dikelola
 */
public abstract class BaseDAO<T> {

    @FunctionalInterface
    public interface Pemetaan<R> {
        R petakan(ResultSet resultSet) throws SQLException;
    }

    @FunctionalInterface
    public interface PembacaBaris {
        void baca(ResultSet resultSet) throws SQLException;
    }

    public abstract List<T> findAll();

    public abstract T findById(int id);

    public abstract void insert(T entity);

    public abstract void update(T entity);

    public abstract void delete(int id);

    protected <R> List<R> ambilBanyak(String sql, Pemetaan<R> pemetaan, Object... parameter) {
        try (Connection koneksi = Database.getConnection();
             PreparedStatement statement = koneksi.prepareStatement(sql)) {
            isiParameter(statement, parameter);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<R> hasil = new ArrayList<>();
                while (resultSet.next()) {
                    hasil.add(pemetaan.petakan(resultSet));
                }
                return hasil;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal membaca data: " + e.getMessage(), e);
        }
    }

    protected <R> R ambilSatu(String sql, Pemetaan<R> pemetaan, Object... parameter) {
        List<R> hasil = ambilBanyak(sql, pemetaan, parameter);
        return hasil.isEmpty() ? null : hasil.get(0);
    }

    protected void bacaBaris(String sql, PembacaBaris pembaca, Object... parameter) {
        try (Connection koneksi = Database.getConnection();
             PreparedStatement statement = koneksi.prepareStatement(sql)) {
            isiParameter(statement, parameter);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    pembaca.baca(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Gagal membaca data: " + e.getMessage(), e);
        }
    }

    protected int ubah(String sql, Object... parameter) {
        try (Connection koneksi = Database.getConnection();
             PreparedStatement statement = koneksi.prepareStatement(sql)) {
            isiParameter(statement, parameter);
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Gagal menyimpan perubahan: " + e.getMessage(), e);
        }
    }

    protected void pastikanTerpengaruh(int jumlahBaris, String pesan) {
        if (jumlahBaris == 0) {
            throw new DataAccessException(pesan);
        }
    }

    private void isiParameter(PreparedStatement statement, Object... parameter) throws SQLException {
        for (int i = 0; i < parameter.length; i++) {
            Object nilai = parameter[i];
            if (nilai instanceof LocalDate tanggal) {
                statement.setString(i + 1, tanggal.toString());
            } else {
                statement.setObject(i + 1, nilai);
            }
        }
    }
}
