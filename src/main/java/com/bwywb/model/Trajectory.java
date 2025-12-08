package com.bwywb.model;

import java.util.List;

public class Trajectory {
    private final String trajectoryId;
    private final List<TrajectoryPoint> points;
    
    // 轨迹的最小外包矩形 (MBR) 和时间范围 [ts, te]
    private final double minX, minY, maxX, maxY;
    private final long startTime, endTime;

    // 构造函数... (Constructor)
    public Trajectory(String trajectoryId, List<TrajectoryPoint> points) {
        this.trajectoryId = trajectoryId;
        this.points = points;

        // 计算轨迹的 MBR 和时间范围
        minX = points.stream().mapToDouble(TrajectoryPoint::getX).min().orElse(0.0);
        minY = points.stream().mapToDouble(TrajectoryPoint::getY).min().orElse(0.0);
        maxX = points.stream().mapToDouble(TrajectoryPoint::getX).max().orElse(0.0);
        maxY = points.stream().mapToDouble(TrajectoryPoint::getY).max().orElse(0.0);
        startTime = points.stream().mapToLong(TrajectoryPoint::getTimestamp).min().orElse(0L);
        endTime = points.stream().mapToLong(TrajectoryPoint::getTimestamp).max().orElse(0L);
    }

    
    // Getters and helper methods (e.g., to compute MBR, length, etc.)
    public String getTrajectoryId() { return trajectoryId; }
    public List<TrajectoryPoint> getPoints() { return points; }

    // Getters for MBR and time range
    public double getMinX() { return minX; }
    public double getMinY() { return minY; }
    public double getMaxX() { return maxX; }
    public double getMaxY() { return maxY; }
    public long getStartTime() { return startTime; }
    public long getEndTime() { return endTime; }

    // 用于非均匀划分的轨迹密度指标 (Metric for Non-uniform Partition)
    public double calculateSpatialDensityMetric() {
        // TODO: 实现轨迹长度、点数或面积作为密度指标的计算
        return points.size(); // 简单示例：使用点数
    }
}