package ErrorMethods;

// ini library buat testing
// import java.awt.image.BufferedImage;
// import java.io.File;
// import java.io.IOException;
// import javax.imageio.ImageIO;

import ImageProcessing.ImageProcessing;

import java.awt.Color;

public class Error {
    // UBAH BLOK GAMBAR JADI GREYSCALE
    private static int[][] toGrayscaleMatrix(ImageProcessing imgProc) {
        int height = imgProc.getHeight();
        int width = imgProc.getWidth();
        int[][] grayscale = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixel = imgProc.getPixel(y, x);
                grayscale[y][x] = (int) (0.299 * pixel.getRed() + 0.587 * pixel.getGreen() + 0.114 * pixel.getBlue());
            }
        }
        return grayscale;
    }

    // MEAN ABSOLUTE DEVIATION (MAD)
    public static double mad(ImageProcessing imgProc) {
        int[][] block = toGrayscaleMatrix(imgProc);
        int N = block.length * block[0].length;
        double mean = 0;

        for (int[] row : block) {
            for (int pixel : row) {
                mean += pixel;
            }
        }
        mean /= N;

        double mad = 0;
        for (int[] row : block) {
            for (int pixel : row) {
                mad += Math.abs(pixel - mean);
            }
        }
        return mad / N;
    }

    // MAX PIXEL DIFFERENCE
    public static double maxPixelDiff(ImageProcessing imgProc) {
        int[][] block = toGrayscaleMatrix(imgProc);
        int maxVal = Integer.MIN_VALUE;
        int minVal = Integer.MAX_VALUE;

        for (int[] row : block) {
            for (int pixel : row) {
                if (pixel > maxVal) {
                    maxVal = pixel;
                }
                if (pixel < minVal) {
                    minVal = pixel;
                }
            }
        }
        return maxVal - minVal;
    }

    // ENTROPY
    public static double entropy(ImageProcessing imgProc) {
        int[][] block = toGrayscaleMatrix(imgProc);
        int[] histogram = new int[256];
        int totalPixels = block.length * block[0].length;

        for (int[] row : block) {
            for (int pixel : row) {
                histogram[pixel]++;
            }
        }

        double ent = 0;
        for (int freq : histogram) {
            if (freq > 0) {
                double prob = (double) freq / totalPixels;
                ent -= prob * (Math.log(prob) / Math.log(2));
            }
        }
        return ent;
    }

    // buat testing
    // public static void main(String[] args) {
    //     try {
    //         BufferedImage image = ImageIO.read(new File("src/ErrorMethods/lily.jpg"));
    //         ImageProcessing imgProc = new ImageProcessing(image);

    //         double madValue = Error.mad(imgProc);
    //         double maxDiff = Error.maxPixelDiff(imgProc);
    //         double entropy = Error.entropy(imgProc);

    //         System.out.println("MAD: " + madValue);
    //         System.out.println("Max Pixel Difference: " + maxDiff);
    //         System.out.println("Entropy: " + entropy);

    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
}
