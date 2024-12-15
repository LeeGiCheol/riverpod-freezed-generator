package com.gicheol.riverpodfreezedgenerator.converter.generator

import com.jetbrains.lang.dart.psi.DartClass
import com.jetbrains.lang.dart.psi.DartComponent
import com.jetbrains.lang.dart.psi.DartMethodDeclaration

class WidgetGenerator {
    companion object {
        fun generateStatelessWidget(
            classElement: DartClass,
            stateClass: DartClass,
            stateClassMethods: List<DartComponent>,
            fields: String,
        ): String {
            val buildMethod = stateClass.methods.find { it.name == "build" } as? DartMethodDeclaration
            val constructor = classElement.constructors.firstOrNull()?.text ?: "  const ${classElement.name}({super.key});\n"

            return StringBuilder().apply {
                append("class ${classElement.name} extends StatelessWidget {\n")
                append(constructor)
                append(fields)
                for (method in stateClassMethods) {
                    append(method.text)
                }
                append("  @override\n")
                if (buildMethod != null) {
                    val buildMethodText = buildMethod.text.replace("Widget build(BuildContext context, WidgetRef ref)", "Widget build(BuildContext context)")
                    append(buildMethodText.replace("@override", "").trim()) // Remove existing @override annotations
                } else {
                    append("  Widget build(BuildContext context) {\n")
                    append("    return Container();\n")
                    append("  }\n")
                }
                append("}\n")
            }.toString()
        }

        fun generateConsumerWidget(
            classElement: DartClass,
            stateClass: DartClass,
            stateClassMethods: List<DartComponent>,
            fields: String,
        ): String {
            val buildMethod = stateClass.methods.find { it.name == "build" } as? DartMethodDeclaration
            val constructor = classElement.constructors.firstOrNull()?.text ?: "  const ${classElement.name}({super.key});\n"

            return StringBuilder().apply {
                append("class ${classElement.name} extends ConsumerWidget {\n")
                append(constructor)
                append(fields)
                for (method in stateClassMethods) {
                    append(method.text)
                }
                append("  @override\n")
                if (buildMethod != null) {
                    val buildMethodText = buildMethod.text.replace("Widget build(BuildContext context)", "Widget build(BuildContext context, WidgetRef ref)")
                    append(buildMethodText.replace("@override", "").trim()) // Remove existing @override annotations
                } else {
                    append("  Widget build(BuildContext context, WidgetRef ref) {\n")
                    append("    return Container();\n")
                    append("  }\n")
                }
                append("}\n")
            }.toString()
        }

        fun generateStatefulWidget(
            classElement: DartClass,
            stateClass: DartClass,
            stateClassMethods: List<DartComponent>,
            fields: String,
        ): String {
            val buildMethod = stateClass.methods.find { it.name == "build" } as? DartMethodDeclaration
            val constructor = stateClass.constructors.firstOrNull()?.text ?: "  const ${classElement.name}({super.key});\n"

            return StringBuilder().apply {
                append("class ${classElement.name} extends StatefulWidget {\n")
                append(constructor)
                append("  @override\n")
                append("  State<${classElement.name}> createState() => _${classElement.name}State();\n")
                append("}\n\n")
                if (stateClass.text.isNotEmpty() && !stateClass.text.contains("extends ConsumerWidget") && !stateClass.text.contains("extends StatelessWidget")) {
                    append(stateClass.text.replace("extends ConsumerState<", "extends State<"))
                } else {
                    append("class _${classElement.name}State extends State<${classElement.name}> {")
                    append(fields)
                    for (method in stateClassMethods) {
                        append(method.text)
                    }
                    append("  @override\n")
                    if (buildMethod != null) {
                        val buildMethodText = buildMethod.text.replace(
                            "Widget build(BuildContext context, WidgetRef ref)",
                            "Widget build(BuildContext context)"
                        )
                        append(buildMethodText.replace("@override", "").trim()) // Remove existing @override annotations
                    } else {
                        append("  Widget build(BuildContext context) {\n")
                        append("    return Container();\n")
                        append("  }\n")
                    }
                    append("}\n")
                }
            }.toString()
        }

        fun generateConsumerStatefulWidget(
            classElement: DartClass,
            stateClass: DartClass,
            classMethods: List<DartComponent>,
            fields: String,
        ): String {
            val buildMethod = stateClass.methods.find { it.name == "build" } as? DartMethodDeclaration
            val constructor = stateClass.constructors.firstOrNull()?.text ?: "  const ${classElement.name}({super.key});\n"

            return StringBuilder().apply {
                append("class ${classElement.name} extends ConsumerStatefulWidget {\n")
                append(constructor)
                append("  @override\n")
                append("  ConsumerState<${classElement.name}> createState() => _${classElement.name}State();\n")
                append("}\n\n")
                if (stateClass.text.isNotEmpty() && !stateClass.text.contains("extends ConsumerWidget") && !stateClass.text.contains("extends StatelessWidget")) {
                    val stateClassText = stateClass.text
                    if (stateClassText.contains("ConsumerState")) {
                        append(stateClassText)
                    } else {
                        append(stateClass.text.replace("extends State<", "extends ConsumerState<"))
                    }
                } else {
                    append("class _${classElement.name}State extends ConsumerState<${classElement.name}> {\n")
                    append(fields)
                    for (method in classMethods) {
                        append(method.text)
                    }
                    append("  @override\n")
                    if (buildMethod != null) {
                        val buildMethodText = buildMethod.text.replace(
                            "Widget build(BuildContext context, WidgetRef ref)",
                            "Widget build(BuildContext context)"
                        )
                        append(buildMethodText.replace("@override", "").trim()) // Remove existing @override annotations
                    } else {
                        append("  Widget build(BuildContext context) {\n")
                        append("    return Container();\n")
                        append("  }\n")
                    }
                    append("}\n")
                }
            }.toString()
        }
    }
}