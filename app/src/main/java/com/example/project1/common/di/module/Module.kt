package com.example.project1.common.di.module

import androidx.room.Room
import com.example.project1.common.database.Database
import com.example.project1.data.repository.RickRepository
import com.example.project1.data.service.RickApiService
import com.example.project1.domain.repository.IRickRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.project1.presentation.viewmodel.CharacterViewModel
import com.example.project1.presentation.viewmodel.CharacterDetailViewModel


val repositoryModule = module {
    single<IRickRepository> {
        RickRepository(RickApiService, get())
    }
}

val databaseModule = module {
    single{
        Room.databaseBuilder(
                    androidContext(),
                    Database::class.java,
                    "database"
                ).build()
    }


}

val viewModelModule = module {
    viewModel { CharacterViewModel(get()) }
    viewModel { CharacterDetailViewModel(get()) }
}



val appModule = module {
    single { get<Database>().characterDAO() }
}

