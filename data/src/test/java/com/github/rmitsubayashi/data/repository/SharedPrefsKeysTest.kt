package com.github.rmitsubayashi.data.repository


import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import org.junit.Test

class SharedPrefsKeysTest {

    @Test
    fun uniqueKeys() {
        val instance = SharedPrefsKeys::class.objectInstance
        val unique = mutableSetOf<String>()
        for (field in SharedPrefsKeys::class.java.declaredFields) {
            val value = field.get(instance)
            // the class itself is unfortunately a field :(
            if (value is SharedPrefsKeys) continue
            assertThat(value).isInstanceOf(String::class.java)
            assertWithMessage(field.name).that(value).isNotIn(unique)
            unique.add(value as String)
        }
    }
}