package com.gicheol.riverpodfreezedgenerator.service.converter.core.generator.code

import com.jetbrains.lang.dart.psi.DartClass

object WidgetFieldGenerator {
    fun generateFields(
        stringBuilder: StringBuilder,
        classElement: DartClass,
        constructor: String,
        fields: MutableMap<String, String>,
    ) {
        for (field in classElement.fields) {
            if (constructor.contains(field.name ?: "")
                && !constructor.contains("super.${field.name}")) {
                stringBuilder.append(field.text).append(";\n")
                fields.remove(field.name ?: "")
            }
        }
        if (classElement.fields.isNotEmpty()) {
            stringBuilder.append("\n")
        }
    }
}