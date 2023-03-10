package com.example.contentproviderdivisascliente

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader

class MainActivity : AppCompatActivity() {

    val mLoaderCallbacks = object : LoaderManager.LoaderCallbacks<Cursor> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            //TODO("Not yet implemented")
            return CursorLoader(
                applicationContext,
                Uri.parse("content://com.example.contentproviderdivisas/divisas"),
                arrayOf<String>("_id", "baseCode", "nombreDivisa", "valor", "fecha"),
                null, null, null)
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {
            //TODO("Not yet implemented")
            Log.i("this_app", " Nada de CP")
        }

        @SuppressLint("Range")
        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
            //TODO("Not yet implemented")

            data?.apply {
                val adapter = SimpleCursorAdapter(applicationContext,
                    android.R.layout.simple_list_item_2,
                    this,
                    arrayOf<String>("_id", "baseCode", "nombreDivisa", "valor", "fecha"),
                    IntArray(5).apply {
                        set(2, android.R.id.text1)
                        set(3, android.R.id.text2)
                    } ,
                    SimpleCursorAdapter.IGNORE_ITEM_VIEW_TYPE
                )
                spn.adapter = adapter
                spn2.adapter = adapter

                val selectedCursor = spn.selectedItem as Cursor
                val text2Value = selectedCursor.getString(selectedCursor.getColumnIndex("valor"))
                txt.text = text2Value

                while (moveToNext()){
                    Log.i("this_app", " id: ${getInt(0)} , code: ${getString(1)}, moneda ${getString(2)}")
                }
            }

        }

    }

    lateinit var txt:TextView


    lateinit var spn : Spinner
    lateinit var spn2 : Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spn = findViewById(R.id.spinner)
        spn2 = findViewById(R.id.spinner2)

        txt = findViewById(R.id.textView)

        LoaderManager.getInstance(this)
            .initLoader<Cursor>(1001, null, mLoaderCallbacks)
        var   micursor   = contentResolver.query(
            Uri.parse("content://com.example.contentproviderdivisas/divisas"),
            arrayOf<String>("_id", "baseCode", "nombreDivisa", "valor", "fecha"),
            null, null,null
        )
        micursor?.apply {
            while (moveToNext()){
                Log.i("this_app", " id: ${getInt(0)} , code: ${getString(1)}, moneda ${getString(2)}")
            }
        }

        spn.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("Range")
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCursor = parent.getItemAtPosition(position) as Cursor
                val text2Value = selectedCursor.getString(selectedCursor.getColumnIndex("valor"))
                txt.text = text2Value
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No se seleccionó ningún elemento
            }
        }
    }
}