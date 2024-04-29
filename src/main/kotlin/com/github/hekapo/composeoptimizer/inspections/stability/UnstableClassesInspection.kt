package com.github.hekapo.composeoptimizer.inspections.stability

import com.github.hekapo.composeoptimizer.compose.composeFunctionVisitor
import com.github.hekapo.composeoptimizer.settings.PluginSettingsComponent
import com.github.hekapo.composeoptimizer.util.getKotlinType
import com.github.hekapo.composeoptimizer.util.getPsiFromConstructor
import com.github.hekapo.composeoptimizer.util.isStableOrParentStable
import com.github.hekapo.composeoptimizer.util.isTypeUnstableCollection
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.idea.codeinsight.api.classic.inspections.AbstractKotlinInspection
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtClass

class UnstableClassesInspection : AbstractKotlinInspection() {

    private val settings by lazy { PluginSettingsComponent.getInstance() }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {

        return composeFunctionVisitor { ktNamedFunction ->
            if (!settings.isFunctionSkippabilityCheckingEnabled) return@composeFunctionVisitor

            val ignoredClasses = settings.stabilityChecksIgnoringClasses
                .split("\n", ",")
                .map(String::trim)
                .filter(String::isNotEmpty)
                .also {
                    println("settings " + it.toString())
                }

            ktNamedFunction.valueParameters.forEach { parameter ->
                parameter.typeReference?.getKotlinType()?.constructor?.getPsiFromConstructor()?.let { psi ->
                    if (psi is KtClass && psi.fqName !in ignoredClasses.map { FqName(it) } && psi.isStableOrParentStable(
                            ignoredClasses
                        ).isEmpty()
                    ) {
                        holder.registerProblem(
                            parameter,
                            "Parameter '${parameter.name}' of type '${parameter.typeReference?.text ?: "Unknown Type"}' " +
                                    "has 'var' properties and is unstable",
                            ProblemHighlightType.WARNING
                        )
                    }
                }
            }

            for (param in ktNamedFunction.valueParameters.filter { it.isTypeUnstableCollection }) {
                val variableName = param.nameAsSafeName.asString()
                val type = param.typeReference?.text ?: "Unknown"


                holder.registerProblem(
                    param,
                    "$variableName is of unstable collection type: $type",
                    ProblemHighlightType.WARNING
                )
            }
        }
    }
}
//override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
//        if (!settings.isFunctionSkippabilityCheckingEnabled) return super.buildVisitor(holder, isOnTheFly)
//        return composeFunctionVisitor { ktNamedFunction ->
//            ktNamedFunction.valueParameters.forEach { parameter ->
//                val typeReference = parameter.typeReference
//                val type = typeReference?.getKotlinType()
//                val descriptor = type?.constructor?.declarationDescriptor
//                val psi = descriptor?.source?.getPsi() as? KtClass
//
//                // todo research: visitor not rerunning after settings change
//                val ignoredClasses = settings.stabilityChecksIgnoringClasses
//                    .split("\n", ",")
//                    .map { it.trim() }
//                    .filter { it.isNotEmpty() }
//
//                if (psi != null) {
//                    val c = psi.isStableOrParentStable(ignoredClasses)
//                    if (c.isEmpty()) {
//                        val parameterTypeText = typeReference.text ?: "Unknown Type"
//                        holder.registerProblem(
//                            parameter,
//                            "Parameter '${parameter.name}' of type '$parameterTypeText' has 'var' properties or constructor parameters and is unstable",
//                            ProblemHighlightType.WARNING
//                        )
//                    }
//                }
//            }
//        }
//    }