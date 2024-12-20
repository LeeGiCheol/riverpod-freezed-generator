package com.gicheol.riverpodfreezedgenerator.service.converter.core.generator.code

import com.gicheol.riverpodfreezedgenerator.common.constant.WidgetType
import com.jetbrains.lang.dart.psi.DartClass
import com.jetbrains.lang.dart.psi.DartComponent
import com.jetbrains.lang.dart.psi.DartMethodDeclaration

object StateClassGenerator {
    fun generate(
        classElement: DartClass,
        stateClassMethods: List<DartComponent>,
        buildMethod: DartMethodDeclaration?,
        fields: MutableMap<String, String>,
        fromType: WidgetType,
        toType: WidgetType,
    ): String = StringBuilder().apply {
        append("class _${classElement.name}State extends ${toType.getStateBaseClass()}<${classElement.name}> {\n")
        fields.values.forEach { append(it) }
        if (fields.isNotEmpty()) append("\n")

        WidgetMethodGenerator.appendMethods(this, stateClassMethods)
        append(BuildMethodGenerator.generate(buildMethod, toType))
        append("}\n")
    }.toString()
}