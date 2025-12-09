package com.bwywb.index;

/**
 * MBR (Minimum Bounding Rectangle) 空间边界定义
 */
public class MBR {
    private final double minX; // 最小经度
    private final double minY; // 最小纬度
    private final double maxX; // 最大经度
    private final double maxY; // 最大纬度

    public MBR(double minX, double minY, double maxX, double maxY) {
        // 确保边界有效性
        if (minX > maxX || minY > maxY) {
            throw new IllegalArgumentException("Invalid MBR boundaries: min > max");
        }
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    // --- Getters ---
    public double getMinX() { return minX; }
    public double getMinY() { return minY; }
    public double getMaxX() { return maxX; }
    public double getMaxY() { return maxY; }

    public double getWidth() { return maxX - minX; }
    public double getHeight() { return maxY - minY; }

    /**
     * 计算 MBR 的中心点 X 坐标
     */
    public double getCenterX() {
        return (minX + maxX) / 2.0;
    }

    /**
     * 计算 MBR 的中心点 Y 坐标
     */
    public double getCenterY() {
        return (minY + maxY) / 2.0;
    }

    /**
     * 判断一个点是否包含在该 MBR 内（不包含上边界）
     */
    public boolean contains(double x, double y) {
        return x >= minX && x < maxX && y >= minY && y < maxY;
    }

    /**
     * 将当前 MBR 均匀四分，生成四个子 MBR
     * @return 包含四个子 MBR 的数组: [左下, 右下, 左上, 右上]
     */
    public MBR[] quadSplit() {
        double midX = getCenterX();
        double midY = getCenterY();

        // 1. 左下 (Bottom-Left)
        MBR bl = new MBR(minX, minY, midX, midY);
        // 2. 右下 (Bottom-Right)
        MBR br = new MBR(midX, minY, maxX, midY);
        // 3. 左上 (Top-Left)
        MBR tl = new MBR(minX, midY, midX, maxY);
        // 4. 右上 (Top-Right)
        MBR tr = new MBR(midX, midY, maxX, maxY);

        return new MBR[]{bl, br, tl, tr};
    }
}