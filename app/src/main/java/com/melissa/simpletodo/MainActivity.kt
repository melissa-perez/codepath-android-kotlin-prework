package com.melissa.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

// handles the user interactions
class MainActivity : AppCompatActivity() {
    var listOfTasks = mutableListOf<String>()
    // Create adapter
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*val onClickListener = object :TaskItemAdapter.OnClickListener{

        }*/
        val onLongClickListener = object :TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                // 1. Remove the item from the list
                listOfTasks.removeAt(position)
                // 2. Notify the adapter of the change
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }
        loadItems()

        // View Ids
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up button and text field listener
        findViewById<Button>(R.id.addTaskButton).setOnClickListener{
            // 1. Grab the user text
            val userInputTask = inputTextField.text.toString()
            // 2. Add that string to listOfTasks
            listOfTasks.add(userInputTask)
            // Notifies the adapter and updates the app to reflect changes
            adapter.notifyItemInserted(listOfTasks.size - 1)
            // 3. Reset text field to empty for next input
            inputTextField.setText("")
            saveItems()
        }
    }

    // Save the data and persist it using File I/0

    //  Create a method to get the file we need
    fun getDataFile(): File {
        // Every line is a specific task
        return File(filesDir, "data.txt")
    }
    // Load the items by reading every line the data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
    // Save the data to file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch(ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Launch Edit Intent
    fun launchEditView() {
        //first parameter is the context, second is the class of the activity to launch
        val i = Intent(this@MainActivity, EditActivity::class.java)
    }
}