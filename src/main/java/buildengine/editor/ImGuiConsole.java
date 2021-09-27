package buildengine.editor;

import imgui.ImGui;
import imgui.ImGuiTextFilter;
import imgui.ImGuiViewport;
import imgui.flag.*;
import imgui.type.ImBoolean;
import imgui.type.ImString;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static buildengine.editor.Console.*;

public class ImGuiConsole {

    private static final Color TITLE_BG_COLOR = new Color(54, 54, 54),
                               WINDOW_BG_COLOR = new Color(253, 228, 111);
//                               ERROR_COLOR = new Color(255, 102, 102),
//                               MESSAGE_COLOR = new Color(225, 255, 250);

    private static final Color TEXT_COLOR = new Color(255, 255, 255),
                               WARNING_COLOR = new Color(253, 228, 111),
                               ERROR_COLOR = new Color(255, 102, 102),
                               MESSAGE_COLOR = new Color(225, 255, 250);

    private final Console console;
    private final ImBoolean open;

    ImString InputBuf = new ImString();
    ImGuiTextFilter Filter = new ImGuiTextFilter();

    List<String> history = new ArrayList<>();
    int HistoryPos;    // -1: new line, 0..History.Size-1 browsing history.

    boolean ScrollToBottom;
    boolean request_input_focus = true;
    ImBoolean autoScroll;
    private boolean fullscreen;

    public ImGuiConsole(Console console, ImBoolean open) {
        this.console = console;
        this.open = open;

        HistoryPos = -1;

        ScrollToBottom = false;
        autoScroll = new ImBoolean(true);
    }

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

        ImGui.text("Current Scene: " + console.getContext().getScene().getName());
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
            logger.clear();
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
            if (ImGui.selectable("Clear")) logger.clear();
            ImGui.endPopup();
        }

        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 4, 1); // Tighten spacing
        if (copy_to_clipboard)
            ImGui.logToClipboard();
        for (final String item : logger) {
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
        HistoryPos = -1;
        for (int i = history.size() - 1; i >= 0; i--)
            if (history.get(i).equalsIgnoreCase(command_line)) {
                history.remove(i);
            }
        history.add(command_line.toUpperCase(Locale.ROOT));

        // Process command
        for(Command command : console.getCommands())
            if(command_line.equalsIgnoreCase(command.getLabel()))
                command.getRunnable().run();
//        if (command_line.equalsIgnoreCase("CLEAR")) {
//            logger.clear();
//        } else if (command_line.equalsIgnoreCase("HELP")) {
//        } else if (command_line.equalsIgnoreCase("HISTORY")) {
//            int first = history.size() - 10;
//            for (int i = Math.max(first, 0); i < history.size(); i++)
//                info(i + "d: " + history.get(i));
//        } else {
//            info("Unknown command: '" + command_line + "'");
//        }

        // On command input, we scroll to bottom even if AutoScroll==false
        ScrollToBottom = true;
    }

    // In C++11 you'd be better off using lambdas for this sort of forwarding callbacks
//        int TextEditCallbackStub(ImGuiInputTextCallbackData data)
//        {
//            ExampleAppConsole console = (ExampleAppConsole*)data->UserData;
//            return console->TextEditCallback(data);
//        }
//
//        int TextEditCallback(ImGuiInputTextCallbackData* data)
//        {
//            //AddLog("cursor: %d, selection: %d-%d", data->CursorPos, data->SelectionStart, data->SelectionEnd);
//            switch (data->EventFlag)
//            {
//                case ImGuiInputTextFlags.CallbackCompletion:
//                {
//                    // Example of TEXT COMPLETION
//
//                    // Locate beginning of current word
//                    String word_end = data->Buf + data->CursorPos;
//                    int word_start = word_end;
//                    while (word_start > data->Buf)
//                    {
//                    const char c = word_start[-1];
//                        if (c == ' ' || c == '\t' || c == ',' || c == ';')
//                            break;
//                        word_start--;
//                    }
//
//                    // Build a list of candidates
//                    ImVector<const char*> candidates;
//                    for (int i = 0; i < Commands.Size; i++)
//                        if (Strnicmp(Commands[i], word_start, (int)(word_end - word_start)) == 0)
//                            candidates.push_back(Commands[i]);
//
//                    if (candidates.Size == 0)
//                    {
//                        // No match
//                        AddLog("No match for \"%.*s\"!\n", (int)(word_end - word_start), word_start);
//                    }
//                    else if (candidates.Size == 1)
//                    {
//                        // Single match. Delete the beginning of the word and replace it entirely so we've got nice casing.
//                        data->DeleteChars((int)(word_start - data->Buf), (int)(word_end - word_start));
//                        data->InsertChars(data->CursorPos, candidates[0]);
//                        data->InsertChars(data->CursorPos, " ");
//                    }
//                    else
//                    {
//                        // Multiple matches. Complete as much as we can..
//                        // So inputing "C"+Tab will complete to "CL" then display "CLEAR" and "CLASSIFY" as matches.
//                        int match_len = (int)(word_end - word_start);
//                        for (;;)
//                        {
//                            int c = 0;
//                            bool all_candidates_matches = true;
//                            for (int i = 0; i < candidates.Size && all_candidates_matches; i++)
//                                if (i == 0)
//                                    c = toupper(candidates[i][match_len]);
//                                else if (c == 0 || c != toupper(candidates[i][match_len]))
//                                    all_candidates_matches = false;
//                            if (!all_candidates_matches)
//                                break;
//                            match_len++;
//                        }
//
//                        if (match_len > 0)
//                        {
//                            data->DeleteChars((int)(word_start - data->Buf), (int)(word_end - word_start));
//                            data->InsertChars(data->CursorPos, candidates[0], candidates[0] + match_len);
//                        }
//
//                        // List matches
//                        AddLog("Possible matches:\n");
//                        for (int i = 0; i < candidates.Size; i++)
//                            AddLog("- %s\n", candidates[i]);
//                    }
//
//                    break;
//                }
//                case ImGuiInputTextFlags.CallbackHistory:
//                {
//                    // Example of HISTORY
//                const int prev_history_pos = HistoryPos;
//                    if (data->EventKey == ImGuiKey_UpArrow)
//                    {
//                        if (HistoryPos == -1)
//                            HistoryPos = History.Size - 1;
//                        else if (HistoryPos > 0)
//                            HistoryPos--;
//                    }
//                    else if (data->EventKey == ImGuiKey_DownArrow)
//                    {
//                        if (HistoryPos != -1)
//                            if (++HistoryPos >= History.Size)
//                                HistoryPos = -1;
//                    }
//
//                    // A better implementation would preserve the data on the current input line along with cursor position.
//                    if (prev_history_pos != HistoryPos)
//                    {
//                    const char* history_str = (HistoryPos >= 0) ? History[HistoryPos] : "";
//                        data->DeleteChars(0, data->BufTextLen);
//                        data->InsertChars(0, history_str);
//                    }
//                }
//            }
//            return 0;
//        }

    private void pushStyleColor(int imGuiCol, Color color) {
        ImGui.pushStyleColor(imGuiCol, (float) color.getRed() / 255f, (float) color.getGreen() / 255f,
                (float) color.getBlue() / 255f, 1f);
    }

    public boolean getOpen() {
        return open.get();
    }

    public void setOpen(boolean open) {
        this.open.set(open);
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }
}
