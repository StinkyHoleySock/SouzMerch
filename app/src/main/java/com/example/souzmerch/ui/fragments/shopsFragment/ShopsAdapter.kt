package com.example.souzmerch.ui.fragments.shopsFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.souzmerch.data.model.Shop
import com.example.souzmerch.databinding.ItemSimpleTextBinding

class ShopsAdapter(
    private val shopClickListener: (shop: Shop) -> Unit
) : RecyclerView.Adapter<ShopsAdapter.ShopViewHolder>() {

    // список ТТ, которые будут отображаться
    private var shopList: MutableList<Shop> = mutableListOf()

    // метод для обновления списка групп
    fun setData(data: List<Shop>) {
        shopList.clear()
        shopList.addAll(data)
        notifyDataSetChanged()
    }

    // ViewHolder, отображающий элемент списка групп
    inner class ShopViewHolder(private val binding: ItemSimpleTextBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(shop: Shop) {
            binding.tvTitleName.text = shop.name

            binding.root.setOnClickListener {
                shopClickListener(shop)
            }
        }
    }

    // создание ViewHolder для элемента списка ТТ
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopViewHolder {
        val binding =
            ItemSimpleTextBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShopViewHolder(binding)
    }

    // привязка данных ТТ к ViewHolder
    override fun onBindViewHolder(holder: ShopViewHolder, position: Int) {
        val shop = shopList[position]
        holder.bind(shop)
    }

    // количество элементов в списке ТТ
    override fun getItemCount() = shopList.size
}