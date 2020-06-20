package com.iacovelli.coroutines

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoryImplTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val apiService: APIService = mockk()

    @After
    fun cleanUp() {
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when fetchCatFact is called then it should call service fetchCatFact`() {
        coEvery { apiService.fetchCatFact() } returns CatFact("12345")

        runBlockingTest {
            RepositoryImpl(apiService).fetchCatFact()
        }

        coVerify { apiService.fetchCatFact() }
    }

}