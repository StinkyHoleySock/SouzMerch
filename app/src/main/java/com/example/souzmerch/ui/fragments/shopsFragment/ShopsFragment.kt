package com.example.souzmerch.ui.fragments.shopsFragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.souzmerch.data.model.Shop
import com.example.souzmerch.databinding.FragmentShopsBinding
import com.example.souzmerch.shared.extensions.applyVisibility
import com.example.souzmerch.ui.fragments.BaseFragment
import com.example.souzmerch.ui.fragments.merchNavigationFragment.MerchNavigationFragmentDirections
import com.example.souzmerch.ui.fragments.merchNavigationFragment.ShopsMerchAdapter
import com.example.souzmerch.ui.fragments.merchNavigationFragment.ShopsViewModel
import com.google.firebase.auth.FirebaseAuth

class ShopsFragment : BaseFragment<FragmentShopsBinding>(FragmentShopsBinding::inflate) {
    private lateinit var shopsViewModel: ShopsViewModel

    private val shopsAdapter by lazy {
        ShopsMerchAdapter() {
            navigateToCreateMission(it)
        }
    }

    private fun navigateToCreateMission(shop: Shop) {
        val action = ShopsFragmentDirections.actionShopsFragmentToCrateMissionFragment(shop.id)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shopsViewModel = ViewModelProvider(this)[ShopsViewModel::class.java]

        shopsViewModel.getAllShops()
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