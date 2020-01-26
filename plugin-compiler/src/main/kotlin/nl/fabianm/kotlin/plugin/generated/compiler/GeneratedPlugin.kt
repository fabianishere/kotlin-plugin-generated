/*
 * Copyright 2019 Fabian Mastenbroek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.fabianm.kotlin.plugin.generated.compiler

import com.intellij.mock.MockProject
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.extensions.impl.ExtensionPointImpl
import com.intellij.openapi.project.Project
import nl.fabianm.kotlin.plugin.generated.compiler.GeneratedConfigurationKeys.ANNOTATION
import nl.fabianm.kotlin.plugin.generated.compiler.GeneratedConfigurationKeys.DEFAULT_ANNOTATION
import nl.fabianm.kotlin.plugin.generated.compiler.GeneratedConfigurationKeys.VISIBLE
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.codegen.extensions.ClassBuilderInterceptorExtension
import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CliOption
import org.jetbrains.kotlin.compiler.plugin.CliOptionProcessingException
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ComponentRegistrar
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey
import org.jetbrains.kotlin.extensions.ProjectExtensionDescriptor
import org.jetbrains.kotlin.name.FqName

object GeneratedConfigurationKeys {
    /**
     * The configuration key for specifying the qualified name of the annotation to annotate generated methods with.
     */
    val ANNOTATION: CompilerConfigurationKey<FqName> =
        CompilerConfigurationKey.create("annotation qualified name")

    /**
     * The configuration key for specifying whether the annotations should be visible during runtime.
     */
    val VISIBLE: CompilerConfigurationKey<Boolean> =
        CompilerConfigurationKey.create("annotation visibility")

    /**
     * By default, the plugin will annotate generated methods with the `lombok.Generated` annotation.
     */
    val DEFAULT_ANNOTATION: FqName = FqName("lombok.Generated")
}

/**
 * The [CommandLineProcessor] for this compiler plugin.
 */
class GeneratedCommandLineProcessor : CommandLineProcessor {
    companion object {
        val ANNOTATION_OPTION = CliOption("annotation", "<fqname>", "Annotation qualified name", required = false)
        val VISIBLE_OPTION = CliOption("visible", "<boolean>", "Flag to indicate whether annotation should be visible", required = false)

        const val PLUGIN_ID = "nl.fabianm.kotlin.plugin.generated"
    }

    override val pluginId = PLUGIN_ID
    override val pluginOptions = listOf(ANNOTATION_OPTION, VISIBLE_OPTION)

    override fun processOption(option: AbstractCliOption, value: String, configuration: CompilerConfiguration) = when (option) {
        ANNOTATION_OPTION -> configuration.put(ANNOTATION, FqName(value))
        VISIBLE_OPTION -> configuration.put(VISIBLE, value.toBoolean())
        else -> throw CliOptionProcessingException("Unknown option: ${option.optionName}")
    }
}

/**
 * The [ComponentRegistrar] for this plugin, which registers the [GeneratedClassBuilderInterceptorExtension].
 */
class GeneratedComponentRegistrar : ComponentRegistrar {
    override fun registerProjectComponents(
        project: MockProject,
        configuration: CompilerConfiguration
    ) {
        // see https://github.com/JetBrains/kotlin/blob/1.1.2/plugins/annotation-collector/src/org/jetbrains/kotlin/annotation/AnnotationCollectorPlugin.kt#L92
        val messageCollector = configuration.get(
            CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY,
            MessageCollector.NONE
        )

        messageCollector.report(CompilerMessageSeverity.INFO, "Generated: Compiler plugin activated")

        ClassBuilderInterceptorExtension.registerExtensionAsFirst(
            project,
            GeneratedClassBuilderInterceptorExtension(
                messageCollector,
                configuration[ANNOTATION] ?: DEFAULT_ANNOTATION,
                configuration[VISIBLE] ?: false
            )
        )
    }
}

fun <T> ProjectExtensionDescriptor<T>.registerExtensionAsFirst(project: Project, extension: T) {
    Extensions.getArea(project)
        .getExtensionPoint(extensionPointName)
        .let { it as ExtensionPointImpl }
        .registerExtension(extension, {})
}
