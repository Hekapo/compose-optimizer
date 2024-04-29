package com.github.hekapo.composeoptimizer.quickFix

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import com.intellij.psi.PsiManager
import org.jetbrains.kotlin.psi.KtPsiFactory

class AddGradleDependencyFix : LocalQuickFix {
    override fun getFamilyName() = "Add kotlinx-collections-immutable dependency to Gradle"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
//        val gradleFile = FileUtil.findFirstThatExist("app/build.gradle.kts")
//        val psiFile = PsiManager.getInstance(project).findFile(gradleFile)
//        psiFile?.let {
//            WriteCommandAction.runWriteCommandAction(project) {
//                val dependencyText = "implementation(\"org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5\")"
//                psiFile.add(KtPsiFactory(project).createExpression(dependencyText))
//            }
//        }
    }
}
