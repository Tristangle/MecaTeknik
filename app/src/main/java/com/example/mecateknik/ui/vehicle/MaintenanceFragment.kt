/*package com.example.mecateknik.ui.maintenance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mecateknik.databinding.FragmentMaintenanceBinding
import com.example.mecateknik.db.AppDatabase
import com.example.mecateknik.viewmodel.MaintenanceBookViewModel
import com.example.mecateknik.viewmodel.factories.MaintenanceBookViewModelFactory

class MaintenanceFragment : Fragment() {

    private var _binding: FragmentMaintenanceBinding? = null
    private val binding get() = _binding!!

    private val database by lazy { AppDatabase.getDatabase(requireContext()) }
    private val viewModelFactory by lazy { MaintenanceBookViewModelFactory(database.maintenanceBookDao()) }
    private val maintenanceViewModel: MaintenanceBookViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMaintenanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val carId = "id_de_la_voiture" // 🔥 À récupérer dynamiquement
        maintenanceViewModel.loadMaintenanceRecords(carId)

        maintenanceViewModel.maintenanceRecords.observe(viewLifecycleOwner) { records ->
            binding.recyclerView.adapter = MaintenanceAdapter(records)
        }

        binding.btnAddMaintenance.setOnClickListener {
            maintenanceViewModel.addMaintenanceRecord(
                carId = carId,
                date = System.currentTimeMillis(),
                description = "Vidange moteur",
                kilometers = 120000,
                garage = "Garage AutoSpeed"
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
*/