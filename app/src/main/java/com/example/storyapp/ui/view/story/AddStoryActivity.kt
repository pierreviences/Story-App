package com.example.storyapp.ui.view.story

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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
import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private val viewModel: AddStoryViewModel by viewModels{
        ViewModelFactory.getInstance(application)
    }
    private lateinit var dataUser: LoginResult
    private lateinit var alertDialog: AlertDialog.Builder
    private val handler = Handler(Looper.getMainLooper())
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        when {
            it[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> getLocation()
            else -> binding.switchLocation.isChecked = false
        }
    }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var location: Location? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        alertDialog = AlertDialog.Builder(this@AddStoryActivity)
        viewModel.getUserLogin().observe(this@AddStoryActivity) {
            dataUser = it
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        binding.switchLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) getLocation()
            else location = null
        }

        setClickListeners()

    }
    private fun setClickListeners() {
        with(binding) {
            btnGalerry.setOnClickListener { startGallery() }
            btnCamera.setOnClickListener { startCamera() }
            buttonAdd.setOnClickListener { uploadImage() }
        }
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
            var lat: RequestBody? = null
            var lon: RequestBody? = null

            if (location != null) {
                lat = location?.latitude.toString()
                    .toRequestBody("text/plain".toMediaType())
                lon = location?.longitude.toString()
                    .toRequestBody("text/plain".toMediaType())
            }
            viewModel.addStory(lat, lon, description, imageFile, dataUser.token).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            showSuccessDialog()
                            handler.postDelayed({
                                startActivity(Intent(this@AddStoryActivity, MainActivity::class.java))
                                finish()
                            }, 3000)
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
    private fun showSuccessDialog() {
        val customDialogView = LayoutInflater.from(this).inflate(R.layout.custom_alertdialog_success, null)
        val alertDialog = AlertDialog.Builder(this)
            .setView(customDialogView)
            .create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()
        val tvDescription: TextView = customDialogView.findViewById(R.id.tv_description)
        tvDescription.text = getString(R.string.success_upload_photo)
        handler.postDelayed({
            alertDialog.dismiss()
        }, 3000)
    }

    private fun getLocation() {
        if (
            ContextCompat.checkSelfPermission(
                this@AddStoryActivity, ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                if (it != null) location = it
                else {
                    showToast(getString(R.string.please_active_locationyou))
                    binding.switchLocation.isChecked = false
                }
            }
        } else requestPermissionLauncher.launch(arrayOf(ACCESS_COARSE_LOCATION))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}