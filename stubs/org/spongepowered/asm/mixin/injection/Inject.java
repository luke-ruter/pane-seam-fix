package org.spongepowered.asm.mixin.injection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Compile-time stand-in. Retention and element names match the real Mixin annotation.
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
    String id() default "";

    String[] method() default {};

    At[] at();

    boolean cancellable() default false;

    boolean remap() default true;

    int require() default -1;
}
