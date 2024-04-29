package com.github.hekapo.composeoptimizer.inspections

import com.github.hekapo.composeoptimizer.compose.composeFunctionVisitor
import com.github.hekapo.composeoptimizer.optimizer.hasUnwrapped
import com.github.hekapo.composeoptimizer.quickFix.WrapWithRememberFix
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.idea.codeinsight.api.classic.inspections.AbstractKotlinInspection

class NotRememberedInspection : AbstractKotlinInspection() {

    private val fix = WrapWithRememberFix()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return composeFunctionVisitor { ktNamedFunction ->
            val unwrapped = ktNamedFunction.originalElement.hasUnwrapped()

            if (unwrapped.isNotEmpty()) {
                unwrapped.forEach { (func, call) ->
                    holder.registerProblem(
                        call,
                        PROBLEM_PERFORMANCE.replace("F", func),
                        fix
                    )
                }
            }
        }
    }

    companion object {
        private const val PROBLEM_PERFORMANCE =
            "Использование .F вне remember в @Composable функций может привести к потере производительности."
    }
}
