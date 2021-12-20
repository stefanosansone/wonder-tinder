package app.wondertinder.ui.cards

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.wondertinder.R
import app.wondertinder.data.response.Result
import app.wondertinder.databinding.CharacterCardBinding
import coil.load

class CharactersAdapter
    : PagingDataAdapter<Result, CharactersAdapter.CharactersViewHolder>(CharactersDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CharactersViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.character_card,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val character = getItem(position)
        holder.binding.result = character
        holder.binding.executePendingBindings()
    }

    inner class CharactersViewHolder(
        val binding: CharacterCardBinding
    ) : RecyclerView.ViewHolder(binding.root)

    private class CharactersDiffCallback : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        val urlHttps = url.replace("http:","https:")
        Log.d("TEST_URL",urlHttps)
        view.load(urlHttps)
    }
}