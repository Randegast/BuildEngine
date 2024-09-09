<h2>BuildEngine</h2>

This is the release version of BuildEngine. BuildEngine is a private project of mine. It's a lightweight java game engine.
Currently the project is split up into two builds; The normal build (ex. BuildEngine v1.1) uses java's build in render library.
The other one (BuildEngine-GL) uses OpenGL, and ImGui to improve performance and add a console and other debug options.
Eventually I'm planning to combine the two in a single build. 

<h3>BuildEngine</h3>

This version of the engine uses the build in java graphics library (AWT) for rendering. This version is the most stable.
Features:
 - A collection of core abstract classes based on ECS, capable of creating any game (see core-architecture).
 - Supports animations, custom rendering, but also an elaborate implementation for default rendering.
 - An advanced collision system, using SAT. Also a basic physics implementation. Both are customizable.
 - <i>A lot</i> of pre-made Directors and Components. 
 - Java rendering using an custom advanced Draw class, handling resizing, unit scaling and rotation.
 - Audio like sound effects and music handling.
 - Configuration file I/O, for easy writing and retrieving data.
 - The input system is capable of detecting press, hold and release for key and mouse plus many more features.
 - A small math library, containing full Vector2 clases, shapes and transforms used for the collision system.
 - Static event scheduler, so you can start timed or repeating events anywere.
 - Asset handling, like reading and saving in chache.
 - Easy and fun to program in :)

<hr>

<h3>BuildEngine GL</h3>

This version of the engine uses OpenGL graphics, and shader programs for rendering. This version is not stable, but more elaborate.
Features:
 - A collection of core abstract classes based on ECS, capable of creating any game (see core-architecture).
 - OpenGL rendering, using the LWJGL. Build in Shader programs. (Will be customisable in the future)
 - Includes the full math library of the JOML math library.
 - An advanced collision system, using SAT.
 - Pre-made Directors and Components. 
 - Console and debug window.
 - Configuration file I/O, for easy writing and retrieving data.
 - The input system is capable of detecting press, hold and release for key and mouse plus many more features.
 - Static event scheduler, so you can start timed or repeating events anywere.
 - Asset handling, like reading and saving in chache.
 - Easy and fun to program in :)

<hr>

<h3>Manual & PNG</h3>

The manual provided is a first draft for myself, but can be used for education purposes.

<br>
<br>

<b>INSTALLATION</b>

1. Download the desired JAR file. This contains the whole library.
2. Add this JAR file to your java project's dependices (using your IDE, or gradle). 
3. Now you can use BuildEngine in your projects.
