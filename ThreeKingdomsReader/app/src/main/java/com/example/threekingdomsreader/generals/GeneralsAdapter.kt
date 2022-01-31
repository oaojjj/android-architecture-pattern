package com.example.threekingdomsreader.generals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.threekingdomsreader.R
import com.example.threekingdomsreader.data.General
import com.example.threekingdomsreader.databinding.ItemGeneralBinding
import com.example.threekingdomsreader.util.Utils

class GeneralsAdapter(items: List<General>, private val itemListener: GeneralItemListener) :
    ListAdapter<General, GeneralsAdapter.GeneralViewHolder>(GeneralsDiffCallback) {

    var generals: List<General> = items
        set(generals) {
            field = generals
            submitList(generals)
        }


    class GeneralViewHolder(val binding: ItemGeneralBinding, listener: GeneralItemListener) :
        RecyclerView.ViewHolder(binding.root) {

        private var currentGeneral: General? = null

        init {
            binding.root.setOnClickListener {
                currentGeneral?.let {
                    listener.onGeneralClick(it)
                }
            }
        }

        fun bind(general: General) {
            currentGeneral = general
            with(binding) {
                nameGeneral.text = general.name
                sexGeneral.text = general.sex
                belongGeneral.text = general.belong
                positionGeneral.text = general.position

                Utils.getColorAccordingBelong(root.context, general.belong).let {
                    container.setBackgroundColor(it)
                }

            }
        }

    }

    override fun getItem(position: Int): General {
        return generals[position]
    }

    override fun getItemCount(): Int {
        return generals.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneralViewHolder {
        return GeneralViewHolder(
            ItemGeneralBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_general, parent, false)
            ), itemListener
        )
    }

    override fun onBindViewHolder(holder: GeneralViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    object GeneralsDiffCallback : DiffUtil.ItemCallback<General>() {
        override fun areItemsTheSame(oldItem: General, newItem: General): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: General, newItem: General): Boolean {
            return oldItem.id == newItem.id
        }

    }
}

interface GeneralItemListener {
    fun onGeneralClick(clickGeneral: General)
}