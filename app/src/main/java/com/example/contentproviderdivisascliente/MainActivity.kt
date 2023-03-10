package com.example.contentproviderdivisascliente

import android.annotation.SuppressLint
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.SimpleCursorAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
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

                val selectedCursor2 = spn2.selectedItem as Cursor
                val text2Value2 = selectedCursor2.getString(selectedCursor.getColumnIndex("valor"))
                txt.text = text2Value2

                while (moveToNext()){
                    //Log.i("this_app", " id: ${getInt(0)} , code: ${getString(1)}, moneda ${getString(2)}")
                }
            }

        }

    }

    lateinit var btn:Button

    lateinit var txt:TextView
    lateinit var txt2:TextView

    lateinit var op:EditText


    lateinit var spn : Spinner
    lateinit var spn2 : Spinner
    lateinit var aux : Spinner
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn = findViewById(R.id.cambiar)

        spn = findViewById(R.id.spinner)
        spn2 = findViewById(R.id.spinner2)

        txt = findViewById(R.id.textView)
        txt2 = findViewById(R.id.textView2)

        op = findViewById(R.id.variable)

        spn2.isEnabled=false

        LoaderManager.getInstance(this)
            .initLoader<Cursor>(1001, null, mLoaderCallbacks)
        var   micursor   = contentResolver.query(
            Uri.parse("content://com.example.contentproviderdivisas/divisas"),
            arrayOf<String>("_id", "baseCode", "nombreDivisa", "valor", "fecha"),
            null, null,null
        )
        micursor?.apply {
            while (moveToNext()){
                //Log.i("this_app", " id: ${getInt(0)} , code: ${getString(1)}, moneda ${getString(2)}")
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
        spn2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("Range")
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCursor = parent.getItemAtPosition(position) as Cursor
                val text2Value = selectedCursor.getString(selectedCursor.getColumnIndex("valor"))
                txt2.text = text2Value
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No se seleccionó ningún elemento
            }
        }

        op.doOnTextChanged { text, start, before, count ->
            operacion()
        }

        btn.setOnClickListener {
            if(spn.isEnabled){
                spn.isEnabled=false
                spn2.isEnabled=true
            }
            else{
                spn2.isEnabled=false
                spn.isEnabled=true
            }
            val pos1 = spn.selectedItemPosition
            val pos2 = spn2.selectedItemPosition
            val adapter1 = spn.adapter
            val adapter2 = spn2.adapter
            spn.adapter = adapter2
            spn2.adapter = adapter1
            spn.setSelection(pos2)
            spn2.setSelection(pos1)

            operacion()
        }
    }

    fun operacion(){

        val vari:EditText
        vari=findViewById(R.id.variable)

        val aux1:String
        if(op.getText().toString().isEmpty()){
            aux1 = "0"
        }
        else{
            if(spn2.isEnabled){
                //Funcion de cambio de divisas

                aux1 = vari.text.toString()
                val aux2 = txt2.text.toString()

                var x:Double
                var y:Double
                var resultado:Double
                x = aux1.toDouble()
                y = aux2.toDouble()

                resultado=x*y

                val resul:EditText
                resul=findViewById(R.id.resultado)

                resul.setText(resultado.toString())
            }
            else{
                //Funcion de cambio de divisas
                val vari:EditText
                vari=findViewById(R.id.variable)

                val multiplicador = vari.text.toString()
                val dividendo = txt.text.toString()

                var x:Double
                var y:Double
                var resultado:Double

                x = multiplicador.toDouble()
                y = dividendo.toDouble()

                var a:Double
                a=1.0
                resultado=(a/y)*x

                val resul:EditText
                resul=findViewById(R.id.resultado)

                resul.setText("$ "+resultado.toString())
            }
        }
    }
}
