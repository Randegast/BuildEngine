package buildengine.graphics.rendering;

import buildengine.core.scene.Actor;
import buildengine.docs.NonNull;
import buildengine.graphics.sprite.SpriteMask;
import buildengine.graphics.sprite.Texture;
import buildengine.graphics.VAO;
import buildengine.math.MathUtils;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL30.*;

/**
 * An object responsible rendering a collection of vao's with one draw-call.
 */
public class RenderBatch implements Comparable<RenderBatch> {

    public static final int TEXTURE_LIMIT = 10; // All graphics cards are guaranteed to have at least 16 texture channels
    public static final int[] TEXTURE_CHANNELS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    public static final int VERTEX_COUNT = 4;

    public final int VERTEX_SIZE = 3;  // float x, float y, float z
    public final int COLOUR_SIZE = 4;  // float r, float g, float b, float a
    public final int TEXTURE_SIZE = 3; // float UV1, float UV2, float ID

    private final int zIndex;
    private final int maxBatchSize;
    private final SpriteMask[] masks;
    private int maskCount;

    private int vaoId, vboId, cboId, tboId, ebo;
    private final float[] vertices;
    private final float[] colours;
    private final float[] textures;

    private final List<Texture> textureCache;

    public RenderBatch(int zIndex, int maxBatchSize) {
        this.zIndex = zIndex;
        this.maxBatchSize = maxBatchSize;

        masks = new SpriteMask[maxBatchSize];
        maskCount = 0;

        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];
        colours = new float[maxBatchSize * 4 * COLOUR_SIZE];
        textures = new float[maxBatchSize * 4 * TEXTURE_SIZE];

        textureCache = new ArrayList<>();
    }

    public void createVertexArray() {
        // Creating VAO
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Vertex streaming
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(0, VERTEX_SIZE, GL_FLOAT, false, 0, 0);

        // Colour streaming
        cboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, cboId);
        glBufferData(GL_ARRAY_BUFFER, (long) colours.length * Float.BYTES, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(1, COLOUR_SIZE, GL_FLOAT, false, 0, 0);

        // Texture streaming
        tboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, tboId);
        glBufferData(GL_ARRAY_BUFFER, (long) textures.length * Float.BYTES, GL_DYNAMIC_DRAW);
        glVertexAttribPointer(2, TEXTURE_SIZE - 1, GL_FLOAT, false, TEXTURE_SIZE * Float.BYTES, 0);
        // Separate pointer for ID
        glVertexAttribPointer(3, TEXTURE_SIZE - 2, GL_FLOAT, false, TEXTURE_SIZE * Float.BYTES, (TEXTURE_SIZE - 1) * Float.BYTES);

        // Element buffers
        ebo = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Unbind       note: DON'T FORGET TO REBIND WHEN RENDERING :$
        VAO.unbindBuffers();
    }

    public void update() {
        boolean bufferVertices = false, bufferSprites = false;
        for (int i = 0; i < maskCount; i++) {
            if(masks[i].getOwner().isDynamic()) {
                loadVertexProperties(i);
                bufferVertices = true;
            }
            if(masks[i].isDirty()) {
                loadSpriteProperties(i);
                bufferSprites = true;
                masks[i].setDirty(false);
            }
        }

        if(bufferVertices) bufferVertices();
        if(bufferSprites) bufferSprites();

        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void bufferVertices() {
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
    }

    public void bufferSprites() {
        glBindBuffer(GL_ARRAY_BUFFER, cboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, colours);
        glBindBuffer(GL_ARRAY_BUFFER, tboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, textures);
    }

    public void render() {
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        for (int i = 0; i < textureCache.size(); i++) {
            glActiveTexture(GL_TEXTURE0 + i);
            textureCache.get(i).bind();
        }

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ebo);

        glDrawElements(GL_TRIANGLES, maskCount * 6, GL_UNSIGNED_INT, 0);

        for (Texture texture : textureCache)
            texture.unbind();

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glBindVertexArray(0);
    }

    public void adopt(SpriteMask mask) {
        if(maskCount >= maxBatchSize)
            throw new IllegalStateException("RenderBatch exceeds defined batch size of '" + maxBatchSize + "'");
        if(textureCache.size() >= TEXTURE_LIMIT)
            throw new IllegalStateException("RenderBatch exceeds texture limit of " + TEXTURE_LIMIT);

        masks[maskCount] = mask;

        if(mask.getSprite().getTexture() != null && !textureCache.contains(mask.getSprite().getTexture()))
            textureCache.add(mask.getSprite().getTexture());

        loadVertexProperties(maskCount);
        loadSpriteProperties(maskCount);

        maskCount++;
    }

    /**
     * Render Batch Translations
     * @param index
     */
    private void loadVertexProperties(int index) {
        SpriteMask mask = masks[index];
        Actor actor = mask.getOwner();

        // Find offset within array (4 vertices per sprite)
        int offset = index * 4 * VERTEX_SIZE;

        // Add vertices with the appropriate properties
        for (int i=0; i < VERTEX_COUNT; i++) {
            float xAdd = -mask.getAnchor().x;
            float yAdd = -mask.getAnchor().y;
            if (i == 1 || i == 2)
                xAdd += 1.0f;
            if (i == 2 || i == 3)
                yAdd += 1.0f;

            // Load position
            Vector2f vertex = MathUtils.rotatePoint(actor.getTransform().rotation, actor.getTransform().getPosition(), new Vector2f(
                    actor.getTransform().position.x + (xAdd * actor.getTransform().getWidth()),
                    actor.getTransform().position.y + (yAdd * actor.getTransform().getHeight())));
            vertices[offset] = vertex.x;
            vertices[offset + 1] = vertex.y;

            offset += VERTEX_SIZE;
        }
    }

    private void loadSpriteProperties(int index) {
        SpriteMask sprite = this.masks[index];

        int colorOffset = index * 4 * COLOUR_SIZE;
        int textureOffset = index * 4 * TEXTURE_SIZE;

        Vector4f color = sprite.getColour();
        int textureId = -1;
        if(sprite.getSprite().getTexture() != null)
            for(int i = 0; i < textureCache.size(); i++)
                if(textureCache.get(i) == sprite.getSprite().getTexture())
                    textureId = i;

        for (int i=0; i < VERTEX_COUNT; i++) {
            // Load color
            colours[colorOffset] = color.x;
            colours[colorOffset + 1] = color.y;
            colours[colorOffset + 2] = color.z;
            colours[colorOffset + 3] = color.w;

            // Load texture
            textures[textureOffset] = sprite.getSprite().getUvCoordinates()[i].x;
            textures[textureOffset + 1] = sprite.getSprite().getUvCoordinates()[i].y;
            textures[textureOffset + 2] = (float) textureId;

            colorOffset += COLOUR_SIZE;
            textureOffset += TEXTURE_SIZE;
        }
    }

    @NonNull
    private int[] generateIndices() {
        int[] indices = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++) {
            int arrayOffset = i * 6;
            int offset = i * 4;

            indices[arrayOffset] = offset;
            indices[arrayOffset + 1] = offset + 1;
            indices[arrayOffset + 2] = offset + 2;
            indices[arrayOffset + 3] = offset + 2;
            indices[arrayOffset + 4] = offset + 3;
            indices[arrayOffset + 5] = offset;
        }
        return indices;
    }

    public boolean isFull() {
        return maskCount >= maxBatchSize;
    }

    public boolean canAdopt(SpriteMask mask) {
        if(isFull())
            return false;
        if(mask.getZIndex() != zIndex)
            return false;
        if(mask.getSprite().getTexture() == null)
            return true;
        return textureCache.size() < TEXTURE_LIMIT || textureCache.contains(mask.getSprite().getTexture());
    }

    public int getzIndex() {
        return zIndex;
    }

    @Override
    public int compareTo(RenderBatch o) {
        return Integer.compare(zIndex, o.zIndex);
    }
}
