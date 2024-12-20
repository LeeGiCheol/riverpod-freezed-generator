package com.gicheol.riverpodfreezedgenerator.converter.action.consumerwidget

import com.gicheol.riverpodfreezedgenerator.converter.core.WidgetConverter
import com.gicheol.riverpodfreezedgenerator.converter.constant.WidgetType
import com.gicheol.riverpodfreezedgenerator.util.WidgetTypeChecker
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

// ConsumerStatefulWidget -> ConsumerWidget
class ConsumerStatefulWidgetToConsumerWidget : PsiElementBaseIntentionAction(), IntentionAction {
    override fun getText(): String = "Convert to ConsumerWidget"

    override fun getFamilyName(): String = text

    override fun isAvailable(project: Project, editor: Editor?, psiElement: PsiElement): Boolean {
        return WidgetTypeChecker.isConsumerStatefulWidget(psiElement)
    }

    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        WriteCommandAction.runWriteCommandAction(project) {
            WidgetConverter.convertWidget(
                project = project,
                editor = editor,
                element = element,
                fromType = WidgetType.ConsumerStatefulWidget,
                toType = WidgetType.ConsumerWidget,
            )
        }
    }

    override fun startInWriteAction(): Boolean = true
}
