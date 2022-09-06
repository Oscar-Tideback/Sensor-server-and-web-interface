package com.sockan.model;

import java.util.List;

public class Message {
    private String from;
    private String to;
    private String content;
    private List<SensorData> allData;
    private String lastData;

    @Override
    public String toString() {
        return super.toString();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDataFromFile(String lastData) {
        this.lastData = lastData;
    }

    public void setDataObject(List<SensorData> allData) {
        this.allData = allData;
    }
}
