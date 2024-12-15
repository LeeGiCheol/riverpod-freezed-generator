package com.gicheol.riverpodfreezedgenerator.action.converter.widget

import com.gicheol.riverpodfreezedgenerator.converter.WidgetConverter
import com.gicheol.riverpodfreezedgenerator.converter.filtype.TargetWidgetType
import com.gicheol.riverpodfreezedgenerator.util.DartExtendsClassChecker
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

// StatefulWidget -> ConsumerStatefulWidget
class StatefulWidgetToConsumerStatefulWidget : PsiElementBaseIntentionAction(), IntentionAction {
    override fun getText(): String = "Convert to ConsumerStatefulWidget"

    override fun getFamilyName(): String = text

    override fun isAvailable(project: Project, editor: Editor?, psiElement: PsiElement): Boolean {
        return DartExtendsClassChecker.isStatefulWidget(psiElement)
    }

    override fun invoke(project: Project, editor: Editor, element: PsiElement) {
        WriteCommandAction.runWriteCommandAction(project) {
            WidgetConverter.convertStatefulWidgetToConsumerStatefulWidget(project, editor, element)
        }
    }

    override fun startInWriteAction(): Boolean = true
}
