package buildengine.editor;

import buildengine.BuildEngine;
import buildengine.core.Console;
import buildengine.core.scene.director.MonoBehaviour;
import buildengine.editor.imgui.element.ImGuiElement;
import buildengine.input.Input;
import imgui.ImGui;
import imgui.ImGuiTextFilter;
import imgui.ImGuiViewport;
import imgui.flag.*;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static buildengine.core.Console.*;

public class DebugConsole extends ImGuiElement implements MonoBehaviour {

    private static final Color TITLE_BG_COLOR = new Color(54, 54, 54),
                               WINDOW_BG_COLOR = new Color(253, 228, 111);

    private static final Color TEXT_COLOR = new Color(255, 255, 255),
                               WARNING_COLOR = new Color(253, 228, 111),
                               ERROR_COLOR = new Color(255, 102, 102),
                               MESSAGE_COLOR = new Color(225, 255, 250);

    private final ImString InputBuf = new ImString();
    private final ImGuiTextFilter Filter = new ImGuiTextFilter();

    private final List<String> history = new ArrayList<>();

    private boolean ScrollToBottom;
    private boolean request_input_focus = true;
    private final ImBoolean autoScroll;
    private boolean fullscreen;

    private final List<Command> commands;

    public DebugConsole() {
        commands = new ArrayList<>();
        commands.addAll(Arrays.asList(
                new Command("version", "Displays current BuildEngine version.", () -> info(BuildEngine.NAME)),
                new Command("help", "Displays a list of possible commands", () -> {
                    info("== Command ======================= Info ==");
                    for (Command command : commands) {
                        info("- " + command.getLabel() +
                                " ".repeat(Math.max(0, 21 - command.getLabel().length())) + command.getDescription());
                    }
                    info("==========================================");
                })));

        ScrollToBottom = false;
        autoScroll = new ImBoolean(true);
    }

    @Override
    public void begin() {}

    @Override
    public void update(float dt) {
        if(Input.getKeyboard().isKeyReleased(GLFW.GLFW_KEY_F2)) {
            open.set(!open.get());
            Input.setSleep(open.get());
        }
    }

    @Override
    public void render() {
        ImGui.setNextWindowSize(520, 600, ImGuiCond.FirstUseEver);
        pushStyleColor(ImGuiCol.TitleBgActive, TITLE_BG_COLOR);
        int tags = fullscreen ? ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoDecoration |
                ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoResize : ImGuiWindowFlags.NoCollapse;

        if (fullscreen) {
            ImGuiViewport viewport = ImGui.getMainViewport();
            ImGui.setNextWindowPos(viewport.getPosX(), viewport.getPosY());
            ImGui.setNextWindowSize(viewport.getSizeX(), viewport.getSizeY());
        }

        if (!ImGui.begin("Console", open, tags)) {
            ImGui.end();
            return;
        }

        // As a specific feature guaranteed by the library, after calling Begin() the last Item represent the title bar.
        // So e.g. IsItemHovered() will return true when hovering the title bar.
        // Here we create a context menu only available from the title bar.
        if (ImGui.beginPopupContextItem()) {
            if (ImGui.menuItem(fullscreen ? "Minimize" : "Maximize"))
                fullscreen = !fullscreen;
            if (ImGui.menuItem("Close"))
                open.set(false);
            ImGui.endPopup();
        }
        ImGui.popStyleColor();

        ImGui.text("Current Scene: NULL");
        ImGui.sameLine();
        pushStyleColor(ImGuiCol.Text, MESSAGE_COLOR);
        ImGui.text("Enter 'HELP' for help.");
        ImGui.popStyleColor();

        // TODO: display items starting from the bottom
        ImGui.separator();

        if (ImGui.smallButton("Add Debug Text")) {
            log("4 some text");
            log("some more text");
            log("display very important message here!");
        }
        ImGui.sameLine();
        if (ImGui.smallButton("Add Debug Error")) {
            err("something went wrong");
        }
        ImGui.sameLine();
        if (ImGui.smallButton("Clear")) {
            getLogger().clear();
        }
        ImGui.sameLine();
        boolean copy_to_clipboard = ImGui.smallButton("Copy");
        //static float t = 0.0f; if (ImGui::GetTime() - t > 0.02f) { t = ImGui::GetTime(); AddLog("Spam %f", t); }

        ImGui.separator();

        // Options menu
        if (ImGui.beginPopup("Options")) {
            ImGui.checkbox("Auto-scroll", autoScroll);
            ImGui.endPopup();
        }

        // Options, Filter
        if (ImGui.button("Options"))
            ImGui.openPopup("Options");
        ImGui.sameLine();
        Filter.draw("Filter (\"incl,-excl\") (\"error\")", 180);
        ImGui.separator();

        // Reserve enough left-over height for 1 separator + 1 input text
        float footer_height_to_reserve = ImGui.getStyle().getItemSpacingY() + ImGui.getFrameHeightWithSpacing();
        ImGui.beginChild("ScrollingRegion", 0, -footer_height_to_reserve, false, ImGuiWindowFlags.HorizontalScrollbar);
        if (ImGui.beginPopupContextWindow()) {
            if (ImGui.selectable("Clear")) Console.getLogger().clear();
            ImGui.endPopup();
        }

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 4, 1); // Tighten spacing
        if (copy_to_clipboard)
            ImGui.logToClipboard();
        for (final String item : Console.getLogger()) {
            if (!Filter.passFilter(item)) // Only search logs, not command messages
                continue;
            if (Filter.isActive() && item.startsWith(MSG_CODEC))
                continue;

            // Normally you would store more information in your item than just a string.
            // (e.g. make Items[] an array of structure, store color/type etc.)

            Color color = TEXT_COLOR;
            String text = item;
            if (item.startsWith(ERROR_CODEC)) {
                color = ERROR_COLOR;
                text = item.replace(ERROR_CODEC, "");
            } else if (item.startsWith(WARNING_CODEC)) {
                color = WARNING_COLOR;
                text = item.replace(WARNING_CODEC, "");
            } else if (item.startsWith(MSG_CODEC)) {
                color = MESSAGE_COLOR;
                text = item.replace(MSG_CODEC, "");
            }

            ImGui.pushStyleColor(ImGuiCol.Text, (float) color.getRed() / 255f, (float) color.getGreen() / 255f, (float) color.getBlue() / 255f, 1f);
            ImGui.textUnformatted(text);
            ImGui.popStyleColor();
        }
        if (copy_to_clipboard)
            ImGui.logFinish();

        if (ScrollToBottom || (autoScroll.get() && ImGui.getScrollY() >= ImGui.getScrollMaxY())) {
            ImGui.setScrollHereY(1.0f);
            ScrollToBottom = false;
        }

        ImGui.popStyleVar();
        ImGui.endChild();
        ImGui.separator();

        // Command-line
        boolean reclaim_focus = false;
        int input_text_flags = ImGuiInputTextFlags.EnterReturnsTrue | ImGuiInputTextFlags.CallbackCompletion | ImGuiInputTextFlags.CallbackHistory;
        if (request_input_focus) {
            ImGui.setKeyboardFocusHere();
            request_input_focus = false;
        }
        pushStyleColor(ImGuiCol.Text, MESSAGE_COLOR);
        if (ImGui.inputText("Input", InputBuf, input_text_flags)) {
            String input = InputBuf.toString().trim();
            if (!input.isBlank()) {
                executeCommand(input);
                InputBuf.clear();
            }
            reclaim_focus = true;
        }
        ImGui.popStyleColor();

        // Auto-focus on window apparition
        ImGui.setItemDefaultFocus();
        if (reclaim_focus)
            ImGui.setKeyboardFocusHere(-1); // Auto focus previous widget

        ImGui.end();
    }

    public void executeCommand(String command_line) {
        info("> " + command_line);

        // Insert into history. First find match and delete it so it can be pushed to the back.
        // This isn't trying to be smart or optimal.
        for (int i = history.size() - 1; i >= 0; i--)
            if (history.get(i).equalsIgnoreCase(command_line)) {
                history.remove(i);
            }
        history.add(command_line.toUpperCase(Locale.ROOT));

        boolean commandFound = false;
        // Process command
        for(Command command : commands)
            if(command_line.equalsIgnoreCase(command.getLabel())) {
                command.getRunnable().run();
                commandFound = true;
            }

        if(!commandFound)
            info("Unknown command. Type 'help' for a list of available commands.");

        // On command input, we scroll to bottom even if AutoScroll==false
        ScrollToBottom = true;
    }

    private void pushStyleColor(int imGuiCol, Color color) {
        ImGui.pushStyleColor(imGuiCol, (float) color.getRed() / 255f, (float) color.getGreen() / 255f,
                (float) color.getBlue() / 255f, 1f);
    }

    public static class Command {

        private final String label, description;
        private final Runnable runnable;

        public Command(String label, String description, Runnable runnable) {
            this.label = label;
            this.description = description;
            this.runnable = runnable;
        }

        public String getLabel() {
            return label;
        }

        public String getDescription() {
            return description;
        }

        public Runnable getRunnable() {
            return runnable;
        }
    }

    public List<Command> getCommands() {
        return commands;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }
}
