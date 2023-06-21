package com.example.gshelppassanger

data class UserModel(
    val uid: String,
    val firstName: String,
    val lastName: String,
    val imageUrl: String
)

data class PriceModel(
    val uid: String,
    val yesterday: String,
    val today: String,
    val tomorrow: String
)
