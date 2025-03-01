package com.example.mecateknik.ui.vehicle

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mecateknik.databinding.FragmentAddMaintenanceRecordBinding
import com.example.mecateknik.db.AppDatabase
import com.example.mecateknik.viewmodel.MaintenanceBookViewModel
import com.example.mecateknik.viewmodel.factories.MaintenanceBookViewModelFactory

class AddMaintenanceRecordFragment : Fragment() {

    private var _binding: FragmentAddMaintenanceRecordBinding? = null
    private val binding get() = _binding!!

    // Safe Args : on récupère l'argument "carId"
    private val args: AddMaintenanceRecordFragmentArgs by navArgs()

    private val database by lazy { AppDatabase.getDatabase(requireContext()) }
    private val viewModelFactory by lazy { MaintenanceBookViewModelFactory(database.maintenanceBookDao()) }
    private val maintenanceViewModel: MaintenanceBookViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddMaintenanceRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // On récupère carId depuis les args
        val carId = args.carId

        // Clic sur "Valider"
        binding.btnValidate.setOnClickListener {
            val description = binding.etDescription.text.toString().trim()
            val kilometersStr = binding.etKilometers.text.toString().trim()
            val garage = binding.etGarage.text.toString().trim()

            if (TextUtils.isEmpty(description) || TextUtils.isEmpty(kilometersStr)) {
                Toast.makeText(requireContext(), "Veuillez remplir la description et le kilométrage", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val kilometers = kilometersStr.toIntOrNull() ?: 0

            maintenanceViewModel.addMaintenanceRecord(
                carId = carId,
                date = System.currentTimeMillis(),
                description = description,
                kilometers = kilometers,
                garage = if (garage.isEmpty()) "Garage inconnu" else garage
            )

            Toast.makeText(requireContext(), "Maintenance ajoutée", Toast.LENGTH_SHORT).show()

            // Retour à MaintenanceFragment
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
