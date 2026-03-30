package com.artar.busan.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.artar.busan.R
import com.artar.busan.ar.ArPanelPayload
import com.artar.busan.ar.MarkerDatabaseFactory
import com.artar.busan.checkin.StampStore
import com.artar.busan.tts.TtsManager
import com.artar.busan.util.LanguageUtil
import com.google.ar.core.AugmentedImage
import com.google.ar.core.TrackingState
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import java.util.ArrayDeque

class ArExperienceFragment : Fragment() {

    private val viewModel: ArViewModel by activityViewModels {
        AppViewModelFactory(StampStore(requireContext()))
    }

    private lateinit var ttsManager: TtsManager

    private var eventId: String = ""
    private var eventName: String = ""

    private var renderedMarkerName: String? = null
    private var lastRenderMillis: Long = 0L
    private val activeAnchors = ArrayDeque<AnchorNode>()
    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(requireContext(), "카메라 권한이 필요합니다", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventId = requireArguments().getString(ARG_EVENT_ID).orEmpty()
        eventName = requireArguments().getString(ARG_EVENT_NAME).orEmpty()
        ttsManager = TtsManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_ar_experience, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ensureCameraPermission()
        view.findViewById<TextView>(R.id.selectedEventText).text = eventName

        val languageSpinner = view.findViewById<Spinner>(R.id.languageSpinner)
        val supportedLangs = listOf("ko", "en", "jp", "cn")
        languageSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, supportedLangs)
        languageSpinner.setSelection(0)
        view.findViewById<Button>(R.id.applyLanguageBtn).setOnClickListener {
            viewModel.setLanguage(languageSpinner.selectedItem.toString())
            Toast.makeText(requireContext(), "언어 변경", Toast.LENGTH_SHORT).show()
        }

        val arHost = childFragmentManager.findFragmentById(R.id.arFragmentHost) as ArFragment
        arHost.setOnSessionConfigurationListener { session, _ ->
            session.configure(MarkerDatabaseFactory.configure(session))
        }

        arHost.arSceneView.scene.addOnUpdateListener {
            val frame = arHost.arSceneView.arFrame ?: return@addOnUpdateListener
            val updated = frame.getUpdatedTrackables(AugmentedImage::class.java)
            for (image in updated) {
                if (image.trackingState != TrackingState.TRACKING) continue
                if (System.currentTimeMillis() - lastRenderMillis < 1200) continue
                if (renderedMarkerName == image.name) continue

                val artwork = viewModel.findArtworkByMarker(image.name) ?: continue
                renderedMarkerName = image.name
                lastRenderMillis = System.currentTimeMillis()

                val anchor = image.createAnchor(image.centerPose)
                val anchorNode = AnchorNode(anchor).apply {
                    setParent(arHost.arSceneView.scene)
                }
                trackAnchor(anchorNode)

                val language = viewModel.currentLanguage.value ?: "ko"
                val payload = ArPanelPayload(artwork = artwork, language = language)
                renderPanel(anchorNode, payload)
                view.findViewById<TextView>(R.id.markerStatusText).setText(R.string.marker_found)
            }
        }

        viewModel.lastCheckin.observe(viewLifecycleOwner) { inserted ->
            when (inserted) {
                true -> Toast.makeText(requireContext(), "스탬프 저장 완료", Toast.LENGTH_SHORT).show()
                false -> Toast.makeText(requireContext(), "이미 체크인된 작품", Toast.LENGTH_SHORT).show()
                null -> Unit
            }
        }

        viewModel.loadArtworks(eventId)
    }

    private fun ensureCameraPermission() {
        val granted = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun trackAnchor(node: AnchorNode) {
        activeAnchors.addLast(node)
        while (activeAnchors.size > 3) {
            val oldest = activeAnchors.removeFirst()
            oldest.anchor?.detach()
            oldest.setParent(null)
        }
    }

    private fun renderPanel(anchorNode: AnchorNode, payload: ArPanelPayload) {
        ViewRenderable.builder()
            .setView(requireContext(), R.layout.view_ar_panel)
            .build()
            .thenAccept { renderable ->
                anchorNode.renderable = renderable
                bindPanelView(renderable.view, payload)
            }
    }

    private fun bindPanelView(panel: View, payload: ArPanelPayload) {
        val artwork = payload.artwork
        val language = payload.language
        val title = LanguageUtil.resolveText(artwork.titleI18n, language)
        val description = LanguageUtil.resolveText(artwork.descriptionI18n, language)

        panel.findViewById<TextView>(R.id.panelTitle).text = title
        panel.findViewById<TextView>(R.id.panelArtist).text = artwork.artist
        panel.findViewById<TextView>(R.id.panelDescription).text = description

        panel.findViewById<Button>(R.id.btnTts).setOnClickListener {
            val ok = ttsManager.speak(description, language)
            if (!ok) {
                Toast.makeText(requireContext(), "TTS를 사용할 수 없습니다", Toast.LENGTH_SHORT).show()
            }
        }

        panel.findViewById<Button>(R.id.btnCheckin).setOnClickListener {
            viewModel.checkIn(artwork.id)
            // TODO: Firebase Analytics 연결 시 checkin_success/checkin_duplicate 이벤트 연결
        }

        panel.findViewById<Button>(R.id.btnMore).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, ArtworkDetailFragment.newInstance(artwork.id, language))
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ttsManager.shutdown()
    }

    companion object {
        private const val ARG_EVENT_ID = "event_id"
        private const val ARG_EVENT_NAME = "event_name"
        private const val ARG_EVENT_COLOR = "event_color"

        fun newInstance(eventId: String, eventName: String, eventColor: String): ArExperienceFragment {
            val fragment = ArExperienceFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_EVENT_ID, eventId)
                putString(ARG_EVENT_NAME, eventName)
                putString(ARG_EVENT_COLOR, eventColor)
            }
            return fragment
        }
    }
}
