package com.hapi.coroutines_datasource

import com.qizhou.annotation.AutoApi
import com.qizhou.annotation.AutoApiWithCache
import com.qizhou.annotation.SpCache
import io.reactivex.Observable

interface TestService {

    @AutoApi
   suspend fun a(a:Int):List<List<User>>

    @SpCache
    suspend fun b(a:Int):Int


    @AutoApiWithCache(providerKey = "aaa")
    suspend fun c(a:Int):List<User>


    @AutoApiWithCache(providerKey = "aaa")
     fun d(a:Int):Observable<List<User>>



}