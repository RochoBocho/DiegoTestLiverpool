package com.example.diegorochintest.data.pagin

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.diegorochintest.data.remote.datasources.ProductRemoteDataSource
import com.example.diegorochintest.data.remote.dto.ProductRecordDto
import com.example.diegorochintest.dom.models.SortOption
import okio.IOException
import retrofit2.HttpException

class ProductPagingSource(
    private val remoteDataSource: ProductRemoteDataSource,
    private val searchTerm: String,
    private val sortOption: SortOption,
) : PagingSource<Int, ProductRecordDto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductRecordDto> {
        val pageNumber = params.key ?: 1

        return try {
            val response =
                remoteDataSource.searchProducts(
                    searchTerm = searchTerm,
                    pageNumber = pageNumber,
                )

            val products = response.plpResults?.records ?: emptyList()

            val sortedProducts =
                when (sortOption) {
                    SortOption.PRICE_LOW_TO_HIGH -> {
                        products.sortedBy { it.promoPrice ?: it.listPrice ?: 0.0 }
                    }
                    SortOption.PRICE_HIGH_TO_LOW -> {
                        products.sortedByDescending { it.promoPrice ?: it.listPrice ?: 0.0 }
                    }
                    SortOption.DEFAULT -> products
                }

            val nextPageNumber = if (products.isEmpty()) null else pageNumber + 1

            LoadResult.Page(
                data = sortedProducts,
                prevKey = if (pageNumber == 1) null else pageNumber - 1,
                nextKey = nextPageNumber,
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductRecordDto>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
}
