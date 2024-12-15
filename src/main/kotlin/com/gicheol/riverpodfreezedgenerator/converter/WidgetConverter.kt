package com.gicheol.riverpodfreezedgenerator.converter

import com.gicheol.riverpodfreezedgenerator.converter.generator.WidgetGenerator
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
        fun convertStatelessWidgetToConsumerWidget(project: Project, editor: Editor, element: PsiElement) {
            val classElement = PsiTreeUtil.getParentOfType(element, DartClass::class.java) ?: throw RuntimeException("classElement is not null")
            val stateClass = findByStateClassName(project, editor, "${classElement.name}")
            val stateClassMethods = findStateClassMethods(stateClass, classElement)
            val fields = findFields(stateClass)
            val newClassText = WidgetGenerator.generateConsumerWidget(
                classElement,
                stateClass,
                stateClassMethods,
                fields,
            )

            WriteCommandAction.runWriteCommandAction(project) {
                createStatelessWidget(editor, project, classElement, newClassText, stateClass)
            }
        }

        fun convertStatelessWidgetToConsumerStatefulWidget(project: Project, editor: Editor, element: PsiElement) {
            val classElement = PsiTreeUtil.getParentOfType(element, DartClass::class.java) ?: throw RuntimeException("classElement is not null")
            val stateClass = findByStateClassName(project, editor, "${classElement.name}")
            val classMethods = findStateClassMethods(classElement, classElement)
            val fields = findFields(stateClass)
            val newClassText = WidgetGenerator.generateConsumerStatefulWidget(
                classElement,
                stateClass,
                classMethods,
                fields,
            )

            WriteCommandAction.runWriteCommandAction(project) {
                createStatefulWidget(editor, project, classElement, newClassText)
            }
        }

        fun convertStatefulWidgetToConsumerWidget(project: Project, editor: Editor, element: PsiElement) {
            val classElement = PsiTreeUtil.getParentOfType(element, DartClass::class.java) ?: throw RuntimeException("classElement is not null")
            val stateClass = findByStateClassName(project, editor, "${classElement.name}State")

            val stateClassMethods = findStateClassMethods(stateClass, classElement)
            val fields = findFields(stateClass)
            val newClassText = WidgetGenerator.generateConsumerWidget(
                classElement,
                stateClass,
                stateClassMethods,
                fields,
            )

            WriteCommandAction.runWriteCommandAction(project) {
                createStatelessWidget(editor, project, classElement, newClassText, stateClass)
            }
        }

        fun convertStatefulWidgetToConsumerStatefulWidget(project: Project, editor: Editor, element: PsiElement) {
            val classElement = PsiTreeUtil.getParentOfType(element, DartClass::class.java) ?: throw RuntimeException("classElement is not null")
            val stateClass = findByStateClassName(project, editor, "${classElement.name}State")
            val stateClassMethods = findStateClassMethods(stateClass, classElement)
            val fields = findFields(stateClass)
            val newClassText = WidgetGenerator.generateConsumerStatefulWidget(
                classElement,
                stateClass,
                stateClassMethods,
                fields,
            )

            WriteCommandAction.runWriteCommandAction(project) {
                replaceCreateStatefulWidget(editor, project, classElement, newClassText, stateClass)
            }
        }

        fun convertConsumerWidgetToStatelessWidget(project: Project, editor: Editor, element: PsiElement) {
            val classElement = PsiTreeUtil.getParentOfType(element, DartClass::class.java) ?: throw RuntimeException("classElement is not null")
            val stateClass = findByStateClassName(project, editor, "${classElement.name}")

            val stateClassMethods = findStateClassMethods(stateClass, classElement)
            val fields = findFields(stateClass)
            val newClassText = WidgetGenerator.generateStatelessWidget(
                classElement,
                stateClass,
                stateClassMethods,
                fields,
            )

            WriteCommandAction.runWriteCommandAction(project) {
                createStatelessWidget(editor, project, classElement, newClassText, stateClass)
            }
        }

        fun convertConsumerWidgetToStatefulWidget(project: Project, editor: Editor, element: PsiElement) {
            val classElement = PsiTreeUtil.getParentOfType(element, DartClass::class.java) ?: throw RuntimeException("classElement is not null")
            val stateClass = findByStateClassName(project, editor, "${classElement.name}")
            val stateClassMethods = findStateClassMethods(stateClass, classElement)
            val fields = findFields(stateClass)
            val newClassText = WidgetGenerator.generateStatefulWidget(
                classElement,
                stateClass,
                stateClassMethods,
                fields,
            )

            WriteCommandAction.runWriteCommandAction(project) {
                createStatefulWidget(editor, project, classElement, newClassText)
            }
        }

        fun convertConsumerWidgetToConsumerStatefulWidget(project: Project, editor: Editor, element: PsiElement) {
            val classElement = PsiTreeUtil.getParentOfType(element, DartClass::class.java) ?: throw RuntimeException("classElement is not null")
            val stateClass = findByStateClassName(project, editor, "${classElement.name}")
            val classMethods = findStateClassMethods(classElement, classElement)
            val fields = findFields(stateClass)
            val newClassText = WidgetGenerator.generateConsumerStatefulWidget(
                classElement,
                stateClass,
                classMethods,
                fields
            )

            WriteCommandAction.runWriteCommandAction(project) {
                createStatefulWidget(editor, project, classElement, newClassText)
            }
        }

        fun convertConsumerStatefulWidgetToStatelessWidget(project: Project, editor: Editor, element: PsiElement) {
            val classElement = PsiTreeUtil.getParentOfType(element, DartClass::class.java) ?: throw RuntimeException("classElement is not null")
            val stateClass = findByStateClassName(project, editor, "${classElement.name}State")
            val stateClassMethods = findStateClassMethods(stateClass, classElement)
            val fields = findFields(stateClass)
            val newClassText = WidgetGenerator.generateStatelessWidget(
                classElement,
                stateClass,
                stateClassMethods,
                fields,
            )

            WriteCommandAction.runWriteCommandAction(project) {
                createStatelessWidget(editor, project, classElement, newClassText, stateClass)
            }
        }

        fun convertConsumerStatefulWidgetToConsumerWidget(project: Project, editor: Editor, element: PsiElement) {
            val classElement = PsiTreeUtil.getParentOfType(element, DartClass::class.java) ?: throw RuntimeException("classElement is not null")
            val stateClass = findByStateClassName(project, editor, "${classElement.name}State")
            val stateClassMethods = findStateClassMethods(stateClass, classElement)
            val fields = findFields(stateClass)
            val newClassText = WidgetGenerator.generateConsumerWidget(
                classElement,
                stateClass,
                stateClassMethods,
                fields,
            )

            WriteCommandAction.runWriteCommandAction(project) {
                createStatelessWidget(editor, project, classElement, newClassText, stateClass)
            }
        }

        fun convertConsumerStatefulWidgetToStatefulWidget(project: Project, editor: Editor, element: PsiElement) {
            val classElement = PsiTreeUtil.getParentOfType(element, DartClass::class.java) ?: throw RuntimeException("classElement is not null")
            val stateClass = findByStateClassName(project, editor, "${classElement.name}State")
            val stateClassMethods = findStateClassMethods(stateClass, classElement)
            val fields = findFields(stateClass)
            val newClassText = WidgetGenerator.generateStatefulWidget(
                classElement,
                stateClass,
                stateClassMethods,
                fields,
            )

            WriteCommandAction.runWriteCommandAction(project) {
                replaceCreateStatefulWidget(editor, project, classElement, newClassText, stateClass)
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

            PsiDocumentManager.getInstance(project).commitDocument(document)

            val psiFileUpdated = PsiDocumentManager.getInstance(project).getPsiFile(document) ?: throw RuntimeException("psiFileUpdated is not null")
            CodeStyleManager.getInstance(project).reformat(psiFileUpdated)

            insertImportRiverpod(document)
        }

        private fun createStatefulWidget(
            editor: Editor,
            project: Project,
            classElement: PsiElement,
            newClassText: String,
        ) {
            editor.document.replaceString(classElement.textRange.startOffset, classElement.textRange.endOffset, newClassText)
            PsiDocumentManager.getInstance(project).commitDocument(editor.document)

            val psiFileUpdated = PsiDocumentManager.getInstance(project).getPsiFile(editor.document) ?: throw RuntimeException("psiFileUpdated is not null")
            CodeStyleManager.getInstance(project).reformat(psiFileUpdated)
        }

        private fun replaceCreateStatefulWidget(
            editor: Editor,
            project: Project,
            classElement: PsiElement,
            newClassText: String,
            stateClass: DartClass,
        ) {
            if (classElement.textRange.startOffset < stateClass.textRange.startOffset) {
                editor.document.replaceString(classElement.textRange.startOffset, stateClass.textRange.endOffset, newClassText)
            } else {
                editor.document.replaceString(stateClass.textRange.startOffset, classElement.textRange.endOffset, newClassText)
            }

            editor.document.replaceString(0, editor.document.text.length, editor.document.text.trimEnd())
            PsiDocumentManager.getInstance(project).commitDocument(editor.document)

            val psiFileUpdated = PsiDocumentManager.getInstance(project).getPsiFile(editor.document) ?: throw RuntimeException("psiFileUpdated is not null")
            CodeStyleManager.getInstance(project).reformat(psiFileUpdated)
        }

        private fun insertImportRiverpod(document: Document) {
            if (!document.text.contains("import 'package:flutter_riverpod/flutter_riverpod.dart';")) {
                document.insertString(0, "import 'package:flutter_riverpod/flutter_riverpod.dart';")
            }
        }
    }
}