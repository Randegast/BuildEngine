package buildengine.imgui;

import buildengine.BuildEngine;
import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.Loader;
import buildengine.core.scene.director.MonoBehaviour;
import buildengine.core.scene.director.Renderer;
import buildengine.imgui.element.ImGuiElement;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class ImGuiContext extends Director implements Renderer, MonoBehaviour {

    protected ImGuiWindow guiWindow;

    private final List<ImGuiElement> elements;

    public ImGuiContext(ImGuiElement... elements) {
        this.elements = new ArrayList<>();
        for(ImGuiElement element : elements)
            addElement(element);
    }

    @Override
    public void begin() {
        if(!(BuildEngine.getEngine().getWindow() instanceof ImGuiWindow))
            throw new IllegalStateException("Can't use ImGuiDirector in a normal window. ImGuiWindow context required.");

        guiWindow = (ImGuiWindow) BuildEngine.getEngine().getWindow();

        for(ImGuiElement element : elements)
            if(element instanceof Loader)
                ((Loader) element).begin();
    }

    @Override
    public void render() {
        guiWindow.getImGuiGlfw().newFrame();
        ImGui.newFrame();

        // Rendering
        for(ImGuiElement element : elements)
            if(element.isOpen())
                element.render();

        ImGui.render();
        guiWindow.getImGuiGl3().renderDrawData(ImGui.getDrawData());

        if(ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
    }

    @Override
    public void update(float dt) {
        for(ImGuiElement element : elements)
            if(element instanceof MonoBehaviour)
                ((MonoBehaviour) element).update(dt);
    }

    public void addElement(ImGuiElement element) {
        if(elements.contains(element))
            return;
        element.setContext(this);
        elements.add(element);
    }

    public List<ImGuiElement> getElements() {
        return elements;
    }
}
