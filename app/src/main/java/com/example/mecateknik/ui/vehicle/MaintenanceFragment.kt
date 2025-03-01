package com.example.mecateknik.ui.vehicle

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mecateknik.databinding.FragmentMaintenanceBinding
import com.example.mecateknik.db.AppDatabase
import com.example.mecateknik.ui.maintenance.MaintenanceAdapter
import com.example.mecateknik.viewmodel.MaintenanceBookViewModel
import com.example.mecateknik.viewmodel.factories.MaintenanceBookViewModelFactory

class MaintenanceFragment : Fragment() {

    private var _binding: FragmentMaintenanceBinding? = null
    private val binding get() = _binding!!

    private val database by lazy { AppDatabase.getDatabase(requireContext()) }
    private val viewModelFactory by lazy { MaintenanceBookViewModelFactory(database.maintenanceBookDao()) }
    private val maintenanceViewModel: MaintenanceBookViewModel by viewModels { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        Log.d("MaintenanceFragment", "onCreateView called!")
        _binding = FragmentMaintenanceBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MaintenanceFragment", "onViewCreated called!")
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Initialise avec un adapter vide
        binding.recyclerView.adapter = MaintenanceAdapter()

        // Récupère l'argument "carId" (si Safe Args est utilisé)
        // Sinon, utilise une valeur par défaut pour tester.
        val carId = arguments?.getString("carId") ?: "id_de_la_voiture"

        // Charger les enregistrements de maintenance pour ce carId
        maintenanceViewModel.loadMaintenanceRecords(carId)

        maintenanceViewModel.maintenanceRecords.observe(viewLifecycleOwner) { records ->
            Log.d("MaintenanceFragment", "Nombre d'enregistrements de maintenance : ${records.size}")

            // Met à jour l'adapter
            (binding.recyclerView.adapter as MaintenanceAdapter).submitList(records)

            // Affiche "Aucun record" si la liste est vide
            binding.tvNoRecords.visibility = if (records.isEmpty()) View.VISIBLE else View.GONE
        }

        // Au clic, on NAVIGUE vers le fragment de saisie
        binding.btnAddMaintenance.setOnClickListener {
            // On veut passer le carId si besoin
            val action = MaintenanceFragmentDirections
                .actionMaintenanceFragmentToAddMaintenanceRecordFragment(carId)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
