package com.example.madlevel5task2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.content_add.*
import java.lang.Exception
import java.util.*

const val EXTRA_GAME = "EXTRA_GAME"

class AddActivity : AppCompatActivity() {

    private var invalidData = R.string.invalid;
    private var empty = R.string.empty;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            onSaveClick()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun onSaveClick() {
        if (etTitle.text.toString().isNotBlank() &&
            etPlatform.text.toString().isNotBlank() &&
            etDay.text.toString().isNotBlank() &&
            etMonth.text.toString().isNotBlank() &&
            etYear.text.toString().isNotBlank()
        ) {
            try {
                val cal = Calendar.getInstance()
                val year = etYear.text.toString().toInt()
                val month = etMonth.text.toString().toInt()
                val day = etDay.text.toString().toInt()
                if (month !in 1..12 || day !in 1..31) throw Exception()
                cal.set(year, month - 1, day)
                val game = Game(etTitle.text.toString(), etPlatform.text.toString(), cal.time)
                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_GAME, game)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this, invalidData, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, empty, Toast.LENGTH_SHORT).show()
        }
    }

}
