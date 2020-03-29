package com.taxi.sa.parsing.city;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(Checkpoint.class)
public class Checkpoint implements Serializable {
    private float price;

    @Id @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name="cityId",nullable = false)
    private CityMap cityMap;
    private int x1,y1,x2,y2;

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

}
