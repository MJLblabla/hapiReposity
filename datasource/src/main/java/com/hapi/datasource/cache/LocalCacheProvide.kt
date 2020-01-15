package com.hapi.datasource.cache



/**
 * 缓存方案
 */
interface LocalCacheProvide<R> {

    /**
     * 获得缓存数据
     */
     fun loadFromLocal(): R?

    /**
     * 存储方案
     */
     fun saveToLocal(data: R)

}