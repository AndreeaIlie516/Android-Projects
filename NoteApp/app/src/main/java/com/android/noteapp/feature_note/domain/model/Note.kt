package com.android.noteapp.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.noteapp.ui.theme.BabyBlue
import com.android.noteapp.ui.theme.LightGreen
import com.android.noteapp.ui.theme.RedOrange
import com.android.noteapp.ui.theme.RedPink
import com.android.noteapp.ui.theme.Violet

@Entity
data class Note (
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors = listOf(
            RedOrange,
            LightGreen,
            Violet,
            BabyBlue,
            RedPink
        )
    }
}

class InvalidNoteException(message: String) : Exception(message)