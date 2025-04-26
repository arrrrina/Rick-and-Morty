package com.example.project1.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharacterListInfoDTO(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?
)
