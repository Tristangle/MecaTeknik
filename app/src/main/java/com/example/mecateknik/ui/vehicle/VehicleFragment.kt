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
import androidx.navigation.NavDirections
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVehicleBinding.inflate(inflater, container, false)
        binding.viewModel = vehicleViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = "Vehicles"

        // Initialiser ViewPager2 et l'adaptateur avec gestion des clics
        viewPager = binding.viewPager
        vehicleAdapter = VehicleAdapter(
            onCarSelected = { selectedCar ->
                Log.d("VehicleFragment", "Véhicule sélectionné : ${selectedCar.brand} ${selectedCar.model}")
            },
            onDeleteClick = { car ->
                showDeleteConfirmationDialog(car)
            },
            onMaintenanceClick = { car ->
                // Créer une action de navigation explicite via Safe Args
                Log.d("VehicleFragment", "Navigating to MaintenanceFragment with carId=${car.id}")
                val action = VehicleFragmentDirections.actionVehicleFragmentToMaintenanceFragment(car.id)
                findNavController().navigate(action)
            }
        )
        viewPager.adapter = vehicleAdapter

        // Observer les véhicules et mettre à jour le ViewPager2
        vehicleViewModel.userCars.observe(viewLifecycleOwner) { cars ->
            Log.d("VehicleFragment", "Nombre de véhicules : ${cars.size}")
            vehicleAdapter.submitList(cars)
            binding.tvNoCars.visibility = if (cars.isEmpty()) View.VISIBLE else View.GONE
        }

        // Désactiver le swipe pour forcer l'utilisation du menu ou des boutons
        viewPager.isUserInputEnabled = false

        // Bouton pour ajouter un véhicule
        binding.btnAddCar.setOnClickListener {
            findNavController().navigate(R.id.action_vehicleFragment_to_addCarFragment)
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
