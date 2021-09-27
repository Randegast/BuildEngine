package buildengine.graphics;

import static org.lwjgl.opengl.GL30.*;

public class VertexArray {

    public static final int VERTEX_INDEX = 0, VERTEX_SIZE = 3;
    public static final int COLOUR_INDEX = 1, COLOUR_SIZE = 4;
    public static final int TEXTURE_INDEX = 2, TEXTURE_SIZE = 2;

    private final int vao, ebo;
    private final int indicesLength;

    public VertexArray(int vao, int ebo, int indicesLength) {
        this.vao = vao;
        this.ebo = ebo;
        this.indicesLength = indicesLength;
    }

    public void bind() {
        glBindVertexArray(vao);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
    }

    public void unbind() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void draw() {
        glDrawElements(GL_TRIANGLES, indicesLength, GL_UNSIGNED_BYTE, 0);
    }

    public void render() {
        bind();
        draw();
        unbind();
    }

}
