package com.hapi.repositycompiler.enitity.func

import com.hapi.repositycompiler.aptUtils.logger.Logger
import com.hapi.repositycompiler.aptUtils.types.asJavaTypeName
import com.hapi.repositycompiler.aptUtils.types.asKotlinTypeName
import com.hapi.repositycompiler.enitity.ReposityMothed
import com.qizhou.annotation.SpCache
import com.sun.tools.javac.code.Symbol
import javax.lang.model.element.ExecutableElement

class SpCacheMethod(private val symbol: Symbol.MethodSymbol, private val mExecutableElement: ExecutableElement) : ReposityMothed(mExecutableElement) {


    override fun initParameters() {
        val annotation: SpCache = executableElement.getAnnotation(SpCache::class.java)
        val defaultVMap = HashMap<String,String>()
        annotation.keys.forEachIndexed { index, s ->
            defaultVMap[s] = annotation.defaultValues[index]
        }
        executableElement.parameters.forEach {

            parameters.add(Parameter(it.simpleName.toString(), it.asType().asKotlinTypeName(),defaultVMap[it.simpleName.toString()]))

            Logger.warn("xxxxxxxxxxxxxxxxxxxx"+   (it as Symbol).type.toString() )
        }
    }

    var cacheTime = 0
    var fetchStrategy = "OnlyRemote"
    /**
     * ｓp的ｋｅｙ  默认以方法名和参数中的基本类型标志每一次不同的请求
     * 如果这个请求的参数
     */
    var key = methodName

    override fun build(){
        super.build()
        val spCache = symbol.getAnnotation(SpCache::class.java)
        cacheTime = spCache.cacheTime
        executableElement.parameters.forEach {
            val fieldType = it as Symbol.VarSymbol

            Logger.warn("SpCacheMethod.  ${ fieldType.simpleName}  ${fieldType.type.asKotlinTypeName().toString()}    ${fieldType.type.isPrimitive}")
            val isString = fieldType.type.asKotlinTypeName().toString() == "kotlin.String"
            if (fieldType.type.isPrimitive || isString  ){
                key = "$key\${${it.simpleName}}"
            }
        }
        if (key == methodName) {
            executableElement.parameters.forEach {
                key = "$key\${${it.simpleName}.hashCode()}"
            }
        }
        Logger.warn("SpCacheMethod.keykeykey    $key")
        fetchStrategy = spCache.fetchStrategy.strategy
        Logger.warn("SpCacheMethod.SpCacheMethod    $fetchStrategy")
        Logger.warn("SpCacheMethod.SpCacheMethod $cacheTime")
    }

}