package buildengine.editor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code Console} provides static functionality for logging and saving debug information.
 */
public class Console {

    // Suppresses default constructor, ensuring non-instantiability.
    private Console() {}

    /**
     * Warning, error and message prefixes. Written before added to the logger, mainly for search functionality.
     */
    public static final String ERROR_CODEC = "%[eRrOr]%",
                               WARNING_CODEC = "%[wArNIng]%",
                               MSG_CODEC = "%MesSaGe%";

    /**
     * The logger used to store the
     */
    private static final List<String> logger = new ArrayList<>();

    private static final boolean timestamps = true;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

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

    public static List<String> getLogger() {
        return logger;
    }
}
