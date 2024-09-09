package buildengine.math.vector;

import buildengine.utils.MathUtils;

/**
 * 2D Vector with int point coordinates
 */
public class Vector2i {

    /**
     * The components of the vector
     */
    public int x, y;

    /**
     * Creates a new vector with both components set to 0
     */
    public Vector2i() {}

    public Vector2i(Vector2f vector2f) {
        this(MathUtils.toInt(vector2f.x), MathUtils.toInt(vector2f.y));
    }

    public Vector2i(Vector2d vector2d) {
        this(MathUtils.toInt(vector2d.x), MathUtils.toInt(vector2d.y));
    }

    /**
     * Creates a new vector with equal components
     * @param d both components value
     */
    public Vector2i(int d) {
        this(d, d);
    }

    /**
     * Creates a new vector with specified components
     * @param x the x component
     * @param y the y component
     */
    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Changes the components of the vector
     * @param x the x component
     * @param y the y component
     * @return the current object
     */
    public Vector2i set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2i add(Vector2i v) {
        return add(v, this);
    }

    public Vector2i add(int d) {
        return add(d, this);
    }

    public Vector2i add(int x, int y) {
        return add(x, y, this);
    }

    public Vector2i add(Vector2i v, Vector2i dest) {
        return add(v.x, v.y, dest);
    }

    public Vector2i add(int d, Vector2i dest) {
        return add(d, d, dest);
    }

    public Vector2i add(int x, int y, Vector2i dest) {
        return dest.set(this.x + x, this.y + y);
    }

    public Vector2i sub(Vector2i v) {
        return sub(v, this);
    }

    public Vector2i sub(int d) {
        return sub(d, this);
    }

    public Vector2i sub(int x, int y) {
        return sub(x, y, this);
    }

    public Vector2i sub(Vector2i v, Vector2i dest) {
        return sub(v.x, v.y, dest);
    }

    public Vector2i sub(int d, Vector2i dest) {
        return sub(d, d, dest);
    }

    public Vector2i sub(int x, int y, Vector2i dest) {
        return dest.set(this.x - x, this.y - y);
    }

    public Vector2i mul(Vector2i v) {
        return mul(v, this);
    }

    public Vector2i mul(int d) {
        return mul(d, this);
    }

    public Vector2i mul(int x, int y) {
        return mul(x, y, this);
    }

    public Vector2i mul(Vector2i v, Vector2i dest) {
        return mul(v.x, v.y, dest);
    }

    public Vector2i mul(int d, Vector2i dest) {
        return mul(d, d, dest);
    }

    public Vector2i mul(int x, int y, Vector2i dest) {
        return dest.set(this.x * x, this.y * y);
    }

    public Vector2i div(Vector2i v) {
        return div(v, this);
    }

    public Vector2i div(int d) {
        return div(d, this);
    }

    public Vector2i div(int x, int y) {
        return div(x, y, this);
    }

    public Vector2i div(Vector2i v, Vector2i dest) {
        return div(v.x, v.y, dest);
    }

    public Vector2i div(int d, Vector2i dest) {
        return div(d, d, dest);
    }

    public Vector2i div(int x, int y, Vector2i dest) {
        return dest.set(this.x / x, this.y / y);
    }

    public int dot(Vector2i v) {
        return x * v.x + y * v.y;
    }

    public Vector2i flip() {
        return flip(this);
    }

    public Vector2i flip(Vector2i dest) {
        return mul(-1, dest);
    }

    public Vector2i normalize() {
        return normalize(this);
    }

    public Vector2i normalize(Vector2i dest) {
        if(x == 0 & y == 0)
            return dest.set(x, y);
        int length = length();
        return dest.set(x / length, y / length);
    }

    // Getters and setters

    public int length() {
        return (int) Math.sqrt(x * x + y * y);
    }

    public int getRadians() {
        return (int) Math.atan2(y, x);
    }

    public Vector2i getPerpendicular() {
        return new Vector2i(-y,  x);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    // Class utility

    public static Vector2i valueOf(int[] ints) {
        if(ints.length < 2)
            return new Vector2i();
        return new Vector2i(ints[0], ints[1]);
    }

    public static Vector2i valueOf(String s) {
        int[] ints = new int[2];
        String[] sub = s.split(",", 2);
        for(int i = 0; i < ints.length; i++) {
            try{
                ints[i] = Integer.parseInt(sub[i]);
            } catch (NumberFormatException e) {
                return new Vector2i();
            }
        }
        return valueOf(ints);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Vector2i v) {
            return v.x == x && v.y == y;
        }
        return false;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    @Override
    public Vector2i clone() {
        return new Vector2i(x, y);
    }
}
