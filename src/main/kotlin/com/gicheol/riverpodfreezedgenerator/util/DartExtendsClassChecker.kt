package com.gicheol.riverpodfreezedgenerator.util

import com.intellij.psi.PsiElement

class DartExtendsClassChecker {
    companion object {
        fun isStatelessWidget(psiElement: PsiElement): Boolean {
            return psiElement.text.contains("StatelessWidget")
        }
        fun isStatefulWidget(psiElement: PsiElement): Boolean {
            return (!psiElement.text.contains("ConsumerStatefulWidget") && psiElement.text.contains("StatefulWidget"))
        }
        fun isConsumerWidget(psiElement: PsiElement): Boolean {
            return psiElement.text.contains("ConsumerWidget")
        }
        fun isConsumerStatefulWidget(psiElement: PsiElement): Boolean {
            return psiElement.text.contains("ConsumerStatefulWidget")
        }
    }
}