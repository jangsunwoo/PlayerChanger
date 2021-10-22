package com.minshigee.playerchanger.domain.annotation;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({
        ElementType.METHOD
})

public @interface MappingChange {
}
