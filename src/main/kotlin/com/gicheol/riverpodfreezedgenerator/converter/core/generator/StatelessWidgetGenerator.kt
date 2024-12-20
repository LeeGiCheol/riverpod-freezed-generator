//package com.gicheol.riverpodfreezedgenerator.converter.core.generator
//
//import com.gicheol.riverpodfreezedgenerator.converter.constant.WidgetType
//import com.jetbrains.lang.dart.psi.DartClass
//import com.jetbrains.lang.dart.psi.DartComponent
//
//class StatelessWidgetGenerator {
//    fun generate(
//        classElement: DartClass,
//        stateClass: DartClass,
//        stateClassMethods: List<DartComponent>,
//        fields: MutableMap<String, String>,
//        widgetType: WidgetType,
//    ): String = StringBuilder().apply {
//        append("class ${classElement.name} extends ${widgetType.widgetName} {\n")
//        WidgetFieldGenerator.generateFields(this, classElement, fields)
//        append(WidgetConstructorGenerator.generate(classElement))
//        fields.values.forEach { append(it) }
//        if (fields.isNotEmpty()) append("\n")
//        WidgetMethodGenerator.appendMethods(this, stateClassMethods)
//        append(BuildMethodGenerator.generate(
//                stateClass.methods.find { it.name == "build" } as? DartMethodDeclaration,
//        widgetType
//        ))
//        append("}\n")
//    }.toString()
//}
