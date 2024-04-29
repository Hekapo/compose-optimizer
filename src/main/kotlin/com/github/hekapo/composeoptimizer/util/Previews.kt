package com.github.hekapo.composeoptimizer.util

import org.jetbrains.kotlin.psi.KtAnnotated
import org.jetbrains.kotlin.psi.KtAnnotationEntry

val KtAnnotated.isPreview: Boolean
    get() = annotationEntries.any { it.isPreviewAnnotation }

val KtAnnotationEntry.isPreviewAnnotation: Boolean
    get() = calleeExpression?.text?.let { PreviewNameRegex.matches(it) } == true

val PreviewNameRegex by lazy {
    Regex(".*Preview[s]*$")
}
