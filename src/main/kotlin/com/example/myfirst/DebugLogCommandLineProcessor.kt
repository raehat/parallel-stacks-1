package com.example.myfirst

import com.google.auto.service.AutoService
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.config.CompilerConfiguration

@AutoService(CommandLineProcessor::class)
class DebugLogCommandLineProcessor : CommandLineProcessor {
    override val pluginId: String
        get() = "debugLog"
    override val pluginOptions: Collection<AbstractCliOption>
        get() = listOf()
}