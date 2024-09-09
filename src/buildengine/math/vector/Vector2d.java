package buildengine.math.vector;

/**
 * 2D Vector with double point coordinates
 */
public class Vector2d {

    /**
     * The components of the vector
     */
    public double x, y;

    /**
     * Creates a new vector with both components set to 0
     */
    public Vector2d() {}

    /**
     * Creates a new vector with equal components
     * @param d both components value
     */
    public Vector2d(double d) {
        this(d, d);
    }

    public Vector2d(Vector2i vector2i) {
        this(vector2i.x, vector2i.y);
    }

    public Vector2d(Vector2f vector2f) {
        this(vector2f.x, vector2f.y);
    }

    /**
     * Creates a new vector with specified components
     * @param x the x component
     * @param y the y component
     */
    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Changes the components of the vector
     * @param x the x component
     * @param y the y component
     * @return the current object
     */
    public Vector2d set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2d add(Vector2d v) {
        return add(v, this);
    }

    public Vector2d add(double d) {
        return add(d, this);
    }

    public Vector2d add(double x, double y) {
        return add(x, y, this);
    }

    public Vector2d add(Vector2d v, Vector2d dest) {
        return add(v.x, v.y, dest);
    }

    public Vector2d add(double d, Vector2d dest) {
        return add(d, d, dest);
    }

    public Vector2d add(double x, double y, Vector2d dest) {
        return dest.set(this.x + x, this.y + y);
    }

    public Vector2d sub(Vector2d v) {
        return sub(v, this);
    }

    public Vector2d sub(double d) {
        return sub(d, this);
    }

    public Vector2d sub(double x, double y) {
        return sub(x, y, this);
    }

    public Vector2d sub(Vector2d v, Vector2d dest) {
        return sub(v.x, v.y, dest);
    }

    public Vector2d sub(double d, Vector2d dest) {
        return sub(d, d, dest);
    }

    public Vector2d sub(double x, double y, Vector2d dest) {
        return dest.set(this.x - x, this.y - y);
    }

    public Vector2d mul(Vector2d v) {
        return mul(v, this);
    }

    public Vector2d mul(double d) {
        return mul(d, this);
    }

    public Vector2d mul(double x, double y) {
        return mul(x, y, this);
    }

    public Vector2d mul(Vector2d v, Vector2d dest) {
        return mul(v.x, v.y, dest);
    }

    public Vector2d mul(double d, Vector2d dest) {
        return mul(d, d, dest);
    }

    public Vector2d mul(double x, double y, Vector2d dest) {
        return dest.set(this.x * x, this.y * y);
    }

    public Vector2d div(Vector2d v) {
        return div(v, this);
    }

    public Vector2d div(double d) {
    	return div(d, this);
    }

    public Vector2d div(double x, double y) {
        return div(x, y, this);
    }

    public Vector2d div(Vector2d v, Vector2d dest) {
        return div(v.x, v.y, dest);
    }

    public Vector2d div(double d, Vector2d dest) {
        return div(d, d, dest);
    }

    public Vector2d div(double x, double y, Vector2d dest) {
        return dest.set(this.x / x, this.y / y);
    }

    public double dot(Vector2d v) {
        return x * v.x + y * v.y;
    }

    public Vector2d flip() {
        return flip(this);
    }

    public Vector2d flip(Vector2d dest) {
        return mul(-1, dest);
    }

    public Vector2d normalize() {
        return normalize(this);
    }

    public Vector2d normalize(Vector2d dest) {
        if(x == 0 & y == 0)
            return dest.set(x, y);
        double length = length();
        return dest.set(x * 1 / length, y * 1 / length);
    }

    // Getters and setters

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double getRadians() {
        return Math.atan2(y, x);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public Vector2d getPerpendicular() {
        return new Vector2d(-y,  x);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    // Class utility
    
    public static Vector2d valueOf(double[] doubles) {
        if(doubles.length < 2)
            return new Vector2d();
        return new Vector2d(doubles[0], doubles[1]);
    }

    public static Vector2d valueOf(String s) {
        double[] doubles = new double[2];
        String[] sub = s.split(",", 2);
        for(int i = 0; i < doubles.length; i++) {
            try{
                doubles[i] = Double.parseDouble(sub[i]);
            } catch (NumberFormatException e) {
                return new Vector2d();
            }
        }
        return valueOf(doubles);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Vector2d) {
            Vector2d v = (Vector2d) o;
            return v.x == x && v.y == y;
        }
        return false;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    public Vector2d duplicate() {
        return new Vector2d(x, y);
    }
}
