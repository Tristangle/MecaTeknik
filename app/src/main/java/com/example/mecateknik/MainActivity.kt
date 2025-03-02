package com.example.mecateknik

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.mecateknik.databinding.ActivityMainBinding
import android.util.Log
import androidx.appcompat.widget.Toolbar
import com.example.mecateknik.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "✅ MainActivity est bien ouverte")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as? NavHostFragment
        val navController = navHostFragment?.navController
            ?: throw IllegalStateException("NavController non trouvé")

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_cart, R.id.vehicleFragment
            )
        )

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navView: BottomNavigationView = binding.navView
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            Log.d("NavDebug", "Navigated to ${destination.label}")
        }


        Log.d("MainActivity", "✅ NavController et NavigationView configurés avec succès")
    }
    override fun onResume() {
        super.onResume()

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            println("⚠️ Utilisateur non connecté, redirection vers LoginActivity")
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

}
