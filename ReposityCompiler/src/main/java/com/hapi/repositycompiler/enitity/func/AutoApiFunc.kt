package com.hapi.repositycompiler.enitity.func

import com.hapi.repositycompiler.enitity.FuncBuilder
import com.hapi.repositycompiler.enitity.ReposityMothed
import com.squareup.kotlinpoet.FunSpec

class AutoApiFunc(private val mReposityMothed: ReposityMothed) : FuncBuilder(mReposityMothed) {

    override fun addStatement(funcBuilder: FunSpec.Builder) {

        val paramsStringBuilder = StringBuilder()
        reposityMothed.parameters.forEach {
            paramsStringBuilder.append(it.name).append(",")
        }

        if(mReposityMothed.addSuspend){
            funcBuilder.addStatement("return apiService.%L(%L)"

                , reposityMothed.methodName
                , paramsStringBuilder.toString().dropLast(1))

        }else{
            funcBuilder.addStatement(" return %T{ \n apiService.%L(%L)"
                    + " \n}.startFetchData()"
                , mReposityMothed.dataFetcherName!!, reposityMothed.methodName
                , paramsStringBuilder.toString().dropLast(1))
        }
    }
}