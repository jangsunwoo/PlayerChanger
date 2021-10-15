package com.minshigee.playerchanger.domain.module;

public class Controller<T> {

    protected T repository;

    public Controller(T repository) {
        this.repository = repository;
    }
}
