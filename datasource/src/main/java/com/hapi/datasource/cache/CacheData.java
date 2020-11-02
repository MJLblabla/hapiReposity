package com.hapi.datasource.cache;

public class CacheData<R> {

    private R data = null;
    private long time = -1L;

    public CacheData(){

    }

    public CacheData( R data ,long time){
        this.data = data;
        this.time = time;
    }

    public R getData() {
        return data;
    }

    public void setData(R data) {
        this.data = data;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
