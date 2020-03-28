package parsing;

import java.io.Serializable;

public class CityMap implements Serializable {

    private String cityId;
    private int width;
    private int height;

    public CityMap(String cityId, int width, int height) {
        this.cityId = cityId;
        this.width = width;
        this.height = height;
    }

    public String getCityId() {
        return cityId;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "City = " + cityId + " width " + width + " height " + height;
    }

}
