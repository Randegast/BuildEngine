package buildengine.configuration;

import buildengine.editor.Console;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code FileConfiguration} represents a {@link Configuration}, with I/O functionality.
 * <p>
 *     This class provides basic file I/O functionality: saving and loading.
 * </p>
 * @author Kai van Maurik
 * @see Configuration
 * @since 1.0
 */
public class FileConfiguration extends Configuration {

    /**
     * A boolean variable keeping track of the validity of the file contents.
     */
    private boolean corrupted;

    /**
     * Initializes a {@code Configuration} and if the system File exists, loads the configuration from the File.
     * Formally, if the file exists, calls the {@code load(File)} function.
     * @param file The system file to load a {@code Configuration} from.
     */
    public FileConfiguration(File file) {
        if(file.exists())
            load(file);
    }

    /**
     * Loads the {@code Configuration} from a specified file to this object.
     * @param file The system file to load the {@code Configuration} from.
     */
    public void load(File file) {
        String raw = "";

        try {
            byte[] encoded = Files.readAllBytes(Paths.get(file.toURI()));
            raw = new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Console.err("An I/O error occurred while reading configuration file: " + file.getPath());
        }

        raw = raw.trim();
        for(String value : raw.split(";")) {
            if(!value.contains(":"))
                continue;
            String[] str = value.split(":", 2);
            if(str.length < 2) {
                Console.err("Error while reading data file: " + file.getName() +
                        ". Expected 2 values, found " + str.length + "(" + value + ")");
                corrupted = true;
                continue;
            }

            set(str[0], str[1]);
        }
    }

    /**
     * Saves this {@code Configuration} to a specified file.
     * @param file The system file to load the {@code Configuration} to.
     */
    public void save(File file) {
        try {
            if(file.createNewFile())
                Console.log("New configuration file created: " + file.getPath());
        } catch (IOException e) {
            Console.err("An I/O error occurred while creating configuration file: " + file.getPath());
        }

        if(corrupted)
            Console.warn("Configuration file " + file.getName() + " saved, with corrupted data.");

        // Create string list and add all the configuration data in the form of: "key: value;"
        List<String> list = new ArrayList<>();
        for(String key : data.keySet())
            list.add(key.replaceAll(":", "").replaceAll(";", "") + ": "
                    + data.get(key).replaceAll(";", "") + ";");

        // Write out all the lines to the file
        try (PrintWriter out = new PrintWriter(file)) {
            for(String line : list)
                out.println(line);
        } catch (FileNotFoundException e) {
            Console.err("Error while trying to safe " + file.getName() + " data file: Could find the file.");
        }
    }

    // Getters and setters

    /**
     * Returns the corruption state. When a {@code Configuration} is loaded the data can fail to load, making this object corrupt.
     * @return {@code true} if the file is corrupted.
     */
    public boolean isCorrupted() {
        return corrupted;
    }
}
