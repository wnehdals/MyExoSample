package com.jdm.myexosample.ui.play.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.jdm.myexosample.databinding.ItemVideoDescriptionBinding
import com.jdm.myexosample.response.Video

class VideoDescriptionHolder(private val binding: ItemVideoDescriptionBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Video) {
        with(binding) {
            videoDescriptionTitle.text = item.title
        }
    }
}