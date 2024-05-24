package org.example.model;

import org.example.enums.WeekDays;

public class Meal {
    private int id;

    private String name;

    private String image;

    private WeekDays weekDay;

    public Meal(int id, String name, String image, WeekDays weekDay) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.weekDay = weekDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return image;
    }

    public void setImages(String image) {
        this.image = image;
    }

    public WeekDays getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(WeekDays weekDay) {
        this.weekDay = weekDay;
    }
}
