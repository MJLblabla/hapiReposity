package com.hapi.datasource;

import com.hapi.datasource.cache.Utils;
import com.pince.renovace2.Renovace;

public abstract class BaseReposity<T> {

    protected T apiService;

    public BaseReposity() {
        apiService = Renovace.create((Class<? extends T>) Utils.findNeedType(getClass()));
    }
}
