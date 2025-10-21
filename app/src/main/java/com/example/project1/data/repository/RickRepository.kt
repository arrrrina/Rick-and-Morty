package com.example.project1.data.repository

import com.example.project1.data.mapper.CharacterMapper
import com.example.project1.data.service.CharactersDao
import com.example.project1.data.service.RickApiService
import com.example.project1.domain.entity.CharacterDetailEntity

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
                dao.insertCharacters(remoteData.results.map { CharacterMapper.mapDTOToModel(it) })
                return dao.getAllCharacters().map {CharacterMapper.mapModelToEntity(it)}
            }
            return localData.map { CharacterMapper.mapModelToEntity(it) }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getCharacterDetail(characterId: Int): CharacterDetailEntity {
        return try {
            val response = apiService.getCharacterDetail(characterId)
            CharacterMapper.detailDTOToCharacterDetailEntity(response)

        } catch (e: Exception) {
            throw e
        }
    }
    override suspend fun getCharactersWithFilters(
        name: String?,
        status: String?,
        gender: String?,
        species: String?
    ): List<CharacterEntity> {
        return try {
            val response = apiService.getCharactersWithFilters(name, status, gender, species)
            val filteredCharacters = response.results.map { CharacterMapper.mapDTOEntity(it) }
            filteredCharacters
        } catch (e: Exception) {
            emptyList()
        }
    }




}