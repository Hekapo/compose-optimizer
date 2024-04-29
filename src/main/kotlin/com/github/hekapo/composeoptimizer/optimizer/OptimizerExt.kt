package com.github.hekapo.composeoptimizer.optimizer

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtNameReferenceExpression

const val REMEMBER = "remember"
private val heavyFunctions =
    listOf(
        "map",
        "filter",
        "reduce",
        "fold",
        "flatMap",
        "groupBy",
        "sorted",
        "sortedBy",
        "reversed",
        "distinct",
        "distinctBy",
        "takeWhile",
        "dropWhile",
        "partition"
    )

fun PsiElement.hasUnwrapped(): Map<String, KtCallExpression> {
    val unwrapped = mutableMapOf<String, KtCallExpression>()

    val callExpressions = PsiTreeUtil.findChildrenOfType(this, KtCallExpression::class.java)

    for (call in callExpressions) {
        val referencedName = (call.calleeExpression as? KtNameReferenceExpression)?.text
        referencedName?.let {
            if (referencedName in heavyFunctions) {
                if (!isWrappedInRemember(call)) {
                    unwrapped[referencedName] = call
                }
            }
        }
    }
    return unwrapped
}

fun isWrappedInRemember(element: PsiElement): Boolean {
    var currentElement = element.parent
    while (currentElement != null) {
        if (currentElement is KtCallExpression) {
            val name = (currentElement.calleeExpression as? KtNameReferenceExpression)?.text
            if (name == REMEMBER) {
                return true
            }
        }
        currentElement = currentElement.parent
    }
    return false
}