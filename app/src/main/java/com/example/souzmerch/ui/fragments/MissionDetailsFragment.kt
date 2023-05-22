package com.example.souzmerch.ui.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.souzmerch.data.enums.MissionState
import com.example.souzmerch.databinding.FragmentMissionDetailsBinding
import com.example.souzmerch.shared.extensions.applyVisibility
import com.example.souzmerch.shared.extensions.convertLongToTime
import com.example.souzmerch.shared.utils.DistanceUtil
import com.example.souzmerch.ui.BaseFragment
import com.example.souzmerch.ui.viewModels.MissionDetailsViewModel
import com.example.souzmerch.ui.viewModels.ShopsViewModel
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MissionDetailsFragment :
    BaseFragment<FragmentMissionDetailsBinding>(FragmentMissionDetailsBinding::inflate) {

    val args: MissionDetailsFragmentArgs by navArgs()
    var photo: String = ""
    var userLocation: String = ""
    var userLat: Double = 0.0
    var userLon: Double = 0.0
    lateinit var missionDetailsViewModel: MissionDetailsViewModel
    lateinit var shopViewModel: ShopsViewModel

    val storageRef = Firebase.storage.reference;

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLocationManager()

        val imagePickerActivityResult: ActivityResultLauncher<Intent> =
        // lambda expression to receive a result back, here we
            // receive single item(photo) on selection
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result != null) {
                    // getting URI of selected Image
                    val imageUri: Uri? = result.data?.data
                    // val fileName = imageUri?.pathSegments?.last()

                    // extract the file name with extension
                    val sd = getFileName(requireContext(), imageUri!!)
                    photo = sd.toString()

                    // Upload Task with upload to directory 'file'
                    // and name of the file remains same
                    val uploadTask = storageRef.child("file/$sd").putFile(imageUri)

                    // On success, download the file URL and display it
                    uploadTask.addOnSuccessListener {
                        // using glide library to display the image
                        storageRef.child("upload/$sd").downloadUrl.addOnSuccessListener {
                            Glide.with(binding.ivTaskImage.context)
                                .load(it)
                                .into(binding.ivTaskImage)

                            Log.e("Firebase", "download passed")
                        }.addOnFailureListener {
                            Log.e("Firebase", "Failed in downloading")
                        }
                    }.addOnFailureListener {
                        Log.e("Firebase", "Image Upload fail")
                    }
                }
            }

        missionDetailsViewModel = ViewModelProvider(this)[MissionDetailsViewModel::class.java]
        shopViewModel = ViewModelProvider(this)[ShopsViewModel::class.java]

        missionDetailsViewModel.getMission(args.missionId)

        missionDetailsViewModel.mission.observe(viewLifecycleOwner) { mission ->
            binding.llComment.applyVisibility(mission.comment != "")

            binding.tvAmount.text = mission.amount.toString()
            binding.tvProduct.text = mission.product
            binding.tvComment.text = mission.comment

            if (mission.startTime?.isNotEmpty() == true)
                binding.tvStartTime.text = mission.startTime.toLong().convertLongToTime()

            when (mission.status) {
                MissionState.CREATE.name -> {
                    binding.btnSave.text = "Принять задачу"
                }
                MissionState.PROGRESS.name -> {
                    binding.btnSave.text = "Сохранить"

                    binding.ivTaskImage.setOnClickListener {
                        // PICK INTENT picks item from data
                        // and returned selected item
                        val galleryIntent = Intent(Intent.ACTION_PICK)
                        // here item is type of image
                        galleryIntent.type = "image/*"
                        // ActivityResultLauncher callback
                        imagePickerActivityResult.launch(galleryIntent)
                    }
                }
                MissionState.VERIFICATION.name -> {
                    binding.btnSave.text = "На проверке"
                    binding.btnSave.isEnabled = false
                }
                MissionState.COMPLETE.name -> {
                    binding.btnSave.text = "Выполнено"
                    binding.btnSave.isEnabled = false
                }
            }
        }

        binding.btnSave.setOnClickListener {
            val mission = missionDetailsViewModel.mission.value
            val results = FloatArray(3)
            val shop = shopViewModel.shop.value
            DistanceUtil.computeDistanceAndBearing(
                userLat,
                userLon,
                shop?.lat?.toDouble() ?: 0.0,
                shop?.lon?.toDouble() ?: 0.0,
                results
            )
            when (mission?.status) {
                MissionState.CREATE.name -> {
                    val startTime = System.currentTimeMillis().toString()
                    if (userLocation != "" && results[0] < 100) {

                        missionDetailsViewModel.updateMissionStatus(
                            args.missionId,
                            MissionState.PROGRESS.name
                        )

                        mission.id.let { id ->
                            missionDetailsViewModel.updateMissionStartTime(
                                id,
                                startTime
                            )
                        }
                        Log.d("develop", "loc: $userLocation")
                        findNavController().popBackStack()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Вы не можете приступить к заданию!",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("develop", "loc: $userLocation")
                    }
                }
                MissionState.PROGRESS.name -> {
                    binding.btnSave.setOnClickListener {
                        if (photo != "") {
                            missionDetailsViewModel.updateMissionStatus(
                                args.missionId,
                                MissionState.VERIFICATION.name
                            )
                            missionDetailsViewModel.updateMissionPhoto(args.missionId, photo)

                            findNavController().popBackStack()
                        } else {
                            Toast.makeText(requireContext(), "Выберете фото!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                MissionState.VERIFICATION.name -> {}
                MissionState.COMPLETE.name -> {}
            }
        }
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }

    private fun initLocationManager() {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("develop", "permission denied")
            return
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            0,
            0f
        ) { location ->
            userLocation = location.latitude.toString() + " " + location.longitude.toString()
            userLat = location.latitude
            userLon = location.longitude
            Log.d("develop", "loc ->: $location")
        }
    }
}