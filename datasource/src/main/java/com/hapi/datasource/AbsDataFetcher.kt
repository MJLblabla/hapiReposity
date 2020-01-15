package com.hapi.datasource

import com.hapi.datasource.cache.LocalCacheProvide

public abstract class AbsDataFetcher<T>(private var cacheProvide: LocalCacheProvide<T>? = null) {


    /**
     * 本地请求
     */
    fun localRequest(): T? {
        return cacheProvide?.loadFromLocal()
    }


    /**
     * 存储缓存
     */
    fun saveToLocal(data: T) {
        cacheProvide?.saveToLocal(data)
    }
}