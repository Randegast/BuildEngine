package buildengine.editor.imgui.element;

import buildengine.core.scene.director.Renderer;
import imgui.type.ImBoolean;

public abstract class ImGuiElement implements Renderer {

    protected ImBoolean open = new ImBoolean();

    public ImBoolean getOpen() {
        return open;
    }

    public boolean isOpen() {
        return open.get();
    }

    public void setOpen(boolean open) {
        this.open.set(open);
    }
}
