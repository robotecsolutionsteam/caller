package com.robotec.temi.test

import org.junit.Assert.*
import org.junit.Test

import com.robotec.temi.navigation.Navigation

class NavigationTest {
    @Test
    fun testSum() {
        val temi = Navigation()
        val result = temi.sum(5, 3)
        assertEquals(8, result)
    }
}
