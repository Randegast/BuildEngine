package buildengine.core.rendering;

import buildengine.BuildEngine;
import buildengine.core.scene.director.Director;
import buildengine.core.scene.director.Loader;
import buildengine.core.scene.director.Renderer;
import buildengine.graphics.Shader;
import buildengine.graphics.sprite.SpriteMask;
import buildengine.utils.ResourceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the system that uses batches to render a scene. Director.
 *
 * @see Director
 * @see RenderBatch
 */
public class BatchRenderer extends Director implements Renderer, Loader {

    public final int MAX_BATCH_SIZE = 1000;

    private final List<SpriteMask> masks;
    private final List<RenderBatch> batches;
    private Shader shader;

    public BatchRenderer() {
        batches = new ArrayList<>();
        masks = new ArrayList<>();
    }

    @Override
    public void begin() {
        shader = ResourceManager.getShader("assets/shaders/default.glsl");
        ResourceManager.getShader("assets/shaders/background.glsl");
        shader.setUniformMat4f("projectionMatrix", BuildEngine.getEngine().getStage().getCamera().getProjectionMatrix());

        for(SpriteMask mask : scene.getComponents(SpriteMask.class))
            add(mask);

        for(RenderBatch batch : batches) {
            batch.bufferVertices();
            batch.bufferSprites();
        }
    }

    @Override
    public void render() {
        boolean addition = false;
        for(SpriteMask mask : scene.getComponents(SpriteMask.class))
            if(!masks.contains(mask)) {
                addition = true;
                add(mask);
            }
        if(addition)
            for(RenderBatch batch : batches) {
                batch.bufferVertices();
                batch.bufferSprites();
            }

        for(RenderBatch batch : batches)
            batch.update();

        shader.setUniformMat4f("viewMatrix", BuildEngine.getEngine().getStage().getCamera().getViewMatrix());
        shader.setUniform1iv("uTexture", RenderBatch.TEXTURE_CHANNELS);

        for(RenderBatch batch : batches)
            batch.render();

        shader.disable();
    }

    public void add(SpriteMask mask) {
        masks.add(mask);
        for(RenderBatch renderBatch : batches) {
            if(!renderBatch.canAdopt(mask))
                continue;
            renderBatch.adopt(mask);
            return;
        }
        // Creating batches. This is the only place that happens
        RenderBatch batch = new RenderBatch(mask.getZIndex(), MAX_BATCH_SIZE);
        batch.createVertexArray();
        batches.add(batch);
        batch.adopt(mask);
        Collections.sort(batches);
    }
}
