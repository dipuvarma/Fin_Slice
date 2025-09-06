package com.dipuguide.finslice.domain.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val photoUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
)