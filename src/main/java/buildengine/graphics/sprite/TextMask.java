package buildengine.graphics.sprite;

import buildengine.core.scene.ActorComponent;
import org.joml.Vector2f;

public class TextMask extends ActorComponent {

    private Vector2f position;
    private String text;

    public TextMask(Vector2f position, String text) {
        this.position = position;
        this.text = text;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
