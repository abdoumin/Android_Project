package com.example.applicationprojettest.recyclerView;

public class CoordonnesData {
    private String date;
    private double lattitude;
    private double longitude;
    private String healthStatus;

    public CoordonnesData(String format, double latitude, double longitude,String healthStatus) {
        this.date = format;
        this.lattitude = latitude;
        this.longitude = longitude;
        this.healthStatus = healthStatus;
    }

    public String getDate() {
        return date;
    }

    public double getLattitude() {
        return lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLattitude(Float lattitude) {
        this.lattitude = lattitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public CoordonnesData(String date, Float lattitude, Float longitude, String healthStatus) {
        this.date = date;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.healthStatus = healthStatus;
    }
}
