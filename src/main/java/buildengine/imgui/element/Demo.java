package buildengine.imgui.element;

import imgui.ImGui;

public class Demo extends ImGuiElement {

    public Demo() {
        open.set(true);
    }

    @Override
    public void render() {
        ImGui.showDemoWindow();
    }
}
