package com.example.project1.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.project1.data.model.CharacterModel
import com.example.project1.data.service.CharactersDao


@Database(
    entities = [CharacterModel::class],
    version = 1,
    exportSchema = false,
)
abstract class Database: RoomDatabase() {
    abstract fun characterDAO(): CharactersDao
}