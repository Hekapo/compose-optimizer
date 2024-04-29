package com.github.hekapo.composeoptimizer.quickFix

import com.github.hekapo.composeoptimizer.optimizer.REMEMBER
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.project.Project
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.openapi.application.runWriteAction
import org.jetbrains.kotlin.idea.base.psi.imports.addImport
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.psiUtil.getParentOfType
import org.jetbrains.kotlin.resolve.ImportPath

class WrapWithRememberFix : LocalQuickFix {

    override fun getFamilyName() = WRAP_FAMILY_NAME

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val targetElement = descriptor.psiElement.getParentOfType<KtExpression>(true) ?: return

        val ktFile = targetElement.containingKtFile
        val factory = KtPsiFactory(project)

        if (!ktFile.importDirectives.any { it.importedFqName?.asString() == "androidx.compose.runtime.remember" }) {
            val rememberImport = FqName("androidx.compose.runtime.remember")
            runWriteAction {
                ktFile.addImport(rememberImport)
            }
        }

        val newExpressionContent = "remember { ${targetElement.text} }"
        val newExpression = factory.createExpression(newExpressionContent)

        runWriteAction {
            val replaced = targetElement.replace(newExpression)
            CodeStyleManager.getInstance(project).reformat(replaced)
        }
    }


    companion object {
        private const val WRAP_FAMILY_NAME = "Wrap with remember"
    }
}
