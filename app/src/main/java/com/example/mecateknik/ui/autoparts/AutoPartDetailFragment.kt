package com.example.mecateknik.ui.autoparts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mecateknik.api.YouTubeApi
import com.example.mecateknik.databinding.FragmentAutoPartDetailBinding
import com.example.mecateknik.db.AppDatabase
import com.example.mecateknik.utils.ApiKeyProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class AutoPartDetailFragment : Fragment() {

    private var _binding: FragmentAutoPartDetailBinding? = null
    private val binding get() = _binding!!
    private var partReference: String? = null

    companion object {
        private const val TAG = "AutoPartDetailFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        partReference = arguments?.getString("partReference")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAutoPartDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fadeIn = AlphaAnimation(0f, 1f).apply { duration = 500 }
        binding.detailContainer.startAnimation(fadeIn)

        binding.btnClose.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        partReference?.let { ref ->
            lifecycleScope.launch {
                val db = AppDatabase.getDatabase(requireContext())
                val autoPart = withContext(Dispatchers.IO) {
                    db.autoPartDao().getPartByReference(ref)
                }
                autoPart?.let { part ->
                    binding.textPartName.text = part.name
                    binding.textPartReference.text = "Référence : ${part.reference}"

                    val formattedConfigs = part.associatedModels.joinToString(", ") { config ->
                        val tokens = config.split(";")
                        if (tokens.size >= 3) {
                            "${tokens[0].capitalize()} ${tokens[1].capitalize()} (${tokens[2]})"
                        } else {
                            config
                        }
                    }
                    binding.textCompatibleModels.text = "Modèles compatibles : $formattedConfigs"
                    binding.textPartPrice.text = "Prix : ${part.price} €"
                    binding.textPartStock.text = "Stock : ${part.quantityInStock}"

                    val configTokens = part.associatedModels.firstOrNull()?.split(";")
                    val modelForQuery = if (configTokens != null && configTokens.size >= 3) configTokens[1] else ""
                    val query = "Tuto installation ${part.name} $modelForQuery"
                    fetchYoutubeVideo(query)
                }
            }
        }
    }

    private fun fetchYoutubeVideo(query: String) {
        lifecycleScope.launch {
            try {
                val encodedQuery = URLEncoder.encode(query, "UTF-8")
                Log.d(TAG, "Encoded query: $encodedQuery")
                val apiKey = ApiKeyProvider.getYoutubeApiKey(requireContext()) ?: ""
                Log.d(TAG, "Using API Key from assets: $apiKey")
                val response = withContext(Dispatchers.IO) {
                    YouTubeApi.retrofitService.searchVideos(
                        query = encodedQuery,
                        key = apiKey
                    )
                }
                Log.d(TAG, "API Response: $response")
                if (response.items.isNotEmpty()) {
                    val videoId = response.items.first().id.videoId
                    Log.d(TAG, "Video found: $videoId")
                    val youtubeEmbedUrl = "https://www.youtube.com/embed/$videoId?autoplay=1"
                    binding.webViewYoutube.webViewClient = WebViewClient()
                    val webSettings: WebSettings = binding.webViewYoutube.settings
                    webSettings.javaScriptEnabled = true
                    Log.d(TAG, "Loading URL: $youtubeEmbedUrl")
                    binding.webViewYoutube.loadUrl(youtubeEmbedUrl)
                } else {
                    Log.e(TAG, "No video found for query: $query")
                    binding.webViewYoutube.loadData(
                        "<html><body><p>Vidéo non trouvée</p></body></html>",
                        "text/html", "UTF-8"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching video", e)
                binding.webViewYoutube.loadData(
                    "<html><body><p>Erreur lors du chargement de la vidéo</p></body></html>",
                    "text/html", "UTF-8"
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
