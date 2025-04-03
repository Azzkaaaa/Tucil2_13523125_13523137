package ErrorMethods;

// ini library buat testing
// import java.awt.image.BufferedImage;
// import java.io.File;
// import java.io.IOException;
// import javax.imageio.ImageIO;

import ImageProcessing.ImageProcessing;
import java.awt.Color;

public class ErrorMethods {

    private static final double k1 = 0.01;
    private static final double k2 = 0.03;
    private static final double L = 255;

    public enum ErrorMethodType {
        MAD, MAX_PIXEL_DIFF, ENTROPY, VARIANCE, SSIM
    }

    // ubah blok gambar jadi greyscale
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
    // VARIANCE
    public static double variance(ImageProcessing imgProc) {
        int height = imgProc.getHeight();
        int width = imgProc.getWidth();
        int N = height * width;

        double meanR = 0, meanG = 0, meanB = 0;
        double varR = 0, varG = 0, varB = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixel = imgProc.getPixel(y, x);
                meanR += pixel.getRed();
                meanG += pixel.getGreen();
                meanB += pixel.getBlue();
            }
        }

        meanR /= N;
        meanG /= N;
        meanB /= N;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color pixel = imgProc.getPixel(y, x);
                varR += Math.pow(pixel.getRed() - meanR, 2);
                varG += Math.pow(pixel.getGreen() - meanG, 2);
                varB += Math.pow(pixel.getBlue() - meanB, 2);
            }
        }

        varR /= N;
        varG /= N;
        varB /= N;

        return (varR + varG + varB) / 3;
    }



    // SSIM
    public static double ssimRGB(ImageProcessing ori, ImageProcessing comp){
        if (ori.getWidth() != comp.getWidth() || ori.getHeight() != comp.getHeight()){
            throw new IllegalArgumentException("Ukuran blok tidak sama untuk perhitungan SSIM.");
        }

        double ssimR = ssimChannel(ori, comp, 'R');
        double ssimG = ssimChannel(ori, comp, 'G');
        double ssimB = ssimChannel(ori, comp, 'B');

        double wR = 1.0 / 3.0;
        double wG = 1.0 / 3.0;
        double wB = 1.0 / 3.0;

        return wR * ssimR + wG * ssimG + wB * ssimB;
    }

    private static double ssimChannel(ImageProcessing ori, ImageProcessing comp, char channel){
        int height = ori.getHeight();
        int width = ori.getWidth();

        int N = width * height;

        double sumO = 0.0, sumC = 0.0;
        double sumO2 = 0.0, sumC2 = 0.0;
        double sumOC = 0.0;

        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                Color co = ori.getPixel(y, x);
                Color cc = comp.getPixel(y, x);

                int vo = (channel == 'R') ? co.getRed() :
                (channel == 'G') ? co.getGreen() : co.getBlue();

                int vc = (channel == 'R') ? cc.getRed() :
                (channel == 'G') ? cc.getGreen() : cc.getBlue();

                sumO += vo;
                sumC += vc;
                sumO2 += (vo * (double) vo);
                sumC2 += (vc * (double) vc);
                sumOC += (vo * (double) vc);
            }
        }

        double meanO = sumO / N;
        double meanC = sumC / N;

        double varO = (sumO2 / N) - (meanO * meanO);
        double varC = (sumC2 / N) - (meanC * meanC);

        double covOC = (sumOC / N) - (meanO * meanC);

        double c1 = (k1 * L) * (k1 * L);
        double c2 = (k2 * L) * (k2 * L);

        double numerator   = (2 * meanO * meanC + c1) * (2 * covOC + c2);
        double denominator = (meanO * meanO + meanC * meanC + c1) * (varO + varC + c2);

        return numerator / denominator;
    }
}
