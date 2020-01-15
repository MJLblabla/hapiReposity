package com.hapi.repositycompiler.enitity

import com.hapi.repositycompiler.aptUtils.types.packageName
import com.squareup.kotlinpoet.asTypeName
import java.util.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

/**
 * Reposity类
 */
class ReposityClass(val typeElement: TypeElement) {


    val simpleName = typeElement.simpleName.toString()
    val serviceType = typeElement.asType().asTypeName()
    val packageName = typeElement.packageName()
    val fields = LinkedList<ReposityMothed>()
    val isAbstract = typeElement.modifiers.contains(Modifier.ABSTRACT)

}