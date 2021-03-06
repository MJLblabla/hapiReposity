package com.hapi.datasource.cache


import android.net.Uri
import android.text.TextUtils
import com.alibaba.fastjson.util.ParameterizedTypeImpl
import com.hapi.datasource.JsonUtil
import com.hapi.ut.EncryptUtil
import com.hapi.ut.SpUtil

import java.lang.reflect.Type

/**
 * 采用sp储存
 * urlKey
 * cacheTime　缓存时间
 */
class SpCacheProvide<R>( val key: String, private val cacheTime: Long = -1, val clazz: Type) : LocalCacheProvide<R> (key){


    private fun getParameterType(): Type {
        val p = ParameterizedTypeImpl(arrayOf<Type>(clazz), CacheData::class.java, CacheData::class.java)
        return p
    }


    private fun getSpType(): Type {
        return  getParameterType()

    }

    override fun saveToLocal(data: R) {
        val urlKey = cacheUriKey
        val temp = CacheData(data, System.currentTimeMillis())
        SpUtil.get(md5Url(urlKey)).saveData("SpCacheProvide_$urlKey", JsonUtil.toJson(temp))
    }

    override fun loadFromLocal(): R? {
        val urlKey = cacheUriKey
        val now = System.currentTimeMillis()
        val jsonStr = SpUtil.get(md5Url(urlKey)).readString("SpCacheProvide_$urlKey", "")
        val temp = if (TextUtils.isEmpty(jsonStr)) null else JsonUtil.fromJson<CacheData<R>>(jsonStr, getSpType())

        if (cacheTime < 0) {
            return temp?.data as R?
        }
        temp?.time?.let {
            if (now - it < cacheTime) {
                return temp.data as R?
            }
        }
        return null
    }

    private fun md5Url(url: String): String {
        return EncryptUtil.MD5(url)
    }


}