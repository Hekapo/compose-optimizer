package com.github.hekapo.composeoptimizer.util

import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtNamedFunction

fun KtNamedFunction.hasAnnotation(fqName: FqName): Boolean = hasAnnotation(fqName.asString())

fun KtNamedFunction.hasAnnotation(fqString: String): Boolean =
    annotationEntries.any { annotation -> annotation.fqNameMatches(fqString) }

fun KtAnnotationEntry.fqNameMatches(fqName: String): Boolean {
    val shortName = shortName?.asString() ?: return false
    return fqName.endsWith(shortName) && fqName == getAnnotator()?.fqName?.asString()
}
