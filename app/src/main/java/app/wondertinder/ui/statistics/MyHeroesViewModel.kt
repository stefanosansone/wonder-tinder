package app.wondertinder.ui.statistics

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.wondertinder.data.entities.Hero
import app.wondertinder.data.repositories.HeroRepository
import app.wondertinder.data.repositories.MainRepository
import app.wondertinder.data.response.Result
import app.wondertinder.ui.cards.CharactersViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.withIndex
import kotlinx.coroutines.launch

class StatisticsViewModel constructor(
    private val heroRepository: HeroRepository
)  : ViewModel() {
    val allHeroes: Flow<List<Hero>> = heroRepository.allHeros
}

class StatisticsViewModelFactory( private val heroRepository: HeroRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StatisticsViewModel(heroRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}