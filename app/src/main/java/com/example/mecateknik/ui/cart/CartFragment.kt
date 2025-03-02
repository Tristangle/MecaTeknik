package com.example.mecateknik.ui.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mecateknik.databinding.FragmentCartBinding
import com.example.mecateknik.ui.autoparts.AutoPartSearchActivity
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[CartViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter avec callback pour supprimer un article
        cartAdapter = CartAdapter { cartItemDetail ->
            lifecycleScope.launch {
                viewModel.deleteCartItem(cartItemDetail)
            }
            Toast.makeText(requireContext(), "Article supprimé", Toast.LENGTH_SHORT).show()
        }

        binding.recyclerViewCart.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }

        // Bouton pour rechercher des pièces auto
        binding.btnSearchAutoParts.setOnClickListener {
            val intent = Intent(requireContext(), AutoPartSearchActivity::class.java)
            startActivity(intent)
        }

        // Bouton de checkout
        binding.btnCheckout.setOnClickListener {
            lifecycleScope.launch {
                viewModel.checkoutCart()
                Toast.makeText(requireContext(), "Commande validée, panier vidé", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.cartItemsDetails.observe(viewLifecycleOwner) { details ->
            if (details.isNullOrEmpty()) {
                binding.recyclerViewCart.visibility = View.GONE
                binding.textEmptyCart.visibility = View.VISIBLE
                binding.btnCheckout.isEnabled = false
            } else {
                binding.recyclerViewCart.visibility = View.VISIBLE
                binding.textEmptyCart.visibility = View.GONE
                binding.btnCheckout.isEnabled = true
            }
            cartAdapter.submitList(details)
            val total = viewModel.getTotalPrice(details)
            binding.textTotalPrice.text = "Total : ${"%.2f".format(total)} €"
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCartItems()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
