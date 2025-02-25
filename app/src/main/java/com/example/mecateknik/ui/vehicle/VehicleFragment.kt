package com.example.mecateknik.ui.vehicle

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mecateknik.R
import com.example.mecateknik.databinding.FragmentVehicleBinding
import com.example.mecateknik.db.AppDatabase
import com.example.mecateknik.viewmodel.VehicleViewModel
import com.example.mecateknik.viewmodel.factories.VehicleViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class VehicleFragment : Fragment() {

    private var _binding: FragmentVehicleBinding? = null
    private val binding get() = _binding!!

    // ✅ Get instance of database
    private val database by lazy { AppDatabase.getDatabase(requireContext()) }

    // ✅ ViewModelFactory to pass DAO dependencies
    private val viewModelFactory by lazy {
        VehicleViewModelFactory(database.userDao(), database.carDao())
    }

    // ✅ ViewModel with Factory
    private val vehicleViewModel: VehicleViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentVehicleBinding.inflate(inflater, container, false)

        // 🔥 Bind ViewModel to XML
        binding.viewModel = vehicleViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = "Vehicles"


        // ✅ Get current user's Firebase UID
        val firebaseUid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // 🔥 Observe LiveData for user cars and update UI dynamically
        vehicleViewModel.userCars.observe(viewLifecycleOwner) { cars ->
            Log.d("VehicleFragment", "Nombre de véhicules : ${cars.size}") // DEBUG
            if (cars.isNotEmpty()) {
                val latestCar = cars.last() // Get the most recently added car
                binding.tvVehicleInfo.text = "${latestCar.model} (${latestCar.year})"
            } else {
                binding.tvVehicleInfo.text = "No vehicles added yet"
            }
        }

        // 🚀 Button Click → Adds a car & updates UI
        binding.btnAddCar.setOnClickListener {
            findNavController().navigate(R.id.action_vehicleFragment_to_addCarFragment)
        }


        // 🔄 Fetch user's cars on fragment start
        vehicleViewModel.getCarsByUser(firebaseUid)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
