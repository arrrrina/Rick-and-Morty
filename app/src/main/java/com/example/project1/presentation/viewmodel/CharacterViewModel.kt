package com.example.project1.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project1.domain.entity.CharacterEntity
import com.example.project1.domain.repository.IRickRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val repository: IRickRepository,
) : ViewModel() {
    private val _characters = MutableStateFlow(emptyList<CharacterEntity>())
    val characters: StateFlow<List<CharacterEntity>> = _characters.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    var isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isError = MutableStateFlow(false)
    var isError: StateFlow<Boolean> = _isError.asStateFlow()

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _selectedStatus = mutableStateOf<String?>(null)
    val selectedStatus: State<String?> = _selectedStatus

    private val _selectedGender = mutableStateOf<String?>(null)
    val selectedGender: State<String?> = _selectedGender

    private val _selectedSpecies = mutableStateOf<String?>(null)
    val selectedSpecies: State<String?> get() = _selectedSpecies


    fun loadCharacter(loadMore: Boolean = false){
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = false
            try {
                val charactersList = repository.getAllCharacters(loadMore)
                _characters.value = charactersList
            } catch(e: Exception){
                _isError.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isNotEmpty()) {
            applyFiltersWithApiFallback()
        } else {
            loadCharacter()
        }
    }

    fun setStatusFilter(status: String?) {
        _selectedStatus.value = status
        applyFiltersWithApiFallback()
    }

    fun setGenderFilter(gender: String?) {
        _selectedGender.value = gender
        applyFiltersWithApiFallback()
    }

    fun setSpeciesFilter(species: String?) {
        _selectedSpecies.value = species
        applyFiltersWithApiFallback()
    }


    fun clearAllFilters() {
        _searchQuery.value = ""
        _selectedStatus.value = null
        _selectedGender.value = null
        _selectedSpecies.value = null
        loadCharacter()
    }

    private fun applyFiltersWithApiFallback() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _isError.value = false
                if (hasActiveFilters()) {
                    val apiCharacters = repository.getCharactersWithFilters(
                        name = _searchQuery.value.ifEmpty { null },
                        status = _selectedStatus.value,
                        gender = _selectedGender.value,
                        species = _selectedSpecies.value
                    )

                    if (apiCharacters.isNotEmpty()) {
                        _characters.value = apiCharacters
                        return@launch
                    }
                }
                applyLocalFilters()

            } catch (e: Exception) {
                _isError.value = true
                applyLocalFilters()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun applyLocalFilters() {
        val allCharacters = repository.getAllCharacters(false)
        _characters.value = allCharacters.filter { character ->
            val matchesSearch = _searchQuery.value.isEmpty() ||
                    character.name.contains(_searchQuery.value, ignoreCase = true) ||
                    character.species.contains(_searchQuery.value, ignoreCase = true)

            val matchesStatus = _selectedStatus.value == null ||
                    character.status.equals(_selectedStatus.value, ignoreCase = true)

            val matchesGender = _selectedGender.value == null ||
                    character.gender.equals(_selectedGender.value, ignoreCase = true)

            val matchesSpecies = _selectedSpecies.value == null ||
                    character.species.equals(_selectedSpecies.value, ignoreCase = true)

            matchesSearch && matchesStatus && matchesGender && matchesSpecies
        }
    }

    private fun hasActiveFilters(): Boolean {
        return _searchQuery.value.isNotEmpty() || _selectedStatus.value != null ||
                _selectedGender.value != null || _selectedSpecies.value != null
    }

}