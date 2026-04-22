package workerapp.models;
public class Coordinates {
    private double x;  // > -764
    private Float y;   // не null

    public Coordinates(double x, Float y) {
        setX(x);
        setY(y);
    }

    public void setX(double x) {
        if (x <= -764) {
            throw new IllegalArgumentException("x должен быть больше -764");
        }
        this.x = x;
    }

    public void setY(Float y) {
        if (y == null) {
            throw new IllegalArgumentException("y не может быть null");
        }
        this.y = y;
    }

    public double getX() { return x; }
    public Float getY() { return y; }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
