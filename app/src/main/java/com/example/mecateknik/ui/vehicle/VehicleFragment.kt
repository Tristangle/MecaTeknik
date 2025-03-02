package com.example.mecateknik.ui.vehicle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.mecateknik.R
import com.example.mecateknik.databinding.FragmentVehicleBinding
import com.example.mecateknik.db.AppDatabase
import com.example.mecateknik.ui.vehicle.adapter.VehicleAdapter
import com.example.mecateknik.viewmodel.VehicleViewModel
import com.example.mecateknik.viewmodel.factories.VehicleViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.example.mecateknik.ui.vehicle.VehicleFragmentDirections

class VehicleFragment : Fragment() {

    private var _binding: FragmentVehicleBinding? = null
    private val binding get() = _binding!!

    private val database by lazy { AppDatabase.getDatabase(requireContext()) }
    private val viewModelFactory by lazy {
        VehicleViewModelFactory(database.userDao(), database.carDao())
    }
    private val vehicleViewModel: VehicleViewModel by viewModels { viewModelFactory }

    private lateinit var vehicleAdapter: VehicleAdapter
    private lateinit var viewPager: ViewPager2

    // Variable pour stocker l'ID du véhicule actuellement affiché
    private var currentCarId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVehicleBinding.inflate(inflater, container, false)
        binding.viewModel = vehicleViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.title = "Vehicles"

        // Initialiser le ViewPager2 et l'adaptateur
        viewPager = binding.viewPager
        viewPager.isUserInputEnabled = true  // Permet le swipe

        vehicleAdapter = VehicleAdapter(
            onCarSelected = { selectedCar ->
                Log.d("VehicleFragment", "Véhicule sélectionné : ${selectedCar.brand} ${selectedCar.model}")
                currentCarId = selectedCar.id
            },
            onDeleteClick = { car ->
                showDeleteConfirmationDialog(car)
            },
            onMaintenanceClick = { car ->
                // Ici, on peut éventuellement laisser ce callback vide
                // puisque le bouton carnet d'entretien est désormais externe.
            }
        )
        viewPager.adapter = vehicleAdapter

        // Listener pour suivre la page actuelle
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val cars = vehicleAdapter.currentList
                if (cars.isNotEmpty() && position < cars.size) {
                    currentCarId = cars[position].id
                    Log.d("VehicleFragment", "Véhicule courant : $currentCarId")
                }
            }
        })

        // Observer la liste des véhicules
        vehicleViewModel.userCars.observe(viewLifecycleOwner) { cars ->
            Log.d("VehicleFragment", "Nombre de véhicules : ${cars.size}")
            vehicleAdapter.submitList(cars)
            binding.tvNoCars.visibility = if (cars.isEmpty()) View.VISIBLE else View.GONE
            if (cars.isNotEmpty() && currentCarId.isEmpty()) {
                currentCarId = cars.first().id
            }
        }

        // Bouton pour ajouter un véhicule
        binding.btnAddCar.setOnClickListener {
            findNavController().navigate(R.id.action_vehicleFragment_to_addCarFragment)
        }

        // Bouton externe pour accéder au carnet d'entretien
        binding.btnMaintenanceBook.setOnClickListener {
            if (currentCarId.isNotEmpty()) {
                val action = VehicleFragmentDirections.actionVehicleFragmentToMaintenanceFragment(currentCarId)
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Aucun véhicule sélectionné", Toast.LENGTH_SHORT).show()
            }
        }

        // Charger les véhicules de l'utilisateur
        val firebaseUid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        vehicleViewModel.getCarsByUser(firebaseUid)
    }

    private fun showDeleteConfirmationDialog(car: com.example.mecateknik.db.entities.CarEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Supprimer ce véhicule ?")
            .setMessage("Êtes-vous sûr de vouloir supprimer ${car.brand} ${car.model} ?")
            .setPositiveButton("Oui") { _, _ ->
                vehicleViewModel.deleteCar(car)
                Toast.makeText(requireContext(), "${car.brand} ${car.model} supprimé", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Non", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
