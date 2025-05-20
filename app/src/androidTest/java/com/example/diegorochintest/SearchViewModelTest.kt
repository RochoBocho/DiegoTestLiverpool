package com.example.diegorochintest

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.example.diegorochintest.dom.models.Product
import com.example.diegorochintest.dom.models.SortOption
import com.example.diegorochintest.dom.usecases.GetSortOptionsUseCase
import com.example.diegorochintest.dom.usecases.SearchProductsUseCase
import com.example.diegorochintest.ui.state.SearchEvent
import com.example.diegorochintest.ui.viewmodels.SearchViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SearchViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private lateinit var searchProductsUseCase: SearchProductsUseCase
    private lateinit var getSortOptionsUseCase: GetSortOptionsUseCase
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        searchProductsUseCase = mockk()
        getSortOptionsUseCase = mockk()

        // Configuración de los mocks
        val sortOptions =
            listOf(
                SortOption.DEFAULT,
                SortOption.PRICE_LOW_TO_HIGH,
                SortOption.PRICE_HIGH_TO_LOW,
            )
        every { getSortOptionsUseCase() } returns sortOptions

        // Inicializar el ViewModel
        viewModel = SearchViewModel(searchProductsUseCase, getSortOptionsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun when_initializing_should_set_available_sort_options() {
        // Assert
        val availableSortOptions = viewModel.state.value.availableSortOptions
        assertEquals(3, availableSortOptions.size)
        assertEquals(SortOption.DEFAULT, availableSortOptions[0])
        assertEquals(SortOption.PRICE_LOW_TO_HIGH, availableSortOptions[1])
        assertEquals(SortOption.PRICE_HIGH_TO_LOW, availableSortOptions[2])
    }

    @Test
    fun when_search_term_changes_should_update_state() {
        // Act
        viewModel.onEvent(SearchEvent.OnSearchTermChange("test"))

        // Assert
        assertEquals("test", viewModel.state.value.searchTerm)
    }

    @Test
    fun when_search_clicked_with_empty_term_should_show_error() =
        runTest {
            // Arrange
            viewModel.onEvent(SearchEvent.OnSearchTermChange(""))

            // Act
            viewModel.onEvent(SearchEvent.OnSearchClick)

            // Assert
            assertEquals("Por favor ingresa un término de búsqueda", viewModel.state.value.errorMessage)
        }

    @Test
    fun when_search_clicked_with_valid_term_should_call_use_case() =
        runTest {
            // Arrange
            val searchTerm = "test"
            val mockPagingFlow = flowOf<PagingData<Product>>()
            coEvery {
                searchProductsUseCase(
                    searchTerm = searchTerm,
                    sortOption = SortOption.DEFAULT,
                )
            } returns mockPagingFlow

            viewModel.onEvent(SearchEvent.OnSearchTermChange(searchTerm))

            // Act
            viewModel.onEvent(SearchEvent.OnSearchClick)

            // Assert
            verify {
                searchProductsUseCase(
                    searchTerm = searchTerm,
                    sortOption = SortOption.DEFAULT,
                )
            }
        }

    @Test
    fun when_sort_option_changes_should_update_state_and_perform_search() =
        runTest {
            // Arrange
            val searchTerm = "test"
            val mockPagingFlow = flowOf<PagingData<Product>>()
            coEvery {
                searchProductsUseCase(
                    searchTerm = any(),
                    sortOption = any(),
                )
            } returns mockPagingFlow

            viewModel.onEvent(SearchEvent.OnSearchTermChange(searchTerm))

            // Act
            viewModel.onEvent(SearchEvent.OnSortOptionChange(SortOption.PRICE_LOW_TO_HIGH))

            // Assert
            assertEquals(SortOption.PRICE_LOW_TO_HIGH, viewModel.state.value.selectedSortOption)
            assertEquals(false, viewModel.state.value.showSortOptionsDialog)

            verify {
                searchProductsUseCase(
                    searchTerm = searchTerm,
                    sortOption = SortOption.PRICE_LOW_TO_HIGH,
                )
            }
        }

    @Test
    fun when_dialog_dismissed_should_update_state() {
        // Arrange
        viewModel.onEvent(SearchEvent.OnSortOptionDialogShow)

        // Act
        viewModel.onEvent(SearchEvent.OnSortOptionDialogDismiss)

        // Assert
        assertEquals(false, viewModel.state.value.showSortOptionsDialog)
    }

    @Test
    fun when_error_cleared_should_update_state() {
        // Arrange
        viewModel.onEvent(SearchEvent.OnSearchTermChange(""))
        viewModel.onEvent(SearchEvent.OnSearchClick)

        // Act
        viewModel.onEvent(SearchEvent.OnClearError)

        // Assert
        assertNull(viewModel.state.value.errorMessage)
    }
}
