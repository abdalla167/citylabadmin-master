package com.muhammed.citylabadmin.data.model.location;

public class LocationModle
{
    String namelabe;
    String lat;
    String log;


    public LocationModle() {
    }

    public LocationModle(String namelabe, String lat, String log) {
        this.namelabe = namelabe;
        this.lat = lat;
        this.log = log;
    }

    public String getNamelabe() {
        return namelabe;
    }

    public void setNamelabe(String namelabe) {
        this.namelabe = namelabe;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
