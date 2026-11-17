-- Skema basis data Sistem Penilaian Akademik
CREATE TABLE IF NOT EXISTS dosen (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nama TEXT NOT NULL,
    username TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS mahasiswa (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nim TEXT NOT NULL UNIQUE,
    nama TEXT NOT NULL,
    jenis_kelamin TEXT NOT NULL CHECK (jenis_kelamin IN ('L', 'P')),
    tanggal_lahir TEXT NOT NULL,
    angkatan INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS mata_kuliah (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    kode TEXT NOT NULL UNIQUE,
    nama TEXT NOT NULL,
    sks INTEGER NOT NULL CHECK (sks BETWEEN 1 AND 6),
    dosen_id INTEGER NOT NULL REFERENCES dosen (id)
);

CREATE TABLE IF NOT EXISTS komponen (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    mata_kuliah_id INTEGER NOT NULL REFERENCES mata_kuliah (id) ON DELETE CASCADE,
    kode TEXT NOT NULL,
    nama TEXT NOT NULL,
    jenis TEXT NOT NULL CHECK (jenis IN ('TUGAS', 'KUIS', 'UJIAN')),
    bobot REAL NOT NULL CHECK (bobot >= 0 AND bobot <= 100),
    UNIQUE (mata_kuliah_id, kode)
);

CREATE TABLE IF NOT EXISTS nilai (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    mahasiswa_id INTEGER NOT NULL REFERENCES mahasiswa (id) ON DELETE CASCADE,
    komponen_id INTEGER NOT NULL REFERENCES komponen (id) ON DELETE CASCADE,
    nilai REAL NOT NULL CHECK (nilai >= 0 AND nilai <= 100),
    tanggal_input TEXT NOT NULL
);
