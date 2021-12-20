package app.wondertinder.ui.statistics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import app.wondertinder.R
import app.wondertinder.data.entities.Hero

class MyHeroesListAdapter : ListAdapter<Hero, MyHeroesListAdapter.MyHeroesViewHolder>(WORDS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHeroesViewHolder {
        return MyHeroesViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyHeroesViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.name)
    }

    class MyHeroesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val heroItemView: TextView = itemView.findViewById(R.id.hero_name)

        fun bind(text: String?) {
            heroItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): MyHeroesViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.hero_item, parent, false)
                return MyHeroesViewHolder(view)
            }
        }
    }

    companion object {
        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<Hero>() {
            override fun areItemsTheSame(oldItem: Hero, newItem: Hero): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Hero, newItem: Hero): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}