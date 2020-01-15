package com.hapi.coroutines_datasource

import  kotlinx.coroutines.flow.Flow
import com.hapi.datasource.BaseReposity
import com.hapi.datasource.FlowDataFetcher
import com.hapi.datasource.RxDataFetcher
import com.hapi.datasource.cache.SpCacheProvide
import com.qizhou.annotation.NetworkFetchStrategy
import io.reactivex.Observable
import kotlin.Int
import kotlin.collections.List

/**
 * This file is generated by kapt, please do not edit this file */
open class TestReposity : BaseReposity<TestService>() {
    suspend fun a(a: Int): List<List<User>> = apiService.a(a)

    fun c(a: Int): Flow<List<User>> {
         return FlowDataFetcher(Ap(),NetworkFetchStrategy.OnlyRemote){
                 apiService.c(a)
                }.startFetchData()
    }

    fun d(a: Int): Observable<List<User>> {
         return RxDataFetcher(Ap(),NetworkFetchStrategy.OnlyRemote){
                 apiService.d(a)
                }.startFetchData()
    }

    fun b(a: Int): Flow<Int> {
         return FlowDataFetcher(SpCacheProvide("b${a}",10800000,Int::class.java),NetworkFetchStrategy.CacheFirst){
                 apiService.b(a)
                }.startFetchData()
    }
}
