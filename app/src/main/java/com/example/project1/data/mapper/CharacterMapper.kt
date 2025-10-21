package com.example.project1.data.mapper

import com.example.project1.data.dto.CharacterDTO
import com.example.project1.data.model.CharacterModel
import com.example.project1.domain.entity.CharacterEntity
import androidx.core.net.toUri
import com.example.project1.data.dto.CharacterDetailDTO
import com.example.project1.domain.entity.CharacterDetailEntity

abstract class CharacterMapper {
    companion object{
        fun mapDTOEntity(dto: CharacterDTO): CharacterEntity {
            return CharacterEntity(
                id = dto.id,
                name = dto.name,
                image = dto.image.toUri(),
                status = dto.status,
                species = dto.species,
                gender = dto.gender
            )
        }
        fun mapModelToEntity(model: CharacterModel): CharacterEntity{
            return CharacterEntity(
                id = model.id,
                name = model.name,
                image = model.image.toUri(),
                status = model.status,
                species = model.species,
                gender = model.gender
            )
        }
        fun mapDTOToModel(dto: CharacterDTO): CharacterModel{
            return CharacterModel(
                name = dto.name,
                image = dto.image,
                status = dto.status,
                species = dto.species,
                gender = dto.gender
            )
        }
        fun detailDTOToCharacterDetailEntity(dto: CharacterDetailDTO): CharacterDetailEntity {
            return CharacterDetailEntity(
                id = dto.id,
                name = dto.name,
                status = dto.status,
                species = dto.species,
                type = dto.type,
                gender = dto.gender,
                origin = CharacterDetailEntity.Location(dto.origin.name, dto.origin.url),
                location = CharacterDetailEntity.Location(dto.location.name, dto.location.url),
                image = dto.image,
                episode = dto.episode,
                url = dto.url,
                created = dto.created
            )
        }
    }

}