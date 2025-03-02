package com.example.mecateknik.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mecateknik.MainActivity
import com.example.mecateknik.R
import com.example.mecateknik.db.AppDatabase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            Log.d("LoginActivity", "✅ Utilisateur déjà connecté : ${auth.currentUser?.email}")
            goToMainActivity()
            return
        }

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvRegister = findViewById(R.id.tvRegister)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInUser(email, password)
            } else {
                Toast.makeText(this, getString(R.string.remplir_tous_champs), Toast.LENGTH_SHORT).show()
            }
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        verifyUserInLocalDB(firebaseUser.uid, email)
                    }
                } else {
                    Log.e("LoginActivity", "❌ Erreur de connexion : ${task.exception?.message}")
                    Toast.makeText(this, getString(R.string.erreur_connexion), Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun verifyUserInLocalDB(userUid: String, email: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(applicationContext)
            val existingUser = db.userDao().getUserByFirebaseId(userUid)

            withContext(Dispatchers.Main) {
                if (existingUser == null) {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.utilisateur_inconnu),
                        Toast.LENGTH_LONG
                    ).show()
                    startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                } else {
                    goToMainActivity()
                }
            }
        }
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
