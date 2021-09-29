package buildengine.editor;

import buildengine.core.Engine;
import buildengine.editor.imgui.ImGuiWindow;
import org.joml.Vector2i;

public class EditorLauncher {

    public static final String VERSION = "0.1";
    public static Engine engine;

    public static void main(String[] args) {
        engine = new Engine(new ImGuiWindow("BuildEngine Editor v" + VERSION, new Vector2i(1600, 900)));
        engine.setStage(new Editor());
        engine.getThread().start();
    }
}
