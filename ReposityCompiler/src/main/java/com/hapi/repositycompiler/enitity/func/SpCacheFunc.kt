package com.hapi.repositycompiler.enitity.func

import com.hapi.repositycompiler.FetchStrategyClassName
import com.hapi.repositycompiler.SpCacheHttpClassName
import com.hapi.repositycompiler.aptUtils.logger.Logger
import com.hapi.repositycompiler.enitity.FuncBuilder
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterizedTypeName

class SpCacheFunc (private val mReposityMothed : SpCacheMethod)  : FuncBuilder(mReposityMothed)  {


    override fun addStatement(funcBuilder: FunSpec.Builder) {
        val paramsStringBuilder = StringBuilder()
        reposityMothed.parameters.forEach {
            paramsStringBuilder.append(it.name).append(",")
        }
        Logger.warn("addStatement.addStatement  SpCacheFuncSpCacheFunc")

        funcBuilder.addStatement(" return %T(%T(%S,%L,%T::class.java),%T.%L){\n " +
                "apiService.%L(%L)"
                + "\n}.startFetchData()"
            , mReposityMothed.dataFetcherName!!
            , SpCacheHttpClassName
            , mReposityMothed.key
            , mReposityMothed.cacheTime
            , (mReposityMothed.returnType as ParameterizedTypeName).typeArguments[0]
            , FetchStrategyClassName
            , mReposityMothed.fetchStrategy
            , reposityMothed.methodName
            , paramsStringBuilder.toString().dropLast(1))
    }


}