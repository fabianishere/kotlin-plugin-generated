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

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.github.johnrengelman.shadow")
}

description = "A compiler plugin for Kotlin that marks Kotlin-generated code with an annotation."

repositories {
    jcenter()
}

dependencies {
    compile(kotlin("stdlib"))
    compileOnly(kotlin("compiler"))
}

/* Compilation */
tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.shadowJar {
    configurations = listOf()
    archiveClassifier.set("embeddable")
    relocate("com.intellij", "org.jetbrains.kotlin.com.intellij")
}

// Create embeddable configuration
configurations.create("embeddable") {
    extendsFrom(configurations.shadow.get())
}
