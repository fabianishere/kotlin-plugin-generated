/*
 * Copyright 2018 Fabian Mastenbroek.
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

package nl.fabianm.kotlin.plugin.generated.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinGradleSubplugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

/**
 * The project-level Gradle plugin behavior that is used specifying the plugin's configuration through the
 * [GeneratedExtension] class.
 */
class GeneratedGradleSubplugin : Plugin<Project> {
    companion object {
        fun isEnabled(project: Project) = project.plugins.findPlugin(GeneratedGradleSubplugin::class.java) != null

        fun getGeneratedExtension(project: Project): GeneratedExtension {
            return project.extensions.getByType(GeneratedExtension::class.java)
        }
    }

    override fun apply(project: Project) {
        project.extensions.create("kotlinGenerated", GeneratedExtension::class.java)
    }
}

/**
 * The compilation-level Gradle plugin for applying the compiler plugin to the Kotlin compiler configuration.
 */
class GeneratedKotlinGradleSubplugin : KotlinGradleSubplugin<AbstractCompile> {
    companion object {
        private const val GENERATED_ARTIFACT_NAME = "plugin-gradle"
        private const val GENERATED_GROUP_ID = "nl.fabianm.kotlin.plugin.generated"
        private const val GENERATED_VERSION = "1.5.0"
        private const val GENERATED_COMPILER_PLUGIN_ID = "nl.fabianm.kotlin.plugin.generated"
        private val ANNOTATION_ARG_NAME = "annotation"
        private val VISIBLE_ARG_NAME = "visible"
    }

    override fun isApplicable(project: Project, task: AbstractCompile) = GeneratedGradleSubplugin.isEnabled(project)

    override fun apply(
        project: Project,
        kotlinCompile: AbstractCompile,
        javaCompile: AbstractCompile?,
        variantData: Any?,
        androidProjectHandler: Any?,
        kotlinCompilation: KotlinCompilation<KotlinCommonOptions>?
    ): List<SubpluginOption> {
        if (!GeneratedGradleSubplugin.isEnabled(project)) {
            return emptyList()
        }

        val extension = project.extensions.findByType(GeneratedExtension::class.java) ?: return emptyList()
        return listOfNotNull(
            extension.annotation?.let { SubpluginOption(ANNOTATION_ARG_NAME, it) },
            SubpluginOption(VISIBLE_ARG_NAME, extension.visible.toString())
        )
    }

    override fun getPluginArtifact(): SubpluginArtifact =
        SubpluginArtifact(GENERATED_GROUP_ID, GENERATED_ARTIFACT_NAME, GENERATED_VERSION)

    override fun getCompilerPluginId() = GENERATED_COMPILER_PLUGIN_ID
}
