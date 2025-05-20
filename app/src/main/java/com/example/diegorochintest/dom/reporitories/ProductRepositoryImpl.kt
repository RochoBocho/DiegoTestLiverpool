package com.example.diegorochintest.dom.reporitories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.diegorochintest.data.mappers.toDomainModel
import com.example.diegorochintest.data.pagin.ProductPagingSource
import com.example.diegorochintest.data.remote.datasources.ProductRemoteDataSource
import com.example.diegorochintest.data.remote.dto.ProductRecordDto
import com.example.diegorochintest.dom.models.Product
import com.example.diegorochintest.dom.models.SortOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRepositoryImpl
    @Inject
    constructor(
        private val remoteDataSource: ProductRemoteDataSource,
    ) : ProductRepository {
        override fun searchProducts(
            searchTerm: String,
            sortOption: SortOption,
        ): Flow<PagingData<Product>> =
            Pager(
                config =
                    PagingConfig(
                        pageSize = PRODUCTS_PER_PAGE,
                        initialLoadSize = PRODUCTS_PER_PAGE,
                        prefetchDistance = 5,
                        enablePlaceholders = false,
                    ),
                pagingSourceFactory = {
                    ProductPagingSource(
                        remoteDataSource = remoteDataSource,
                        searchTerm = searchTerm,
                        sortOption = sortOption,
                    )
                },
            ).flow.map { pagingData: PagingData<ProductRecordDto> ->
                pagingData.map { productDto: ProductRecordDto ->
                    productDto.toDomainModel()
                }
            }

        companion object {
            private const val PRODUCTS_PER_PAGE = 20
        }
    }
