package com.example.project1.data.service

import android.util.Log
import com.example.project1.common.api.NetworkModule
import com.example.project1.data.dto.CharacterListDTO
import io.ktor.client.call.body
import io.ktor.client.request.get

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
    suspend fun getCharacterById(id: Int): CharacterListDTO{
        return NetworkModule.publicClient.get("$BASE_URL/character/$id").body()
    }

    private fun resetPagination() : Int {
        if(currentPage < maxPage){
            currentPage++
        }
        return currentPage
    }
}