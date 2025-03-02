package com.example.mecateknik.ui.autoparts

import android.net.Uri
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
import com.example.mecateknik.db.entities.CartItemEntity
import com.example.mecateknik.utils.ApiKeyProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class AutoPartDetailFragment : Fragment() {

    private var _binding: FragmentAutoPartDetailBinding? = null
    private val binding get() = _binding!!
    private var partReference: String? = null
    private var selectedQuantity: Int = 1
    private var currentStock: Int = 0

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

        // Animation fade-in
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 500
        binding.detailContainer.startAnimation(fadeIn)

        // Bouton de fermeture
        binding.btnClose.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Gestion des boutons de quantité
        binding.btnDecrease.setOnClickListener {
            if (selectedQuantity > 1) {
                selectedQuantity--
                binding.tvQuantity.text = selectedQuantity.toString()
            }
        }
        binding.btnIncrease.setOnClickListener {
            if (selectedQuantity < currentStock) {
                selectedQuantity++
                binding.tvQuantity.text = selectedQuantity.toString()
            }
        }

        // Bouton "Ajouter au panier"
        binding.btnAddToCart.setOnClickListener {
            addToCart()
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
                    // Formatage des configurations en "Brand model (year)"
                    val formattedConfigs = part.associatedModels.joinToString(", ") { config ->
                        val tokens = config.split(";")
                        if (tokens.size >= 3) {
                            "${tokens[0].capitalize()} ${tokens[1].capitalize()} (${tokens[2]})"
                        } else {
                            config
                        }
                    }
                    binding.textCompatibleModels.text = "Modèles compatibles : $formattedConfigs"
                    binding.textPartPrice.text = "Prix : ${String.format("%.2f", part.price)} €"
                    binding.textPartStock.text = "Stock : ${part.quantityInStock}"
                    currentStock = part.quantityInStock
                    binding.tvQuantity.text = selectedQuantity.toString()

                    // Recherche vidéo via YouTube API
                    val configTokens = part.associatedModels.firstOrNull()?.split(";")
                    val modelForQuery = if (configTokens != null && configTokens.size >= 3) configTokens[1] else ""
                    val query = "Tuto installation ${part.name} $modelForQuery"
                    fetchYoutubeVideo(query)
                }
            }
        }
    }

    private fun addToCart() {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(requireContext())
            val userUid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: return@launch
            // Récupérer tous les articles du panier pour cet utilisateur
            val existingCartItems = db.cartDao().getCartItemsByUser(userUid)
            // Filtrer l'article correspondant à cette pièce
            val matchingItems = existingCartItems.filter { it.autoPartReference == partReference }
            val currentCartQuantity = matchingItems.sumOf { it.quantity }
            // Vérifier que la quantité totale (existante + celle à ajouter) ne dépasse pas le stock
            if (currentCartQuantity + selectedQuantity > currentStock) {
                withContext(Dispatchers.Main) {
                    android.widget.Toast.makeText(
                        requireContext(),
                        "Pas assez de produit en stock",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                if (matchingItems.isNotEmpty()) {
                    // Mettre à jour la quantité de l'article existant
                    val existingItem = matchingItems.first()
                    val updatedItem = existingItem.copy(quantity = existingItem.quantity + selectedQuantity)
                    db.cartDao().updateCartItem(updatedItem)
                } else {
                    // Créer un nouvel article dans le panier
                    val cartItem = CartItemEntity(
                        userUid = userUid,
                        autoPartReference = partReference ?: return@launch,
                        quantity = selectedQuantity
                    )
                    db.cartDao().insertCartItem(cartItem)
                }
                withContext(Dispatchers.Main) {
                    android.widget.Toast.makeText(requireContext(), "Article ajouté au panier", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    @Suppress("BlockingMethodInNonBlockingContext")
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
