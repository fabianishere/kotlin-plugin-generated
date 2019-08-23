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

import com.intellij.openapi.externalSystem.model.DataNode
import com.intellij.openapi.externalSystem.model.project.ModuleData
import org.jetbrains.kotlin.idea.configuration.GradleProjectImportHandler
import org.jetbrains.kotlin.idea.facet.KotlinFacet
import org.jetbrains.plugins.gradle.model.data.GradleSourceSetData

class GeneratedGradleImportHandler : GradleProjectImportHandler {
    override fun importBySourceSet(facet: KotlinFacet, sourceSetNode: DataNode<GradleSourceSetData>) {
        GeneratedImportHandler.modifyCompilerArguments(facet, PLUGIN_GRADLE_JAR, PLUGIN_GRADLE_TITLE)
    }

    override fun importByModule(facet: KotlinFacet, moduleNode: DataNode<ModuleData>) {
        GeneratedImportHandler.modifyCompilerArguments(facet, PLUGIN_GRADLE_JAR, PLUGIN_GRADLE_TITLE)
    }

    companion object {
        private val PLUGIN_GRADLE_JAR = "plugin-gradle"

        /**
         * The implementation title to search for.
         */
        private val PLUGIN_GRADLE_TITLE = "nl.fabianm.kotlin.plugin.generated.gradle"
    }
}
