package com.hapi.repositycompiler.enitity

import com.hapi.repositycompiler.aptUtils.logger.Logger
import com.hapi.repositycompiler.aptUtils.types.javaToKotlinType
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec

/**
 * reposity方法
 */
abstract class FuncBuilder(val reposityMothed :ReposityMothed) {

    fun build(typeBuilder: TypeSpec.Builder){
        Logger.warn("FuncBuilder.FuncBuilder    FuncBuilder"+reposityMothed.returnType.toString())
        reposityMothed.build()
        val funcBuilder = FunSpec.builder(reposityMothed.methodName)
                .addModifiers(KModifier.PUBLIC)
                .returns(reposityMothed.returnType.javaToKotlinType())

        if(reposityMothed.addSuspend){
            funcBuilder.addModifiers(KModifier.SUSPEND)
        }

        reposityMothed.parameters.forEach {

            val paramSpecBuilder = ParameterSpec.builder(it.name, it.type)
            //默认参数
            it.defaultValue?.let {
                defaultValue->
                paramSpecBuilder.defaultValue("%L", defaultValue)
            }
            funcBuilder.addParameter(paramSpecBuilder.build())
        }

        addStatement(funcBuilder)
        typeBuilder.addFunction(funcBuilder.build())
    }

    abstract fun addStatement(funcBuilder: FunSpec.Builder)

}