package com.gicheol.riverpodfreezedgenerator.util

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
    }
}