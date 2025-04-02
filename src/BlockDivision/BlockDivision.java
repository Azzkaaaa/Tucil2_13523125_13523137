// Pembagian Blok
// Bandingkan nilai variansi blok dengan threshold

package BlockDivision;

import ColorNormalizer.ColorNormalizer;
import ErrorMethods.ErrorMethods;
import ErrorMethods.ErrorMethods.ErrorMethodType;
import ImageProcessing.ImageProcessing;
import QuadTree.QuadTreeNode;
import java.awt.image.BufferedImage;

public class BlockDivision {
    private int minBlockSize;
    private double threshold;
    private BufferedImage image;
    private ColorNormalizer colorNormalizer;
    private ErrorMethodType errorMethodType;

    // konstruktor untuk inisialisasi block division
    public BlockDivision(int minBlockSize, double threshold, ErrorMethodType errorMethodType, BufferedImage image) {
        this.minBlockSize = minBlockSize;
        this.threshold = threshold;
        this.errorMethodType = errorMethodType;
        this.image = image;
        this.colorNormalizer = new ColorNormalizer(image);
    }

    // menentukan apakah sebuah node perlu dibagi berdasarkan error methods
    public boolean shouldDivided(QuadTreeNode node) {
        ImageProcessing imgProc = new ImageProcessing(image, node.getX(), node.getY(), node.getWidth(), node.getHeight());
        
        // jika ukuran blok sudah minimum, jangan dibagi lagi
        if (imgProc.getWidth() <= minBlockSize || imgProc.getHeight() <= minBlockSize) {
            return false;
        }

        double err = 0;
        
        switch (errorMethodType) {
            case MAD:
                err = ErrorMethods.mad(imgProc);
                break;
            case MAX_PIXEL_DIFF:
                err = ErrorMethods.maxPixelDiff(imgProc);
                break;
            case ENTROPY:
                err = ErrorMethods.entropy(imgProc);
                break;
            case VARIANCE:
                err = ErrorMethods.variance(imgProc);
                break;
        }

        return err > threshold;
    }

    public void divideNode(QuadTreeNode node) {
        int halfWidth = node.getWidth() / 2;
        int halfHeight = node.getHeight() / 2;

        // buat 4 anak node dengan ukuran setengah dari node saat ini
        QuadTreeNode topLeft = new QuadTreeNode(node.getX(), node.getY(), halfWidth, halfHeight, node.getDepth() + 1);
        QuadTreeNode topRight = new QuadTreeNode(node.getX() + halfWidth, node.getY(), node.getWidth() - halfWidth, halfHeight, node.getDepth() + 1);
        QuadTreeNode bottomLeft = new QuadTreeNode(node.getX(), node.getY() + halfHeight, halfWidth, node.getHeight() - halfHeight, node.getDepth() + 1);
        QuadTreeNode bottomRight = new QuadTreeNode(node.getX() + halfWidth, node.getY() + halfHeight, node.getWidth() - halfWidth, node.getHeight() - halfHeight, node.getDepth() + 1);

        //gunakan setter untuk menetapkan anak node
        node.setChildren(topLeft, topRight, bottomLeft, bottomRight);
    }

    public void processLeafNode(QuadTreeNode node) {
        colorNormalizer.normalizeColor(node);
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }

    public double getThreshold() {
        return threshold;
    }
}