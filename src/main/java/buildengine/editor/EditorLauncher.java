package buildengine.editor;

import buildengine.BuildEngine;
import buildengine.Window;
import buildengine.core.scene.Scene;
import buildengine.imgui.ImGuiContext;
import buildengine.imgui.ImGuiWindow;

public class EditorLauncher {

    public static void main(String[] args) {
        BuildEngine.create();

        Scene editorScene = new Scene("Editor");
        editorScene.addDirector(new ImGuiContext());

        BuildEngine.getEngine().getStage().queueScene(editorScene);
    }
}
