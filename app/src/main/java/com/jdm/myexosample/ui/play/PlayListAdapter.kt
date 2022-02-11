package com.jdm.myexosample.ui.play

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.jdm.myexosample.const.VIDEO_DESCRIPTION_ITEM
import com.jdm.myexosample.const.VIDEO_ITEM
import com.jdm.myexosample.databinding.ItemVideoBinding
import com.jdm.myexosample.databinding.ItemVideoDescriptionBinding
import com.jdm.myexosample.response.Video
import com.jdm.myexosample.ui.play.viewholder.VideoDescriptionHolder
import com.jdm.myexosample.ui.play.viewholder.VideoViewHolder

class PlayListAdapter : ListAdapter<Video, RecyclerView.ViewHolder>(videoDifUtil) {
    var onClick: (Int) -> Unit = {}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIDEO_DESCRIPTION_ITEM -> VideoDescriptionHolder(
                ItemVideoDescriptionBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> VideoViewHolder(
                ItemVideoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), onClick
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val video = getItem(position)
        if (video != null) {
            when (holder) {
                is VideoDescriptionHolder -> holder.bind(video)
                else -> (holder as VideoViewHolder).bind(video, position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        var item = currentList.get(position)
        return when (item.id) {
            VIDEO_DESCRIPTION_ITEM -> VIDEO_DESCRIPTION_ITEM
            else -> VIDEO_ITEM
        }
    }


    companion object {
        val videoDifUtil = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem.id == newItem.id && oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem.id == newItem.id && oldItem.title == newItem.title
            }
        }
    }
}