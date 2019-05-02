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
    jacoco
}

repositories {
    jcenter()
}

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib")
    runtimeOnly(project(":plugin-compiler", configuration = "embeddable"))

    val junitJupiterVersion: String by extra
    val junitPlatformVersion: String by extra

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitJupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testImplementation("org.junit.platform:junit-platform-launcher:$junitPlatformVersion")
}

/* Compilation */
tasks.withType<KotlinCompile> {
    kotlinOptions {
        // Obtain the path to the embeddable (shaded) compiler plugin via the runtime classpath
        val pluginJar = configurations.runtimeClasspath.get().first { it.name.startsWith("plugin-compiler") }

        jvmTarget = "1.8"
        // We force the plugin to make the annotations visible at runtime
        freeCompilerArgs = listOf("-Xplugin=$pluginJar", "-P", "plugin:nl.fabianm.kotlin.plugin.generated:visible=true")
    }
}

/* Test setup */
tasks.test {
    useJUnitPlatform {}

    testLogging {
        events("passed", "skipped", "failed")
    }

    reports {
        html.isEnabled = true
    }
}

tasks.jacocoTestReport {
    sourceSets(sourceSets.main.get(), sourceSets.test.get())
}
