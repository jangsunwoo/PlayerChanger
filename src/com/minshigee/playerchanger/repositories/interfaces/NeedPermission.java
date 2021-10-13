package com.minshigee.playerchanger.repositories.interfaces;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface NeedPermission {
    boolean needOp() default true;
}
