package app.wondertinder.ui.cards

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.wondertinder.data.entities.Hero
import app.wondertinder.data.repositories.DatastoreRepository
import app.wondertinder.data.repositories.HeroRepository
import app.wondertinder.data.repositories.MainRepository
import app.wondertinder.data.response.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CharactersViewModel constructor(
    private val repository: MainRepository,
    private val heroRepository: HeroRepository,
    private val datastoreRepository: DatastoreRepository
)  : ViewModel() {

    var charactersList: Flow<PagingData<Result>>? = null

    val heroesRated = datastoreRepository.heroesRatedFlow

    fun getAllCharactersFlow(heroesRated: Int): Flow<PagingData<Result>> {
        val newResultFlow: Flow<PagingData<Result>> =
            repository.getCharacters(heroesRated).cachedIn(viewModelScope)
        charactersList = newResultFlow
        return newResultFlow
    }

    fun addCharacterToMyHeroes(character: Result, like: Boolean){
        viewModelScope.launch(Dispatchers.IO) {
            val myHero = Hero(character.id,character.name,like)
            heroRepository.insert(myHero)
        }
    }

    fun incrementMyHeroes(){
        viewModelScope.launch {
            datastoreRepository.addHeroesRated()
        }
    }
}

class CharactersViewModelFactory constructor(
    private val repository: MainRepository,
    private val heroRepository: HeroRepository,
    private val datastoreRepository: DatastoreRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CharactersViewModel::class.java)) {
            CharactersViewModel(this.repository,this.heroRepository,this.datastoreRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}