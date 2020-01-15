package com.hapi.repositycompiler

import com.hapi.repositycompiler.aptUtils.logger.Logger
import com.hapi.repositycompiler.aptUtils.types.asKotlinTypeName
import com.hapi.repositycompiler.enitity.ReposityClass
import com.hapi.repositycompiler.enitity.func.AutoApiWithCacheMethod
import com.hapi.repositycompiler.enitity.func.AutoMethod
import com.hapi.repositycompiler.enitity.func.SpCacheMethod
import com.qizhou.annotation.AutoApi
import com.qizhou.annotation.AutoApiWithCache
import com.qizhou.annotation.Provider
import com.qizhou.annotation.SpCache
import com.qizhou.compiler.aptUtils.AptContext
import com.qizhou.compiler.enitity.ReposityClassBuilder

import com.squareup.kotlinpoet.TypeName
import com.sun.tools.javac.code.Symbol
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

class BuilderProcessor : AbstractProcessor() {


    fun String.transformFromKaptPathToAptPath(): String {
        Logger.warn("transformFromKaptPathToAptPath  "+this)
        return File(this).parentFile.parentFile.parentFile.parentFile.parentFile.path + "/src/main/java"
    }
    /**
     * 输出路径
     */
    private lateinit var mOutputDirectory: String
    /**
     * 输出路径key 可通过这个判断被注解的包
     */
    private  val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"

    /**
     * 支持的注解　目前去掉了两个还没有用的
     */
    private val supportedAnnotations = mutableSetOf(AutoApi::class.java,AutoApiWithCache::class.java,Provider::class.java
            , SpCache::class.java// DbCache::class.java
    )

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val res = supportedAnnotations.map { it.canonicalName }.toMutableSet()
        return res
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        AptContext.init(processingEnv)
        for (item in processingEnv.options) {
            if (item.key.equals(KAPT_KOTLIN_GENERATED_OPTION_NAME)) {
                mOutputDirectory = item.value.transformFromKaptPathToAptPath()
            }
        }
    }

    override fun process(annotations: MutableSet<out TypeElement>?, env: RoundEnvironment): Boolean {
        Logger.warn("process.process    start")
        val reposityClass = HashMap<Element, ReposityClass>()


        val providerMap =  HashMap<String, TypeName>()
        env.getElementsAnnotatedWith(Provider::class.java).forEach {
            val key = it.getAnnotation(Provider::class.java)
            providerMap[key.providerKey] = it.asType().asKotlinTypeName()
        }


        env.getElementsAnnotatedWith(AutoApi::class.java)
                .forEach {
                    val serviceElement = it.enclosingElement as TypeElement
                    var rc = reposityClass[serviceElement]
                    if (rc == null) {
                        rc = ReposityClass(serviceElement)
                        reposityClass[serviceElement] = rc
                    }

                    rc.fields.add(AutoMethod(it as ExecutableElement))
                }


        env.getElementsAnnotatedWith(AutoApiWithCache::class.java)
                .forEach {
                    val serviceElement = it.enclosingElement as TypeElement
                    var rc2 = reposityClass[serviceElement]
                    if (rc2 == null) {
                        rc2 = ReposityClass(serviceElement)
                        reposityClass[serviceElement] = rc2
                    }
                    rc2.fields.add(AutoApiWithCacheMethod(providerMap,it as Symbol.MethodSymbol, it as ExecutableElement))
                }
//
        env.getElementsAnnotatedWith(SpCache::class.java)
                .forEach {
                    val serviceElement = it.enclosingElement as TypeElement
                    var rc3 = reposityClass[serviceElement]
                    if (rc3 == null) {
                        rc3 = ReposityClass(serviceElement)
                        reposityClass[serviceElement] = rc3
                    }
                    rc3.fields.add(SpCacheMethod(it as Symbol.MethodSymbol, it as ExecutableElement))
                }

        reposityClass.forEach { k, v ->
            ReposityClassBuilder(v).build(AptContext.filer,mOutputDirectory)
        }

        return true
    }
}