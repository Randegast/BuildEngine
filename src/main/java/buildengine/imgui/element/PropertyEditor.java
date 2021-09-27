package buildengine.imgui.element;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiTableFlags;
import imgui.flag.ImGuiTreeNodeFlags;
import imgui.type.ImFloat;

public class PropertyEditor extends ImGuiElement {

    private ImFloat property1 = new ImFloat(12);
    private float[] floats = new float[5];
    
    public PropertyEditor() {
        open.set(true);
    }
    
    @Override
    public void render() {
        ShowExampleAppPropertyEditor();
    }

    private void ShowPlaceholderObject(String prefix, int uid)
    {
    // Use object uid as identifier. Most commonly you could also use the object pointer as a base ID.
    ImGui.pushID(uid);

    // Text and Tree nodes are less high than framed widgets, using AlignTextToFramePadding() we add vertical spacing to make the tree lines equal high.
    ImGui.tableNextRow();
    ImGui.tableSetColumnIndex(0);
    ImGui.alignTextToFramePadding();
    boolean node_open = ImGui.treeNode("Object", prefix + "_" + uid);
    ImGui.tableSetColumnIndex(1);
    ImGui.text("my sailor is rich");

    if (node_open)
    {
        float[] placeholder_members = { 0.0f, 0.0f, 1.0f, 3.1416f, 100.0f, 999.0f };
        for (int i = 0; i < 8; i++)
        {
            ImGui.pushID(i); // Use field index as identifier.
            if (i < 2)
            {
                ShowPlaceholderObject("Child", 424242);
            }
            else
            {
                // Here we use a TreeNode to highlight on hover (we could use e.g. Selectable as well)
                ImGui.tableNextRow();
                ImGui.tableSetColumnIndex(0);
                ImGui.alignTextToFramePadding();
                int flags = ImGuiTreeNodeFlags.Leaf | ImGuiTreeNodeFlags.NoTreePushOnOpen | ImGuiTreeNodeFlags.Bullet;
                ImGui.treeNodeEx("Field", flags, "Field_" + i);

                ImGui.tableSetColumnIndex(1);
                ImGui.setNextItemWidth(10);
                if (i >= 5)
                    ImGui.inputFloat("##value", property1, 1.0f);
                else
                ImGui.dragFloat("##value", floats, 0.01f);
                ImGui.nextColumn();
            }
            ImGui.popID();
        }
        ImGui.treePop();
    }
    ImGui.popID();
}

    // Demonstrate create a simple property editor.
    void ShowExampleAppPropertyEditor()
    {
        ImGui.setNextWindowSize(430, 450, ImGuiCond.FirstUseEver);
        if (!ImGui.begin("Example: Property editor", open))
        {
            ImGui.end();
            return;
        }

        ImGui.pushStyleVar(ImGuiStyleVar.FramePadding, 2, 2);
        if (ImGui.beginTable("split", 2, ImGuiTableFlags.BordersOuter | ImGuiTableFlags.Resizable))
        {
            // Iterate placeholder objects (all the same data)
            for (int obj_i = 0; obj_i < 4; obj_i++)
            {
                ShowPlaceholderObject("Object", obj_i);
                //ImGui.Separator();
            }
            ImGui.endTable();
        }
        ImGui.popStyleVar();
        ImGui.end();
    }
}
