package com.example.souzmerch.ui.fragments.merchNavigationFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.souzmerch.data.model.Shop
import com.example.souzmerch.databinding.ItemSimpleTextBinding

class ShopsMerchAdapter(
    private val shopClickListener: (shop: Shop) -> Unit
) : RecyclerView.Adapter<ShopsMerchAdapter.ShopMerchViewHolder>() {

    // список заданий, которые будут отображаться
    private var shopList: MutableList<Shop> = mutableListOf()

    // метод для обновления списка заданий
    fun setData(data: List<Shop>) {
        shopList.clear()
        shopList.addAll(data)
        notifyDataSetChanged()
    }

    // ViewHolder, отображающий элемент списка заданий
    inner class ShopMerchViewHolder(private val binding: ItemSimpleTextBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(shop: Shop) {
            binding.tvTitleName.text = shop.name

            binding.root.setOnClickListener {
                shopClickListener(shop)
            }
        }
    }

    // создание ViewHolder для элемента списка заданий
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopMerchViewHolder {
        val binding = ItemSimpleTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopMerchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopMerchViewHolder, position: Int) {
        val shop = shopList[position]
        holder.bind(shop)
    }

    // количество элементов в списке заданий
    override fun getItemCount() = shopList.size
}
