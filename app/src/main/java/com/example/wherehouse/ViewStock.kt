package com.example.wherehouse

import android.content.Intent
import android.content.res.AssetManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class ViewStock : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_stock)

        val showDataButton: Button = findViewById(R.id.showDataButton)
        val dataTextView: TextView = findViewById(R.id.dataTextView) // Assuming you have a TextView in your layout

        showDataButton.setOnClickListener {
            // Get the AssetManager instance from the application context
            val assetManager: AssetManager = applicationContext.assets

            // Assuming you have the "config.json" file in the assets directory
            val configFileName = "config.json"

            // Call the DatabaseConnection functions
            val connection = DatabaseConnection.getConnection(assetManager, configFileName)

            // Fetch data and set it to the TextView
            GlobalScope.launch(Dispatchers.Main) {
                val fetchedData = fetchDataFromDatabase(connection, "tblitems", "ItemID")
                dataTextView.text = fetchedData
            }
        }
    }

    private suspend fun fetchDataFromDatabase(connection: Connection, tableName: String, columnName: String): String {
        val query = "SELECT * FROM $tableName"
        val data = StringBuilder()

        try {
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery(query)

            while (resultSet.next()) {
                val rowData = resultSet.getString(columnName)
                data.append(rowData).append("\n")
            }

            resultSet.close()
            statement.close()
        } catch (e: SQLException) {
            data.append("Error fetching data: ${e.message}")
        }

        return data.toString()
    }
}
