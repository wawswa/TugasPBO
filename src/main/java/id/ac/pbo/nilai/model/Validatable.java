package id.ac.pbo.nilai.model;

import id.ac.pbo.nilai.exception.ValidasiException;

/**
 * Interface yang diimplementasikan lebih dari satu kelas model.
 */
public interface Validatable {

    /**
     * Memeriksa seluruh aturan validasi objek.
     *
     * @throws ValidasiException bila ada data yang tidak valid
     */
    void validasi() throws ValidasiException;
}
