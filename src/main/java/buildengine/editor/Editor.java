package buildengine.editor;

import buildengine.configuration.Configuration;
import buildengine.core.Stage;
import buildengine.core.scene.director.MonoBehaviour;
import buildengine.editor.imgui.ImGuiWindow;
import buildengine.editor.imgui.element.Debug;
import buildengine.editor.imgui.element.ImGuiElement;
import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.flag.*;
import imgui.type.ImBoolean;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Editor extends Stage {

    // Context
    protected ImGuiWindow guiWindow;
    private List<ImGuiElement> elements = new ArrayList<>();
    private Configuration configuration;

    // Elements
    private ImBoolean demoWindow = new ImBoolean();
    private DebugConsole console = new DebugConsole();

    public Editor() {}

    @Override
    public void begin() {
        guiWindow = (ImGuiWindow) EditorLauncher.engine.getWindow();

        elements.add(console);

        ImGui.getIO().addConfigFlags(ImGuiConfigFlags.DockingEnable);
    }

    @Override
    public void update(float dt) {
        for(ImGuiElement imGuiElement : elements)
            if(imGuiElement instanceof MonoBehaviour)
                ((MonoBehaviour) imGuiElement).update(dt);
    }

    @Override
    public void render() {
        guiWindow.getImGuiGlfw().newFrame();
        ImGui.newFrame();

        // Main menu bar
        if(ImGui.beginMainMenuBar()) {

            ///////////////// FILE \\\\\\\\\\\\\\\\\

            if(ImGui.beginMenu("File")) {
                if(ImGui.menuItem("Open...")) {
                    // Open file
                }
                if(ImGui.menuItem("Save")) {
                    // Save file
                }
                if(ImGui.menuItem("Save as...")) {
                    // Save file as
                }
                ImGui.separator();
                if(ImGui.menuItem("Settings...")) {
                    // Open settings menu
                }
                ImGui.separator();
                if(ImGui.menuItem("Exit", "Alt+F4")) {
                    // Exit
                    EditorLauncher.engine.stop();
                }
                ImGui.endMenu();
            }

            ///////////// WINDOW \\\\\\\\\\\\\\

            if(ImGui.beginMenu("Window")) {
                ImGui.menuItem("Console", "F2", console.getOpen());
                ImGui.menuItem("ImGui Demo Window", "", demoWindow);
                ImGui.endMenu();
            }
            ImGui.endMainMenuBar();
        }

        ImGuiViewport viewport = ImGui.getMainViewport();

        ImGui.setNextWindowPos(viewport.getWorkPosX(), viewport.getWorkPosY());
        ImGui.setNextWindowSize(viewport.getWorkSizeX(), viewport.getWorkSizeY());
        ImGui.setNextWindowViewport(viewport.getID());

        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);

        int window_flags = ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize |
                ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.pushStyleVar(ImGuiStyleVar.WindowPadding, 0.0f, 0.0f);

        ImGui.begin("Main Workspace", window_flags);

        ImGui.popStyleVar();
        ImGui.popStyleVar(2);

        int dockspaceId = ImGui.getID("MainDockspace");
        ImGui.dockSpace(dockspaceId, 0, 0, ImGuiDockNodeFlags.None);

        // Main workspace

        if(demoWindow.get())
            ImGui.showDemoWindow(demoWindow);

        for(ImGuiElement element : elements)
            if(element.isOpen())
                element.render();

        ImGui.end(); // End dockspace window

        ImGui.render();
        guiWindow.getImGuiGl3().renderDrawData(ImGui.getDrawData());

        // If windows can exit the main window
        if(ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = GLFW.glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            GLFW.glfwMakeContextCurrent(backupWindowPtr);
        }
    }
}
