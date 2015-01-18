package dataclass;

import java.io.Serializable;

/**
 * Created by yhoupert on 17/01/15.
 */
public class PointOfInterest implements Serializable {

    private int x;
    private int y;

    public PointOfInterest(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "PointOfInterest{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
