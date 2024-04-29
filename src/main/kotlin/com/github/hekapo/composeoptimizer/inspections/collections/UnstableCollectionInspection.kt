package com.github.hekapo.composeoptimizer.inspections.collections

import com.github.hekapo.composeoptimizer.compose.composeFunctionVisitor
import com.github.hekapo.composeoptimizer.quickFix.GenerateCollectionWrapperFix
import com.github.hekapo.composeoptimizer.util.isTypeUnstableCollection
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.idea.codeinsight.api.classic.inspections.AbstractKotlinInspection

class UnstableCollectionInspection : AbstractKotlinInspection() {

    private val listFix = GenerateCollectionWrapperFix(CollectionType.LIST)
    private val mapFix = GenerateCollectionWrapperFix(CollectionType.MAP)

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return composeFunctionVisitor { ktNamedFunction ->
//            for (param in ktNamedFunction.valueParameters.filter { it.isTypeUnstableCollection }) {
//                val variableName = param.nameAsSafeName.asString()
//                val type = param.typeReference?.text ?: "Unknown"
//                val appropriateFix = when (type.takeWhile { it != '<' }) {
//                    "List" -> listFix
//                    "Map" -> mapFix
//                    else -> null
//                }
//
//                val message = "$variableName is of unstable collection type: $type"
//                if (appropriateFix != null) {
//                    holder.registerProblem(param, message, ProblemHighlightType.WARNING)
//                }
//            }
        }
    }
}
