package com.example.mecateknik.ui.forum

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mecateknik.data.model.forum.ForumTopic
import com.example.mecateknik.databinding.ItemForumTopicBinding

class ForumTopicsAdapter(
    private val topics: List<ForumTopic>,
    private val onTopicClick: (ForumTopic) -> Unit
) : RecyclerView.Adapter<ForumTopicsAdapter.TopicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val binding = ItemForumTopicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TopicViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = topics[position]
        holder.bind(topic)
        holder.itemView.setOnClickListener { onTopicClick(topic) }
    }

    override fun getItemCount(): Int = topics.size

    class TopicViewHolder(private val binding: ItemForumTopicBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(topic: ForumTopic) {
            binding.tvTopicTitle.text = topic.title
            // Par exemple, afficher la date en formatant le timestamp
            binding.tvTopicDate.text = "Créé le : " + android.text.format.DateFormat.format("dd/MM/yyyy", topic.creationDate)
        }
    }
}
