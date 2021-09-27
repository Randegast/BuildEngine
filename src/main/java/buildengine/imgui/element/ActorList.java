package buildengine.imgui.element;

import buildengine.core.scene.Actor;
import buildengine.core.scene.ActorComponent;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiTabBarFlags;

public class ActorList extends ImGuiElement {

    int selected = 0;

    public ActorList() {
        open.set(true);
    }

    @Override
    public void render() {
        Actor actor = context.getScene().getActors().get(selected);

        ImGui.setNextWindowSize(500, 440, ImGuiCond.FirstUseEver);
        if(ImGui.begin("Actors", open)) {
            ImGui.beginChild("left pane", 150, 0, true);
            for (int i = 0; i < context.getScene().getActors().size(); i++)
            {
                if (ImGui.selectable(context.getScene().getActors().get(i).getName(), selected == i))
                    selected = i;
            }
            ImGui.endChild();
            ImGui.sameLine();

            // Right
            {
                ImGui.beginGroup();
                ImGui.beginChild("item view", 0, -ImGui.getFrameHeightWithSpacing()); // Leave room for 1 line below us
                ImGui.text(actor.getName());
                ImGui.separator();
                if (ImGui.beginTabBar("##Tabs", ImGuiTabBarFlags.None))
                {
                    if (ImGui.beginTabItem("Components"))
                    {
                        ImGui.text("Transform: " + actor.getTransform());
                        for(ActorComponent component : actor.getComponents())
                            ImGui.text(component.getClass().getSimpleName());
                        ImGui.endTabItem();
                    }
                    if (ImGui.beginTabItem("Details"))
                    {
                        ImGui.text("Scene: " + actor.getScene().getName());
                        ImGui.text("Active: " + actor.isActive());
                        ImGui.text("Dynamic: " + actor.isDynamic());
                        ImGui.text("Destroyed: " + actor.isDestroyed());
                        ImGui.text("Java Class: " + actor.getClass());
                        ImGui.endTabItem();
                    }
                    ImGui.endTabBar();
                }
                ImGui.endChild();
                if (ImGui.button("Destroy")) {

                }
                ImGui.sameLine();
                if (ImGui.button("Save")) {

                }
                ImGui.endGroup();
            }
        }
        ImGui.end();
    }
}
