// Normalisasi Warna
// Untuk blok yang tidak lagi dibagi, lakukanlah normalisasi warna blok sesuai dengan
// rata-rata nilai RGB blok.


package ColorNormalizer;

import QuadTree.QuadTreeNode;
import java.awt.Color;
import java.awt.image.BufferedImage;
// import QuadTree.QuadTreeBuilder;

public class ColorNormalizer {
    private BufferedImage image;

    public ColorNormalizer(BufferedImage image) {
        this.image = image;
    }

    // normalisasi warna untuk setiap node dalam QuadTree
    public void normalizeColor(QuadTreeNode node) {
        int totR = 0;
        int totG = 0;
        int totB = 0;
        int pixelCount = 0;

        for(int y = node.getY(); y < node.getY() + node.getHeight() && y < image.getHeight(); y++) {
            for (int x = node.getX(); x < node.getX() + node.getWidth() && x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));
                totR += color.getRed();
                totG += color.getGreen();
                totB += color.getBlue();
                pixelCount++;
            }
        }

        if(pixelCount > 0) {
            int avgR = totR / pixelCount;
            int avgG = totG / pixelCount;
            int avgB = totB / pixelCount;
            node.setColor(new Color(avgR, avgG, avgB));
        } else {
            node.setColor(Color.BLACK);
        }
    }
}