package dataclass;

import java.io.Serializable;

/**
 * Created by yhoupert on 08/01/15.
 */
public class GeoLocal implements Serializable {

    private double longitude;
    private double latitude;

    public GeoLocal() {
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLatAndLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "GeoLocal{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
