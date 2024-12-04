package com.gicheol.riverpodfreezedgenerator.generator

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import java.util.*

class DartFileContentGenerator {
    companion object {
        private val gson: Gson = GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create()

        fun createField(json: String): String {
            if (json.isBlank()) {
                return ""
            }

            try {
                val jsonObject = gson.fromJson(json, JsonObject::class.java)


                val result = StringBuilder()
                val entrySet = jsonObject.entrySet()
                val lastIndex = entrySet.size - 1
                var currentIndex = 0

                for ((key, value) in entrySet) {
                    when (value) {
                        is JsonObject -> {
                            result.append("|   @Default({}) Map ").append(key).append(",")
                        }

                        is JsonArray -> {
                            result.append("|    @Default([]) List ").append(key).append(",")
                        }

                        is JsonPrimitive -> {
                            when {
                                value.isNumber -> {
                                    val numberValue = value.asNumber
                                    if (numberValue.toDouble() == numberValue.toInt().toDouble()) {
                                        // 정수일 경우
                                        result.append("|    @Default(0) int ").append(key).append(",")
                                    } else {
                                        // 소수일 경우
                                        result.append("|    @Default(0) double ").append(key).append(",")
                                    }
                                }

                                value.isBoolean -> result.append("|    @Default(false) bool ").append(key).append(",")
                                value.isString -> result.append("|    @Default('') String ").append(key).append(",")
                            }
                        }

                        else -> {
                        }
                    }

                    // 마지막 요소가 아니면 개행 추가
                    if (currentIndex < lastIndex) {
                        result.append("\n")
                    }

                    currentIndex++
                }

                return result.toString()
            } catch (e: Exception) {
                return ""
            }
        }

        fun createOnChangeFunction(json: String): String {
            if (json.isBlank()) {
                return ""
            }

            val jsonObject = gson.fromJson(json, JsonObject::class.java)

            val result = StringBuilder()
            val entrySet = jsonObject.entrySet()
            val lastIndex = entrySet.size - 1
            var currentIndex = 0

            for ((key, value) in entrySet) {
                when (value) {
                    is JsonObject, is JsonArray -> {
                        currentIndex++
                        continue
                    }

                    is JsonPrimitive -> {
                        result.append("|  void onChange")

                        when {
                            value.isNumber -> {
                                val numberValue = value.asNumber
                                if (numberValue.toDouble() == numberValue.toInt().toDouble()) {
                                    result.append("${key.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}(int $key) {\n")
                                        .append("|    state = state.copyWith($key: $key);\n")
                                        .append("|  }")
                                } else {
                                    result.append("${key.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}(double $key) {\n")
                                        .append("|    state = state.copyWith($key: $key);\n")
                                        .append("|  }")
                                }
                            }

                            value.isBoolean -> {
                                result.append("${key.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}(bool $key) {\n")
                                    .append("|    state = state.copyWith($key: $key);\n")
                                    .append("|  }")
                            }

                            value.isString -> {
                                result.append("${key.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}(String $key) {\n")
                                    .append("|    state = state.copyWith($key: $key);\n")
                                    .append("|  }")
                            }
                        }

                        // 마지막 항목이 아니라면 개행 추가
                        if (currentIndex < lastIndex) {
                            result.append("\n\n")
                        }
                    }

                    else -> {
                        currentIndex++
                    }
                }

                currentIndex++
            }

            return result.toString()
        }
    }
}