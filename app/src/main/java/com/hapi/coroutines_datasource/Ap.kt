package com.hapi.coroutines_datasource

import com.hapi.datasource.cache.LocalCacheProvide
import com.qizhou.annotation.Provider

@Provider(providerKey = "aaa")
class Ap(key:String) : LocalCacheProvide<List<User>>(key) {
    /**
     * 获得缓存数据
     */
    override fun loadFromLocal(): List<User>? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 存储方案
     */
    override fun saveToLocal(data: List<User>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}