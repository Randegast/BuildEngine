package buildengine.math.shape;

import buildengine.math.vector.Vector2f;

/**
 * Base abstract shape object
 */
public abstract class Shape {

    protected final Vector2f center;
    protected final float diameter;

    public Shape(Vector2f center, float diameter) {
        this.center = center;
        this.diameter = diameter;
    }

    public Vector2f getCenter() {
        return center;
    }

    public float getDiameter() {
        return diameter;
    }
}
