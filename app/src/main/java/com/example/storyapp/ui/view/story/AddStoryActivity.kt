package com.example.storyapp.ui.view.story

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.storyapp.R
import com.example.storyapp.data.model.auth.LoginResult
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.ui.factory.ViewModelFactory
import com.example.storyapp.ui.view.main.MainActivity
import com.example.storyapp.ui.viewmodel.AddStoryViewModel
import com.example.storyapp.utils.Result
import com.example.storyapp.utils.getImageUri
import com.example.storyapp.utils.reduceFileImage
import com.example.storyapp.utils.uriToFile

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private val viewModel: AddStoryViewModel by viewModels{
        ViewModelFactory.getInstance(application)
    }
    private lateinit var data: LoginResult
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getUserLogin().observe(this@AddStoryActivity) {
            data = it
        }
        binding.btnGalerry.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.buttonAdd.setOnClickListener { uploadImage() }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }
    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }


    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }
    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.edAddDescription.text.toString()
            viewModel.addStory(description, imageFile, data.token).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                            showLoading(false)
                        }
                        is Result.Error -> {
                            showToast(result.error)
                            showLoading(false)
                        }
                    }
                }
            }




        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}