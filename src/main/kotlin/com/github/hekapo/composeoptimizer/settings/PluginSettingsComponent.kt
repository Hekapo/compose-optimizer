package com.github.hekapo.composeoptimizer.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(
    name = "com.github.hekapo.composeoptimizer.ui.PluginSettingsComponent",
    storages = [Storage("MyComponentSettings.xml")]
)
class PluginSettingsComponent :
    SimplePersistentStateComponent<PluginSettingsComponent.State>(State()) {

    var isFunctionSkippabilityCheckingEnabled: Boolean
        set(value) {
            state.isFunctionSkippabilityCheckingEnabled = value
        }
        get() = state.isFunctionSkippabilityCheckingEnabled

    var stabilityChecksIgnoringClasses: String
        set(value) {
            state.stabilityChecksIgnoringClasses = value
        }
        get() = state.stabilityChecksIgnoringClasses.orEmpty()

    var stabilityConfigurationPath: String
        set(value) {
            state.stabilityConfigurationPath = value
        }
        get() = state.stabilityConfigurationPath.orEmpty()

    class State : BaseState() {
        var isFunctionSkippabilityCheckingEnabled by property(true)
        var stabilityChecksIgnoringClasses by string("")
        var stabilityConfigurationPath by string("")
    }

    companion object {
        fun getInstance(): PluginSettingsComponent = ApplicationManager.getApplication().getService(
            PluginSettingsComponent::class.java
        )
    }
}
