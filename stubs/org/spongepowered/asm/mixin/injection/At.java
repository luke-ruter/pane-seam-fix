package org.spongepowered.asm.mixin.injection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Compile-time stand-in. Retention and element names match the real Mixin annotation.
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface At {
    String value();

    String target() default "";

    boolean remap() default true;
}
