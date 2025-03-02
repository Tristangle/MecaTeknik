package com.example.mecateknik.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mecateknik.databinding.FragmentHomeBinding
import com.example.mecateknik.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            binding.tvWelcome.text = "Bienvenue ${currentUser.displayName ?: "Utilisateur"}"
            binding.tvEmail.text = "${currentUser.email}"
        }

        binding.btnLogout.setOnClickListener {
            logoutUser()
        }

        return root
    }


    private fun logoutUser() {
        val auth = FirebaseAuth.getInstance()

        auth.signOut()
        println("✅ Utilisateur déconnecté")

        Handler(Looper.getMainLooper()).postDelayed({
            val currentUser = auth.currentUser
            println("🔄 Vérification après déconnexion : $currentUser")

            if (currentUser == null) {
                println("✅ Redirection vers LoginActivity")
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            } else {
                println("⚠️ L'utilisateur est toujours connecté après signOut()")
            }
        }, 1500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
