package com.vertex.crud.IoTdata;


public class IoTData {
    private int id;
    private String data;

    public IoTData(int id, String data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
