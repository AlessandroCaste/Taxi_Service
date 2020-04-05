package com.taxi.sa.parsing.output.user;

import com.taxi.sa.parsing.input.user.InputCoordinate;
import com.taxi.sa.parsing.output.city.CityMap;

import javax.persistence.*;

@Entity
@Table(name="Taxi")
public class Taxi {

    @Id
    private String taxiId;
    private int x,y;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private CityMap cityMap;

    public Taxi() {}

    public Taxi(String taxiId, InputCoordinate position) {
        this.taxiId = taxiId;
        this.x = position.getX();
        this.y = position.getY();
    }

    public Taxi(String taxiId, int x, int y) {
        this.taxiId = taxiId;
        this.x = x;
        this.y = y;
    }

    public String getTaxiId() {
        return taxiId;
    }

    private void setPosition(Coordinate position) {
        x = position.getX();
        y = position.getY();
    }

    public void setCity(CityMap cityMap) {
        this.cityMap = cityMap;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Taxi) && (taxiId.toLowerCase().equals(((Taxi) o).getTaxiId().toLowerCase()));
    }

    @Override
    public int hashCode() {
        return taxiId.hashCode();
    }

}
