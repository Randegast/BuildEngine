package buildengine.imgui.element;

import buildengine.core.scene.director.Renderer;
import buildengine.imgui.ImGuiContext;
import imgui.type.ImBoolean;

public abstract class ImGuiElement implements Renderer {

    protected ImGuiContext context;
    protected ImBoolean open = new ImBoolean();

    public ImGuiContext getContext() {
        return context;
    }

    public void setContext(ImGuiContext context) {
        this.context = context;
    }

    public boolean isOpen() {
        return open.get();
    }

    public void setOpen(boolean open) {
        this.open.set(open);
    }
}
