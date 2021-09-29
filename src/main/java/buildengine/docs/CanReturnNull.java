package buildengine.docs;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This method can return null.
 */
@Documented
@Target({ElementType.METHOD})
public @interface CanReturnNull {}
