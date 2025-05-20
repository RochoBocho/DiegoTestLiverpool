package com.example.diegorochintest.dom.reporitories

import androidx.paging.PagingData
import com.example.diegorochintest.dom.models.Product
import com.example.diegorochintest.dom.models.SortOption
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun searchProducts(
        searchTerm: String,
        sortOption: SortOption,
    ): Flow<PagingData<Product>>
}
