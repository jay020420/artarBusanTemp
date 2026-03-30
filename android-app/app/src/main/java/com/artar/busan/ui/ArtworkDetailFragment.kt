package com.artar.busan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.artar.busan.R
import com.artar.busan.checkin.StampStore
import com.artar.busan.util.LanguageUtil

class ArtworkDetailFragment : Fragment() {

    private val viewModel: ArViewModel by activityViewModels {
        AppViewModelFactory(StampStore(requireContext()))
    }

    private var artworkId: String = ""
    private var language: String = "ko"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        artworkId = requireArguments().getString(ARG_ARTWORK_ID).orEmpty()
        language = requireArguments().getString(ARG_LANGUAGE).orEmpty().ifBlank { "ko" }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_artwork_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val artwork = viewModel.artworks.value?.firstOrNull { it.id == artworkId }
        val title = artwork?.let { LanguageUtil.resolveText(it.titleI18n, language) }.orEmpty()
        val description = artwork?.let { LanguageUtil.resolveText(it.descriptionI18n, language) }.orEmpty()

        view.findViewById<TextView>(R.id.detailTitle).text = title
        view.findViewById<TextView>(R.id.detailDescription).text = description
        view.findViewById<Button>(R.id.detailClose).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        private const val ARG_ARTWORK_ID = "artwork_id"
        private const val ARG_LANGUAGE = "language"

        fun newInstance(artworkId: String, language: String): ArtworkDetailFragment {
            return ArtworkDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_ARTWORK_ID, artworkId)
                    putString(ARG_LANGUAGE, language)
                }
            }
        }
    }
}
