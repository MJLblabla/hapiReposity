package com.hapi.repositycompiler.enitity

import com.hapi.repositycompiler.*
import com.hapi.repositycompiler.aptUtils.logger.Logger
import com.hapi.repositycompiler.aptUtils.types.javaToKotlinType
import com.hapi.repositycompiler.aptUtils.types.simpleName
import com.qizhou.annotation.AutoApiWithCache
import com.qizhou.annotation.DbCache
import com.qizhou.annotation.SpCache
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.jvm.jvmWildcard
import java.util.*
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import kotlin.collections.ArrayList


/**
 * reposity的方法
 */
abstract class ReposityMothed(val executableElement: ExecutableElement) {


    val methodName = executableElement.simpleName.toString()
    val parameters = LinkedList<Parameter>()
    var returnType = executableElement.returnType.asTypeName()
    var isObservable = false
    var isSuspend = false
    var isUseCache = false
    var addSuspend = false
    var dataFetcherName: ClassName? = null

    var cacheKey:String=""

    abstract fun initParameters()

    open fun build() {

        initParameters()
        isUseCache =
            (executableElement.getAnnotation(SpCache::class.java) != null || executableElement.getAnnotation(
                AutoApiWithCache::class.java
            ) != null ||
                    executableElement.getAnnotation(DbCache::class.java) != null
                    )

        isObservable = executableElement.returnType.simpleName() == ObservableSimpleName

        if (!isObservable && !parameters.isEmpty()) {
            val ct = parameters[parameters.size - 1]
            val ctType = ct.type

            if (ctType.toString().contains(ContinuationType.simpleName)) {
                isSuspend = true

                if (ctType is ParameterizedTypeName) {

                    var returnTypeTemp = ctType.typeArguments[0].javaToKotlinType()
                    var returnTypeTempStr = returnTypeTemp.toString()

                    if (returnTypeTempStr.contains('<') && returnTypeTempStr.contains('>')) {

                        val returnTypeTempStrArray = returnTypeTempStr.split("<", ">")
                        val list = ArrayList<TypeName>()
                        returnTypeTempStrArray.forEach {
                            if (!it.isEmpty()) {
                                val string = it.replaceFirst("in ", "").replaceFirst("out ", "")
                                list.add(getClassName(string))
                            }
                        }

                        var typeName = list[list.size - 1]

                        for (i in list.size - 2 downTo 0) {
                            typeName = (list[i] as ClassName).parameterizedBy(typeName)
                        }

                        returnType = typeName


                    } else {
                        val typeStr = returnTypeTemp.toString()
                        returnType = getClassName(typeStr.replaceFirst("in ", ""))
                    }
                }
            }
        }
        if (isSuspend) {
            parameters.removeLast()
        }
        if (isSuspend && !isUseCache) {
            addSuspend = true
        } else {

            if (isObservable) {
                dataFetcherName = RxDataSourceClassType
            } else {
                returnType = (FlowClassType as ClassName).parameterizedBy(returnType)
                dataFetcherName = FlowDataSourceClassType
            }
        }

        var pStr =if(parameters.isEmpty()){""}else{"?"}
        parameters?.forEachIndexed { index, parameter ->
            var str ="${parameter.name}=\${${parameter.name}}"
            if(index!=parameters.size-1){
                str= "$str&"
            }
            pStr += str
        }
        cacheKey = "cache//${(executableElement.enclosingElement as TypeElement).qualifiedName}:80/${methodName}${pStr}"
    }


    private fun getClassName(typeStr: String): TypeName {
        val index = typeStr.lastIndexOf('.')

        return ClassName(
            typeStr.substring(0, index),
            typeStr.substring(index + 1, typeStr.length)
        ).javaToKotlinType()
    }


    /**
     * 参数
     */
    inner class Parameter(var name: String, val type: TypeName, val defaultValue: String? = null)
}