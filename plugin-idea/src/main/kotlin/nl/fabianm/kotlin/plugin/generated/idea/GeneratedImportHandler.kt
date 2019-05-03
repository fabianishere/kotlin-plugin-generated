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

package nl.fabianm.kotlin.plugin.generated.idea

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import nl.fabianm.kotlin.plugin.generated.compiler.GeneratedCommandLineProcessor
import org.jetbrains.kotlin.cli.common.arguments.CommonCompilerArguments
import org.jetbrains.kotlin.idea.facet.KotlinFacet
import java.io.File
import java.io.FileInputStream
import java.util.jar.JarInputStream

internal object GeneratedImportHandler {
    /**
     * The path to the IntelliJ compatible compiler plugin version.
     */
    private val PLUGIN_JPS_JAR: String?
        get() = PathManager.getJarPathForClass(GeneratedCommandLineProcessor::class.java)

    /**
     * The [Logger] instance for this class.
     */
    private val logger = Logger.getInstance(GeneratedImportHandler::class.java)

    /**
     * Modify the compiler arguments for the specified [KotlinFacet] to remove the incompatible compiler plugin from
     * the classpath and replace it with an IntelliJ compatible version.
     *
     * @param facet The facet to modify.
     * @param buildSystemPluginJar The name of the jar file to remove rom the classpath.
     */
    fun modifyCompilerArguments(facet: KotlinFacet, buildSystemPluginJar: String) {
        logger.info("Probing for Gradle plugin")

        val facetSettings = facet.configuration.settings
        val commonArguments = facetSettings.compilerArguments ?: CommonCompilerArguments.DummyImpl()
        val regex = "(.*\\${File.separator}?$buildSystemPluginJar-.*\\.jar".toRegex()

        // Remove the incompatible compiler plugin from the classpath if found
        var isEnabled = false
        val oldPluginClasspaths = (commonArguments.pluginClasspaths ?: emptyArray()).filterTo(mutableListOf()) {
            val match = regex.matches(it) && validateJar(it)
            logger.debug("$it [$match]")
            if (match) {
                isEnabled = true
            }
            !match
        }

        // Add the compatible compiler plugin version to the classpath if available and is enabled in Gradle
        val newPluginClasspaths = if (isEnabled && PLUGIN_JPS_JAR != null)
            oldPluginClasspaths + PLUGIN_JPS_JAR!!
        else
            oldPluginClasspaths

        commonArguments.pluginClasspaths = newPluginClasspaths.toTypedArray()
        facetSettings.compilerArguments = commonArguments
    }

    /**
     * Validate whether the specified jar file is actually our compiler plugin.
     *
     * We need to perform this rather ugly check, because the artifact id of the Gradle plugin is not unique and rather
     * general (`plugin-gradle`). We therefore check whether the manifest contains references to this project.
     */
    private fun validateJar(path: String): Boolean {
        return try {
            val jar = JarInputStream(FileInputStream(path))
            val manifest = jar.manifest
            manifest.mainAttributes.getValue("Implementation-Title").startsWith("nl.fabianm.kotlin.plugin.generated")
        } catch (_: Exception) {
            false
        }
    }
}
