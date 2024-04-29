package com.github.hekapo.composeoptimizer.util

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.resolve.source.getPsi
import org.jetbrains.kotlin.types.TypeConstructor

fun TypeConstructor?.getPsiFromConstructor(): PsiElement? {
   return this?.declarationDescriptor?.source?.getPsi()
}
