package com.company.placesnearme;


public class adapter {
    public Double latitude,longitude;
    public adapter()
    {
        this.latitude=0.0;
        this.longitude=0.0;
    }
    public adapter(double lat, double lng)
    {
        setLat(lat);
        System.out.println("........................................................... set lat has been called "+latitude);
        setLng(lng);
        System.out.println("..................................................... set long has been called "+longitude);
    }
    public void setLat(double lat)
    {
        this.latitude=lat;
    }
    public void setLng(double lng)
    {
        this.longitude=lng;
    }

    public Double getLatitude() {
        System.out.println("........................................................... get lat has been called "+latitude);
        return latitude;
    }

    public Double getLongitude()
    {
        System.out.println("..................................................... get long has been called "+longitude);
        return longitude;
    }
}
