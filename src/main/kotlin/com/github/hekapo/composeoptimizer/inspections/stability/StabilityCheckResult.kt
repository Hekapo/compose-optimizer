package com.github.hekapo.composeoptimizer.inspections.stability

sealed interface StabilityCheckResult {

    data class InterfaceParameter(
        val message: String,
    ) : StabilityCheckResult

    data class HasVarProperty(
        val message: String,
    ) : StabilityCheckResult
}
