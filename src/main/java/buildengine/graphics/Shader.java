package buildengine.graphics;

import buildengine.math.BufferFactory;
import buildengine.utils.ShaderLoader;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

/**
 * Abstract shader object.
 */
public class Shader {

    /** Invalid id */
    private static final int INVALID = -1;

    /** OpenGL Shader program ID */
    private final int id;
    /** Cache for saving uniform locations */
    private final Map<String, Integer> locationCache;

    /** The validation status of this shader */
    private final boolean valid;

    /** Create abstract shader. Program must be created in ShaderUtils.
     * @see ShaderLoader
     **/
    public Shader(int id) {
        this.id = id;
        locationCache = new HashMap<>();
        valid = id != INVALID;
    }

    // Functionality

    /** Use shader program */
    public void enable() {
        if(isInvalid())
            return;
        glUseProgram(id);
    }

    /** Clear shader program */
    public void disable() {
        if(isInvalid())
            return;
        glUseProgram(0);
    }

    // Uniform variable handling

    /** Returns the location of a specific uniform variable. */
    private int getUniformLocation(String uniformName) {
        if(isInvalid())
            return 0;

        if(locationCache.containsKey(uniformName))
            return locationCache.get(uniformName);

        int result = glGetUniformLocation(id, uniformName);
        if(result == -1)
            System.err.println("Could not find uniform variable '" + uniformName + "'!");
        else
            locationCache.put(uniformName, result);
        return glGetUniformLocation(id, uniformName);
    }

    /** Uploads uniform variable */
    public void setUniform1i(String name, int value) {
        if(isInvalid())
            return;
        enable();
        glUniform1i(getUniformLocation(name), value);
    }

    /** Uploads int array uniform variable */
    public void setUniform1iv(String name, int[] value) {
        if(isInvalid())
            return;
        enable();
        glUniform1iv(getUniformLocation(name), value);
    }

    /** Uploads uniform variable */
    public void setUniform1f(String name, float value) {
        if(isInvalid())
            return;
        enable();
        glUniform1f(getUniformLocation(name), value);
    }

    /** Uploads uniform variable */
    public void setUniform2f(String name, Vector2f value) {
        if(isInvalid())
            return;
        enable();
        glUniform2f(getUniformLocation(name), value.x, value.y);
    }

    /** Uploads uniform variable */
    public void setUniform3f(String name, Vector3f value) {
        if(isInvalid())
            return;
        enable();
        glUniform3f(getUniformLocation(name), value.x, value.y, value.z);
    }

    /** Uploads uniform variable */
    public void setUniform4f(String name, Vector4f value) {
        if(isInvalid())
            return;
        enable();
        glUniform4f(getUniformLocation(name), value.x, value.y, value.z, value.w);
    }

    /** Uploads uniform variable */
    public void setUniformMat4f(String name, Matrix4f value) {
        if(isInvalid())
            return;
        enable();
        glUniformMatrix4fv(getUniformLocation(name), false, BufferFactory.createFloatBuffer(value));
    }

    // Validating

    /** Returns false if the shader program is inactive.
     * @return {@code true} - if the program is not in use. */
    public boolean isInvalid() {
        return !valid;
    }

}
