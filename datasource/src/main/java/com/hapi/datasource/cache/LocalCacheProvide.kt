package com.hapi.datasource.cache

import android.net.Uri


/**
 * 缓存方案
 *
 * @param cacheUriKey cache://类名／方法名？key=
 */
abstract class LocalCacheProvide<R>(val cacheUriKey:String) {

    /**
     * 获得缓存数据
     */
    abstract fun loadFromLocal(): R?

    /**
     * 存储方案
     */
   abstract  fun saveToLocal(data: R)



}