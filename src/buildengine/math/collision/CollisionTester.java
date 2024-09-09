package buildengine.math.collision;

import buildengine.Unstable;
import buildengine.math.shape.Polygon;
import buildengine.math.shape.Rectangle;
import buildengine.math.shape.Shape;
import buildengine.math.vector.Vector2f;

/**
 * Handling Collisions using the BuildEngine shape system.
 */
public class CollisionTester {

    private CollisionTester() {}

    /**
     * Compare two shapes for collision. This method applies the fastest method to do that,
     * but is still pretty performance intensive if used extensively. If so, make sure you
     * do proximity checks first, than use this method for possible collisions.
     * @param a The first shape.
     * @param b The second shape.
     * @return  The contact object containing the vector to move {@code a} out of {@code b}.
     *          Returns {@code null} if there is no collision.
     * @throws IllegalArgumentException If there is not (yet) a way to compare the two shape
     *          objects.
     */
    @Unstable
    public static Contact compare(Shape a, Shape b) {
        if(a instanceof Polygon && b instanceof Polygon)
            return SAT.collidePolygons((Polygon) a, (Polygon) b);
        throw new IllegalArgumentException("Collision didn't find a way to compare a " +
                a.getClass().getSimpleName() + " and a " + b.getClass().getSimpleName());
    }

    /**
     * Compare two rectangles for collision. Does a lazy check first.
     * @param a The first rectangle.
     * @param b The second rectangle.
     * @return  The contact object containing the vector to move {@code a} out of {@code b}.
     *          Returns {@code null} if there is no collision.
     */
    public static Contact compareRectangle(Rectangle a, Rectangle b) {
        if(!a.intersectsLazy(b))
            return null;
        return SAT.collidePolygons(a, b);
    }

    /**
     * Contains a point in a shape for collision. This method applies the fastest method to
     * do that, but is still pretty performance intensive if used extensively. If so, make
     * sure you do proximity checks first, than use this method for possible collisions.
     * @param a The shape
     * @param point The point
     * @return  A contact object if there is collision containing the vector to move {@code a}
     *          out of {@code point}.
     *          Otherwise {@code null}.
     * @throws IllegalArgumentException If there is not (yet) a way to contain the shape object.
     */
    @Unstable
    public static Contact contain(Shape a, Vector2f point) {
        if(a instanceof Polygon)
            return SAT.containsPoint((Polygon) a, point);
        throw new IllegalArgumentException("Collision didn't find a way to contain a " +
                a.getClass().getSimpleName());
    }
}
