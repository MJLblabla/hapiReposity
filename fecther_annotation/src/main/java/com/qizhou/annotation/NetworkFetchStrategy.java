package com.qizhou.annotation;

public enum NetworkFetchStrategy {
    /**
     * 只请求缓存
     */
    OnlyCache("OnlyCache"),
    /**
     * 只请求网络
     */
    OnlyRemote("OnlyRemote"),
    /**
     * 优先缓存，缓存拿到就展示缓存，缓存没拿到就请求网络
     */
    CacheFirst("CacheFirst"),
    /**
     * 缓存和网络同时请求，数据会返回2次（如果都成功的话）
     */
    Both("Both");

    public String strategy = "OnlyRemote";

    NetworkFetchStrategy(String i) {
        strategy = i;
    }

}
