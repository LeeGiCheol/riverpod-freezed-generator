package com.gicheol.riverpodfreezedgenerator.service.converter.core.generator.code

import com.jetbrains.lang.dart.psi.DartComponent

object WidgetMethodGenerator {
    fun appendMethods(
        stringBuilder: StringBuilder,
        methods: List<DartComponent>,
    ) {
        methods.forEach { method ->
            stringBuilder.append(method.text)
        }
    }
}