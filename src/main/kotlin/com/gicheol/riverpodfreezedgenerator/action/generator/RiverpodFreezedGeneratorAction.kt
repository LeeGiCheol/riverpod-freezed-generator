package com.gicheol.riverpodfreezedgenerator.action.generator

import com.gicheol.riverpodfreezedgenerator.generator.filetype.DartFileType
import com.gicheol.riverpodfreezedgenerator.constant.RadioType
import com.gicheol.riverpodfreezedgenerator.generator.DartFileContentGenerator
import com.gicheol.riverpodfreezedgenerator.generator.template.DartFileContentTemplate
import com.gicheol.riverpodfreezedgenerator.ui.JsonInputDialog
import com.gicheol.riverpodfreezedgenerator.util.Utility
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.PsiManager

class RiverpodFreezedGeneratorAction : AnAction("Riverpod Freezed Generator") {

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.getData(PlatformDataKeys.PROJECT) ?: return

        val dataContext = event.dataContext
        val module = LangDataKeys.MODULE.getData(dataContext) ?: return

        val currentDirectory = when (val navigatable = LangDataKeys.NAVIGATABLE.getData(dataContext)) {
            is PsiDirectory -> navigatable
            is PsiFile -> navigatable.containingDirectory
            else -> {
                val root = ModuleRootManager.getInstance(module)
                root.sourceRoots
                    .asSequence()
                    .mapNotNull {
                        PsiManager.getInstance(project).findDirectory(it)
                    }.firstOrNull()
            }
        } ?: return

        val psiFileFactory = PsiFileFactory.getInstance(project)
        val inputDialog = JsonInputDialog(project)

        inputDialog.show()
        val dartFileName = inputDialog.getDartFileName()
        // 파일명 없으면 종료
        if (dartFileName == "") {
            return
        }

        val inputString = inputDialog.getJsonInputString()

        // service 선택 시 model은 빈값처리
        val modelString = if (inputDialog.getSelectedRadioOption() == RadioType.SERVICE) {
            ""
        } else {
            DartFileContentGenerator.createField(inputString)
        }

        val onChange = if (inputDialog.getCheckboxClicked()) {
            DartFileContentGenerator.createOnChangeFunction(inputString)
        } else {
            ""
        }

        val dartFileContent = when (val fileType = inputDialog.getSelectedRadioOption()) {
            RadioType.MODEL -> {
                DartFileContentTemplate.generateModelDartCode(dartFileName, modelString)
            }
            RadioType.PROVIDER -> {
                    DartFileContentTemplate.generateProviderDartCode(
                        dartFileName,
                        modelString,
                        onChange,
                        fileType,
                    )
            }
            else -> {
                DartFileContentTemplate.generateServiceDartCode(dartFileName)
            }
        }

        generateDartFile(
            project,
            dartFileName,
            psiFileFactory,
            currentDirectory,
            dartFileContent,
        )
    }

    private fun generateDartFile(
        project: Project,
        dartFileName: String,
        psiFileFactory: PsiFileFactory,
        currentDirectory: PsiDirectory,
        dartFileContent: String,
    ) {
        action(project) {
            val file =
                psiFileFactory.createFileFromText(
                    "${Utility.camelToSnake(dartFileName).trim('`')}.dart",
                    DartFileType(),
                    dartFileContent
                )
            currentDirectory.add(file)
        }
    }

    private fun action(project: Project?, action: (Project?) -> Unit) {
        CommandProcessor.getInstance().executeCommand(project, {
            ApplicationManager.getApplication().runWriteAction {
                action.invoke(project)
            }
        }, "insertDart", "insertDart")
    }
}