# Image Compression with QuadTree

## Author
1. Dita Maheswari 13523125
2. Muhammad Aulia Azka 13523137

## Penjelasan Singkat Program

Program ini merupakan pengaplikasian kommpresi gambar dengan metode QuadTree dan menggunakan algoritma devide dan conquer sebagai algoritma utama. Program ini membagi gambar menjadi blok-blok berdasarkan tingkat kesamaan warna, dengan blok yang lebih homogen direpresentasikan sebagai satu warna rata-rata. Hal ini memungkinkan kompresi gambar dengan mempertahankan detail pada area kompleks sambil menyederhanakan area yang homogen.

## Requirement Program
Untuk menjalankan program ini, Anda memerlukan:
1. Java Develpoment Kit (JDK) versi 8 atau lebih tinggi
2. Java Runtime Environment (JRE)
3. Sistem operasi yang mendukung Java, seperti windows, macOS, dan linux.

## Cara compile program
1. Pastikan semua file Java berada dalam struktur direktori yang sesuai dengan package-nya:
   - `ImageProcessing/ImageProcessing.java`
   - `QuadTree/QuadTreeNode.java`
   - `QuadTree/QuadTreeBuilder.java`
   - `BlockDivision/BlockDivision.java`
   - `ErrorMethods/ErrorMethods.java`
   - `ColorNormalizer/ColorNormalizer.java`
   - `OutputImage/OutputImage.java`
   - `Main.java` (terpisah di src) 

2. Buka terminal atau command prompt dan navigasikan ke direktori root project (direktori yang berisi folder-folder package).

3. Kompilasi semua file Java dengan perintah:
```
javac Main.java ImageProcessing/*.java QuadTree/*.java BlockDivision/*.java ErrorMethods/*.java ColorNormalizer/*.java OutputImage/*.java
```

Setelah program berhasil dikompilasi, jalankan program dengan perintah:
```
java Main
```

4. Masukkan input sesuai yang diminta oleh program

## Struktur Program
Program terdiri dari beberapa komponen utama:
1. **ImageProcessing**: Menangani representasi dan manipulasi piksel gambar.
2. **QuadTreeNode**: Menyimpan informasi tentang node dalam quadtree (posisi, ukuran, warna, dan child nodes).
3. **QuadTreeBuilder**: Membangun struktur quadtree dari gambar input.
4. **BlockDivision**: Menentukan kapan dan bagaimana blok gambar harus dibagi.
5. **ErrorMethods**: Menyediakan berbagai metode untuk menghitung error/heterogenitas blok.
6. **ColorNormalizer**: Menghitung warna rata-rata untuk setiap blok.
7. **OutputImage**: Merekonstruksi dan menyimpan gambar hasil kompresi.
8. **Main**: Menangani input pengguna dan mengkoordinasikan alur program.

Program ini mengimplementasikan algoritma divide and conquer melalui struktur data Quadtree untuk kompresi gambar. Berikut adalah langkah-langkahnya:

### 1. Divide (Pembagian)
- Gambar dibagi menjadi empat kuadran yang sama besar (kiri atas, kanan atas, kiri bawah, kanan bawah).
- Untuk setiap kuadran, program menghitung nilai error menggunakan metode yang dipilih (Mean Absolute Deviation, Max Pixel Difference, Entropy, atau Variance).
- Jika nilai error melebihi threshold yang ditentukan dan ukuran blok masih di atas ukuran minimum, blok tersebut akan dibagi lagi menjadi empat sub-blok.
- Proses pembagian dilakukan secara rekursif hingga salah satu kondisi penghentian terpenuhi.

### 2. Conquer (Penyelesaian)
- Ketika blok tidak perlu dibagi lagi (nilai error di bawah threshold atau ukuran blok sudah minimum), blok tersebut menjadi leaf node dalam quadtree.
- Untuk setiap leaf node, warna rata-rata dari piksel-piksel dalam blok dihitung dan disimpan sebagai representasi dari seluruh blok.

### 3. Combine (Penggabungan)
- Setelah quadtree selesai dibangun, gambar direkonstruksi dengan mengisi setiap blok (leaf node) dengan warna rata-rata yang telah dihitung.
- Hasil akhirnya adalah gambar yang telah dikompresi, di mana area dengan detail tinggi (heterogen) direpresentasikan dengan blok-blok kecil, sedangkan area homogen direpresentasikan dengan blok-blok besar.
