# Sistem Inventaris Alat Kantor

## Deskripsi Singkat
Sistem Inventaris Alat Kantor adalah aplikasi desktop berbasis **Java Swing**
yang digunakan untuk mengelola data inventaris alat kantor, meliputi pencatatan
barang, pemantauan stok, pencatatan aktivitas pengguna, serta ekspor laporan
ke format Excel.

---

## ✨ Fitur Aplikasi

1. **Login & Register User**
    - Autentikasi pengguna untuk mengamankan akses aplikasi
    - Tersedia akun default (admin)

2. **Manajemen Inventaris (CRUD Barang)**
    - Menambah data barang
    - Melihat daftar barang
    - Mengubah data barang
    - Menghapus data barang

3. **Dashboard**
    - Menampilkan ringkasan dan statistik inventaris
    - Informasi total barang, kondisi, dan kategori

4. **Pencarian & Filter**
    - Pencarian data inventaris secara real-time

5. **Log History Aktivitas**
    - Mencatat aktivitas pengguna seperti login, tambah, edit, dan hapus data

6. **Export Data ke Excel**
    - Mengunduh data inventaris ke file `.xlsx` menggunakan Apache POI

7. **Antarmuka Modern**
    - Tampilan GUI bersih (Clean UI) berbasis Java Swing

---

## 🛠️ Teknologi yang Digunakan
- Bahasa Pemrograman : Java (JDK 17+)
- GUI Framework : Java Swing
- Build Tool : Maven
- Library Eksternal : Apache POI
- Penyimpanan Data : File lokal (`.csv`, `.txt`, `.xlsx`)

---

## 🚀 Cara Menjalankan Program

### 1. Persiapan
Pastikan telah terinstall:
- Java Development Kit (JDK) versi 17 atau lebih tinggi
- IDE (IntelliJ IDEA / NetBeans / Eclipse)

### 2. Menjalankan Aplikasi
1. Clone atau download repository project
2. Buka project menggunakan IDE
3. Pastikan struktur package `org.example` sudah benar
4. Jika menggunakan Maven, pastikan dependency berhasil terunduh
5. Buka file `Main.java`
6. Jalankan program dengan memilih **Run `Main.main()`**

### 3. Login Awal
Gunakan akun default berikut:
- **Username** : admin
- **Password** : admin123

---

## 📁 Penyimpanan Data
Data aplikasi disimpan secara lokal pada folder `data` yang akan dibuat
secara otomatis saat program dijalankan, meliputi:
- Data user (`users.csv`)
- Data inventaris (`inventaris.csv`)
- Log aktivitas (`history.txt`)
- File hasil ekspor Excel (`inventaris.xlsx`)

---

## 🔄 Version Control
Project ini menggunakan **Git** dengan branching untuk setiap fitur,
kemudian di-merge ke branch utama (`main`).
