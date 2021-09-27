package buildengine.math.shape;

import org.joml.Vector2f;

/**
 * Circle object
 */
public class Circle extends Shape {

    protected float radius;

    public Circle(float radius) {
        super(new Vector2f(), radius * 2);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }
}
