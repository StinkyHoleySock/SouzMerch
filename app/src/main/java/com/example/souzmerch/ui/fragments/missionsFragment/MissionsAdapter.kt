package com.example.souzmerch.ui.fragments.missionsFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.souzmerch.data.enums.MissionState
import com.example.souzmerch.data.model.Mission
import com.example.souzmerch.databinding.ItemMissionBinding

class MissionsAdapter(
    private val missionClickListener: (mission: Mission) -> Unit
) : RecyclerView.Adapter<MissionsAdapter.MissionViewHolder>() {

    // список заданий, которые будут отображаться
    private var missionList: MutableList<Mission> = mutableListOf()

    // метод для обновления списка заданий
    fun setData(data: List<Mission>) {
        missionList.clear()
        missionList.addAll(data)
        notifyDataSetChanged()
    }

    // ViewHolder, отображающий элемент списка заданий
    inner class MissionViewHolder(private val binding: ItemMissionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(mission: Mission) {
            binding.tvTaskName.text = mission.product
            binding.tvTaskDate.text = "123"


            when (mission.status) {
                MissionState.CREATE.name -> {binding.tvTaskStatus.text = "Создано"}
                MissionState.PROGRESS.name -> {binding.tvTaskStatus.text = "Принято"}
                MissionState.VERIFICATION.name -> {binding.tvTaskStatus.text = ""}
                MissionState.COMPLETE.name -> {binding.tvTaskStatus.text = mission.status}
            }

            binding.root.setOnClickListener {
                missionClickListener(mission)
            }
        }
    }

    // создание ViewHolder для элемента списка заданий
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionViewHolder {
        val binding =
            ItemMissionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MissionViewHolder(binding)
    }

    // привязка данных заданий к ViewHolder
    override fun onBindViewHolder(holder: MissionViewHolder, position: Int) {
        val mission = missionList[position]
        holder.bind(mission)
    }

    // количество элементов в списке заданий
    override fun getItemCount() = missionList.size
}