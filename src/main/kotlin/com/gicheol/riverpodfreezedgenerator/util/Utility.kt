package com.gicheol.riverpodfreezedgenerator.util

import com.jetbrains.lang.dart.psi.DartClass
import com.jetbrains.lang.dart.psi.DartComponent

class Utility {
    open fun String.replaceLast(oldValue: Char, newValue: Char): String =
        substringBeforeLast(oldValue) + newValue + substringAfterLast(oldValue)

    companion object {
        fun snakeToCamelCase(
            input: String,
            type: String = "",
        ): String {
            val result = input.split("_")
                .joinToString("") { it.capitalize() }

            return if (type.isNotEmpty() && result.endsWith(type, ignoreCase = true)) {
                result.dropLast(type.length)
            } else {
                result
            }
        }

        fun snakeToCamelCaseFirstLowerCase(
            input: String,
            type: String = "",
        ): String {
            val result = input.split("_")
                .joinToString("") { it.capitalize() }
                .decapitalize()

            return if (type.isNotEmpty() && result.endsWith(type, ignoreCase = true)) {
                result.dropLast(type.length)
            } else {
                result
            }
        }

        fun camelToSnake(camelStr: String): String {
            return camelStr.replace(Regex("([a-z])([A-Z])"), "$1_$2").toLowerCase()
        }

        fun findStateClassMethods(
            stateClass: DartClass,
            classElement: DartClass
        ): List<DartComponent> {
            return stateClass.methods.filter {
                it.name != "build" &&
                        (it.parent.parent.parent.text.contains("class ${classElement.name}State") ||
                        it.parent.parent.parent.text.contains("class _${classElement.name}State") ||
                        it.parent.parent.parent.text.contains("extends ConsumerWidget") ||
                        it.parent.parent.parent.text.contains("extends StatelessWidget"))
            }.sortedBy { it.name }
        }

        fun findFields(stateClass: DartClass): String {
            var result = ""
            for (field in stateClass.fields) {
                val stateClassText = stateClass.text
                if (stateClassText.contains(field.text)) {
                    val startIndex = stateClassText.indexOf(field.text)
                    val endIndex = stateClassText.indexOf(";", startIndex)
                    if (endIndex != -1) {
                        result += "${stateClassText.substring(startIndex, endIndex + 1)}\n"
                    }
                }
            }

            if (result.isNotEmpty()) {
                result += "\n"
            }

            return result
        }
    }
}