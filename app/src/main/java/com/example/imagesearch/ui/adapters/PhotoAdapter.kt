package com.example.imagesearch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.imagesearch.R
import com.example.imagesearch.api.ConstantsWebservice
import com.example.imagesearch.data.models.Result
import com.example.imagesearch.databinding.OneItemBinding
import java.lang.StringBuilder

class PhotoAdapter : PagingDataAdapter<Result, PhotoAdapter.PhotoViewHolder>(diffUtilItemCallback) {

    companion object {
        private val diffUtilItemCallback = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val binding = OneItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoViewHolder(binding).also {
            binding.root.setOnClickListener {
            }
        }
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val result = getItem(position)
        result?.let {
            with(holder) {
                bind(result)
                itemView.setOnClickListener { onItemClickShowDetails?.invoke(result) }
            }
        }
    }

    class PhotoViewHolder(private val binding: OneItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Result) {
            binding.apply {
                Glide.with(itemView)
                    .load(result.urls.regular)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_image)
                    .into(imageView)

                val text = StringBuilder()
                    .append(ConstantsWebservice.PHOTO_BY)
                    .append(ConstantsWebservice.SPACE)
                    .append(result.user.name)
                    .append(ConstantsWebservice.SPACE)
                    .append(ConstantsWebservice.ON_UNSPLASH)
                    .toString()

                textView.text = text
            }
        }
    }

    var onItemClickShowDetails: ((Result) -> Unit)? = null
}