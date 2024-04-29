package com.github.hekapo.composeoptimizer.inspections.collections

enum class CollectionType(
    val wrapperName: String,
    val kotlinType: String,
    private val typeParameters: List<String>
) {
    LIST("ImmutableList", "List", listOf("T")),
    MAP("ImmutableMap", "Map", listOf("K", "V"));

    val typeArgumentsString: String
        get() = typeParameters.joinToString(", ")

    companion object {
        const val IMPORT_IMMUTABLE = "androidx.compose.runtime.Immutable"
    }
}
