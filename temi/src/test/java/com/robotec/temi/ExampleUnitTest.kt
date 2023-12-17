package com.robotec.temi

import org.junit.Assert.*
import org.junit.Test

import com.robotec.temi.Navigation


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testSum() {
        val temi = Navigation()
        val result = temi.sum(5, 3)
        assertEquals(8, result)
    }

}