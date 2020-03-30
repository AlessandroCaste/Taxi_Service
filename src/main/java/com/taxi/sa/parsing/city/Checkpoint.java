package com.taxi.sa.parsing.city;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="Checkpoint")
public class Checkpoint implements Serializable {

    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name="city_id",nullable = false)
    private CityMap cityMap;
    private float price;
    private int x1,y1,x2,y2;

    public Checkpoint() {}

    public Checkpoint(float price, int x1, int y1, int x2, int y2) {
        this.price = price;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public CityMap getCityMap() {
        return cityMap;
    }

    public void setCityMap(CityMap cityMap) {
        this.cityMap = cityMap;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }

    public String coordinatesToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(x1).append(",").append(y1)
                .append(")->(")
                .append(x2).append(",").append(y2).append(")");
        return sb.toString();
    }

}
