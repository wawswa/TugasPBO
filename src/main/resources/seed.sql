-- Akun demo admin tunggal dan dosen
-- Password semua akun demo: admin123
INSERT INTO dosen (id, nama, username, password_hash, role) VALUES
(1, 'Dr. Budi Santoso, M.Kom.', 'budi', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'DOSEN'),
(2, 'Siti Rahma, M.Sc.', 'siti', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'DOSEN'),
(3, 'Administrator', 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN');

INSERT INTO mahasiswa (id, nim, nama, jenis_kelamin, tanggal_lahir, angkatan) VALUES
(1, '221001', 'Andi Pratama', 'L', '2003-05-14', 2022),
(2, '221002', 'Budi Wijaya', 'L', '2003-09-02', 2022),
(3, '221003', 'Citra Lestari', 'P', '2004-01-21', 2022),
(4, '221004', 'Dewi Anggraini', 'P', '2003-12-11', 2022),
(5, '221005', 'Eko Saputra', 'L', '2003-07-30', 2022),
(6, '231001', 'Fajar Nugroho', 'L', '2004-03-17', 2023),
(7, '231002', 'Gita Permata', 'P', '2004-06-25', 2023),
(8, '231003', 'Hana Safitri', 'P', '2004-10-08', 2023);

INSERT INTO mata_kuliah (id, kode, nama, sks, dosen_id) VALUES
(1, 'IF101', 'Pemrograman Berorientasi Objek', 3, 1),
(2, 'IF102', 'Struktur Data', 3, 1),
(3, 'IF201', 'Basis Data', 2, 2);

INSERT INTO komponen (id, mata_kuliah_id, kode, nama, jenis, bobot) VALUES
(1, 1, 'T01', 'Tugas 1', 'TUGAS', 20),
(2, 1, 'T02', 'Tugas 2', 'TUGAS', 20),
(3, 1, 'K01', 'Kuis 1', 'KUIS', 20),
(4, 1, 'U01', 'Ujian Akhir', 'UJIAN', 40),
(5, 2, 'T01', 'Tugas 1', 'TUGAS', 20),
(6, 2, 'T02', 'Tugas 2', 'TUGAS', 20),
(7, 2, 'K01', 'Kuis 1', 'KUIS', 20),
(8, 2, 'U01', 'Ujian Akhir', 'UJIAN', 40),
(9, 3, 'T01', 'Tugas 1', 'TUGAS', 25),
(10, 3, 'K01', 'Kuis 1', 'KUIS', 25),
(11, 3, 'U01', 'Ujian Akhir', 'UJIAN', 50);

INSERT INTO nilai (mahasiswa_id, komponen_id, nilai, tanggal_input) VALUES
(1, 1, 85, '2026-09-20'),
(1, 2, 90, '2026-10-18'),
(1, 3, 80, '2026-10-05'),
(1, 3, 95, '2026-10-25'),
(1, 4, 78, '2026-11-10'),
(2, 1, 70, '2026-09-20'),
(2, 2, 75, '2026-10-18'),
(2, 3, 60, '2026-10-05'),
(2, 3, 88, '2026-10-25'),
(2, 4, 35, '2026-11-10'),
(3, 1, 92, '2026-09-20'),
(3, 2, 95, '2026-10-18'),
(3, 3, 90, '2026-10-05'),
(3, 4, 88, '2026-11-10'),
(4, 1, 78, '2026-09-20'),
(4, 2, 80, '2026-10-18'),
(4, 3, 85, '2026-10-05'),
(4, 4, 72, '2026-11-10'),
(5, 1, 60, '2026-09-20'),
(1, 5, 88, '2026-09-22'),
(1, 6, 91, '2026-10-20'),
(1, 7, 79, '2026-10-06'),
(1, 8, 85, '2026-11-11'),
(3, 5, 90, '2026-09-22'),
(3, 6, 93, '2026-10-20'),
(3, 7, 86, '2026-10-06'),
(3, 8, 92, '2026-11-11'),
(2, 9, 82, '2026-09-25'),
(2, 10, 78, '2026-10-08'),
(2, 11, 80, '2026-11-12'),
(4, 9, 90, '2026-09-25'),
(4, 10, 88, '2026-10-08'),
(4, 11, 91, '2026-11-12');
