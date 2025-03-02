package com.example.mecateknik.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mecateknik.R
import com.example.mecateknik.db.AppDatabase
import com.example.mecateknik.db.entities.UserEntity
import com.example.mecateknik.utils.EmailValidator
import com.example.mecateknik.utils.NameValidator
import com.example.mecateknik.utils.PasswordValidator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Activité d'inscription des utilisateurs.
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnRegister: Button
    private val emailValidator = EmailValidator()
    private val passwordValidator = PasswordValidator()
    private val nameValidator = NameValidator()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnRegister = findViewById(R.id.btnRegister)
        auth = FirebaseAuth.getInstance()
        btnRegister.setOnClickListener { registerUser() }
    }

    /**
     * Valide les champs, le nom, l'email et le mot de passe puis procède à l'inscription de l'utilisateur.
     * En cas de succès, l'utilisateur est inséré dans la base de données locale et redirigé vers la page de connexion.
     */
    private fun registerUser() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.remplir_tous_champs), Toast.LENGTH_SHORT).show()
            return
        }
        if (!nameValidator.isValidName(name)) {
            Toast.makeText(this, getString(R.string.nom_invalide), Toast.LENGTH_SHORT).show()
            return
        }
        if (!emailValidator.isValidEmail(email)) {
            Toast.makeText(this, getString(R.string.email_invalide), Toast.LENGTH_SHORT).show()
            return
        }
        if (!passwordValidator.isValidPassword(password)) {
            Toast.makeText(this, getString(R.string.mot_de_passe_invalide), Toast.LENGTH_SHORT).show()
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    if (firebaseUser != null) {
                        val userEntity = UserEntity(
                            firebaseUid = firebaseUser.uid,
                            name = name,
                            email = email
                        )
                        lifecycleScope.launch(Dispatchers.IO) {
                            val db = AppDatabase.getDatabase(applicationContext)
                            db.userDao().insertUser(userEntity)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@RegisterActivity, getString(R.string.inscription_reussie), Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                                finish()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, getString(R.string.erreur_inscription), Toast.LENGTH_SHORT).show()
                }
            }
    }
}
