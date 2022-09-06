package com.sockan.model;

public class SensorData {
    private String payload;
    private String timestamp;
    private String data_type;

    public String getPayload() {
        return payload;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public String getData_type() {
        return data_type;
    }

    /*public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }*/
    public void setPayload(String payload) {
        this.payload = payload;
    }
    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public SensorData(String payload, String timestamp, String data_type) {
        this.payload = payload;
        this.timestamp = timestamp;
        this.data_type = data_type;
    }
}
