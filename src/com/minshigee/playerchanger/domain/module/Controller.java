package com.minshigee.playerchanger.domain.module;

import com.minshigee.playerchanger.domain.annotation.IsController;

@IsController
public class Controller<T> {

    protected T repository;
    protected boolean isAvailable = true;

    public boolean getIsAvailable(){
        return isAvailable;
    }

    public Controller(T repository) {
        this.repository = repository;
    }
}
