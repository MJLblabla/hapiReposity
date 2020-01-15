package com.hapi.datasource

import com.hapi.datasource.cache.LocalCacheProvide
import com.qizhou.annotation.NetworkFetchStrategy
import io.reactivex.Observable

/**
 * rx网络请求
 */
class RxDataFetcher<R>(
    private var cacheProvide: LocalCacheProvide<R>? = null,
    private var fetchStrategy: NetworkFetchStrategy? = NetworkFetchStrategy.OnlyRemote,
    private var remoteQuest: () -> Observable<R>
) : AbsDataFetcher<R>(cacheProvide) {

    /**
     * 请求网络或者读取缓存
     */
    fun startFetchData(): Observable<R> {
        var obs: Observable<R>? = null
        when (fetchStrategy) {
            NetworkFetchStrategy.OnlyCache -> {
                obs = Observable.just(localRequest())
            }
            NetworkFetchStrategy.OnlyRemote -> {
                obs = remoteRequest()
            }
            NetworkFetchStrategy.CacheFirst -> {
                obs = Observable.concat(localRequest2Rx(), remoteRequest().map {
                    saveToLocal(it)
                    it
                }).firstElement().toObservable();
            }

            NetworkFetchStrategy.Both -> {
                obs = Observable.concat(localRequest2Rx(), remoteRequest().map {
                    saveToLocal(it)
                    it
                })
            }

        }

        return obs!!
    }

    private fun localRequest2Rx(): Observable<R> {
        return Observable.create<R> {
            val local = localRequest()
            if (local != null) {
                it.onNext(local)
            }
            it.onComplete()
        }
    }

    /**
     * 网络请求
     */
    private fun remoteRequest(): Observable<R> {
        return remoteQuest.invoke()
    }
}