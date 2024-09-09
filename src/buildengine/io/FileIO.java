package buildengine.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Static file read/write utility
 */
public class FileIO {

	private FileIO() {}

	public static void writeLinesToFile(String path, List<String> content) throws IOException {
		Path file = Paths.get(path);
		Files.write(file, content, StandardCharsets.UTF_8);
	}

	public static void writeStringToFile(String path, String string) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(path));
		writer.write(string);
		writer.close();
	}

	public static String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}

	public static String readFileOrNull(String path) {
		try {
			byte[] encoded = Files.readAllBytes(Paths.get(path));
			return new String(encoded);
		} catch(IOException e) {
			return null;
		}
	}

}
