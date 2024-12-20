package com.gicheol.riverpodfreezedgenerator.converter.core

import com.gicheol.riverpodfreezedgenerator.converter.core.generator.WidgetCodeGenerator
import com.gicheol.riverpodfreezedgenerator.converter.constant.WidgetType
import com.gicheol.riverpodfreezedgenerator.util.Utility.Companion.findFields
import com.gicheol.riverpodfreezedgenerator.util.Utility.Companion.findStateClassMethods
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.lang.dart.psi.DartClass

class WidgetConverter {
    companion object {
        fun convertWidget(
            project: Project,
            editor: Editor,
            element: PsiElement,
            fromType: WidgetType,
            toType: WidgetType
        ) {
            val classElement = PsiTreeUtil.getParentOfType(element, DartClass::class.java)
                ?: throw RuntimeException("classElement is not null")

            val stateClassName = "${classElement.name}${fromType.stateSuffix}"
            val stateClass = findByStateClassName(project, editor, stateClassName)

            val methods = findStateClassMethods(stateClass, classElement)
            val fields = findFields(stateClass)

            val newClassText = WidgetCodeGenerator.generateWidgetCode(
                classElement = classElement,
                stateClass = stateClass,
                methods = methods,
                fields = fields,
                fromType = fromType,
                toType = toType,
            )

            WriteCommandAction.runWriteCommandAction(project) {
                when {
                    // StatelessWidget 변환
                    toType.stateSuffix.isEmpty() ->
                        createStatelessWidget(
                            editor = editor,
                            project = project,
                            classElement = classElement,
                            newClassText = newClassText,
                            stateClass = stateClass,
                            isTargetRiverpod = toType == WidgetType.ConsumerWidget
                        )
                    // StatefulWidget <-> ConsumerStatefulWidget 변환
                    (fromType == WidgetType.StatefulWidget && toType == WidgetType.ConsumerStatefulWidget) ||
                            (fromType == WidgetType.ConsumerStatefulWidget && toType == WidgetType.StatefulWidget) ->
                        replaceCreateStatefulWidget(
                            editor = editor,
                            project = project,
                            classElement = classElement,
                            newClassText = newClassText,
                            stateClass = stateClass,
                            isTargetRiverpod = toType == WidgetType.ConsumerStatefulWidget
                        )
                    // 그 외 StatefulWidget 관련 변환
                    else ->
                        createStatefulWidget(
                            editor = editor,
                            project = project,
                            classElement = classElement,
                            newClassText = newClassText,
                            isTargetRiverpod = toType == WidgetType.ConsumerStatefulWidget
                        )
                }
            }
        }

        private fun findByStateClassName(
            project: Project,
            editor: Editor,
            className: String,
        ): DartClass {
            val document = editor.document
            val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(document) ?: throw RuntimeException("psiFile is not null")

            return psiFile.children
                .filterIsInstance<DartClass>()
                .firstOrNull { it.name?.contains(className) == true  } ?: throw RuntimeException("DartClass is not null")
        }

        private fun createStatelessWidget(
            editor: Editor,
            project: Project,
            classElement: DartClass,
            newClassText: String,
            stateClass: DartClass,
            isTargetRiverpod: Boolean,
        ) {
            val document = editor.document

            val classTextRange = classElement.textRange
            val stateClassTextRange = stateClass.textRange

            if (classTextRange.startOffset < stateClassTextRange.startOffset) {
                document.deleteString(classTextRange.startOffset, stateClassTextRange.endOffset)
            } else {
                document.deleteString(stateClassTextRange.startOffset, classTextRange.endOffset)
            }

            if (classTextRange.startOffset < stateClassTextRange.startOffset) {
                document.insertString(classTextRange.startOffset, newClassText)
            } else {
                document.insertString(stateClassTextRange.startOffset, newClassText)
            }

            if (isTargetRiverpod) {
                insertImportRiverpod(document)
            } else {
                deleteImportRiverpod(document)
            }

            document.replaceString(0, document.text.length, document.text.trimEnd())

            PsiDocumentManager.getInstance(project).commitDocument(document)

            val psiFileUpdated = PsiDocumentManager.getInstance(project).getPsiFile(document) ?: throw RuntimeException("psiFileUpdated is not null")
            CodeStyleManager.getInstance(project).reformat(psiFileUpdated)
        }

        private fun createStatefulWidget(
            editor: Editor,
            project: Project,
            classElement: PsiElement,
            newClassText: String,
            isTargetRiverpod: Boolean,
        ) {
            val document = editor.document
            document.replaceString(classElement.textRange.startOffset, classElement.textRange.endOffset, newClassText)

            if (isTargetRiverpod) {
                insertImportRiverpod(document)
            } else {
                deleteImportRiverpod(document)
            }

            document.replaceString(0, document.text.length, document.text.trimEnd())

            PsiDocumentManager.getInstance(project).commitDocument(document)
            val psiFileUpdated = PsiDocumentManager.getInstance(project).getPsiFile(editor.document) ?: throw RuntimeException("psiFileUpdated is not null")
            CodeStyleManager.getInstance(project).reformat(psiFileUpdated)
        }

        private fun replaceCreateStatefulWidget(
            editor: Editor,
            project: Project,
            classElement: PsiElement,
            newClassText: String,
            stateClass: DartClass,
            isTargetRiverpod: Boolean,
        ) {
            val document = editor.document
            if (classElement.textRange.startOffset < stateClass.textRange.startOffset) {
                document.replaceString(classElement.textRange.startOffset, stateClass.textRange.endOffset, newClassText)
            } else {
                document.replaceString(stateClass.textRange.startOffset, classElement.textRange.endOffset, newClassText)
            }

            if (isTargetRiverpod) {
                insertImportRiverpod(document)
            } else {
                deleteImportRiverpod(document)
            }

            document.replaceString(0, document.text.length, document.text.trimEnd())

            PsiDocumentManager.getInstance(project).commitDocument(document)

            val psiFileUpdated = PsiDocumentManager.getInstance(project).getPsiFile(document) ?: throw RuntimeException("psiFileUpdated is not null")
            CodeStyleManager.getInstance(project).reformat(psiFileUpdated)
        }

        private fun insertImportRiverpod(document: Document) {
            if (!document.text.contains("import 'package:flutter_riverpod/flutter_riverpod.dart';")) {
                document.insertString(0, "import 'package:flutter_riverpod/flutter_riverpod.dart';")
            }
        }

        private fun deleteImportRiverpod(document: Document) {
            if (document.text.contains("import 'package:flutter_riverpod/flutter_riverpod.dart';")) {

                val textToRemove = "import 'package:flutter_riverpod/flutter_riverpod.dart';\n"
                val startIndex = document.text.indexOf(textToRemove)
                if (startIndex != -1) {
                    val endIndex = startIndex + textToRemove.length
                    document.replaceString(startIndex, endIndex, "")
                }

                document.text.replace("import 'package:flutter_riverpod/flutter_riverpod.dart';", "").trim()
            }
        }
    }
}