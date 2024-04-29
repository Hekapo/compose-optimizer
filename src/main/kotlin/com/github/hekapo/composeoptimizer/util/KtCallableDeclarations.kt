package com.github.hekapo.composeoptimizer.util

import org.jetbrains.kotlin.psi.KtCallableDeclaration

val KtCallableDeclaration.isTypeMutable: Boolean
    get() = typeReference?.text?.matchesAnyOf(KnownMutableCommonTypesRegex) == true

val KnownMutableCommonTypesRegex = sequenceOf(
    // Set
    "MutableSet<.*>\\??",
    "ArraySet<.*>\\??",
    "HashSet<.*>\\??",
    // List
    "MutableList<.*>\\??",
    "ArrayList<.*>\\??",
    // Array
    "SparseArray<.*>\\??",
    "SparseArrayCompat<.*>\\??",
    "LongSparseArray<.*>\\??",
    "SparseBooleanArray\\??",
    "SparseIntArray\\??",
    // Map
    "MutableMap<.*>\\??",
    "HashMap<.*>\\??",
    "Hashtable<.*>\\??",
    // Compose
    "MutableState<.*>\\??",
    "SnapshotStateList<.*>\\??",
    // Flow
    "MutableStateFlow<.*>\\??",
    "MutableSharedFlow<.*>\\??",
    // RxJava & RxRelay
    "PublishSubject<.*>\\??",
    "BehaviorSubject<.*>\\??",
    "ReplaySubject<.*>\\??",
    "PublishRelay<.*>\\??",
    "BehaviorRelay<.*>\\??",
    "ReplayRelay<.*>\\??",
).map { Regex(it) }

val KtCallableDeclaration.isTypeUnstableCollection: Boolean
    get() = typeReference?.text?.matchesAnyOf(KnownUnstableCollectionTypesRegex) == true

val KnownUnstableCollectionTypesRegex = sequenceOf(
    "Set<.*>\\??",
    "List<.*>\\??",
    "Map<.*>\\??",
).map { Regex(it) }
