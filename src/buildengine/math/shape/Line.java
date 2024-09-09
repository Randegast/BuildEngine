package buildengine.math.shape;

import buildengine.math.vector.Vector2f;

/**
 * Line object, consisting of two vertices
 */
public class Line extends Polygon {

    public static final int VERTICES_LENGTH = 2;

    public Line(Vector2f a, Vector2f b) {
        super(new Vector2f[VERTICES_LENGTH], b.add(a.sub(b, new Vector2f()).div(2)), a.sub(b, new Vector2f()).length(), 0);

        vertices[0] = a;
        vertices[1] = b;
    }

    @Override
    public Vector2f[] getVertices() {
        // Returns the normal vertices (no rotation shit)
        return vertices;
    }
}
