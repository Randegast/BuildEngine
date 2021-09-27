package buildengine.graphics;

import buildengine.math.BufferFactory;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static buildengine.graphics.VertexArray.*;

public class VAO {

    private VAO() {}

    public static VertexArray load(float[] vertices, float[] colors, float[] textures, byte[] indices, int drawMode) {
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);

        VAO.bindFloatBuffer(VERTEX_INDEX, VERTEX_SIZE, vertices, drawMode);
        VAO.bindFloatBuffer(COLOUR_INDEX, COLOUR_SIZE, colors, drawMode);
        VAO.bindFloatBuffer(TEXTURE_INDEX, TEXTURE_SIZE, textures, drawMode);
        int ebo = VAO.bindElementBuffer(indices);

        VAO.unbindBuffers();
        return new VertexArray(vao, ebo, indices.length);
    }

    public static int bindFloatBuffer(int location, int size, float[] bufferData, int drawMode) {
        int id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, BufferFactory.createFloatBuffer(bufferData), drawMode);
        glVertexAttribPointer(location, size, GL_FLOAT, false, 0, 0);
        return id;
    }

    public static int bindFloatBuffer(int location, int size, long byteLength, int drawMode) {
        int id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, byteLength, drawMode);
        glVertexAttribPointer(location, size, GL_FLOAT, false, 0, 0);
        return id;
    }

    public static int bindElementBuffer(byte[] indices) {
        int ebo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, BufferFactory.createByteBuffer(indices), GL_STATIC_DRAW);
        return ebo;
    }

    public static void unbindBuffers() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
}
