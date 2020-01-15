package com.hapi.datasource;

import com.hapi.datasource.cache.Utils;
import retrofit2.Retrofit;

public abstract class BaseReposity<T> {

    protected T apiService;

    public BaseReposity() {
        apiService = ReposityManager.get().getRetrofitClient().create((Class<? extends T>) Utils.findNeedType(getClass()));
    }
}
