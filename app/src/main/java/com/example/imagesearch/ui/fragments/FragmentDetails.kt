package com.example.imagesearch.ui.fragments

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.imagesearch.R
import com.example.imagesearch.api.ConstantsWebservice
import com.example.imagesearch.databinding.FragmentDetailsBinding
import com.example.imagesearch.viewmodel.PhotoViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder

@AndroidEntryPoint
class FragmentDetails : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PhotoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val result = viewModel.resultToShowInFragmentDetails.value

        result?.let {
            with(binding) {
                Glide.with(this@FragmentDetails)
                    .load(result.urls.regular)
                    .error(R.drawable.ic_image)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.isVisible = false
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.isVisible = false
                            result.description?.let {
                                textViewDescription.isVisible = true
                            }
                            textViewAuthor.isVisible = true
                            return false
                        }
                    })
                    .into(imageView)

                textViewDescription.text = result.description

                val path = StringBuilder()
                    .append(ConstantsWebservice.URL)
                    .append(result.user.username)
                    .append(ConstantsWebservice.LINK_PARAMETER)
                    .toString()

                val uri = Uri.parse(path)
                val intent = Intent(Intent.ACTION_VIEW, uri)

                with(textViewAuthor) {

                    text = StringBuilder()
                        .append(ConstantsWebservice.PHOTO_BY)
                        .append(ConstantsWebservice.SPACE)
                        .append(result.user.name)
                        .append(ConstantsWebservice.SPACE)
                        .append(ConstantsWebservice.ON_UNSPLASH)
                        .toString()

                    paint.isUnderlineText = true

                    setOnClickListener { requireActivity().startActivity(intent) }
                }
            }
        }
    }
}