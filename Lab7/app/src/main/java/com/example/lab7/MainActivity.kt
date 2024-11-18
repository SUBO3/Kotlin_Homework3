package com.example.lab7

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.GridView
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { view, insets ->
            val padding = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(padding.left, padding.top, padding.right, padding.bottom)
            insets
        }

        val spinner = findViewById<Spinner>(R.id.spinner)
        val listView = findViewById<ListView>(R.id.listView)
        val gridView = findViewById<GridView>(R.id.gridView)

        val itemCount = generateItemCount()
        val items = generateItems()

        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemCount)
        gridView.numColumns = 3
        gridView.adapter = MyAdapter(this, items, R.layout.adapter_vertical)
        listView.adapter = MyAdapter(this, items, R.layout.adapter_horizontal)
    }

    private fun generateItemCount(): List<String> {
        return (1..10).map { "$it 個" }
    }

    private fun generateItems(): List<Item> {
        val priceRange = 10..100
        val imageArray = resources.obtainTypedArray(R.array.image_list)
        val items = (0 until imageArray.length()).map { index ->
            val photo = imageArray.getResourceId(index, 0)
            val name = "水果${index + 1}"
            val price = priceRange.random()
            Item(photo, name, price)
        }
        imageArray.recycle()
        return items
    }
}
