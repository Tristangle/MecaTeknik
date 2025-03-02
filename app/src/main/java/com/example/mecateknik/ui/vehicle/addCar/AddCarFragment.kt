package com.example.mecateknik.ui.vehicle.addCar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mecateknik.databinding.FragmentAddCarBinding
import com.example.mecateknik.db.AppDatabase
import com.example.mecateknik.db.entities.CarEntity
import com.example.mecateknik.utils.AutoPartGenerator
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

class AddCarFragment : Fragment() {

    private var _binding: FragmentAddCarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnAddCar.setOnClickListener {
            saveCarToDatabase()
        }

        return root
    }

    private fun saveCarToDatabase() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val brand = binding.etBrand.text.toString()
        val model = binding.etModel.text.toString()
        val year = binding.etYear.text.toString().toIntOrNull()

        if (brand.isEmpty() || model.isEmpty() || year == null) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val newCar = CarEntity(
            id = UUID.randomUUID().toString(),
            userUid = user.uid,
            brand = brand,
            model = model,
            year = year,
            engineType = "Unknown",
            specification = "Default",
            countryOrigin = "Unknown"
        )

        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(requireContext())

            val existingUser = db.userDao().getUserByFirebaseId(user.uid)
            if (existingUser == null) {
                launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "User not found in local database", Toast.LENGTH_SHORT).show()
                }
                return@launch
            }

            // Insère la voiture en BD
            db.carDao().insertCar(newCar)

            // Intégration du générateur : créer 5 pièces auto pour la voiture ajoutée
            AutoPartGenerator.generatePartsForCar(requireContext(), newCar)

            launch(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Car added successfully", Toast.LENGTH_SHORT).show()
                requireActivity().onBackPressed()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
