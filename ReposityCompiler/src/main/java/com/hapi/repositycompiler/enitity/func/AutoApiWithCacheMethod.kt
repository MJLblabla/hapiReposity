package com.hapi.repositycompiler.enitity.func

import com.hapi.repositycompiler.aptUtils.types.asKotlinTypeName
import com.hapi.repositycompiler.enitity.ReposityMothed
import com.qizhou.annotation.AutoApi
import com.qizhou.annotation.AutoApiWithCache
import com.squareup.kotlinpoet.TypeName
import com.sun.javafx.logging.Logger
import com.sun.tools.javac.code.Symbol
import javax.lang.model.element.ExecutableElement

class AutoApiWithCacheMethod (private val providerMap: HashMap<String, TypeName>, private val symbol: Symbol.MethodSymbol, private val mExecutableElement: ExecutableElement) : ReposityMothed(mExecutableElement) {

    override fun initParameters() {
        val annotation: AutoApiWithCache = executableElement.getAnnotation(AutoApiWithCache::class.java)
        val defaultVMap = HashMap<String, String>()
        annotation.keys.forEachIndexed { index, s ->
            defaultVMap[s] = annotation.defaultValues[index]
        }
        executableElement.parameters.forEach {
            parameters.add(Parameter(it.simpleName.toString(), it.asType().asKotlinTypeName(), defaultVMap[it.simpleName.toString()]))
        }
    }


    var fetchStrategy = "OnlyRemote"
    var provideClass :TypeName? = null

    override fun build() {

        super.build()
        val cache = symbol.getAnnotation(AutoApiWithCache::class.java)
        val providerKey = cache.providerKey
        provideClass = providerMap[providerKey]
        fetchStrategy = cache.fetchStrategy.strategy

    }
}