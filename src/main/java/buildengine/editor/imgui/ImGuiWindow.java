package buildengine.editor.imgui;

import buildengine.Window;
import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.joml.Vector2i;

public class ImGuiWindow extends Window {

    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public ImGuiWindow(String title, Vector2i resolution) {
        super(title, resolution);
    }

    @Override
    public void init() {
        super.init();

        ImGui.createContext();
        imGuiGlfw.init(windowId, true);
        imGuiGl3.init(glslVersion);
    }

    @Override
    public void terminate() {
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();

        super.terminate();
    }

    public ImGuiImplGlfw getImGuiGlfw() {
        return imGuiGlfw;
    }

    public ImGuiImplGl3 getImGuiGl3() {
        return imGuiGl3;
    }
}
