package com.bwywb.index;


import com.bwywb.model.POIData;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * 多尺度时间索引: TR_Multi-scale = L1(Year/Month)::L2(Day)::L3(TP_min)
 */
public class MultiScaleTrIndex {
    private final long TP_LENGTH_MS = 30 * 60 * 1000L; // 最小时间段 30 分钟

    /**
     * 1. 编码 L1 (Year/Month)
     * @param zdt ZonedDateTime
     * @return 整数编码
     */
    private int encodeL1_YearMonth(ZonedDateTime zdt) {
        // 简化: 假设从 1970 年开始的月份总数
        return (zdt.getYear() - 1970) * 12 + zdt.getMonthValue();
    }

    /**
     * 2. 编码 L2 (Day of Year)
     * @param zdt ZonedDateTime
     * @return 整数编码
     */
    private int encodeL2_Day(ZonedDateTime zdt) {
        return zdt.getDayOfYear();
    }

    /**
     * 3. 编码 L3 (Minimal Time Period Index i)
     * 模拟原始 TR Index 的 i*N + (j-i) 中的 i
     * @param timestamp Unix epoch time
     * @return 最小 TP 索引 i
     */
    private long encodeL3_TPIndex(long timestamp) {
        // 原始 TR Index 的 i (时间段索引)
        return timestamp / TP_LENGTH_MS;
    }

    /**
     * 生成多尺度 TR 编码 (Key 的时间前缀)
     * @param data POI 数据点
     * @return 层次编码串联字符串
     */
    public String generateTrCode(POIData data) {
        // 使用 UTC 或固定时区以确保一致性
        ZonedDateTime zdt = Instant.ofEpochMilli(data.getTimestamp())
                .atZone(ZoneId.of("UTC"));

        // L1
        String codeL1 = String.format("%04d", encodeL1_YearMonth(zdt));
        // L2
        String codeL2 = String.format("%03d", encodeL2_Day(zdt));
        // L3 (最细粒度)
        String codeL3 = String.valueOf(encodeL3_TPIndex(data.getTimestamp()));

        // 串联编码 (确保 Key 的字典序与时间序一致)
        return codeL1 + codeL2 + codeL3; 
    }
}