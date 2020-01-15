package com.hapi.datasource;

import java.util.LinkedHashMap;

import retrofit2.Retrofit;

public class ReposityManager {
    private static ReposityManager instance = null;
    private LinkedHashMap<Class<? extends BaseReposity>, BaseReposity> repoMap = null;

    private Retrofit retrofit;

    private ReposityManager() {
        repoMap = new LinkedHashMap<>();
    }

    public static ReposityManager get() {
        synchronized (ReposityManager.class) {
            if (instance == null) {
                instance = new ReposityManager();
            }
            return instance;
        }
    }

    public Retrofit getRetrofitClient(){
        return retrofit;
    }


    public void setRetrofitClient(Retrofit retrofit){
        this.retrofit = retrofit;
    }

    public void addRepo(BaseReposity<?> repo) {
        repoMap.put(repo.getClass(), repo);
    }

    public <T> T getRepo(Class<T> cls) {
        if (BaseReposity.class.isAssignableFrom(cls)) {
            BaseReposity repo = repoMap.get(cls);
            if (repo == null) {
                try {
                    repo = (BaseReposity) cls.newInstance();
                    addRepo(repo);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return (T) repo;
        }
        throw new IllegalArgumentException("the class must extend BaseReposity");
    }

    public void clear() {
        repoMap.clear();
    }
}