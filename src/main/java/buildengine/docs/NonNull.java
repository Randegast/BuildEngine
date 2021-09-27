package buildengine.docs;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * This element/method is guaranteed not te be null.
 */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface NonNull {}
