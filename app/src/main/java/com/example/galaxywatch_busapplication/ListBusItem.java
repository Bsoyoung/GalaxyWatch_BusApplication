package com.example.galaxywatch_busapplication;

public class ListBusItem {
    private String busID;
    private String firstBus;
    private String secondBus;

    public String getBusID() {
        return busID;
    }

    public void setBusID(String busID) {
        this.busID = busID;
    }

    public String getFirstBus() {
        return firstBus;
    }

    public void setFirstBus(String firstBus) {
        this.firstBus = firstBus;
    }

    public String getSecondBus() {
        return secondBus;
    }

    public void setSecondBus(String secondBus) {
        this.secondBus = secondBus;
    }

    ListBusItem(String busID, String firstBus, String secondBus){
        this.busID = busID;
        this.firstBus = firstBus;
        this.secondBus = secondBus;
    }

}
