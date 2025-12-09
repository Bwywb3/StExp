package com.bwywb.index;

import com.bwywb.model.POIData;
import com.bwywb.utils.NuhUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 非均匀 Hilbert 空间填充曲线索引 (NUH Index)
 * 实现了非均匀划分 (Algorithm 2-1) 和基于迭代法的编码排序 (Algorithm 2-2)。
 */
public class NuhIndex {
    private final double DENSITY_THRESHOLD;
    private final int MAX_DEPTH = 15;
    private final int M;

    private final MBR globalMBR;

    public NuhIndex(double densityThreshold, MBR globalMBR) {
        this.DENSITY_THRESHOLD = densityThreshold;
        this.globalMBR = globalMBR;
        this.M = 1 << MAX_DEPTH;

        if (globalMBR.getWidth() == 0 || globalMBR.getHeight() == 0) {
            throw new IllegalArgumentException("Global MBR must have non-zero width and height for coordinate mapping.");
        }
    }

    /**
     * 1. 非均匀划分 (对应原论文 Algorithm 2-1)
     */
    public void nonUniformPartition(
            List<POIData> data,
            MBR currentMBR,
            int currentDepth,
            List<Subspace> minimalSubspaces) {

        int currentDensity = data.size();

        // 停止划分条件：密度低于阈值 OR 达到最大深度
        if (currentDensity <= DENSITY_THRESHOLD || currentDepth >= MAX_DEPTH) {

            // 映射中心点到 Hilbert 虚拟网格坐标 (xs, ys)
            int xs = NuhUtils.mapCoordinateToGrid(currentMBR.getCenterX(), globalMBR.getMinX(), globalMBR.getMaxX(), M);
            int ys = NuhUtils.mapCoordinateToGrid(currentMBR.getCenterY(), globalMBR.getMinY(), globalMBR.getMaxY(), M);

            // 构造最小子空间 (Row 对应 Y, Col 对应 X)
            Subspace minimal = new Subspace(currentMBR, data, currentDepth, ys, xs);
            minimalSubspaces.add(minimal);
            return;
        }

        // 均匀四分，并递归调用
        MBR[] subMBRs = currentMBR.quadSplit();
        int nextDepth = currentDepth + 1;

        for (MBR subMBR : subMBRs) {
            // 过滤点要素: 找出落入子 MBR 内的所有点
            List<POIData> subData = data.stream()
                    .filter(p -> subMBR.contains(p.getX(), p.getY()))
                    .collect(Collectors.toList());

            if (!subData.isEmpty()) {
                nonUniformPartition(subData, subMBR, nextDepth, minimalSubspaces);
            }
        }
    }

    /**
     * 2. 迭代法排序与编码 (对应原论文 1.1 节和 Algorithm 2-2)
     */
    public List<Subspace> sortAndEncode(List<Subspace> minimalSubspaces) {

        // 1. 计算每个最小子空间的 Hilbert 编码 (长度 d)
        for (Subspace subspace : minimalSubspaces) {
            int xs = subspace.getCol();
            int ys = subspace.getRow();

            // 【关键调用】使用我们自己实现的迭代法计算 Hilbert 编码 d
            long d = NuhUtils.computeHilbertCodeIterative(xs, ys, M);
            subspace.setHilbertCode(d);
        }

        // 2. 按编码 d 升序排序
        minimalSubspaces.sort((s1, s2) -> Long.compare(s1.getHilbertCode(), s2.getHilbertCode()));

        // TODO: (原论文 Algorithm 2-4) 连接线生成：处理非均匀断层

        return minimalSubspaces;
    }

    /**
     * 整个非均匀 Hilbert 索引生成流程的入口
     */
    public List<Subspace> generateNuhIndex(List<POIData> data) {
        if (data == null || data.isEmpty()) {
            return new ArrayList<>();
        }
        List<Subspace> minimalSubspaces = new ArrayList<>();

        // 启动非均匀划分 (从深度 0 开始)
        nonUniformPartition(data, globalMBR, 0, minimalSubspaces);

        // 对划分结果进行 Hilbert 编码和排序
        return sortAndEncode(minimalSubspaces);
    }
}