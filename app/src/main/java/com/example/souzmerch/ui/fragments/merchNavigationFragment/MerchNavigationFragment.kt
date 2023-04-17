package com.example.souzmerch.ui.fragments.merchNavigationFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.souzmerch.data.model.Shop
import com.example.souzmerch.databinding.FragmentMerchNavigationBinding
import com.example.souzmerch.shared.extensions.applyVisibility
import com.example.souzmerch.ui.fragments.BaseFragment
import com.example.souzmerch.ui.fragments.shopsFragment.ShopsFragmentDirections
import com.google.firebase.auth.FirebaseAuth

class MerchNavigationFragment :
    BaseFragment<FragmentMerchNavigationBinding>(FragmentMerchNavigationBinding::inflate) {

    private lateinit var shopsViewModel: ShopsViewModel

    private val shopsAdapter by lazy {
        ShopsMerchAdapter() {
            navigateToMissionsList(it)
        }
    }

    private fun navigateToMissionsList(it: Shop) {
        val action = MerchNavigationFragmentDirections.actionMerchNavigationFragmentToMissionsFragment(it.id)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shopsViewModel = ViewModelProvider(this)[ShopsViewModel::class.java]
        val user = FirebaseAuth.getInstance().currentUser

        user?.uid?.let { shopsViewModel.getShopsForMerchandiser(it) }
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