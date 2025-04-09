import BlockDivision.BlockDivision;
import ErrorMethods.ErrorMethods.ErrorMethodType;
import OutputImage.OutputImage;
import QuadTree.QuadTreeBuilder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            // 1. Input alamat gambar asli
            System.out.print("Masukkan alamat absolut gambar yang akan dikompresi (jpg, png, atau jpeg): ");
            String inputPath = scanner.nextLine();
            File inputFile = new File(inputPath);
            if (!inputFile.exists()) {
                System.err.println("File tidak ditemukan: " + inputPath);
                return;
            }
            BufferedImage originalImage = ImageIO.read(inputFile);

            // 2. Pilih metode perhitungan error
            int methodChoice = 0;
            boolean validMethodChoice = false;
            while (!validMethodChoice) {
                try {
                    System.out.println("Pilih metode perhitungan error:");
                    System.out.println("1. Mean Absolute Deviation (MAD)");
                    System.out.println("2. Max Pixel Difference");
                    System.out.println("3. Entropy");
                    System.out.println("4. Variance");
                    System.err.println("5. Structural Similarity Index (SSIM)");
                    System.err.println("Masukkan Pilihan (1 - 5): ");
                    methodChoice = Integer.parseInt(scanner.nextLine().trim());
                    if (methodChoice >= 1 && methodChoice <= 5) {
                        validMethodChoice = true;
                    } else {
                        System.out.println("Pilihan tidak valid. Silakan masukkan angka 1 - 5.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input tidak valid. Silakan masukkan angka 1 - 5.");
                }
            }

            // 3. Input ambang batas
            double threshold = 0;
            boolean validThreshold = false;
            while (!validThreshold) {
                try {
                    System.out.print("Masukkan ambang batas (sesuai dengan metode yang dipilih): ");
                    threshold = Double.parseDouble(scanner.nextLine().trim());
                    validThreshold = true;
                } catch (NumberFormatException e) {
                    System.out.println("Input tidak valid. Silakan masukkan nilai numerik.");
                }
            }

            // 4. Input ukuran blok minimum
            int minBlockSize = 0;
            boolean validBlockSize = false;
            while (!validBlockSize) {
                try {
                    System.out.print("Masukkan ukuran blok minimum: ");
                    minBlockSize = Integer.parseInt(scanner.nextLine().trim());
                    if (minBlockSize > 0) {
                        validBlockSize = true;
                    } else {
                        System.out.println("Ukuran blok harus lebih besar dari 0.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Input tidak valid. Silakan masukkan nilai numerik.");
                }
            }

            // // 5. Input target persentase kompresi
            // double targetCompression = 0;
            // boolean validCompression = false;
            // while (!validCompression) {
            //     try {
            //         System.out.print("Masukkan target persentase kompresi (0 untuk nonaktif): ");
            //         targetCompression = Double.parseDouble(scanner.nextLine().trim());
            //         if (targetCompression >= 0) {
            //             validCompression = true;
            //         } else {
            //             System.out.println("Persentase kompresi tidak boleh negatif.");
            //         }
            //     } catch (NumberFormatException e) {
            //         System.out.println("Input tidak valid. Silakan masukkan nilai numerik.");
            //     }
            // }

            // 5. Input alamat gambar hasil kompresi
            System.out.print("Masukkan alamat absolut gambar hasil kompresi (png, jpg, atau jpeg): ");
            String outputPath = scanner.nextLine().trim();

            // Waktu mulai eksekusi
            long startTime = System.nanoTime();

            // Proses konversi ke ImageProcessing
            // ImageProcessing imgProc = new ImageProcessing(originalImage);

            // Pilih metode error
            ErrorMethodType errorMethodType;
            // double computedThreshold = 0;
            
            switch (methodChoice) {
                case 1: 
                    errorMethodType = ErrorMethodType.MAD;
                    // computedThreshold = ErrorMethods.mad(imgProc); 
                    break;
                case 2: 
                    errorMethodType = ErrorMethodType.MAX_PIXEL_DIFF;
                    // computedThreshold = ErrorMethods.maxPixelDiff(imgProc); 
                    break;
                case 3: 
                    errorMethodType = ErrorMethodType.ENTROPY;
                    // computedThreshold = ErrorMethods.entropy(imgProc); 
                    break;
                case 4: 
                    errorMethodType = ErrorMethodType.VARIANCE;
                    // computedThreshold = ErrorMethods.variance(imgProc); 
                    break;
                case 5:
                    errorMethodType = ErrorMethodType.SSIM;
                    break;
                default: 
                    throw new IllegalArgumentException("Metode tidak valid!");
            }

            // Jika mode kompresi aktif, sesuaikan threshold
            // if (targetCompression > 0) {
            //     threshold = computedThreshold * targetCompression;
            //     System.out.println("Ambang batas yang disesuaikan: " + threshold);
            // }

            // Bangun quadtree
            System.out.println("Membangun quadtree...");
            BlockDivision blockDivision = new BlockDivision(minBlockSize, threshold, errorMethodType, originalImage);
            QuadTreeBuilder quadTree = new QuadTreeBuilder(originalImage, blockDivision);
            quadTree.buildTree();

            // Simpan hasil kompresi
            System.out.println("Menyimpan hasil kompresi...");
            OutputImage outputImage = new OutputImage(originalImage, quadTree, outputPath);
            outputImage.saveCompressedImage();

            // Waktu selesai eksekusi
            long endTime = System.nanoTime();

            // 8. Waktu eksekusi
            double executionTime = (endTime - startTime) / 1e9;
            System.out.println("Waktu eksekusi: " + executionTime + " detik");

            // 9. Ukuran gambar sebelum
            // File originalFile = new File(inputPath);
            long originalSize = inputFile.length();
            System.out.println("Ukuran gambar sebelum: " + originalSize + " bytes");

            // 10. Ukuran gambar setelah
            File compressedFile = new File(outputPath);
            long compressedSize = compressedFile.length();
            System.out.println("Ukuran gambar setelah: " + compressedSize + " bytes");

            // 11. Persentase kompresi
            double compressionPercentage = (1 - (double) compressedSize / originalSize) * 100;
            System.out.println("Persentase kompresi: " + compressionPercentage + "%");

            // 12. Kedalaman pohon
            System.out.println("Kedalaman pohon: " + quadTree.getMaxDepth());

            // 13. Banyak simpul pada pohon
            System.out.println("Banyak simpul pada pohon: " + quadTree.getNodeCount());

            // 14. Gambar hasil kompresi disimpan
            System.out.println("Gambar hasil kompresi telah disimpan di: " + outputPath);

        } catch (IOException e) {
            System.err.println("Terjadi kesalahan saat membaca atau menyimpan gambar: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Terjadi kesalahan: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
