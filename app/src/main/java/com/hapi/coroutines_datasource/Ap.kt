package com.hapi.coroutines_datasource

import com.hapi.datasource.cache.LocalCacheProvide
import com.qizhou.annotation.Provider

@Provider(providerKey = "aaa")
class Ap : LocalCacheProvide<User> {
    /**
     * 存储方案
     */
    override fun saveToLocal(data: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 获得缓存数据
     */
    override fun loadFromLocal(): User? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}