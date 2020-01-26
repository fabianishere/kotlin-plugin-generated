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

plugins {
    kotlin("jvm") version "1.3.60" apply false
    id("org.jlleitschuh.gradle.ktlint") version "8.2.0" apply false
    id("com.github.johnrengelman.shadow") version "5.0.0" apply false
}

allprojects {
    group = "nl.fabianm.kotlin.plugin.generated"
    version = "1.5.0"

    extra["junitJupiterVersion"] = "5.4.2"
    extra["junitPlatformVersion"] = "1.4.2"
}
