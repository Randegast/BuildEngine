package buildengine.editor;

import buildengine.BuildEngine;
import buildengine.core.scene.Actor;
import buildengine.core.scene.director.MonoBehaviour;
import buildengine.imgui.element.ImGuiElement;
import buildengine.input.Input;
import buildengine.time.Time;
import imgui.type.ImBoolean;
import org.lwjgl.glfw.GLFW;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Console extends ImGuiElement implements MonoBehaviour {

    public static final String ERROR_CODEC = "%[eRrOr]%",
                               WARNING_CODEC = "%[wArNIng]%",
                               MSG_CODEC = "%MesSaGe%";

    public static final List<String> logger = new ArrayList<>();

    private static final boolean timestamps = true;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm.ss");

    private ImGuiConsole console;
    private final List<Command> commands;

    public Console() {
        commands = new ArrayList<>();
        commands.addAll(Arrays.asList(
                new Command("version", "Displays current BuildEngine version.", () -> info(BuildEngine.welcome())),
                new Command("listactors", "Displays all actors in a scene.", () -> {
                    info("== Name =========== Active ==");
                    for (Actor actor : context.getScene().getActors())
                        info(actor.getName() + " ".repeat(Math.max(0, 21 - actor.getName().length())) +
                                actor.isActive());
                    info("=============================");
                    info(context.getScene().getName() + " contains " + context.getScene().getActors().size() + " actors.");
                    info("=============================");
                }),
                new Command("help", "Displays a list of posiible commands", () -> {
                    info("== Command ======================= Info ==");
                    for (Command command : commands) {
                        info("- " + command.getLabel() +
                                " ".repeat(Math.max(0, 21 - command.getLabel().length())) + command.getDescription());
                    }
                    info("==========================================");
                })));
    }

    @Override
    public void begin() {
        console = new ImGuiConsole(this, open);
    }

    @Override
    public void render() {
        if(open.get())
            console.render();
    }

    @Override
    public void update(float dt) {
        if(Input.getKeyboard().isKeyReleased(GLFW.GLFW_KEY_F2)) {
            open.set(!open.get());
            Input.setSleep(open.get());
        }
    }

    public static void log(String msg) {
        if(timestamps)
            msg = "[" + formatter.format(LocalDateTime.now()) + "] " + msg;
        System.out.println(msg);
        logger.add(msg);
    }

    public static void err(String msg) {
        if(timestamps)
            msg = "[" + formatter.format(LocalDateTime.now()) + "] " + msg;
        System.err.println(msg);
        logger.add(ERROR_CODEC + msg);
    }

    public static void warn(String msg) {
        if(timestamps)
            msg = "[" + formatter.format(LocalDateTime.now()) + "] " + msg;
        System.err.println(msg);
        logger.add(WARNING_CODEC + msg);
    }

    public static void info(String msg) {
        logger.add(MSG_CODEC + msg);
    }

    public List<Command> getCommands() {
        return commands;
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
}
