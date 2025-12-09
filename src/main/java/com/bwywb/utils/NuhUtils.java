package com.bwywb.utils;

/**
 * 非均匀 Hilbert 编码辅助工具 (NuhUtils)
 * 包含静态的坐标映射逻辑和自实现的迭代法 Hilbert 编码。
 */
public class NuhUtils {

    /**
     * 将坐标映射到虚拟网格的 [0, m-1] 整数范围
     */
    public static int mapCoordinateToGrid(double coord, double min, double max, int m) {
        if (max - min == 0) return 0;
        // 归一化并映射到 [0, m-1]
        // 使用 Math.floor 确保坐标对应网格的左下角
        long gridCoord = (long) Math.floor((coord - min) / (max - min) * m);
        // 确保不超出范围 [0, m-1]
        return (int) Math.max(0, Math.min(gridCoord, m - 1));
    }

    /**
     * 【核心实现】基于迭代法计算 Hilbert 编码 d (2D 空间)
     * 对应于原论文中“基于迭代法”的思想，通过位操作和旋转/翻转实现。
     * @param xs 虚拟网格 x 坐标
     * @param ys 虚拟网格 y 坐标
     * @param m 虚拟网格边长 (2^阶数)
     * @return Hilbert 编码 d
     */
    public static long computeHilbertCodeIterative(int xs, int ys, int m) {
        long d = 0;
        // 阶数 N = log2(M)
        int bits = (int) (Math.log(m) / Math.log(2));

        long currentX = xs;
        long currentY = ys;

        // 核心迭代：从最高位开始，逐级计算和变换坐标
        for (int i = bits; i > 0; i--) {
            // 掩码，用于提取当前层的位
            long mask = 1L << (i - 1);

            // 提取当前层的位 (rx, ry)
            long rx = (currentX & mask) > 0 ? 1 : 0;
            long ry = (currentY & mask) > 0 ? 1 : 0;

            // 确定当前象限 q (0-3), Z-order 索引
            long q = (ry << 1) | rx;

            // 将 q 加入到距离 d 中 (d = ... q)
            d = (d << 2) | q;

            // 核心 Hilbert 变换: 根据所在的象限 (q) 对下一级坐标进行旋转和翻转
            // 这部分逻辑将当前坐标 (currentX, currentY) 变换到标准的第一象限，以便在下一级递归中继续使用相同的规则。
            long tempX = currentX;
            long tempY = currentY;

            if (q == 0) {
                // 象限 0 (左下): 变换 (y, x) -> (x', y')
                currentX = tempY;
                currentY = tempX;
            } else if (q == 3) {
                // 象限 3 (右上): 变换 (m-1-y, m-1-x) -> (x', y')
                currentX = (m - 1) - tempY;
                currentY = (m - 1) - tempX;
            }
            // 象限 1 和 2 只需要处理坐标的偏移，但由于我们在全坐标上操作，简化为上述两种变换。
        }

        return d;
    }
}