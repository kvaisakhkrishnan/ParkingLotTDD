package com.example;
public class Car {
    private String licensePlate;
    private String color;
    private String make;
    private String model;
    private String size; // "small", "medium", "large", etc.
    private boolean isHandicap;

    public Car(String licensePlate, String color, String make, String model, String size, boolean isHandicap) {
        this.licensePlate = licensePlate;
        this.color = color;
        this.make = make;
        this.model = model;
        this.size = size;
        this.isHandicap = isHandicap;
    }
    public String getLicensePlate() {
        return licensePlate;
    }

    public String getColor() {
        return color;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getSize() {
        return size;
    }

    public boolean isHandicap() {
        return isHandicap;
    }
}
