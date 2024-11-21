package com.mobiledimas.bukucatatan

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mobiledimas.bukucatatan.database.NoteDatabaseHelper
import com.mobiledimas.bukucatatan.databinding.ActivityUpdateNoteBinding
import com.mobiledimas.bukucatatan.model.Note

class update_note_activity : AppCompatActivity() {
    private lateinit var binding : ActivityUpdateNoteBinding
    private lateinit var db : NoteDatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        noteId = intent.getIntExtra("note_id",-1)
        if(noteId==-1){
            finish()
            return
        }

        val note = db.getNoteById(noteId)
        binding.updatetitleEt.setText(note.title)
        binding.updatecontentEt.setText(note.content)

        binding.updatesaveButton.setOnClickListener(){
            val newTitle = binding.updatetitleEt.text.toString()
            val newContent = binding.updatecontentEt.text.toString()

            val updateNote = Note(noteId,newTitle,newContent)
            db.updatenote(updateNote)

            Toast.makeText(this,"Catatan diperbarui",Toast.LENGTH_SHORT).show()
            finish()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}