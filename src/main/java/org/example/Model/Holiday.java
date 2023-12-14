package org.example.Model;

public class Holiday {
    private String name;
    private String date;
    private String country;

    public Holiday() {}

    public Holiday(String name, String date, String country) {
        this.name = name;
        this.date = date;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
