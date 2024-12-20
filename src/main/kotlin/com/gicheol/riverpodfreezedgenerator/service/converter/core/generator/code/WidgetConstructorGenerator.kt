package com.gicheol.riverpodfreezedgenerator.service.converter.core.generator.code

import com.jetbrains.lang.dart.psi.DartClass

object WidgetConstructorGenerator {
    fun generate(classElement: DartClass): String {
        return classElement.constructors.firstOrNull()?.text
            ?: "  const ${classElement.name}({super.key});\n"
    }
}