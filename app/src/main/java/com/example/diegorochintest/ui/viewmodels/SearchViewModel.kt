package com.example.diegorochintest.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.diegorochintest.dom.usecases.GetSortOptionsUseCase
import com.example.diegorochintest.dom.usecases.SearchProductsUseCase
import com.example.diegorochintest.ui.state.SearchEvent
import com.example.diegorochintest.ui.state.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        private val searchProductsUseCase: SearchProductsUseCase,
        private val getSortOptionsUseCase: GetSortOptionsUseCase,
    ) : ViewModel() {
        private val _state = MutableStateFlow(SearchState())
        val state: StateFlow<SearchState> = _state.asStateFlow()

        init {
            _state.update { currentState ->
                currentState.copy(
                    availableSortOptions = getSortOptionsUseCase(),
                )
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
        fun onEvent(event: SearchEvent) {
            when (event) {
                is SearchEvent.OnSearchTermChange -> {
                    _state.update { currentState ->
                        currentState.copy(searchTerm = event.searchTerm)
                    }
                }

                is SearchEvent.OnSearchClick -> {
                    performSearch()
                }

                is SearchEvent.OnSortOptionChange -> {
                    _state.update { currentState ->
                        currentState.copy(
                            selectedSortOption = event.sortOption,
                            showSortOptionsDialog = false,
                        )
                    }
                    performSearch()
                }

                is SearchEvent.OnSortOptionDialogShow -> {
                    _state.update { currentState ->
                        currentState.copy(showSortOptionsDialog = true)
                    }
                }

                is SearchEvent.OnSortOptionDialogDismiss -> {
                    _state.update { currentState ->
                        currentState.copy(showSortOptionsDialog = false)
                    }
                }

                is SearchEvent.OnClearError -> {
                    _state.update { currentState ->
                        currentState.copy(errorMessage = null)
                    }
                }
            }
        }

        private fun performSearch() {
            viewModelScope.launch {
                try {
                    val searchTerm = _state.value.searchTerm

                    if (searchTerm.isBlank()) {
                        _state.update { currentState ->
                            currentState.copy(
                                errorMessage = "Por favor ingresa un término de búsqueda",
                            )
                        }
                        return@launch
                    }

                    _state.update { currentState ->
                        currentState.copy(
                            isSearching = true,
                            errorMessage = null,
                            productsFlow =
                                searchProductsUseCase(
                                    searchTerm = searchTerm,
                                    sortOption = currentState.selectedSortOption,
                                ).cachedIn(viewModelScope),
                        )
                    }

                    _state.update { currentState ->
                        currentState.copy(isSearching = false)
                    }
                } catch (e: Exception) {
                    _state.update { currentState ->
                        currentState.copy(
                            isSearching = false,
                            errorMessage = "Error al buscar productos: ${e.localizedMessage}",
                        )
                    }
                }
            }
        }
    }
