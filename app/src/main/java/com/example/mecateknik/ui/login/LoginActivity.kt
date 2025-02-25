package com.example.mecateknik.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mecateknik.MainActivity
import com.example.mecateknik.R
import com.example.mecateknik.db.AppDatabase
import com.example.mecateknik.db.entities.UserEntity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            Log.d("LoginActivity", "✅ Utilisateur déjà connecté : ${auth.currentUser?.email}")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInUser(email, password)
            } else {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    if (firebaseUser != null) {
                        saveUserToLocalDB(firebaseUser.uid, email)
                    }

                    Log.d("LoginActivity", "✅ Connexion réussie, ouverture de MainActivity")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.e("LoginActivity", "❌ Erreur de connexion : ${task.exception?.message}")
                    Toast.makeText(this, "Erreur de connexion", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun saveUserToLocalDB(userUid: String, email: String) {
        lifecycleScope.launch(Dispatchers.IO) { // 🚀 Lancer sur un thread en arrière-plan
            val db = AppDatabase.getDatabase(applicationContext)
            val existingUser = db.userDao().getUserByFirebaseId(userUid)

            if (existingUser == null) {
                val newUser = UserEntity(
                    firebaseUid = userUid,
                    email = email,
                    name = "Unknown" // ⚠️ Modifier si besoin
                )
                db.userDao().insertUser(newUser)
                Log.d("LoginActivity", "✅ Utilisateur ajouté en local : $userUid")
            } else {
                Log.d("LoginActivity", "ℹ️ Utilisateur déjà en local")
            }
        }
    }


}
