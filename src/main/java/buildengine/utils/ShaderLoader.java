package buildengine.utils;

import buildengine.graphics.Shader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

/**
 * Static class responsible for loading GLSL files and creating opengl shader programs.
 */
public class ShaderLoader {

    private ShaderLoader() {}

    /**
     * Load a shader from glsl or other text file. This function expects a file containing both a fragment and vertex
     * shader, defined with a line with type announcement. Sample syntax: {@code #type vertex}
     * @param path The path to the shader file
     * @return The created shader object
     */
    public static Shader load(String path) {
        String fileContents = FileUtils.readFile(path);

        if(fileContents == null) {
            System.err.println("ERROR: Unable to read file '" + path + "'. Does it exist?");
            return new Shader(-1);
        }

        String[] sourceCode = fileContents.split("(#type)( )+([a-zA-Z])+");

        if(sourceCode.length < 3) {
            System.err.println("ERROR: '" + path + "' contains less than 2 types. " +
                    "Expected #type vertex and #type fragment.");
            return new Shader(-1);
        }

        String vertexSource = null;
        String fragmentSource = null;

        // Find the first pattern after #type <pattern>
        int index = fileContents.indexOf("#type") + 6;
        int endOfLine = fileContents.indexOf("\r\n", index);
        String firstPattern = fileContents.substring(index, endOfLine).trim();

        // Find the second pattern after #type <pattern>
        index = fileContents.indexOf("#type", endOfLine) + 6;
        endOfLine = fileContents.indexOf("\r\n", index);
        String secondPattern = fileContents.substring(index, endOfLine).trim();

        // Resolving first pattern
        if(firstPattern.equalsIgnoreCase("vertex")) {
            vertexSource = sourceCode[1];
        } else if(firstPattern.equalsIgnoreCase("fragment")) {
            fragmentSource = sourceCode[1];
        } else {
            System.err.println("ERROR: Unexpected token '" + firstPattern + "' in '" + path + "'. " +
                    "#type can only be vertex or fragment.");
            return new Shader(-1);
        }

        // Resolving second pattern
        if(secondPattern.equalsIgnoreCase("vertex")) {
            vertexSource = sourceCode[2];
        } else if(secondPattern.equalsIgnoreCase("fragment")) {
            fragmentSource = sourceCode[2];
        } else {
            System.err.println("ERROR: Unexpected token '" + secondPattern + "' in '" + path + "'. " +
                    "Expected 'vertex' or 'fragment'.");
            return new Shader(-1);
        }

        // Checking for empty or missing sources
        if(vertexSource == null || vertexSource.isBlank()) {
            System.err.println("ERROR: '" + path + "' incomplete. Missing or empty vertex shader.");
            return new Shader(-1);
        }
        if(fragmentSource == null || fragmentSource.isBlank()) {
            System.err.println("ERROR: '" + path + "' incomplete. Missing or empty fragment shader.");
            return new Shader(-1);
        }

        // Creating and returning shader object
        return new Shader(create(vertexSource, fragmentSource));
    }

    /**
     * Load a shader from glsl or other text files. Takes in a vertex and fragment shader file.
     * @param vertexPath    Vertex file of the shader
     * @param fragmentPath  Fragment file of the shader
     * @return The created shader object
     */
    public static Shader load(String vertexPath, String fragmentPath) {
        String vert = FileUtils.readFile(vertexPath);
        String frag = FileUtils.readFile(fragmentPath);

        if(vert == null || vert.isBlank()) {
            System.err.println("ERROR: '" + vertexPath + "' incomplete. Missing or empty vertex shader.");
            return new Shader(-1);
        }

        if(frag == null || frag.isBlank()) {
            System.err.println("ERROR: '" + fragmentPath + "' incomplete. Missing or empty fragment shader.");
            return new Shader(-1);
        }

        return new Shader(create(vert, frag));
    }

    /**
     * Creates an OpenGL shader program, compiling and linking a vertex and fragment shader and
     * returning the ID of the new program.
     * @param vertex    A string containing the vertex shader GLSL code
     * @param fragment  A string containing the fragment shader GLSL code
     * @return The ID of the created shader program
     */
    private static int create(String vertex, String fragment) {
        if(vertex == null || fragment == null)
            throw new IllegalStateException("Tried creating a shader program while vertex or fragment is 'null'");

        int program = glCreateProgram();
        int vertID = glCreateShader(GL_VERTEX_SHADER);
        int fragID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(vertID, vertex);
        glShaderSource(fragID, fragment);

        glCompileShader(vertID);
        if(glGetShaderi(vertID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile vertex shader.");
            System.err.println(glGetShaderInfoLog(vertID));
            return -1;
        }
        glCompileShader(fragID);
        if(glGetShaderi(fragID, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println("Failed to compile fragment shader.");
            System.err.println(glGetShaderInfoLog(fragID));
            return -1;
        }

        glAttachShader(program, vertID);
        glAttachShader(program, fragID);
        glLinkProgram(program);
        glValidateProgram(program);

        glDeleteShader(vertID);
        glDeleteShader(fragID);

        return program;
    }

}
