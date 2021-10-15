package com.minshigee.playerchanger.domain.annitation;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD
})

public @interface NeedOP {
    boolean value() default true;
}
