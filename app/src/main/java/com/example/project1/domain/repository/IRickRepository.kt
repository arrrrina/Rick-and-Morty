package com.example.project1.domain.repository

import com.example.project1.domain.entity.CharacterDetailEntity
import com.example.project1.domain.entity.CharacterEntity

interface IRickRepository {
    suspend fun getAllCharacters(forceRefresh: Boolean): List<CharacterEntity>

    suspend fun getCharacterDetail(characterId: Int): CharacterDetailEntity

    suspend fun getCharactersWithFilters(
        name: String?,
        status: String?,
        gender: String?,
        species: String?
    ): List<CharacterEntity>
}