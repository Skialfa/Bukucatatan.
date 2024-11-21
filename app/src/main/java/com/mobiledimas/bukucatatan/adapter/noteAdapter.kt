package com.mobiledimas.bukucatatan.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mobiledimas.bukucatatan.DetailNotesActivity
import com.mobiledimas.bukucatatan.R
import com.mobiledimas.bukucatatan.database.NoteDatabaseHelper
import com.mobiledimas.bukucatatan.model.Note
import com.mobiledimas.bukucatatan.update_note_activity

class noteAdapter (private var notes: List<Note> ,context: Context) : RecyclerView.Adapter<noteAdapter.NoteViewHolder>() {
    private val db: NoteDatabaseHelper = NoteDatabaseHelper(context)

    class NoteViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.txtjudul)
        val content = itemView.findViewById<TextView>(R.id.txtdeskripsi)
        val updatebutton = itemView.findViewById<ImageView>(R.id.updatebutton)
        val deletebutton = itemView.findViewById<ImageView>(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size


    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.title.text = note.title
        holder.content.text = note.content

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailNotesActivity::class.java).apply {
                // Mengirim data melalui intent
                putExtra("title", note.title)
                putExtra("content",note.content)
            }
            context.startActivity(intent)
        }

        holder.updatebutton.setOnClickListener(){
            val intent = Intent(holder.itemView.context,update_note_activity::class.java).apply {
                putExtra("note_id",note.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deletebutton.setOnClickListener(){
            AlertDialog.Builder(holder.itemView.context).apply {
                setTitle("Konfirmasi")
                setMessage("Apakah anda ingin melanjutkan?")

                setPositiveButton("Yakin"){dialogInterface, i ->
                    db.deleteNote(note.id) // Hapus catatan berdasarkan ID
                    refreshData(db.getAllNotes()) // Perbarui daftar catatan
                }

                setNegativeButton("Batal"){dialogInterface, i->
                    dialogInterface.dismiss()
                }
            }.show()
        }


    }

    //fungsi untuk auto refresh list
    fun refreshData(newNotes : List<Note>){
        notes = newNotes
        notifyDataSetChanged()
    }
}