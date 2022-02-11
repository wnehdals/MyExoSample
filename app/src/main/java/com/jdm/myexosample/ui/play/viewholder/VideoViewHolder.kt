package com.jdm.myexosample.ui.play.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.jdm.myexosample.databinding.ItemVideoBinding
import com.jdm.myexosample.response.Video

class VideoViewHolder(private val binding: ItemVideoBinding, private val onClick: (Int) -> Unit): RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Video, position: Int) {
        with(binding) {
            Glide.with(videoItemThumbnail.context)
                .load(item.thumbnail)
                .transform(CenterCrop())
                .into(videoItemThumbnail)
            videoItemTitle.text = item.title
            videoItemConstraintlayout.setOnClickListener { onClick(position) }
        }
    }
}