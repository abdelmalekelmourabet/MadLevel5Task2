package com.example.madlevel5task2

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.*

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val gameRepo = GameRepository(application.applicationContext)
    private val ioScope = CoroutineScope(Dispatchers.IO)

    val games: LiveData<List<Game>> = gameRepo.getGames()

    fun addGame(game: Game) {
        ioScope.launch {
            gameRepo.insertGame(game)
        }
    }

    fun deleteGame(game: Game) {
        ioScope.launch {
            gameRepo.deleteGame(game)
        }
    }

    fun deleteAllGames() {
        ioScope.launch {
            gameRepo.deleteAllGames()
        }
    }

}