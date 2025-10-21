package com.example.project1.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CharacterDetailDTO (
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: LocationDTO,
    val location: LocationDTO,
    val image: String,
    val episode: List<String>,
    val url: String,
    val created: String
){
    @Serializable
    data class LocationDTO(
        val name: String,
        val url: String
    )
}