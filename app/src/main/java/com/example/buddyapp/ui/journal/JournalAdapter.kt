package com.example.buddyapp.ui.journal

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buddyapp.R
import com.example.buddyapp.data.local.Journal
import com.example.buddyapp.databinding.ItemJournalBinding
import com.example.buddyapp.helper.JournalDiffCallback

class JournalAdapter : RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {

    private val listJournal = ArrayList<Journal>()
    fun setListJournal(listJournal: List<Journal>) {
        val diffCallback = JournalDiffCallback(this.listJournal, listJournal)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listJournal.clear()
        this.listJournal.addAll(listJournal)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        val binding = ItemJournalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JournalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        holder.bind(listJournal[position])
    }

    override fun getItemCount(): Int = listJournal.size

    class JournalViewHolder(private val binding: ItemJournalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(journal: Journal) {
            with(binding) {
                tvItemTitle.text = journal.title
                val imageUri = Uri.parse(journal.image)
                Glide.with(imgItemImage.context)
                    .load(imageUri)
                    .placeholder(R.drawable.default_image_buddy)
                    .into(imgItemImage)
                tvItemDesc.text = journal.description
                tvTimestamp.text = journal.timestamp
                cvItemJournal.setOnClickListener {
                    val intent = Intent(itemView.context, DetailJournalActivity::class.java)
                    intent.putExtra(DetailJournalActivity.EXTRA_JOURNAL, journal)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}