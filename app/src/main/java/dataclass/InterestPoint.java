package dataclass;

import java.io.Serializable;

/**
 * Created by yhoupert on 17/01/15.
 */
public class InterestPoint implements Serializable {

    private int x;
    private int y;

    public InterestPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "InterestPoint{" +
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
