package com.example.project1.domain.entity

import android.net.Uri

data class CharacterEntity(
    val name: String,
    val image: Uri,
    val status: String,
    val species: String,
)
