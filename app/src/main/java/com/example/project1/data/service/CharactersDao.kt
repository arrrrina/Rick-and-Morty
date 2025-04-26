package com.example.project1.data.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project1.data.model.CharacterModel

@Dao
interface CharactersDao {
    @Query("SELECT * FROM characters")
    suspend fun getAllCharacters(): List<CharacterModel>

    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharactersById(id: String): CharacterModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterModel>)
}