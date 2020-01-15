package com.hapi.datasource

import com.hapi.datasource.cache.LocalCacheProvide
import com.qizhou.annotation.NetworkFetchStrategy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


/**
 * 协程网络请求
 */
 class FlowDataFetcher<T>(
    private var cacheProvide: LocalCacheProvide<T>? = null,
    private var fetchStrategy: NetworkFetchStrategy? = NetworkFetchStrategy.OnlyRemote,
    private var remoteQuest: suspend () -> T
) : AbsDataFetcher<T>(cacheProvide) {

    fun startFetchData(): Flow<T> {

        val obs: Flow<T>
        when (fetchStrategy) {


            NetworkFetchStrategy.OnlyCache -> {
                obs = flow {
                    val localData = localRequest()
                    if (localData != null) {
                        emit(localData!!)
                    }
                }
            }
            NetworkFetchStrategy.OnlyRemote ->
                obs = flow {
                    emit(remoteRequest())
                }

            NetworkFetchStrategy.CacheFirst ->
                obs = flow {
                    val localData = localRequest()
                    if (localData != null) {
                        emit(localData!!)
                    } else {
                        val remote = remoteRequest()
                        saveToLocal(remote)
                        emit(remote)
                    }
                }


            NetworkFetchStrategy.Both ->
                obs = flow {
                    val localData = localRequest()
                    if (localData != null) {
                        emit(localData!!)
                    }
                    val remote = remoteRequest()
                    saveToLocal(remote)
                    emit(remote)

                }

            else -> obs = flow {
                emit(remoteRequest())
            }
        }
        return obs
    }

    /**
     * 网络请求
     */
    private suspend fun remoteRequest(): T {
        return remoteQuest.invoke()
    }

}