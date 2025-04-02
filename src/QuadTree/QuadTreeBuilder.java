// Rekursi dan Penghentian
// Proses pembagian blok dilakukan secara rekursif untuk setiap sub-blok hingga semua
// blok memenuhi salah satu dari dua kondisi

package QuadTree;

import BlockDivision.BlockDivision;
import java.awt.image.BufferedImage;

public class QuadTreeBuilder {
    private BufferedImage oriImage;
    private BlockDivision blockDivision;
    private QuadTreeNode root;
    private int maxDepth = 0;
    private int nodeCount = 0;

    public QuadTreeBuilder(BufferedImage oriImage, BlockDivision blockDivision) {
        this.oriImage = oriImage;
        this.blockDivision = blockDivision;
    }

    // buat quadtree dari gambar
    public void buildTree() {
        root = new QuadTreeNode(0, 0, oriImage.getWidth(), oriImage.getHeight(), 0);
        buildQuadTree(root);
    }

    // fungsi rekursif untuk membuat quadtree
    private void buildQuadTree(QuadTreeNode node) {
        if(blockDivision.shouldDivided(node)) {
            blockDivision.divideNode(node);

            // buat 4 anak node secara rekursif
            buildQuadTree(node.getTopLeft());
            buildQuadTree(node.getTopRight());
            buildQuadTree(node.getBottomLeft());
            buildQuadTree(node.getBottomRight());
        }
        else {
            blockDivision.processLeafNode(node);
            maxDepth = Math.max(maxDepth, node.getDepth());
            nodeCount++;
        }
    }

    // Buat kembali gambar dari quadtree - NOTE: Ada bug typo di perulangan for
    private void reconstructImage(QuadTreeNode node, BufferedImage image) {
        if (node == null) return;

        if (node.isLeaf()) {
            for (int y = node.getY(); y < node.getY() + node.getHeight() && y < image.getHeight(); y++) {
                for (int x = node.getX(); x < node.getX() + node.getWidth() && x < image.getWidth(); x++) { // Bug diperbaiki di sini, sebelumnya "y++"
                    image.setRGB(x, y, node.getColor().getRGB());
                }
            }
        }
        else {
            // buat gambar untuk 4 anak node secara rekursif
            reconstructImage(node.getTopLeft(), image);
            reconstructImage(node.getTopRight(), image);
            reconstructImage(node.getBottomLeft(), image);
            reconstructImage(node.getBottomRight(), image);
        }
    }
    //getters
    public QuadTreeNode getRoot() {
        return root;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public int getNodeCount() {
        return nodeCount;
    }
}