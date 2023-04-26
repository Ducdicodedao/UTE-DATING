package com.client.utedating.models;

import java.io.Serializable;
import java.util.Arrays;

public class Locat implements Serializable {
    private String type;

    private Number[] coordinates;

    public Locat() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Number[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Number[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "Locat{" +
                "type='" + type + '\'' +
                ", coordinates=" + Arrays.toString(coordinates) +
                '}';
    }
}
