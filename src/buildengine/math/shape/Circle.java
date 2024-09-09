package buildengine.math.shape;

import buildengine.Unstable;
import buildengine.math.vector.Vector2f;

/**
 * Circle object
 * @Unstable not yet added to the SAT system
 */
@Unstable
public class Circle extends Shape {

    protected final float radius;

    public Circle(float radius) {
        super(new Vector2f(), radius * 2);
        this.radius = radius;
    }

    public float getRadius() {
        return radius;
    }
}
