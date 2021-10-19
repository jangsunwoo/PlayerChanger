package com.minshigee.playerchanger.domain.module;

import com.minshigee.playerchanger.domain.annotation.IsRepository;
import com.minshigee.playerchanger.logic.view.ViewData;

@IsRepository
public class Repository<T> {
    protected Integer viewCode;
    protected T localDB;

    public Repository(T localDB, Integer viewCode) {this.localDB = localDB;this.viewCode = viewCode;}
    
}
