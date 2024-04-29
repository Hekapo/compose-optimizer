package com.github.hekapo.composeoptimizer.settings

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.BoundSearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.builder.rows
import com.intellij.ui.dsl.builder.selected

internal class PluginConfigurable : BoundSearchableConfigurable("ComposeOptimizer", "preferences.compose_optimizer") {

    private val settings = PluginSettingsComponent.getInstance()

    override fun createPanel(): DialogPanel =
        panel {

            lateinit var skippabilityChecks: Cell<JBCheckBox>

            row {
                skippabilityChecks = checkBox("Check parameters stability of composable functions")
                    .bindSelected(settings::isFunctionSkippabilityCheckingEnabled)
            }

            row {
                label("Stability configuration path:")
            }.visibleIf(skippabilityChecks.selected)

            row {
                textFieldWithBrowseButton(
                    fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor(),
                    fileChosen = { newFile -> newFile.path }
                ).align(AlignX.FILL).bindText(settings::stabilityConfigurationPath)
            }.visibleIf(skippabilityChecks.selected)

            row {
                label("Ignored classes e.g. (com.example.app.MyClass)")
            }.visibleIf(skippabilityChecks.selected)

            row {
                textArea()
                    .rows(5)
                    .align(AlignX.FILL)
                    .bindText(settings::stabilityChecksIgnoringClasses)
            }.visibleIf(skippabilityChecks.selected)
        }
}
