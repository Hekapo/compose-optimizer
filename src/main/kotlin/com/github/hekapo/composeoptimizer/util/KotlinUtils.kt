package com.github.hekapo.composeoptimizer.util

import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.psi.KtUserType

fun String?.matchesAnyOf(patterns: Sequence<Regex>): Boolean {
    if (isNullOrEmpty()) return false
    for (regex in patterns) {
        if (matches(regex)) return true
    }
    return false
}

fun extractGenericTypes(typeReference: KtTypeReference): List<String>? {
    val typeElement = typeReference.typeElement as? KtUserType ?: return null
    return typeElement.typeArguments.mapNotNull {
        (it.typeReference?.typeElement as? KtUserType)?.referencedName
    }
}
