package com.example.diegorochintest.data.mappers

import com.example.diegorochintest.data.remote.dto.ProductRecordDto
import com.example.diegorochintest.data.remote.dto.VariantColorDto
import com.example.diegorochintest.dom.models.Color
import com.example.diegorochintest.dom.models.Product

fun ProductRecordDto.toDomainModel(): Product =
    Product(
        id = productId ?: "",
        name = productDisplayName ?: "",
        regularPrice = listPrice ?: 0.0,
        discountPrice = promoPrice ?: 0.0,
        imageUrl = lgImage ?: "",
        availableColors = variantsColor?.map { it.toDomainModel() } ?: emptyList(),
    )

fun VariantColorDto.toDomainModel(): Color =
    Color(
        name = colorName ?: "",
        hexCode = colorHex ?: "",
        imageUrl = colorImageURL,
    )
