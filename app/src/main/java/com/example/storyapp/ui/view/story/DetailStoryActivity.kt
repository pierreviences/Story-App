package com.example.storyapp.ui.view.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.storyapp.data.model.story.ListStoryItem
import com.example.storyapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyData = intent.getParcelableExtra<ListStoryItem>(STORY_INTENT_DATA)

        storyData?.let {
            binding.tvDetailName.text = it.name
            binding.tvDetailDescription.text = it.description

            Glide.with(this)
                .load(it.photoUrl)
                .into(binding.ivDetailPhoto)
        }
    }
    companion object {
        const val STORY_INTENT_DATA = "STORY_INTENT_DATA"
    }
}