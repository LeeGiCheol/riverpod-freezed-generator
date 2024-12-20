package com.gicheol.riverpodfreezedgenerator.converter.core.generator

import com.gicheol.riverpodfreezedgenerator.converter.constant.WidgetType
import com.jetbrains.lang.dart.psi.DartClass
import com.jetbrains.lang.dart.psi.DartComponent
import com.jetbrains.lang.dart.psi.DartMethodDeclaration

class WidgetCodeGenerator {
    companion object {
        fun generateWidgetCode(
            classElement: DartClass,
            stateClass: DartClass,
            methods: List<DartComponent>,
            fields: MutableMap<String, String>,
            fromType: WidgetType,
            toType: WidgetType,
        ): String {
            return when {
                // StatelessWidget 기반 위젯 (StatelessWidget, ConsumerWidget)
                !toType.hasState -> {
                    generateStatelessBasedWidget(
                        classElement = classElement,
                        stateClass = stateClass,
                        stateClassMethods = methods,
                        fields = fields,
                        fromType = fromType,
                        toType = toType,
                    )
                }
                // StatefulWidget 기반 위젯 (StatefulWidget, ConsumerStatefulWidget)
                else -> {
                    val constructor = when {
                        // ConsumerStatefulWidget -> StatefulWidget 변환
                        fromType == WidgetType.ConsumerStatefulWidget &&
                                toType == WidgetType.StatefulWidget -> {
                            classElement.constructors.firstOrNull()?.text
                                ?: "  const ${classElement.name}({super.key});\n"
                        }
                        // StatefulWidget -> ConsumerStatefulWidget 변환
                        else -> {
                            stateClass.constructors.firstOrNull()?.text
                                ?: "  const ${classElement.name}({super.key});\n"
                        }
                    }

                    generateStatefulWidgetBase(
                        classElement = classElement,
                        stateClass = stateClass,
                        stateClassMethods = methods,
                        constructor = constructor,
                        fields = fields,
                        fromType = fromType,
                        toType = toType,
                    )
                }
            }
        }

        private fun generateStatelessBasedWidget(
            classElement: DartClass,
            stateClass: DartClass,
            stateClassMethods: List<DartComponent>,
            fields: MutableMap<String, String>,
            fromType: WidgetType,
            toType: WidgetType,
        ): String = StringBuilder().apply {
            append("class ${classElement.name} extends ${toType.widgetName} {\n")

            generateFields(this, classElement, fields)
            append(generateConstructor(classElement))

            fields.values.forEach { append(it) }
            if (fields.isNotEmpty()) append("\n")

            appendMethods(this, stateClassMethods)
            append(generateBuildMethod(
                buildMethod = stateClass.methods.find { it.name == "build" }
                        as? DartMethodDeclaration,
                toType = toType,
                fromType = fromType,
            ))
            append("}\n")
        }.toString()

        private fun generateStatefulWidgetBase(
            classElement: DartClass,
            stateClass: DartClass,
            stateClassMethods: List<DartComponent>,
            constructor: String,
            fields: MutableMap<String, String>,
            fromType: WidgetType,
            toType: WidgetType,
        ): String {
            val buildMethod = stateClass.methods.find { it.name == "build" }
                    as? DartMethodDeclaration

            return StringBuilder().apply {
                append("class ${classElement.name} extends ${toType.widgetName} {\n")

                generateFields(this, classElement, fields)
                append(constructor)
                append("\n")

                // createState 메서드 추가
                append("  @override\n")
                append("  ${toType.getStateBaseClass()}<${classElement.name}> ")
                append("createState() => _${classElement.name}State();\n")
                append("}\n\n")

                // State 클래스 생성
                append(generateStateClass(
                    classElement = classElement,
                    stateClass = stateClass,
                    stateClassMethods = stateClassMethods,
                    buildMethod = buildMethod,
                    fields = fields,
                    fromType = fromType,
                    toType = toType,
                ))
            }.toString()
        }

        private fun generateStateClass(
            classElement: DartClass,
            stateClass: DartClass,
            stateClassMethods: List<DartComponent>,
            buildMethod: DartMethodDeclaration?,
            fields: MutableMap<String, String>,
            fromType: WidgetType,
            toType: WidgetType,
        ): String {
            val stateBaseClass = toType.getStateBaseClass()
            val stateClassName = "_${classElement.name}State"

            // State 클래스가 이미 존재하는 경우
            if (stateClass.text.isNotEmpty() &&
                !stateClass.text.contains("extends ConsumerWidget") &&
                !stateClass.text.contains("extends StatelessWidget")) {

                return StringBuilder().apply {
                    append("class $stateClassName extends $stateBaseClass<${classElement.name}> {\n")

                    // 기존 필드들 복사
                    for (key in fields.keys) {
                        append(fields[key])
                    }
                    if (fields.isNotEmpty()) {
                        append("\n")
                    }

                    // 기존 메서드들 복사 (build 메서드 제외)
                    stateClass.methods
                        .filterNot { it.name == "build" }
                        .forEach { method ->
                            append(method.text).append("\n")
                        }

                    // build 메서드 새로 생성 (fromType 전달)
                    append(generateBuildMethod(buildMethod, fromType, toType))
                    append("}\n")
                }.toString()
            }

            // 새로운 State 클래스 생성
            return StringBuilder().apply {
                append("class $stateClassName extends $stateBaseClass<${classElement.name}> {\n")
                fields.values.forEach { append(it) }
                if (fields.isNotEmpty()) {
                    append("\n")
                }
                appendMethods(this, stateClassMethods)
                append(generateBuildMethod(buildMethod, fromType, toType))
                append("}\n")
            }.toString()
        }

        private fun generateFields(
            sb: StringBuilder,
            classElement: DartClass,
            fields: MutableMap<String, String>
        ) {
            val constructor = classElement.constructors.firstOrNull()?.text ?:
                "  const ${classElement.name}({super.key});\n"

            for (field in classElement.fields) {
                if (constructor.contains(field.name ?: "")) {
                    sb.append(field.text).append(";\n")
                    fields.remove(field.name ?: "")
                }
            }
            if (classElement.fields.isNotEmpty()) {
                sb.append("\n")
            }
        }

        private fun generateConstructor(
            classElement: DartClass
        ): String {
            return classElement.constructors.firstOrNull()?.text
                ?: "  const ${classElement.name}({super.key});\n"
        }

        private fun appendMethods(
            stringBuilder: StringBuilder,
            methods: List<DartComponent>
        ) {
            methods.forEach { method ->
                stringBuilder.append(method.text)
            }
        }

        private fun generateBuildMethod(
            buildMethod: DartMethodDeclaration?,
            fromType: WidgetType? = null,
            toType: WidgetType,
        ): String {
            var signature = toType.getBuildMethodSignature()
            if (toType == WidgetType.ConsumerStatefulWidget || (fromType?.needsRiverpod == true && !toType.needsRiverpod)) {
                signature = signature.replace(", WidgetRef ref", "")
            }

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
}