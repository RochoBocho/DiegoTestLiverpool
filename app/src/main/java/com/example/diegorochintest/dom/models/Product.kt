package com.example.diegorochintest.dom.models

data class Product(
    val id: String,
    val name: String,
    val regularPrice: Double,
    val discountPrice: Double,
    val imageUrl: String,
    val availableColors: List<Color>,
)

data class Color(
    val name: String,
    val hexCode: String,
    val imageUrl: String?,
)
