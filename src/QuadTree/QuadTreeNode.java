package QuadTree;

import ImageProcessing.ImageProcessing;
import java.awt.Color;

public class QuadTreeNode {
    int x, y, width, height, depth;
    Color color;
    QuadTreeNode topLeft, topRight, bottomLeft, bottomRight;
    private ImageProcessing imgProc;

    public QuadTreeNode(int x, int y, int width, int height, int depth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.color = Color.BLACK;
    }

    public void setChildren(QuadTreeNode topLeft, QuadTreeNode topRight, QuadTreeNode bottomLeft, QuadTreeNode bottomRight) {
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
    }    

    public boolean isLeaf() {
        return topLeft == null && topRight == null && bottomLeft == null && bottomRight == null;
    }

    // getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getDepth() { return depth; }
    public Color getColor() { return color; }
    public QuadTreeNode getTopLeft() { return topLeft; }
    public QuadTreeNode getTopRight() { return topRight; }
    public QuadTreeNode getBottomLeft() { return bottomLeft; }
    public QuadTreeNode getBottomRight() { return bottomRight; }

    public void setColor(Color color) { this.color = color; }

    public ImageProcessing getImageProcessing() {
        return imgProc;
    }

}
