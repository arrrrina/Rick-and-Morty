package com.example.project1.data.repository

import android.util.Log
import com.example.project1.data.mapper.CharacterMapper
import com.example.project1.data.service.CharactersDao
import com.example.project1.data.service.RickApiService
import com.example.project1.domain.entity.CharacterEntity
import com.example.project1.domain.repository.IRickRepository

class RickRepository(
    private val apiService: RickApiService,
    private val dao: CharactersDao,
): IRickRepository {
    override suspend fun getAllCharacters(forceRefresh: Boolean): List<CharacterEntity> {
        try {
            val localData = dao.getAllCharacters()
            if (localData.isEmpty() || forceRefresh) {
                val remoteData = apiService.getAllCharacters()
                val entities = remoteData.results.map { CharacterMapper.mapDTOEntity(it) }
                dao.insertCharacters(remoteData.results.map { CharacterMapper.mapDTOToModel(it) })
                return entities
            }
            return localData.map { CharacterMapper.mapModelToEntity(it) }
        } catch (e: Exception) {
            throw e
        }
    }
}