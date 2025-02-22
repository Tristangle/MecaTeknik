package com.example.mecateknik

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mecateknik.databinding.ActivityMainBinding
import android.content.Intent
import com.example.mecateknik.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        auth.signOut() // 🔥 Déconnexion forcée
        auth.currentUser?.reload()?.addOnCompleteListener {
            val refreshedUser = auth.currentUser
            if (refreshedUser == null) {
                println("🚨 Aucun utilisateur après signOut, redirection vers LoginActivity")
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                println("✅ Utilisateur toujours détecté après signOut : ${refreshedUser.email}")
            }
        }



        // 🛠 Chargement normal de l'interface
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as? NavHostFragment
        val navController = navHostFragment?.navController
            ?: throw IllegalStateException("NavController non trouvé")

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        val navView: BottomNavigationView = binding.navView
        navView.setupWithNavController(navController)
    }

}
