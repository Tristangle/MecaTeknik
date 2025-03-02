package com.example.mecateknik.ui.forum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mecateknik.R
import com.example.mecateknik.databinding.FragmentForumTopicsBinding
import com.example.mecateknik.data.model.forum.ForumTopic

class ForumTopicsFragment : Fragment() {

    private var _binding: FragmentForumTopicsBinding? = null
    private val binding get() = _binding!!

    // Exemple de liste de topics (à remplacer par vos données réelles)
    private val topicsList = listOf(
        ForumTopic("1", "Premier Topic", "user1", System.currentTimeMillis()),
        ForumTopic("2", "Deuxième Topic", "user2", System.currentTimeMillis()),
        ForumTopic("3", "Troisième Topic", "user3", System.currentTimeMillis())
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForumTopicsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure le RecyclerView
        binding.recyclerViewTopics.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTopics.adapter = ForumTopicsAdapter(topicsList) { topic ->
            // Gérer le clic sur un topic, par exemple naviguer vers ForumMessagesFragment
            // Ex : findNavController().navigate(ForumTopicsFragmentDirections.actionForumTopicsFragmentToForumMessagesFragment(topic.id))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
