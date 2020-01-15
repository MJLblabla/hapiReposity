package com.hapi.datasource.cache


/**
 * 存储方法由调用处决定时，采用函数式编程代码简洁
 */
class SimpleChacheProvide<R>(private val saveCallResultFunc: (data: R) -> Unit, private val loadFromLocalFunc: () -> R?) : LocalCacheProvide<R> {

    override fun saveToLocal(data: R) {
        saveCallResultFunc.invoke(data)
    }

    override fun loadFromLocal(): R? {
        return loadFromLocalFunc.invoke()
    }

}