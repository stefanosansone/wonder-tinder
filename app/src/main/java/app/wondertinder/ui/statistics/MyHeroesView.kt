package app.wondertinder.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.wondertinder.WonderTinderApp
import app.wondertinder.databinding.FragmentMyHeroesBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MyHeroesView : Fragment() {

    private lateinit var binding: FragmentMyHeroesBinding
    private val likeAdapter = MyHeroesListAdapter()
    private val dislikeAdapter = MyHeroesListAdapter()
    private var likedHeroesJob: Job? = null
    private var dislikedHeroesJob: Job? = null
    private val viewModel: StatisticsViewModel by viewModels {
        StatisticsViewModelFactory((activity?.application as WonderTinderApp).heroRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentMyHeroesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayoutAndAdapter()
        startHeroesJob()
    }

    private fun setupLayoutAndAdapter(){
        binding.likedHeroList.adapter = likeAdapter
        binding.likedHeroList.layoutManager = LinearLayoutManager(this.context)
        binding.dislikedHeroList.adapter = dislikeAdapter
        binding.dislikedHeroList.layoutManager = LinearLayoutManager(this.context)
    }

    private fun startHeroesJob() {
        likedHeroesJob?.cancel()
        likedHeroesJob = lifecycleScope.launch {
            viewModel.allHeroes
                .collectLatest {
                    likeAdapter.submitList(it.filter { it.like }.sortedBy { it.name })
                }
        }
        dislikedHeroesJob?.cancel()
        dislikedHeroesJob = lifecycleScope.launch {
            viewModel.allHeroes
                .collectLatest {
                    dislikeAdapter.submitList((it.filter { !it.like }).sortedBy { it.name })
                }
        }
    }

}