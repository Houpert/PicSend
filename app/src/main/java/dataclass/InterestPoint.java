package dataclass;

/**
 * Created by yhoupert on 17/01/15.
 */
public class InterestPoint {

    private int x;
    private int y;

    public InterestPoint(int x, int y) {
        this.x = x;
        this.y = y;
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
