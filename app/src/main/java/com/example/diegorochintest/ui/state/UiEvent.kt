package com.example.diegorochintest.ui.state

import com.example.diegorochintest.dom.models.SortOption

sealed class SearchEvent {
    data class OnSearchTermChange(
        val searchTerm: String,
    ) : SearchEvent()

    data object OnSearchClick : SearchEvent()

    data class OnSortOptionChange(
        val sortOption: SortOption,
    ) : SearchEvent()

    data object OnSortOptionDialogDismiss : SearchEvent()

    data object OnSortOptionDialogShow : SearchEvent()

    data object OnClearError : SearchEvent()
}
