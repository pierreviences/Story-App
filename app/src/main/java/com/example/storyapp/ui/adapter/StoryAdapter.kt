package com.example.storyapp.ui.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyapp.R
import com.example.storyapp.data.model.story.ListStoryItem
import com.example.storyapp.databinding.ItemStoryBinding
import com.example.storyapp.ui.view.story.DetailStoryActivity
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter

class StoryAdapter : PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem) = with(binding) {
            tvItemName.text = data.name
            tvItemDescription.text = data.description

            Glide.with(itemView.context)
                .load(data.photoUrl)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(ivItemPhoto)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailStoryActivity::class.java).apply {
                    putExtra(DetailStoryActivity.STORY_INTENT_DATA, data)
                }

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(ivItemPhoto, "picture"),
                        Pair(tvItemName, "name"),
                        Pair(tvItemDescription, "description"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )



    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}