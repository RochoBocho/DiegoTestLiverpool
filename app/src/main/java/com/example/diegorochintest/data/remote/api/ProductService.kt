package com.example.diegorochintest.data.remote.api

import com.example.diegorochintest.data.remote.dto.ProductResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {
    @GET("appclienteservices/services/v3/plp")
    suspend fun searchProducts(
        @Query("search-string") searchTerm: String,
        @Query("page-number") pageNumber: Int,
    ): ProductResponseDto
}
