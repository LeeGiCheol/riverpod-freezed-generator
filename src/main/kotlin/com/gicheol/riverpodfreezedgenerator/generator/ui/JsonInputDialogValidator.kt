package com.gicheol.riverpodfreezedgenerator.generator.ui

import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.InputValidator


class JsonInputDialogValidator : InputValidator {
    var jsonInputEditor: Editor? = null

    override fun checkInput(className: String): Boolean {
        // serviceButton 클릭 시 className만 검사
        if (className.isNotBlank() && serviceButton.isSelected) {
            return true
        }
        if (jsonInputEditor == null) {
            return false
        }
        return className.isNotBlank() && inputIsValidJson(jsonInputEditor!!.document.text)
    }

    override fun canClose(inputString: String): Boolean = true

    private fun inputIsValidJson(string: String): Boolean {
        return try {
            if (serviceButton.isSelected) return true;

            val jsonElement = JsonParser().parse(string)
            (jsonElement.isJsonObject || jsonElement.isJsonArray)
        } catch (e: JsonSyntaxException) {
            false
        }
    }


    private fun inputIsValidJson2(string: String) = try {
        val jsonElement = JsonParser().parse(string)
        (jsonElement.isJsonObject || jsonElement.isJsonArray)
    } catch (e: JsonSyntaxException) {
        false
    }
}