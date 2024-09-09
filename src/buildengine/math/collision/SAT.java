package buildengine.math.collision;

import buildengine.math.shape.Polygon;
import buildengine.math.vector.Vector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Separating Axis Theorem
 * This is a static-only class and contains calculations
 * regarding collisions. The collisions are calculated
 * using an implementation off Separating Axis Theorem.
 * This class uses the custom Shape library.
 *
 * @author Kai v. Maurik
 */
public class SAT {

    /**
     * Check if two polygons are colliding
     * @param a The first polygon
     * @param b The second polygon
     * @return  A contact object if there is collision containing the collision
     *          information. Otherwise, returns {@code null}.
     */
    public static Contact collidePolygons(Polygon a, Polygon b) {
        // Collision data
        double penetration = Double.MAX_VALUE;
        Vector2f smallest = null;
        // Retrieving axes
        List<Vector2f> axes = new ArrayList<>();
        axes.addAll(Arrays.asList(a.getAxes()));
        axes.addAll(Arrays.asList(b.getAxes()));
        // Loop over the axes
        for(Vector2f axis : axes) {
            // Project both shapes onto the axis
            Projection p0 = a.project(axis);
            Projection p1 = b.project(axis);
            // Check overlap
            if(!p0.overlap(p1))
                return null;
            // Collision on axis
            double overlap = p0.getOverlap(p1);
            double pen = Math.abs(overlap);
            // Containment
            if(p0.contains(p1) || p1.contains(p0)) {
                double min = Math.abs(p0.x - p1.x);
                double max = Math.abs(p0.y - p1.y);
                pen += Math.min(min, max);
            }
            // Data Collect
            if(pen < penetration) {
                penetration = pen;
                smallest = overlap < 0 ? axis.flip(new Vector2f()) : axis;
            }
        }
        return new Contact(smallest, (float) penetration);
    }

    /**
     * Check if a polygon contains a specified point
     * @param a The polygon
     * @param v The point
     * @return  A contact object if there is collision containing the collision
     *          information. Otherwise, returns {@code null}.
     */
    public static Contact containsPoint(Polygon a, Vector2f v) {
        // Collision data
        double penetration = Double.MAX_VALUE;
        Vector2f smallest = null;
        // Retrieving axes
        List<Vector2f> axes = new ArrayList<>(Arrays.asList(a.getAxes()));
        // Loop over the axes
        for(Vector2f axis : axes) {
            // Project both shapes onto the axis
            Projection p0 = a.project(axis);
            float p1 = axis.dot(v);
            // Check overlap
            if(!(p1 > p0.x && p1 < p0.y))
                return null;
            // Collision on axis
            double overlap = Math.abs(p1 - p0.x) < Math.abs(p1 - p0.y) ? p0.x - p1 : p0.y - p1;
            double pen = Math.abs(overlap);
            // Data Collect
            if(pen < penetration) {
                penetration = pen;
                smallest = overlap > 0 ? axis.flip(new Vector2f()) : axis;
            }
        }
        return new Contact(smallest, (float) penetration);
    }

    /**
     * @author Kai v. Maurik
     */
    public static class Projection {

        public final double x, y;

        public Projection(double min, double max) {
            x = min;
            y = max;
        }

        public boolean overlap(Projection p) {
            return !(p.x > y || x > p.y);
        }

        // Return the penetration AND direction (-d / +d)
        public double getOverlap(Projection p) {
            return (y < p.y) ? p.x - y : p.y - x;
        }

        public boolean contains(Projection p) {
            return p.x > x && p.y < y;
        }

        public double getMin() {
            return x;
        }

        public double getMax() {
            return y;
        }
    }
}
