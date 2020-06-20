package com.iacovelli.coroutines

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val repository: Repository = mockk()
    private val factObserver: Observer<String> = mockk(relaxed = true)
    private val loadingObserver: Observer<Boolean> = mockk(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when onViewReady is called then it should call repository to fetch cat fact`() {
        val catFact = CatFact("bla")
        coEvery { repository.fetchCatFact() } returns catFact
        instantiate().onViewReady()

        coVerify { repository.fetchCatFact() }
    }

    @Test
    fun `when repository takes 1s to complete then loading should still be set as false after fact is updated`() {
        val catFact = CatFact("bla")
        coEvery { repository.fetchCatFact() } coAnswers {
            delay(1000)
            catFact
        }

        instantiate().onViewReady()

        testDispatcher.advanceTimeBy(1000)

        coVerifyOrder {
            loadingObserver.onChanged(true)
            repository.fetchCatFact()
            factObserver.onChanged(catFact.text)
            loadingObserver.onChanged(false)
        }
    }


    private fun instantiate(): MainViewModel {
        val viewModel = MainViewModel(repository)
        viewModel.fact.observeForever(factObserver)
        viewModel.loading.observeForever(loadingObserver)
        return viewModel
    }
}
