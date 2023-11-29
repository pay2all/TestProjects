package com.demo.apppay2all.FlightTicketBooking.CityAirport;

import java.io.Serializable;

public class CityItems implements Serializable {

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }

    public String getAirportcode() {
        return airportcode;
    }

    public void setAirportcode(String airportcode) {
        this.airportcode = airportcode;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    String dd="";
    String airportcode="";
    String citycode="";
    String cityName="";
    String countryName="";
}
