package com.github.hekapo.composeoptimizer.compose

import com.github.hekapo.composeoptimizer.util.hasAnnotation
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.namedFunctionVisitor
import org.jetbrains.kotlin.psi.stubs.elements.KtStubElementTypes
import org.jetbrains.kotlin.utils.addToStdlib.UnsafeCastFunction
import org.jetbrains.kotlin.utils.addToStdlib.cast

@OptIn(UnsafeCastFunction::class)
inline fun composeFunctionVisitor(
    crossinline block: (KtNamedFunction) -> Unit
): PsiElementVisitor {
    return namedFunctionVisitor { namedFunction ->
        when (namedFunction.node.elementType) {
            KtStubElementTypes.FUNCTION -> {
                val function = namedFunction.originalElement.cast<KtNamedFunction>()
                if (function.hasAnnotation(ComposeAnnotation.Composable)) {
                    block(namedFunction)
                }
            }
        }
    }
}
