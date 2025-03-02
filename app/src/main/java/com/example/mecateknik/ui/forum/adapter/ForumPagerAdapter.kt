package com.example.mecateknik.ui.forum

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ForumPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2 // Par exemple, 2 pages : Topics et Messages

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ForumTopicsFragment()    // Votre fragment existant pour afficher la liste des topics
            1 -> ForumMessagesFragment()  // Votre fragment existant pour afficher les messages
            else -> throw IllegalStateException("Position inattendue $position")
        }
    }
}
