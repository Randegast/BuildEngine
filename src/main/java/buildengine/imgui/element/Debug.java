package buildengine.imgui.element;

import buildengine.core.scene.director.MonoBehaviour;
import buildengine.input.Input;
import buildengine.math.MathUtils;
import buildengine.time.RepeatingEvent;
import buildengine.time.Scheduler;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import org.lwjgl.glfw.GLFW;

public class Debug extends ImGuiElement implements MonoBehaviour {

    private float deltaTime;

    private float dt;
    private int count;

    private static Object track;

    public static void track(Object track) {
        Debug.track = track;
    }

    @Override
    public void begin() {
        deltaTime = Float.NaN;
        dt = 0.0f;
        count = 0;

        Scheduler.addEvent(new RepeatingEvent(() -> {
            deltaTime = dt;
            dt = 0.0f;
            count = 0;
        }, 500));
    }

    @Override
    public void render() {
        ImGui.setNextWindowBgAlpha(.3f);
        ImGui.setNextWindowPos(10, 10);
        if(ImGui.begin("Debug", open, ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.AlwaysAutoResize |
                ImGuiWindowFlags.NoSavedSettings | ImGuiWindowFlags.NoFocusOnAppearing | ImGuiWindowFlags.NoNav | ImGuiWindowFlags.NoResize)) {
            ImGui.text("Performance");
            ImGui.separator();
            ImGui.text("Avg. delta time: " + MathUtils.round(deltaTime * 1000, 1) + " ms");
            ImGui.text("Frames/sec: " + MathUtils.round(1 / deltaTime, 1) + " frames");
            ImGui.separator();
            if(track != null)
                ImGui.text("Debug: " + track);
            ImGui.separator();
            ImGui.pushStyleColor(ImGuiCol.Text, .9f, .9f, .3f, 1f);
            ImGui.text("Press F2 to open console.");
            ImGui.popStyleColor();
        }
        ImGui.end();
    }

    @Override
    public void update(float dt) {
        if(Input.getKeyboard().isKeyReleased(GLFW.GLFW_KEY_F1)) open.set(!open.get());
        if(!Float.isFinite(dt)) return;
        this.dt = (this.dt * count + dt) / ++count;
    }
}
