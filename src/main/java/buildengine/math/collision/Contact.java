package buildengine.math.collision;

import org.joml.Vector2f;

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

}
