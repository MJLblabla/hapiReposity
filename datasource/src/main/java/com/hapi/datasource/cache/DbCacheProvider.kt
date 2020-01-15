package com.pince.datasource.cache

import com.hapi.datasource.cache.LocalCacheProvide


/**
 * @author athoucai
 * @date 2018/11/19
 */
class DbCacheProvider<T>(private var db: DbProvider<T>) : LocalCacheProvide<T> {

    override fun loadFromLocal(): T? {
        return db.queryCache()
    }

    override fun saveToLocal(data: T) {
        db.insertCache(data)
    }

    interface DbProvider<T> {

        fun queryCache(): T

        fun insertCache(data: T)
    }
}