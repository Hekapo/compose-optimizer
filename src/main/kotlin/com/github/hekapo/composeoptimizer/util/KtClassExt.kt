package com.github.hekapo.composeoptimizer.util

import com.github.hekapo.composeoptimizer.compose.ComposeAnnotation.stableAnnotations
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.resolve.descriptorUtil.fqNameSafe
import org.jetbrains.kotlin.resolve.source.getPsi

fun KtClass.isStableOrParentStable(ignoredClasses: List<String> = emptyList()): List<KtClass> =
    getSuperClasses().filterNot { it.fqName?.asString() in ignoredClasses }
        .filter { it.hasStableAnnotation() || (!it.hasVarProperty() && it.isInterface().not()) }

fun KtClass.hasVarProperty(): Boolean =
    getProperties().any { it.isVar } ||
            primaryConstructorParameters.any { it.valOrVarKeyword?.text == "var" }

fun KtClass.hasStableAnnotation(): Boolean =
    annotationEntries.any { it.typeReference?.getKotlinType()?.constructor?.declarationDescriptor?.fqNameSafe in stableAnnotations }

fun KtClass.getSuperClasses(): Set<KtClass> = generateSequence(this) { it.getSuperClass() }.toSet()

fun KtClass.getSuperClass(): KtClass? =
    (superTypeListEntries
        .firstOrNull()
        ?.typeReference?.getKotlinType()?.constructor?.declarationDescriptor as? ClassDescriptor)
        ?.source?.getPsi() as? KtClass
