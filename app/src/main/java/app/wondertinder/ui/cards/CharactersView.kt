package app.wondertinder.ui.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import app.wondertinder.WonderTinderApp
import app.wondertinder.data.api.MarvelApi
import app.wondertinder.data.repositories.DatastoreRepository
import app.wondertinder.data.repositories.MainRepository
import app.wondertinder.databinding.FragmentCharactersBinding
import com.yuyakaido.android.cardstackview.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CharactersView : Fragment(), CardStackListener {

    private lateinit var binding: FragmentCharactersBinding
    private val adapter = CharactersAdapter()
    private lateinit var layoutManager: CardStackLayoutManager
    private val marvelApi = MarvelApi.invoke()
    private var charactersJob: Job? = null
    private val viewModel: CharactersViewModel by viewModels {
        CharactersViewModelFactory(
            MainRepository(marvelApi),
            (activity?.application as WonderTinderApp).heroRepository,
            (activity?.application as WonderTinderApp).datastoreRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentCharactersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtons()
        setupLayoutAndAdapter()
        loadCharactersJob()
    }

    private fun setupLayoutAndAdapter(){
        layoutManager = CardStackLayoutManager(this.context, this).apply {
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator(LinearInterpolator())
        }
        layoutManager.setStackFrom(StackFrom.Top)
        binding.charactersCardView.layoutManager = layoutManager
        binding.charactersCardView.adapter = adapter
        binding.charactersCardView.itemAnimator.apply {
            if (this is DefaultItemAnimator) {
                supportsChangeAnimations = false
            }
        }
    }

    private fun loadCharactersJob() {
        val test = runBlocking { viewModel.heroesRated.first() }
        charactersJob?.cancel()
        charactersJob = lifecycleScope.launch {
            viewModel.getAllCharactersFlow(test?:0)
                .collectLatest {
                    adapter.submitData(viewLifecycleOwner.lifecycle,it)
                }
        }
    }

    private fun setupButtons() {
        binding.skipButton.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            layoutManager.setSwipeAnimationSetting(setting)
            binding.charactersCardView.swipe()
        }

        binding.rewindButton.setOnClickListener {
            val setting = RewindAnimationSetting.Builder()
                .setDirection(Direction.Bottom)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(DecelerateInterpolator())
                .build()
            layoutManager.setRewindAnimationSetting(setting)
            binding.charactersCardView.rewind()
        }

        binding.likeButton.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(AccelerateInterpolator())
                .build()
            layoutManager.setSwipeAnimationSetting(setting)
            binding.charactersCardView.swipe()
        }

        binding.myHeroes.setOnClickListener {
            findNavController().navigate(
                CharactersViewDirections.openStatistics()
            )
        }
    }

    override fun onResume() {
        super.onResume()
        loadCharactersJob()
    }

    override fun onCardSwiped(direction: Direction?) {
        val like = direction == Direction.Right
        val swipedCharacter = adapter.snapshot().items[layoutManager.topPosition - 1]
        viewModel.addCharacterToMyHeroes(swipedCharacter,like)
        viewModel.incrementMyHeroes()
    }

    override fun onCardDisappeared(view: View?, position: Int) {}
    override fun onCardDragging(direction: Direction?, ratio: Float) {}
    override fun onCardCanceled() {}
    override fun onCardAppeared(view: View?, position: Int) {}
    override fun onCardRewound() {}
}