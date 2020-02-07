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

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("org.jetbrains.intellij") version "0.4.8"
}

description = "IntelliJ IDEA plugin for marking Kotlin-generated code with an annotation."

repositories {
    jcenter()
}

dependencies {
    compile(kotlin("stdlib"))
    compile(project(":plugin-compiler"))
}

/* Compilation */
tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.jar {
    manifest {
        attributes["Specification-Title"] = project.name
        attributes["Specification-Version"] = project.version
        attributes["Implementation-Title"] = "nl.fabianm.kotlin.plugin.generated.gradle"
        attributes["Implementation-Version"] = project.version
    }
}

/* IntelliJ */
intellij {
    pluginName = "kotlin-plugin-generated"
    version = "2019.3"
    setPlugins("gradle", "org.jetbrains.kotlin:1.3.61-release-IJ2019.3-1")
}
