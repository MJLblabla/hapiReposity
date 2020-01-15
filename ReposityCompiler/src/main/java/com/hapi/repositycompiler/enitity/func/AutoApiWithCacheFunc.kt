package com.hapi.repositycompiler.enitity.func


import com.hapi.repositycompiler.FetchStrategyClassName
import com.hapi.repositycompiler.aptUtils.logger.Logger
import com.hapi.repositycompiler.enitity.FuncBuilder
import com.squareup.kotlinpoet.FunSpec

class AutoApiWithCacheFunc (private val mReposityMothed : AutoApiWithCacheMethod)  : FuncBuilder(mReposityMothed) {


    override fun addStatement(funcBuilder: FunSpec.Builder) {
        val paramsStringBuilder = StringBuilder()
        reposityMothed.parameters.forEach {
            paramsStringBuilder.append(it.name).append(",")
        }
        Logger.warn("addStatement.addStatement  SpCacheFuncSpCacheFunc")
        funcBuilder.addStatement(" return %T(%T(),%T.%L){\n " +
                "apiService.%L(%L)"

                + "\n}.startFetchData()"
            , mReposityMothed.dataFetcherName!!
            ,  mReposityMothed.provideClass!!

            , FetchStrategyClassName
            , mReposityMothed.fetchStrategy
            , reposityMothed.methodName
            , paramsStringBuilder.toString().dropLast(1)
           )
    }

}