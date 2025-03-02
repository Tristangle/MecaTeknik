package com.example.mecateknik.ui.forum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mecateknik.databinding.FragmentForumBinding
import com.google.android.material.tabs.TabLayoutMediator

class ForumFragment : Fragment() {

    private var _binding: FragmentForumBinding? = null
    private val binding get() = _binding!!

    // Adapter pour le ViewPager2 (qui contiendra par exemple ForumTopicsFragment et ForumMessagesFragment)
    private lateinit var forumPagerAdapter: ForumPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Configurez le ViewPager2 et l’adapter
        forumPagerAdapter = ForumPagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = forumPagerAdapter

        // Associez le TabLayout au ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Topics"
                1 -> "Messages"
                else -> "Onglet ${position + 1}"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
