package com.example.project1.data.service

import android.util.Log
import com.example.project1.common.api.NetworkModule
import com.example.project1.data.dto.CharacterDetailDTO
import com.example.project1.data.dto.CharacterListDTO
import com.example.project1.domain.entity.CharacterDetailEntity
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

object RickApiService {
    private const val BASE_URL = "https://rickandmortyapi.com/api"
    private var currentPage = 1
    private var maxPage = 1
    suspend fun getAllCharacters(): CharacterListDTO {
        try {
            val response = NetworkModule.publicClient
                .get("$BASE_URL/character/?page=$currentPage")
                .body<CharacterListDTO>()
            maxPage = response.info.pages
            currentPage = resetPagination()
           return response
        } catch (e: Exception) {
            throw Exception(e)
        }
    }
    suspend fun getCharacterById(id: Int): CharacterDetailDTO{
        return NetworkModule.publicClient
            .get("$BASE_URL/character/$id")
            .body<CharacterDetailDTO>()
    }

    private fun resetPagination() : Int {
        if(currentPage < maxPage){
            currentPage++
        }
        return currentPage
    }



    suspend fun getCharacterDetail(characterId: Int): CharacterDetailDTO {
        return NetworkModule.publicClient
            .get("$BASE_URL/character/$characterId")
            .body<CharacterDetailDTO>()
    }

    suspend fun getCharactersWithFilters(
        name: String? = null,
        status: String? = null,
        gender: String? = null,
        species: String? = null
    ): CharacterListDTO {
        try {
            val response = NetworkModule.publicClient
                .get("$BASE_URL/character/") {
                    name?.let { parameter("name", it) }
                    status?.let { parameter("status", it) }
                    gender?.let { parameter("gender", it) }
                    species?.let { parameter("species", it) }
                }
                .body<CharacterListDTO>()

            return response
        } catch (e: Exception) {
            throw Exception(e)
        }
    }



}