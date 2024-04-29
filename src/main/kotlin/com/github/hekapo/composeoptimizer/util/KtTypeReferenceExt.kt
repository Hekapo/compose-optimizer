package com.github.hekapo.composeoptimizer.util

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.idea.caches.resolve.analyze
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtTypeReference
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.lazy.BodyResolveMode
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.util.slicedMap.ReadOnlySlice
import org.jetbrains.kotlin.util.slicedMap.WritableSlice

fun KtTypeReference?.getKotlinType(
    bodyResolveMode: BodyResolveMode = BodyResolveMode.PARTIAL,
    bindingContext: ReadOnlySlice<KtTypeReference, KotlinType> = BindingContext.TYPE,
): KotlinType? {
    return this?.let {
        analyze(bodyResolveMode).get(bindingContext, it)
    }
}

fun KtAnnotationEntry?.getAnnotator(
    bodyResolveMode: BodyResolveMode = BodyResolveMode.PARTIAL,
    bindingContext: WritableSlice<KtAnnotationEntry, AnnotationDescriptor> = BindingContext.ANNOTATION,
): AnnotationDescriptor? {
    return this?.let {
        analyze(bodyResolveMode).get(bindingContext, it)
    }
}
