package ImageProcessing;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class ImageProcessing {
    private Color[][] matriksImage;
    private int width;
    private int height;

    // Constructor untuk seluruh gambar
    public ImageProcessing(BufferedImage image) {
        this.width = image.getWidth();
        this.height = image.getHeight();
        matriksImage = new Color[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                matriksImage[y][x] = new Color(image.getRGB(x, y));
            }
        }
    }

    // Constructor untuk region gambar
    public ImageProcessing(BufferedImage image, int startX, int startY, int regionWidth, int regionHeight) {
        this.width = Math.min(regionWidth, image.getWidth() - startX);
        this.height = Math.min(regionHeight, image.getHeight() - startY);
        matriksImage = new Color[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (startX + x < image.getWidth() && startY + y < image.getHeight()) {
                    matriksImage[y][x] = new Color(image.getRGB(startX + x, startY + y));
                } else {
                    matriksImage[y][x] = Color.BLACK; // Default jika di luar gambar
                }
            }
        }
    }

    // Getter
    public Color getPixel(int y, int x) {
        return matriksImage[y][x];
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Color avgColor(int startX, int startY, int blockWidth, int blockHeight) {
        long sumR = 0;
        long sumG = 0;
        long sumB = 0;

        int ctr = 0;
        for (int y = startY; y < startY + blockHeight && y < height; y++) {
            for (int x = startX; x < startX + blockWidth && x < width; x++) {
                Color c = matriksImage[y][x];
                sumR += c.getRed();
                sumG += c.getGreen();
                sumB += c.getBlue();
                ctr++;
            }
        }

        if (ctr == 0) {
            return new Color(0, 0, 0);
        }

        return new Color((int)(sumR / ctr), (int)(sumG / ctr), (int)(sumB / ctr));
    }
}
