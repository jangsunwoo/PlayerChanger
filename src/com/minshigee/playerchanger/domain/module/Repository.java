package com.minshigee.playerchanger.domain.module;

public class Repository<T> {
    protected T localDB;

    public Repository(T localDB) {
        this.localDB = localDB;
    }

}
