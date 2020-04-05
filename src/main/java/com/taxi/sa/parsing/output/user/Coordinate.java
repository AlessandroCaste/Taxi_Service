package com.taxi.sa.parsing.output.user;

import com.taxi.sa.parsing.input.user.InputCoordinate;

public class Coordinate {

    private int x, y;

    public Coordinate(InputCoordinate inputCoordinate) {
        this.x = inputCoordinate.getX();
        this.y = inputCoordinate.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

}
