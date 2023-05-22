package com.example.souzmerch.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.souzmerch.data.model.Shop
import com.example.souzmerch.databinding.FragmentMerchNavigationBinding
import com.example.souzmerch.shared.extensions.applyVisibility
import com.example.souzmerch.ui.adapters.ShopsMerchAdapter
import com.example.souzmerch.ui.BaseFragment
import com.example.souzmerch.ui.viewModels.ShopsViewModel
import com.google.firebase.auth.FirebaseAuth

class MerchNavigationFragment :
    BaseFragment<FragmentMerchNavigationBinding>(FragmentMerchNavigationBinding::inflate) {

    private lateinit var shopsViewModel: ShopsViewModel

    private val shopsAdapter by lazy {
        ShopsMerchAdapter() {
            navigateToMissionsList(it)
        }
    }

    private fun navigateToMissionsList(shop: Shop) {
        val action =
            MerchNavigationFragmentDirections.actionMerchNavigationFragmentToMissionsFragment(shop.id)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shopsViewModel = ViewModelProvider(this)[ShopsViewModel::class.java]
        val user = FirebaseAuth.getInstance().currentUser

        user?.uid?.let { id -> shopsViewModel.getShopsForMerchandiser(id) }
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
            adapter = shopsAdapter
        }

        shopsViewModel.shopList.observe(viewLifecycleOwner) {
            shopsAdapter.setData(it)
        }

        shopsViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.applyVisibility(it)
        }
    }
}