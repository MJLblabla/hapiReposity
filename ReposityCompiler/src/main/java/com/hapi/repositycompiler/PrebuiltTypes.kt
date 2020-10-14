package com.hapi.repositycompiler

import com.squareup.kotlinpoet.ClassName


const val spUrlKey:String = "urlKey"
val StringClassName = ClassName("kotlin", "String")
const val ObservableSimpleName = "Observable"

val BaseReposityClasType = ClassName("com.hapi.datasource","BaseReposity")

val FlowClassType =  ClassName(" kotlinx.coroutines.flow","Flow")
val ContinuationType = ClassName("kotlin.coroutines","Continuation")
val FlowDataSourceClassType = ClassName("com.hapi.datasource","FlowDataFetcher")
val RxDataSourceClassType = ClassName("com.hapi.datasource","RxDataFetcher")
val SpCacheHttpClassName = ClassName("com.hapi.datasource.cache","SpCacheProvide")
val FetchStrategyClassName = ClassName("com.qizhou.annotation","NetworkFetchStrategy")

val ParameterizedTypeImplClassName = ClassName("com.alibaba.fastjson.util","ParameterizedTypeImpl")

