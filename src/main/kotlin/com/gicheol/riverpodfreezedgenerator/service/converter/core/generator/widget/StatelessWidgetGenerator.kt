package com.gicheol.riverpodfreezedgenerator.service.converter.core.generator.widget

import com.gicheol.riverpodfreezedgenerator.common.constant.WidgetType
import com.gicheol.riverpodfreezedgenerator.service.converter.core.generator.code.BuildMethodGenerator
import com.gicheol.riverpodfreezedgenerator.service.converter.core.generator.code.WidgetConstructorGenerator
import com.gicheol.riverpodfreezedgenerator.service.converter.core.generator.code.WidgetFieldGenerator
import com.gicheol.riverpodfreezedgenerator.service.converter.core.generator.code.WidgetMethodGenerator
import com.jetbrains.lang.dart.psi.DartClass
import com.jetbrains.lang.dart.psi.DartComponent
import com.jetbrains.lang.dart.psi.DartMethodDeclaration

class StatelessWidgetGenerator {
    fun generate(
        classElement: DartClass,
        stateClass: DartClass,
        stateClassMethods: List<DartComponent>,
        constructor: String,
        fields: MutableMap<String, String>,
        toType: WidgetType,
    ): String = StringBuilder().apply {
        append("class ${classElement.name} extends ${toType.widgetName} {\n")

        WidgetFieldGenerator.generateFields(
            this,
            classElement,
            constructor,
            fields,
        )
        append(WidgetConstructorGenerator.generate(classElement))
        fields.values.forEach { append(it) }
        if (fields.isNotEmpty()) append("\n")

        WidgetMethodGenerator.appendMethods(this, stateClassMethods)
        append(
            BuildMethodGenerator.generate(
            stateClass.methods.find { it.name == "build" } as? DartMethodDeclaration,
            toType,
        ))
        append("}\n")
    }.toString()
}