package com.jdm.myexosample.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.jdm.myexosample.databinding.ItemVideoBinding
import com.jdm.myexosample.response.Video

class VideoListAdapter: ListAdapter<Video, VideoListAdapter.ViewHolder>(videoDifUtil) {
    var onClick: (Int) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = getItem(position)
        if(video != null) {
            holder.bind(video)
            holder.binding.videoItemConstraintlayout.setOnClickListener { onClick(position) }
        }

    }

    class ViewHolder(val binding: ItemVideoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Video) {
            with(binding) {
                Glide.with(videoItemThumbnail.context)
                    .load(item.thumbnail)
                    .transform(CenterCrop())
                    .into(videoItemThumbnail)
                videoItemTitle.text = item.title
            }
        }
    }
    companion object {
        val videoDifUtil = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}