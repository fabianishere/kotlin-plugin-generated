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

package nl.fabianm.kotlin.plugin.generated.tests

import lombok.Generated
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Test the code generated of data classes
 */
internal class DataClassTest {
    data class DataClass(
        val name: String,
        val ssn: String
    ) {
        val fine: String = name
        val test: String get() = name

        override fun toString(): String {
            return "tests"
        }
    }

    @Test
    fun `getters are correctly annotated`() {
        assertTrue(DataClass::class.java.getMethod("getName").isAnnotationPresent(Generated::class.java))
    }

    @Test
    fun `custom getters are not annotated`() {
        assertFalse(DataClass::class.java.getMethod("getTest").isAnnotationPresent(Generated::class.java))
    }

    @Test
    fun `custom toString is not annotated`() {
        assertFalse(DataClass::class.java.getMethod("toString").isAnnotationPresent(Generated::class.java))
    }
}
