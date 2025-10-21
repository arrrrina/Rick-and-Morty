package com.example.project1.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.project1.domain.entity.CharacterDetailEntity
import com.example.project1.domain.repository.IRickRepository
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CharacterDetailViewModel (
    private val repository: IRickRepository,
): ViewModel(){
    private val _characterDetail = mutableStateOf<CharacterDetailEntity?>(null)
    val characterDetail: State<CharacterDetailEntity?> = _characterDetail

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _isError = mutableStateOf(false)
    val isError: State<Boolean> = _isError


    fun loadCharacterDetail(characterId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = false
            try {
                val result = repository.getCharacterDetail(characterId)
                _characterDetail.value = result
            } catch (e: Exception) {
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }
}