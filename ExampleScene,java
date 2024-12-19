import buildengine.BuildEngine;
import buildengine.engine.Debug;
import buildengine.engine.Display;
import buildengine.engine.graphics.animation.Sprite;
import buildengine.engine.stage.director.BasicCharacterController;
import buildengine.engine.stage.scene.Scene;
import buildengine.engine.stage.scene.actor.Actor;
import buildengine.math.Transform;
import buildengine.utils.AssetManager;

public class Example {

    public static void main(String[] args) {
        // Print a message checking if BuildEngine is working correctly.
        System.out.println(BuildEngine.welcome());

        // Create the engine window.
        BuildEngine.create("Example Window", Display.RESOLUTION_720p, false, true);
        // Enable debug mode, showing the FPS and Scene
        Debug.enable(Debug.FPS_AND_STAGE, true, true);

        // Load a custom scene into the current stage.
        BuildEngine.getEngine().getStage().loadScene(new SampleScene());
    }

    // Your custom scene. Scenes are collections of Actors, the objects in your game.
    public static class SampleScene extends Scene {

        // Overwrite constructor to add our own stuff and settings to this scene.
        public SampleScene() {
            // Give your scene a name
            super("Sample Scene");

            // Create an Actor (object) called "Player", give it a Transform (this is its position, and size), and give
            // it a Sprite object. This contains the drawing information. In this case we are adding a single image
            // to the actor. But we can give more parameters to add multiple, or import an Animation object.
            Actor player = new Actor("Sample Player", new Transform(0, 0, 1, 1),
                    new Sprite(AssetManager.getImage("path/to/player/image.png")));

            // Actually add the Player Actor to the scene.
            add(player);
            // Add the BasicCharacterController director. Directors are responsible for functionality in the scene.
            // This is a pre-made director that comes with BuildEngine, and gives the user the ability to move an
            // Actor around the scene.
            addDirector(new BasicCharacterController(player, BasicCharacterController.Mode.WASD));
        }
    }
}
