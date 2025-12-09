package com.bwywb.index;

import com.bwywb.index.MBR;
import com.bwywb.model.POIData;

import java.util.List;

/**
 * 子空间 (Subspace): 对应非均匀划分中的一个网格单元
 */
public class Subspace {
    private final MBR range;                       // 空间范围
    private final List<POIData> dataSet;           // 子空间要素集 (POI 点数据)
    private final int density;                     // 密度指标 (POI 点数量)

    private final int depth;                       // 划分深度
    private final int row;                         // 行号 (Y坐标, 相对于划分)
    private final int col;                         // 列号 (X坐标, 相对于划分)

    private long hilbertCode;                      // 最终的 Hilbert 编码 d

    /**
     * 构造函数
     * @param range 空间范围 MBR
     * @param dataSet 包含的 POI 点数据集
     * @param depth 划分深度
     * @param row 行号 (在当前划分层级中的相对位置，或相对于最大网格的绝对位置)
     * @param col 列号 (在当前划分层级中的相对位置，或相对于最大网格的绝对位置)
     */
    public Subspace(MBR range, List<POIData> dataSet, int depth, int row, int col) {
        this.range = range;
        this.dataSet = dataSet;
        this.density = dataSet.size(); // POI 点数据密度 = 包含的点数量
        this.depth = depth;
        this.row = row;
        this.col = col;
        this.hilbertCode = -1; // 初始化为未编码
    }

    // --- Getters ---
    public MBR getRange() { return range; }
    public List<POIData> getDataSet() { return dataSet; }
    public int getDensity() { return density; }
    public int getDepth() { return depth; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public long getHilbertCode() { return hilbertCode; }

    // --- Setters (用于排序阶段) ---
    public void setHilbertCode(long hilbertCode) {
        this.hilbertCode = hilbertCode;
    }

    /**
     * 计算该子空间的中心点 MBR，用于迭代法编码时的坐标映射
     */
    public double getCenterX() {
        return range.getCenterX();
    }
    public double getCenterY() {
        return range.getCenterY();
    }
}