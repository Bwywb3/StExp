package com.bwywb.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * POI 点数据模型 (Yelp 评论/签到)
 */
public class POIData implements Serializable {
    private String business_id;
    private String user_id;
    private String review_id;
    private double x;         // 经度
    private double y;         // 纬度
    private long timestamp;   // 事件时间戳 (Unix epoch time)
    // 其他属性 (例如: 评分 AttrCode)
    private Map<String, Object> attributes = new HashMap<>();

    public POIData(String business_id, String user_id, String review_id, double longitude, double latitude, long timestamp, Map<String, Object> attributes) {
        this.business_id = business_id;
        this.user_id = user_id;
        this.review_id = review_id;
        this.x = longitude;
        this.y = latitude;
        this.timestamp = timestamp;
        this.attributes = attributes;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getReview_id() {
        return review_id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }


}