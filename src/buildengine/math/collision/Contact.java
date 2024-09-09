package buildengine.math.collision;

import buildengine.math.vector.Vector2f;

public class Contact {

    private final Vector2f normal;
    private final float penetration;

    public Contact(Vector2f normal, float penetration) {
        this.normal = normal;
        this.penetration = penetration;
    }

    public Vector2f getNormal() {
        return normal;
    }

    public float getPenetration() {
        return penetration;
    }

    @Override
    public String toString() {
        return normal.toString() + " > " + penetration;
    }

    public static Contact valueOf(String s) {
        String[] values = s.trim().replaceAll("\\s+","").split(">", 1);
        if(values.length < 2)
            throw new NumberFormatException("Invalid contact format.");
        return new Contact(Vector2f.valueOf(values[0]), Float.parseFloat(values[1]));
    }

}
