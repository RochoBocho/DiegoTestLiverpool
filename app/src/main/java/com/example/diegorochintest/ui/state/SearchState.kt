package com.example.diegorochintest.ui.state

import androidx.paging.PagingData
import com.example.diegorochintest.dom.models.Product
import com.example.diegorochintest.dom.models.SortOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchState(
    val searchTerm: String = "",
    val isSearching: Boolean = false,
    val productsFlow: Flow<PagingData<Product>> = emptyFlow(),
    val selectedSortOption: SortOption = SortOption.DEFAULT,
    val availableSortOptions: List<SortOption> = emptyList(),
    val showSortOptionsDialog: Boolean = false,
    val errorMessage: String? = null,
)
