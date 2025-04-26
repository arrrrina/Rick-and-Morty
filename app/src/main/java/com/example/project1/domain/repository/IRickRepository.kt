package com.example.project1.domain.repository

import com.example.project1.domain.entity.CharacterEntity

interface IRickRepository {
    suspend fun getAllCharacters(forceRefresh: Boolean): List<CharacterEntity>
}