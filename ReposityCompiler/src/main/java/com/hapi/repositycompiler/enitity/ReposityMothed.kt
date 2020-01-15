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
import java.util.*
import javax.lang.model.element.ExecutableElement


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

                    returnType = ctType.typeArguments[0].asNonNullable()


//                    val typeStr =  returnType.toString()
//                    val index = typeStr.lastIndexOf('.')
//                    Logger.warn("returnType    xiechen  "+typeStr)
//                    returnType = ClassName(typeStr.substring(3,index),typeStr.substring(index+1,typeStr.length))
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
    }


    /**
     * 参数
     */
    inner class Parameter(var name: String, val type: TypeName, val defaultValue: String? = null)
}