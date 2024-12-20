package com.gicheol.riverpodfreezedgenerator.service.converter.core.generator.code

import com.gicheol.riverpodfreezedgenerator.common.constant.WidgetType
import com.jetbrains.lang.dart.psi.DartMethodDeclaration

object BuildMethodGenerator {
    fun generate(
        buildMethod: DartMethodDeclaration?,
        toType: WidgetType,
    ): String {
        val signature = toType.getBuildMethodSignature()
        return buildMethod?.let { method ->
            val methodBody = extractBuildMethodBody(method)

            "  @override\n  $signature {\n$methodBody\n  }\n"
        } ?: """
            |  @override
            |  $signature {
            |    return Container();
            |  }
            """.trimMargin()
    }

    private fun extractBuildMethodBody(method: DartMethodDeclaration): String {
        val text = method.text
        val start = text.indexOf("{") + 1
        val end = text.lastIndexOf("}")
        return text.substring(start, end)
    }
}