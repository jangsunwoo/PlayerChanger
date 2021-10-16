package com.minshigee.playerchanger.domain.annotation;

import com.minshigee.playerchanger.domain.GameState;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MappingEvent {
    GameState[] states() default {GameState.Enable};
}
