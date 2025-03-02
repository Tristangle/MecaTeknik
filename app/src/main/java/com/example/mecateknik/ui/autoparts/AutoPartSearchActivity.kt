package com.example.mecateknik.ui.autoparts

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mecateknik.databinding.ActivityAutoPartSearchBinding
import com.example.mecateknik.db.entities.CarEntity
import com.example.mecateknik.ui.autoparts.adapter.AutoPartAdapter
import com.google.firebase.auth.FirebaseAuth

/**
 * Activity permettant la recherche de pièces auto.
 * L'utilisateur sélectionne son véhicule parmi ceux de son profil,
 * saisit un filtre optionnel sur le nom de la pièce et lance la recherche.
 */
class AutoPartSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAutoPartSearchBinding
    private val viewModel: AutoPartSearchViewModel by viewModels()
    private lateinit var autoPartAdapter: AutoPartAdapter

    // Map pour retrouver l'objet CarEntity à partir de sa représentation String
    private val carMap = mutableMapOf<String, CarEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutoPartSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupVehicleDropdown()

        binding.btnSearch.setOnClickListener {
            val selectedCarStr = binding.autoCompleteVehicle.text.toString()
            val car = carMap[selectedCarStr]
            if (car != null) {
                val configuration = "${car.brand};${car.model};${car.year}"
                val partNameFilter = binding.etPartNameFilter.text.toString().trim()
                viewModel.searchAutoParts(configuration, partNameFilter)
            }
        }


        viewModel.autoParts.observe(this) { parts ->
            autoPartAdapter.submitList(parts)
        }

        // Charger les véhicules de l'utilisateur (via Firebase Auth)
        val firebaseUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        viewModel.loadUserCars(firebaseUid)
    }

    private fun setupRecyclerView() {
        autoPartAdapter = AutoPartAdapter { autoPart ->
            // Crée un bundle avec la référence de la pièce
            val bundle = Bundle().apply {
                putString("partReference", autoPart.reference)
            }
            // Ouvre le fragment de détails
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, AutoPartDetailFragment().apply { arguments = bundle })
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerViewParts.apply {
            layoutManager = LinearLayoutManager(this@AutoPartSearchActivity)
            adapter = autoPartAdapter
        }
    }

    private fun setupVehicleDropdown() {
        viewModel.userCars.observe(this) { cars ->
            val carStrings = cars.map { car ->
                val s = "${car.brand} ${car.model} (${car.year})"
                carMap[s] = car
                s
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, carStrings)
            binding.autoCompleteVehicle.setAdapter(adapter)
        }
    }
}
