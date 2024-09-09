package buildengine.math.shape;

import buildengine.math.collision.SAT;
import buildengine.math.vector.Vector2f;
import buildengine.utils.MathUtils;

/**
 * @author Kai v. Maurik
 */
public class Rectangle extends Polygon {

    public static final int VERTICES_LENGTH = 4;

    public float x, y, width, height;

    public Rectangle(float width, float height) {
        this(0, 0, width, height, 0);
    }
    public Rectangle(float x, float y, float width, float height) {
        this(x, y, width, height, 0);
    }
    public Rectangle(float x, float y, float width, float height, double rotation) {
        super(new Vector2f[VERTICES_LENGTH], new Vector2f(x + width / 2, y + height / 2), new Vector2f(width, height).length(), rotation);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(Vector2f point) {
        return SAT.containsPoint(this, point) != null;
    }

    public boolean intersects(Rectangle with) {
        return SAT.collidePolygons(this, with) != null;
    }

    public boolean intersectsLazy(Rectangle with) {
        float distance = new Vector2f(with.getX(), with.getY()).sub(new Vector2f(x, y), new Vector2f()).length();
        float diameters = getDiameter() + with.getDiameter();
        return distance < diameters;
    }

    /**
     * Translates bounds to vertices.
     * @return vertices of rectangle
     */
    @Override
    public Vector2f[] getVertices() {
        float xPos = x;
        float yPos = y;
        // Set the rectangle vertices
        vertices[0] = new Vector2f(xPos, yPos);
        vertices[1] = new Vector2f(xPos + width, yPos);
        vertices[2] = new Vector2f(xPos + width, yPos + height);
        vertices[3] = new Vector2f(xPos, yPos + height);
        // Rotate them accordingly
        for(int i = 0; i < vertices.length; i++)
            vertices[i] = MathUtils.rotatePoint(rotation, getCenter(), vertices[i]);
        // Return
        return vertices;
    }

    /**
     * Gets the normalized axes of every edge of the Polygon.
     * This is used in for collision detection. Because this
     * is a Rectangle object, only 2 axes are required to check.
     *
     * @return axes of all edges.
     * @see Polygon#getAxes()
     */
    @Override
    public Vector2f[] getAxes() {
        getVertices(); // Update vertices
        Vector2f[] axes = new Vector2f[2];
        axes[0] = vertices[1].sub(vertices[0], new Vector2f()).getPerpendicular().normalize();
        axes[1] = vertices[2].sub(vertices[1], new Vector2f()).getPerpendicular().normalize();
        return axes;
    }

    @Override
    public Vector2f getCenter() {
        return new Vector2f(x + width / 2, y + height / 2);
    }

    // Getters and setters

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
