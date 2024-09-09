package buildengine.math.shape;

import buildengine.math.collision.SAT;
import buildengine.math.vector.Vector2f;
import buildengine.utils.MathUtils;

/**
 * Polygon
 * All not-round shapes. For example a triangle.
 *
 * @author Kai v. Maurik
 * @see #vertices Specifies the corner points of the Polygon.
 */

public abstract class Polygon extends Shape {

    /** Default vertices length */
    public static final int VERTICES_LENGTH = 0;

    /**
     * Vertices of the Polygon. The vertices are to be
     * CW(Counter-Clockwise) defined.
     */
    protected final Vector2f[] vertices;

    // Rotation
    protected double rotation;

    /**
     * @param vertices Specifies how many cords the Polygon consists of.
     * @param center The default center for rotation calculations.
     */
    public Polygon(Vector2f[] vertices, float diameter, Vector2f center) {
        this(vertices, center, diameter, 0);
    }

    /**
     * @param vertices Specifies how many cords the Polygon consists of.
     * @param center The default center for rotation calculations.
     * @param rotation The rotation of the object.
     */
    public Polygon(Vector2f[] vertices, Vector2f center, float diameter, double rotation) {
        super(center, diameter);
        this.vertices = vertices;
        this.rotation = rotation;
        for(int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vector2f();
        }
    }

    /**
     * Project vertices on an axis
     * @see #vertices
     * @return A new Projection object.
     */
    public SAT.Projection project(Vector2f axis) {
        Vector2f[] vertices = getVertices();
        double min = axis.dot(vertices[0]);
        double max = min;
        for(int i = 1; i < vertices.length; i++) {
            double p = axis.dot(vertices[i]);
            if(p < min)
                min = p;
            else if(p > max)
                max = p;
        }
        return new SAT.Projection(min, max);
    }

    /**
     * Gets the normalized axes of every edge of the Polygon.
     * This is used in for collision detection.
     *
     * @return axes of all edges.
     */
    public Vector2f[] getAxes() {
        Vector2f[] vertices = getVertices();
        Vector2f[] axes = new Vector2f[vertices.length];
        for(int i = 0; i < vertices.length; i++) {
            Vector2f v1 = vertices[i];
            Vector2f v2 = vertices[i + 1 == vertices.length ? 0 : i + 1];
            Vector2f edge = v2.sub(v1, new Vector2f());
            Vector2f normal = edge.getPerpendicular().normalize();
            axes[i] = normal;
        }
        return axes;
    }

    /**
     * @return vertices of Polygon
     */
    public Vector2f[] getVertices() {
        for(int i = 0; i < vertices.length; i++)
            vertices[i] = MathUtils.rotatePoint(rotation, getCenter(), vertices[i]);
        return vertices;
    }

    // Getters and setters

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
}
