package com.quicklib.android.network.strategy

import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.Deferred
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Matchers.any
import org.mockito.Mockito
import org.mockito.Mockito.`when` as _when

class LocalDataOnlyStrategyTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }


    @Test
    fun testWorks(){
        assert(true)
    }


    @Test
    fun testAbstract(){

        val sut = object : LocalDataOnlyStrategy<String>(){
            override suspend fun readData(): Deferred<String> {
                return CompletableDeferred("test")
            }
        }
    }

}