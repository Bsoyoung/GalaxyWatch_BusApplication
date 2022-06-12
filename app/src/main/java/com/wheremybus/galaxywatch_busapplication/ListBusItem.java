package com.wheremybus.galaxywatch_busapplication;

public class ListBusItem{
    private String busID;
    private String firstBus;
    private String secondBus;
    private String routeID;
    private String stationID;
    private String stationName;

    public String getStationName() { return stationName;}
    public void setStationName(String stationName){ this.stationName = stationName;}
    public String getRouteID() { return routeID; };
    public void setRouteID(String routeID){
        this.routeID = routeID;
    }
    public String getStationID() { return stationID; }
    public void setStationID(String stationID) { this.stationID = stationID;}
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

    ListBusItem(String busID, String stationName,String routeID, String stationID){
        this.busID = busID;
        this.stationName = stationName;
        this.stationID = stationID;
        this.routeID = routeID;
        this.firstBus = "도착정보를 원하시면 눌러주세요.";
        this.secondBus = "도착정보를 원하시면 눌러주세요.";
        //this.firstBus = firstBus;
        //this.secondBus = secondBus;

    }
    /*
    ListBusItem(String busID, String firstBus, String secondBus){
        this.busID = busID;
        //this.firstBus = firstBus;
        //this.secondBus = secondBus;

    }*/
}
