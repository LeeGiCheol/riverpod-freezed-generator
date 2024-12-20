package com.gicheol.riverpodfreezedgenerator.service.converter.core.generator.widget

import com.gicheol.riverpodfreezedgenerator.common.constant.WidgetType
import com.gicheol.riverpodfreezedgenerator.service.converter.core.generator.code.StateClassGenerator
import com.gicheol.riverpodfreezedgenerator.service.converter.core.generator.code.WidgetFieldGenerator
import com.jetbrains.lang.dart.psi.DartClass
import com.jetbrains.lang.dart.psi.DartComponent
import com.jetbrains.lang.dart.psi.DartMethodDeclaration

class StatefulWidgetGenerator {
    fun generate(
        classElement: DartClass,
        stateClass: DartClass,
        stateClassMethods: List<DartComponent>,
        constructor: String,
        fields: MutableMap<String, String>,
        fromType: WidgetType,
        toType: WidgetType,
    ): String = StringBuilder().apply {
        // 메인 위젯 클래스
        append("class ${classElement.name} extends ${toType.widgetName} {\n")
        WidgetFieldGenerator.generateFields(
            this,
            classElement,
            constructor,
            fields,
        )
        append(constructor)
        append("\n")
        append("  @override\n")
        append("  ${toType.getStateBaseClass()}<${classElement.name}> ")
        append("createState() => _${classElement.name}State();\n")
        append("}\n\n")

        // State 클래스
        append(
            StateClassGenerator.generate(
            classElement = classElement,
            stateClassMethods = stateClassMethods,
            buildMethod = stateClass.methods.find { it.name == "build" }
                    as? DartMethodDeclaration,
            fields = fields,
            fromType = fromType,
            toType = toType,
        ))
    }.toString()
}