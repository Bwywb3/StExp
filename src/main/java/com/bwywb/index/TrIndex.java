package com.bwywb.index;


import com.bwywb.model.Trajectory;

public class TrIndex {
    // 最大时间箱跨度，例如 48 小时，用于限制编码空间 (Maximum Time Bin Span, N)
    private final int N; 
    // 时间段长度，例如 30 分钟 (Time Period length in milliseconds)
    private final long TP_LENGTH_MS; 
    
    public TrIndex(int N_periods, long tpLengthMs) {
        this.N = N_periods;
        this.TP_LENGTH_MS = tpLengthMs;
    }

    /**
     * 将 Unix 时间戳转换为时间段索引 i
     * @param timestamp Unix epoch time
     * @return Time Period index (i)
     */
    public long getTPIndex(long timestamp) {
        // 假设时间线从 UNIX 纪元开始 (1970-01-01)
        return timestamp / TP_LENGTH_MS; 
    }

    /**
     * 计算轨迹的时间箱 TR 索引值。
     * TR(TB_i,j) = i * N + (j - i)
     * @param trajectory 轨迹对象
     * @return TR Index 编码 (一维长整型)
     */
    public long encode(Trajectory trajectory) {
        long ts = trajectory.getStartTime();
        long te = trajectory.getEndTime();

        long i = getTPIndex(ts); // 起始时间段 i
        long j = getTPIndex(te); // 结束时间段 j
        
        // 确保跨度不超过 N
        if (j - i >= N) {
            // TODO: 实际处理长轨迹的策略（例如，裁剪或使用最大跨度）
            j = i + N - 1; 
        }

//        [cite_start]// TR Index 编码公式 [cite: 248]
        return i * N + (j - i); 
    }
    
    // TODO: 实现 TRQ 查询窗口生成 (根据 Lemma 5)
}