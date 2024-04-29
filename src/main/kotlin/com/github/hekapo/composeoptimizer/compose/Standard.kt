package com.github.hekapo.composeoptimizer.compose

import org.jetbrains.kotlin.name.FqName

const val COMPOSE_PACKAGE = "androidx.compose"
private const val COMPOSE_RUNTIME = "$COMPOSE_PACKAGE.runtime"

object ComposeAnnotation {
    val Composable = FqName("$COMPOSE_RUNTIME.Composable")
    val StableMarker = FqName("$COMPOSE_RUNTIME.StableMarker")
    val Stable = FqName("$COMPOSE_RUNTIME.Stable")
    val Immutable = FqName("$COMPOSE_RUNTIME.Immutable")
    val Preview = FqName("$COMPOSE_RUNTIME.Preview")

    val stableAnnotations = listOf(Stable, Immutable)
}

object Files {
    val ConfFileExt = "conf"
}
