package com.example.project1.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterModel(
    @PrimaryKey (autoGenerate = true) val id: Int = 0,
    val name: String,
    val image: String,
    val status: String,
    val species: String,
    val gender: String,
)