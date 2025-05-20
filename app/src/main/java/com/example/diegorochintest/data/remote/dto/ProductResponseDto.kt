package com.example.diegorochintest.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductResponseDto(
    @SerializedName("status") val status: StatusDto? = null,
    @SerializedName("pageType") val pageType: String? = "",
    @SerializedName("plpResults") val plpResults: PlpResultsDto? = null,
)

data class StatusDto(
    @SerializedName("status") val status: String? = "",
    @SerializedName("statusCode") val statusCode: Int? = 0,
)

data class PlpResultsDto(
    @SerializedName("sortOptions") val sortOptions: List<SortOptionDto>? = emptyList(),
    @SerializedName("refinementGroups") val refinementGroups: List<Any>? = emptyList(),
    @SerializedName("records") val records: List<ProductRecordDto>? = emptyList(),
    @SerializedName("navigation") val navigation: NavigationDto? = null,
    @SerializedName("customUrlParam") val customUrlParam: Map<String, Any>? = emptyMap(),
    @SerializedName("metaData") val metaData: Map<String, Any>? = emptyMap(),
    @SerializedName("totalRecordCount") val totalRecordCount: Int? = 0,
    @SerializedName("plpState") val plpState: PlpStateDto? = null,
)

data class SortOptionDto(
    @SerializedName("label") val label: String? = "",
    @SerializedName("sortBy") val sortBy: String? = "",
)

data class ProductRecordDto(
    @SerializedName("productId") val productId: String? = "",
    @SerializedName("productDisplayName") val productDisplayName: String? = "",
    @SerializedName("listPrice") val listPrice: Double? = 0.0,
    @SerializedName("minimumListPrice") val minimumListPrice: Double? = 0.0,
    @SerializedName("maximumListPrice") val maximumListPrice: Double? = 0.0,
    @SerializedName("promoPrice") val promoPrice: Double? = 0.0,
    @SerializedName("minimumPromoPrice") val minimumPromoPrice: Double? = 0.0,
    @SerializedName("maximumPromoPrice") val maximumPromoPrice: Double? = 0.0,
    @SerializedName("smImage") val smImage: String? = "",
    @SerializedName("lgImage") val lgImage: String? = "",
    @SerializedName("xlImage") val xlImage: String? = "",
    @SerializedName("variantsColor") val variantsColor: List<VariantColorDto>? = emptyList(),
    @SerializedName("isNew") val isNew: Boolean? = false,
)

data class VariantColorDto(
    @SerializedName("colorName") val colorName: String? = "",
    @SerializedName("colorHex") val colorHex: String? = "",
    @SerializedName("colorImageURL") val colorImageURL: String? = "",
)

data class NavigationDto(
    @SerializedName("ancester") val ancester: List<Any>? = emptyList(),
    @SerializedName("current") val current: List<CurrentNavDto>? = emptyList(),
    @SerializedName("childs") val childs: List<Any>? = emptyList(),
)

data class CurrentNavDto(
    @SerializedName("label") val label: String? = "",
    @SerializedName("categoryId") val categoryId: String? = "",
)

data class PlpStateDto(
    @SerializedName("categoryId") val categoryId: String? = "",
    @SerializedName("originalSearchTerm") val originalSearchTerm: String? = "",
    @SerializedName("currentFilters") val currentFilters: String? = "",
    @SerializedName("currentSortOption") val currentSortOption: String? = "",
    @SerializedName("firstRecNum") val firstRecNum: Int? = 0,
    @SerializedName("lastRecNum") val lastRecNum: Int? = 0,
    @SerializedName("recsPerPage") val recsPerPage: Int? = 0,
    @SerializedName("totalNumRecs") val totalNumRecs: Int? = 0,
)
