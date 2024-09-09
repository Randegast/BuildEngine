package buildengine.math.vector;

import buildengine.utils.MathUtils;

/**
 * 2D Vector with float point coordinates
 */
public class Vector2f {

    /**
     * The components of the vector
     */
    public float x, y;

    /**
     * Creates a new vector with both components set to 0
     */
    public Vector2f() {}

    /**
     * Creates a new vector with equal components
     * @param d both components value
     */
    public Vector2f(float d) {
        this(d, d);
    }

    public Vector2f(Vector2i vector2i) {
        this(vector2i.x, vector2i.y);
    }

    public Vector2f(Vector2d vector2d) {
        this(MathUtils.toFloat(vector2d.x), MathUtils.toFloat(vector2d.y));
    }

    /**
     * Creates a new vector with specified components
     * @param x the x component
     * @param y the y component
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Changes the components of the vector
     * @param x the x component
     * @param y the y component
     * @return the current object
     */
    public Vector2f set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2f add(Vector2f v) {
        return add(v, this);
    }

    public Vector2f add(float d) {
        return add(d, this);
    }

    public Vector2f add(float x, float y) {
        return add(x, y, this);
    }

    public Vector2f add(Vector2f v, Vector2f dest) {
        return add(v.x, v.y, dest);
    }

    public Vector2f add(float d, Vector2f dest) {
        return add(d, d, dest);
    }

    public Vector2f add(float x, float y, Vector2f dest) {
        return dest.set(this.x + x, this.y + y);
    }

    public Vector2f sub(Vector2f v) {
        return sub(v, this);
    }

    public Vector2f sub(float d) {
        return sub(d, this);
    }

    public Vector2f sub(float x, float y) {
        return sub(x, y, this);
    }

    public Vector2f sub(Vector2f v, Vector2f dest) {
        return sub(v.x, v.y, dest);
    }

    public Vector2f sub(float d, Vector2f dest) {
        return sub(d, d, dest);
    }

    public Vector2f sub(float x, float y, Vector2f dest) {
        return dest.set(this.x - x, this.y - y);
    }

    public Vector2f mul(Vector2f v) {
        return mul(v, this);
    }

    public Vector2f mul(float d) {
        return mul(d, this);
    }

    public Vector2f mul(float x, float y) {
        return mul(x, y, this);
    }

    public Vector2f mul(Vector2f v, Vector2f dest) {
        return mul(v.x, v.y, dest);
    }

    public Vector2f mul(float d, Vector2f dest) {
        return mul(d, d, dest);
    }

    public Vector2f mul(float x, float y, Vector2f dest) {
        return dest.set(this.x * x, this.y * y);
    }

    public Vector2f div(Vector2f v) {
        return div(v, this);
    }

    public Vector2f div(float d) {
    	return div(d, this);
    }

    public Vector2f div(float x, float y) {
        return div(x, y, this);
    }

    public Vector2f div(Vector2f v, Vector2f dest) {
        return div(v.x, v.y, dest);
    }

    public Vector2f div(float d, Vector2f dest) {
        return div(d, d, dest);
    }

    public Vector2f div(float x, float y, Vector2f dest) {
        return dest.set(this.x / x, this.y / y);
    }

    public float dot(Vector2f v) {
        return x * v.x + y * v.y;
    }

    public Vector2f flip() {
        return flip(this);
    }

    public Vector2f flip(Vector2f dest) {
        return mul(-1, dest);
    }

    public Vector2f normalize() {
        return normalize(this);
    }

    public Vector2f normalize(Vector2f dest) {
        if(x == 0 & y == 0)
            return dest.set(x, y);
        float length = length();
        return dest.set(x * 1 / length, y * 1 / length);
    }

    public Vector2f rotate(double theta) {
        return rotate(theta, this);
    }

    public Vector2f rotate(double theta, Vector2f dest) {
        return dest.set(MathUtils.toFloat(x * Math.cos(theta) + y * Math.sin(theta)),
                MathUtils.toFloat(-x * Math.sin(theta) + y * Math.cos(theta)));
    }

    // Getters and setters

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float getRadians() {
        return (float) Math.atan2(y, x);
    }

    @SuppressWarnings("SuspiciousNameCombination")
    public Vector2f getPerpendicular() {
        return new Vector2f(-y,  x);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    // Class utility
    
    public static Vector2f valueOf(float[] floats) {
        if(floats.length < 2)
            return new Vector2f();
        return new Vector2f(floats[0], floats[1]);
    }

    public static Vector2f valueOf(String s) {
        float[] floats = new float[2];
        String[] sub = s.split(",", 2);
        for(int i = 0; i < floats.length; i++) {
            try{
                floats[i] = Float.parseFloat(sub[i]);
            } catch (NumberFormatException e) {
                return new Vector2f();
            }
        }
        return valueOf(floats);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Vector2f) {
            Vector2f v = (Vector2f) o;
            return v.x == x && v.y == y;
        }
        return false;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    public Vector2f duplicate() {
        return new Vector2f(x, y);
    }
}
