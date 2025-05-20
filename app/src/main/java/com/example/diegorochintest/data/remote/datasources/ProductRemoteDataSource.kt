package com.example.diegorochintest.data.remote.datasources

import com.example.diegorochintest.data.remote.api.ProductService
import com.example.diegorochintest.data.remote.dto.ProductResponseDto
import javax.inject.Inject

class ProductRemoteDataSource
    @Inject
    constructor(
        private val productService: ProductService,
    ) {
        suspend fun searchProducts(
            searchTerm: String,
            pageNumber: Int,
        ): ProductResponseDto = productService.searchProducts(searchTerm, pageNumber)
    }
