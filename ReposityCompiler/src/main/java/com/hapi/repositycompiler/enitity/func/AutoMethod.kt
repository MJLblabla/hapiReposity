package com.hapi.repositycompiler.enitity.func

import com.hapi.repositycompiler.aptUtils.types.asJavaTypeName
import com.hapi.repositycompiler.aptUtils.types.asKotlinTypeName
import com.hapi.repositycompiler.enitity.ReposityMothed
import com.qizhou.annotation.AutoApi
import com.sun.tools.javac.code.Symbol
import javax.lang.model.element.ExecutableElement

class AutoMethod (private val mExecutableElement: ExecutableElement) : ReposityMothed(mExecutableElement) {


    override fun initParameters() {

        val annotation: AutoApi = executableElement.getAnnotation(AutoApi::class.java)
        val defaultVMap = HashMap<String,String>()
        annotation.keys.forEachIndexed { index, s ->
            defaultVMap[s] = annotation.defaultValues[index]
        }
        executableElement.parameters.forEach {
         //   (it as Symbol).type.asJavaTypeName()
            parameters.add(Parameter(it.simpleName.toString(), it.asType().asKotlinTypeName(),defaultVMap[it.simpleName.toString()]))
        }
    }




}