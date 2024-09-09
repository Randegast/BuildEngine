package buildengine.configuration;

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
 * File containing a configuration.
 * @see Configuration
 */
public class ConfigurationFile {
    
    private final File file;
    private final Configuration configuration;
    private boolean corrupted;
    
    public ConfigurationFile(File file) {
        this.file = file;
        configuration = new Configuration();
        if(file.exists())
            load(file);
    }
    
    public void load(File file) {
        String raw = "";
    
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(file.toURI()));
            raw = new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error while reading the datafile: " + file.getPath());
        }
    
        raw = raw.replaceAll("\\s+","");
        for(String value : raw.split(";")) {
            if(!value.contains(":"))
                continue;
            String[] str = value.split(":");
            if(str.length < 2) {
                System.err.println("Error while reading data file: " + file.getName() +
                        ". Expected 2 values, found " + str.length + "(" + value + ")");
                corrupted = true;
                continue;
            }
            if(str.length > 2) {
                corrupted = true;
                System.err.println("Warning: Value in data file can't contain more than two ':' characters. File: "
                        + file.getName() + ", line: " + value);
            }

            configuration.set(str[0], str[1]);
        }
    }
    
    public void save() {
        if(!file.exists()) {
            try {
                if (!file.createNewFile())
                    // IDK how you ever end up here...
                    throw new IllegalStateException("The DataFile " + file.getName() +
                            " no longer exists, and creating a new one failed because it already exist.");
            } catch (IOException e) {
                System.err.println("The DataFile " + file.getName() + " no longer exists, and creating a new one failed.");
            }
        }
        
        if(corrupted) {
            System.err.println("The file " + file.getName() + " couldn't be saved, because the data was corrupted.");
            return;
        }
        
        List<String> list = new ArrayList<>();
        
        for(String key : configuration.getData().keySet()) {
            list.add(key + ": " + configuration.getData().get(key) + ";");
        }
    
        try (PrintWriter out = new PrintWriter(file)) {
            for(String line : list)
                out.println(line);
        } catch (FileNotFoundException e) {
            System.err.println("Error while trying to safe " + file.getName() + " data file: Could find the file.");
        }
    }
    
    // Values
    
    public File getFile() {
        return file;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public boolean isCorrupted() {
        return corrupted;
    }
}
