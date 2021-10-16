package com.minshigee.playerchanger.domain.module;

import com.minshigee.playerchanger.domain.annotation.IsRepository;

@IsRepository
public class Repository<T> {
    protected T localDB;

    public Repository(T localDB) {
        this.localDB = localDB;
    }

}
