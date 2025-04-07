package OutputImage;

import QuadTree.QuadTreeBuilder;
import QuadTree.QuadTreeNode;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class OutputImage {
    private BufferedImage originalImage;
    private QuadTreeNode root;
    private String outputPath;
    private BufferedImage compressedImage;

    public OutputImage(BufferedImage originalImage, QuadTreeBuilder quadTree, String outputPath) {
        this.originalImage = originalImage;
        this.root = quadTree.getRoot();
        this.outputPath = outputPath;
        this.compressedImage = new BufferedImage(originalImage.getWidth(), 
        originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
    }


    public void saveCompressedImage() throws IOException {
        try {
            // reconstruct image dari quadtree
            reconstructImage(root, compressedImage);
            
            // pastikan direktori ada
            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (!parentDir.mkdirs()){
                    System.out.println("Peringatan: Gagal membuat direktori " + parentDir);
                }
                // boolean dirCreated = parentDir.mkdirs();
                // if (!dirCreated) {
                //     System.out.println("Peringatan: Gagal membuat direktori.");
                // }
            }

            String extension = getExtension(outputPath);
            
            if (extension == null){
                extension = "png"; // extension default
            }

            extension = extension.toLowerCase();

            boolean success = false;

            if (extension.equals("jpg") || extension.equals("jpeg")){
                success = ImageIO.write(compressedImage, "jpg", outputFile);
            }else if (extension.equals("png")){
                success = ImageIO.write(compressedImage, "png", outputFile);
            }else{
                extension = "png";
                success = ImageIO.write(compressedImage, "png", outputFile);
            }
            // Coba pendekatan alternatif - gunakan File langsung
            // boolean success = ImageIO.write(compressedImage, "jpg", outputFile);
            
            if (!success) {
                // Jika pendekatan pertama gagal, coba pendekatan kedua dengan format berbeda
                System.out.println("Gagal menyimpan dalam format " + extension + ", mencoba fallback ke PNG...");
                extension = "png";
                success = ImageIO.write(compressedImage, "png", outputFile);
                
                if (!success) {
                    // Jika pendekatan kedua gagal, coba pendekatan ketiga dengan FileOutputStream
                    // System.out.println("Format JPG gagal, mencoba dengan pendekatan FileOutputStream...");
                    // BufferedImage tempImage = new BufferedImage(compressedImage.getWidth(), compressedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                    // Graphics2D g2d = tempImage.createGraphics();
                    // g2d.drawImage(compressedImage, 0, 0, null);
                    // g2d.dispose();
                    
                    // // Coba simpan ke lokasi yang pasti bisa diakses
                    // File tempFile = new File(System.getProperty("user.home") + "/compressed_image.jpg");
                    // ImageIO.write(tempImage, "jpg", tempFile);
                    // System.out.println("Gambar berhasil disimpan sementara di: " + tempFile.getAbsolutePath());
                    
                    throw new IOException("Gagal menyimpan gambar dalam format PNG fallback.");
                    // Coba pindahkan ke lokasi yang diinginkan
                    // try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                    //     byte[] imageData = java.nio.file.Files.readAllBytes(tempFile.toPath());
                    //     fos.write(imageData);
                    //     tempFile.delete();
                    // } catch (Exception e) {
                    //     throw new IOException("Gagal menyimpan gambar ke lokasi akhir: " + e.getMessage());
                    // }
                }
            }
            
        } catch (Exception e) {
            // Tangkap semua exception untuk debugging
            System.err.println("Detail error: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            throw new IOException("Error saat menyimpan gambar: " + e.getMessage());
        }
    }


    private void reconstructImage(QuadTreeNode node, BufferedImage image) {
        if (node == null) return;

        if (node.isLeaf()) {
  
            for (int y = node.getY(); y < node.getY() + node.getHeight() && y < image.getHeight(); y++) {
                for (int x = node.getX(); x < node.getX() + node.getWidth() && x < image.getWidth(); x++) {
                    image.setRGB(x, y, node.getColor().getRGB());
                }
            }
        } else {
            // Recursively reconstruct the image from child nodes
            reconstructImage(node.getTopLeft(), image);
            reconstructImage(node.getTopRight(), image);
            reconstructImage(node.getBottomRight(), image);
            reconstructImage(node.getBottomLeft(), image);
        }
    }

    private  String getExtension(String path){
        int dotIndex = path.lastIndexOf('.');

        if (dotIndex == -1 || dotIndex == path.length() - 1){
            return null;
        }

        return path.substring(dotIndex + 1);
    }
}