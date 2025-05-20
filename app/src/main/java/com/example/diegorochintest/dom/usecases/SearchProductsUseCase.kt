package com.example.diegorochintest.dom.usecases

import androidx.paging.PagingData
import com.example.diegorochintest.dom.models.Product
import com.example.diegorochintest.dom.models.SortOption
import com.example.diegorochintest.dom.reporitories.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProductsUseCase
    @Inject
    constructor(
        private val productRepository: ProductRepository,
    ) {
        operator fun invoke(
            searchTerm: String,
            sortOption: SortOption = SortOption.DEFAULT,
        ): Flow<PagingData<Product>> =
            productRepository.searchProducts(
                searchTerm = searchTerm.trim(),
                sortOption = sortOption,
            )
    }
