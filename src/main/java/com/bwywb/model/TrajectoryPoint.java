package com.bwywb.model;

import java.time.LocalDateTime;

public class TrajectoryPoint {
    private final double x; // 经度 (Longitude)
    private final double y; // 纬度 (Latitude)
    private final long timestamp; // 时间戳 (Unix epoch time)


    public TrajectoryPoint(double x, double y, long timestamp) {
        this.x = x;
        this.y = y;
        this.timestamp = timestamp;
    }
    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public long getTimestamp() { return timestamp; }

}