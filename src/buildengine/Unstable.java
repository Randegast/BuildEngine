package buildengine;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;

/**
 * This element is unstable and is not yet (fully) supported by the engine.
 * It can have bad implementation or not work properly with other systems.
 */
@Documented
@Inherited
public @interface Unstable {
    String concerning() default "this element";
}
