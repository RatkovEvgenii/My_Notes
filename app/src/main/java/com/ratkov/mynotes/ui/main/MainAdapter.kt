package com.ratkov.mynotes.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ratkov.mynotes.model.Note
import com.ratkov.mynotes.R
import com.ratkov.mynotes.getColorInt
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note.*

class MainAdapter(private val onItemClickListener: (Note) -> Unit) : RecyclerView.Adapter<MainAdapter.NoteViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int): Unit {
        holder.bind(notes[position])
    }

    @Suppress("DEPRECATION")

    inner class NoteViewHolder(override val containerView: View) :
            RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(note: Note) {
            title.text = note.title
            //body.text = note.body
            body.text = note.note
            itemView.setBackgroundColor(note.color.getColorInt(itemView.context))
            itemView.setOnClickListener { onItemClickListener(note) }

        }
    }


}
