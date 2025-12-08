package com.bwywb.manager;


import com.bwywb.index.NuhIndex;
import com.bwywb.index.TrIndex;
import com.bwywb.model.Trajectory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 时空索引管理和 Key-Value 存储抽象
 */
public class STIndexManager {
    private final TrIndex trIndex;
    private final NuhIndex nuhIndex;
    
    // 抽象 Key-Value 存储: Key = ST Key (TR_Code::NUH_Code), Value = Trajectory Data
    private final Map<String, Trajectory> primaryTable = new HashMap<>();

    public STIndexManager(TrIndex trIndex, NuhIndex nuhIndex) {
        this.trIndex = trIndex;
        this.nuhIndex = nuhIndex;
    }

    /**
     * 构建时空索引 (ST Index) 并存储轨迹
     * 这是一个静态/批量构建的示例
     * @param trajectories 待索引的轨迹列表
     */
    public void buildIndex(List<Trajectory> trajectories) {
        // 1. 划分时间箱: 根据 TR Index 对轨迹进行分组
        Map<Long, List<Trajectory>> trajectoriesByTRCode = new HashMap<>();
        for (Trajectory t : trajectories) {
            long trCode = trIndex.encode(t);
            trajectoriesByTRCode.computeIfAbsent(trCode, k -> new List<>()).add(t);
        }

        // 2. 在每个时间箱内进行非均匀空间划分和编码
        for (Map.Entry<Long, List<Trajectory>> entry : trajectoriesByTRCode.entrySet()) {
            long trCode = entry.getKey();
            List<Trajectory> segment = entry.getValue();

            // TODO: 计算当前时间箱 segment 的总 MBR
            MBR segmentMBR = calculateSegmentMBR(segment);

            // 生成该时间箱内的 非均匀 Hilbert 编码列表
            List<Long> nuhCodes = nuhIndex.generateNuhCodes(segment, segmentMBR);

            // 3. 整合编码并存入主表 Key-Value 存储
            for (int i = 0; i < segment.size(); i++) {
                long nuhCode = nuhCodes.get(i);
                Trajectory t = segment.get(i);

                // ST Key: TR_Code::NUH_Code
                String stKey = trCode + "::" + nuhCode; 
                primaryTable.put(stKey, t); 
            }
        }
    }
    
    // TODO: 实现增量更新方法 (updateIndex)
    // TODO: 实现时空范围查询方法 (querySTRange)
}