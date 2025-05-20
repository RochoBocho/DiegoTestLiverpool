package com.example.diegorochintest.dom.usecases

import com.example.diegorochintest.dom.models.SortOption
import javax.inject.Inject

class GetSortOptionsUseCase
    @Inject
    constructor() {
        operator fun invoke(): List<SortOption> =
            listOf(
                SortOption.DEFAULT,
                SortOption.PRICE_LOW_TO_HIGH,
                SortOption.PRICE_HIGH_TO_LOW,
            )
    }
